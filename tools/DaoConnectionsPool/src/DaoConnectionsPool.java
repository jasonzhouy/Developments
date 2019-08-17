/**
 * @author zhouyang
 * @date 2019/8/17
 */

import sun.tools.tree.ThisExpression;
import virtual.Pool;

import java.sql.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @Author: zhouyang
 * @Date: 2019/8/17 19:08
 * @Description:
 **/
public class DaoConnectionsPool {

    /**
     * 数据库驱动
     */
    private String jdbcDriver = "";

    /**
     * 数据库连接地址 例如: http://localhost:3306/dataBaseName
     */
    private String jdbcUrl;

    /**
     * 数据库用户名
     */
    private String jdbcUser;

    /**
     * 密码
     */
    private String jdbcPassword;

    /**
     * 正常连接之前，测试连接是否正常使用的表
     */
    private String testTable;

    /**
     * 初始化时，默认的可用连接数
     */
    private int initialConnections = 25;

    /**
     * 默认最大连接数
     */
    private int maxConnections = 100;

    /**
     * 默认每次扩容的大小
     */
    private int incrementalConnections = 25;

    /**
     * 连接保存的容器 可以使用Lock包中其他的线程安全容器代替
     */
    private Vector connections = null;

    /**
     * 责任链模式初始化连接池
     */

    public DaoConnectionsPool(String jdbcDriver, String jdbcUrl, String jdbcUser, String jdbcPassword) {
        this.jdbcDriver = jdbcDriver;
        this.jdbcUrl = jdbcUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPassword = jdbcPassword;
    }


    public int getInitialConnections() {
        return this.initialConnections;
    }


    public void setInitialConnections(int initialConnections) {
        this.initialConnections = initialConnections;
    }


    public int getIncrementalConnections() {
        return this.incrementalConnections;
    }


    public void setIncrementalConnections(int incrementalConnections) {
        this.incrementalConnections = incrementalConnections;
    }


    public int getMaxConnections() {
        return this.maxConnections;
    }


    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     * 设置连接测试表
     *
     * @param testTable
     */
    public void setTestTable(String testTable) {
        this.testTable = testTable;

    }

    /**
     * 获取连接测试表
     *
     * @return
     */
    public String getTestTable() {
        return this.testTable;
    }

    public synchronized void createPool() throws Exception {

        // 如果连接池已经被创建
        // 直接返回
        if (connections != null) {
            return;
        }

        // 注册数据库驱动
        Driver driver = (Driver) Class.forName(this.jdbcDriver).newInstance();
        DriverManager.registerDriver(driver);

        connections = new Vector(0);

        createConnections(this.initialConnections);


    }

    private void createConnections(int numConnections) throws SQLException {
        // 循环创建指定数目的数据库连接
        for (int x = 0; x < numConnections; x++) {
            // 是否连接池中的数据库连接的数量己经达到最大？最大值由类成员 maxConnections
            // 指出，假如 maxConnections 为 0 或负数，表示连接数量没有限制。
            // 假如连接数己经达到最大，即退出。
            if (this.maxConnections > 0 &&
                    this.connections.size() >= this.maxConnections) {
                break;
            }
            //add a new DaoConnection object to connections vector
            // 增加一个连接到连接池中（向量 connections 中）
            connections.addElement(new DaoConnection(newConnection()));


            System.out.println(" 数据库连接己创建 ......");
        }
    }

    private Connection newConnection() throws SQLException {
        // 创建一个数据库连接
        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser,
                jdbcPassword);
        // 假如这是第一次创建数据库连接，即检查数据库，获得此数据库答应支持的
        // 最大客户连接数目
        //connections.size()==0 表示目前没有连接己被创建
        if (connections.size() == 0) {
            DatabaseMetaData metaData = conn.getMetaData();
            int driverMaxConnections = metaData.getMaxConnections();
            // 数据库返回的 driverMaxConnections 若为 0 ，表示此数据库没有最大
            // 连接限制，或数据库的最大连接限制不知道
            //driverMaxConnections 为返回的一个整数，表示此数据库答应客户连接的数目
            // 假如连接池中设置的最大连接数量大于数据库答应的连接数目 , 则置连接池的最大
            // 连接数目为数据库答应的最大数目
            if (driverMaxConnections > 0 &&
                    this.maxConnections > driverMaxConnections) {
                this.maxConnections = driverMaxConnections;
            }
        }
        // 返回创建的新的数据库连接
        return conn;
    }


    public synchronized Connection getConnection() throws SQLException {
        // 确保连接池己被创建
        // 连接池还没创建，则返回 null
        if (connections == null) {
            return null;
        }
        // 获得一个可用的数据库连接
        Connection conn = getFreeConnection();
        // 假如目前没有可以使用的连接，即所有的连接都在使用中
        while (conn == null) {
            // 等一会再试
            wait(250);
            // 重新再试，直到获得可用的连接，假如
            //getFreeConnection() 返回的为 null
            // 则表明创建一批连接后也不可获得可用连接
            conn = getFreeConnection();

        }
        // 返回获得的可用的连接
        return conn;
    }


    private Connection getFreeConnection() throws SQLException {
        // 从连接池中获得一个可用的数据库连接
        Connection conn = findFreeConnection();
        if (conn == null) {
            // 假如目前连接池中没有可用的连接
            // 创建一些连接
            createConnections(incrementalConnections);
            // 重新从池中查找是否有可用连接
            conn = findFreeConnection();
            if (conn == null) {
                // 假如创建连接后仍获得不到可用的连接，则返回 null
                return null;
            }
        }
        return conn;
    }

    private Connection findFreeConnection() throws SQLException {
        Connection conn = null;
        DaoConnection pConn = null;
        // 获得连接池向量中所有的对象
        Enumeration enumerate = connections.elements();
        // 遍历所有的对象，看是否有可用的连接
        while (enumerate.hasMoreElements()) {
            pConn = (DaoConnection) enumerate.nextElement();
            if (!pConn.isUsing()) {
                // 假如此对象不忙，则获得它的数据库连接并把它设为忙
                conn = pConn.getConnection();
                pConn.setUsing(true);
                // 测试此连接是否可用
                if (!testConnection(conn)) {
                    // 假如此连接不可再用了，则创建一个新的连接，
                    // 并替换此不可用的连接对象，假如创建失败，返回 null
                    try {
                        conn = newConnection();
                    } catch (SQLException e) {
                        System.out.println(" 创建数据库连接失败！ " + e.getMessage());
                        return null;
                    }
                    pConn.setConnection(conn);
                }
                break; // 己经找到一个可用的连接，退出
            }
        }
        // 返回找到到的可用连接
        return conn;


    }

    private boolean testConnection(Connection conn) {
        try {
            // 判定测试表是否存在
            if (testTable.equals("")) {
                // 假如测试表为空，试着使用此连接的 setAutoCommit() 方法
                // 来判定连接否可用（此方法只在部分数据库可用，假如不可用 ,
                // 抛出异常）。注重：使用测试表的方法更可靠
                conn.setAutoCommit(true);
            } else {
                // 有测试表的时候使用测试表测试
                //check if this connection is valid
                Statement stmt = conn.createStatement();
                stmt.execute("select count(*) from " + testTable);
            }
        } catch (SQLException e) {
            // 上面抛出异常，此连接己不可用，关闭它，并返回 false;
            closeConnection(conn);
            return false;
        }
        // 连接可用，返回 true
        return true;
    }

    private void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(" 关闭数据库连接出错： " + e.getMessage());
        }
    }

    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
            System.out.println("不影响运行~");
            System.out.println("——————————————线程被异常中断————————————");
            System.out.println(e.getMessage());
            System.out.println("——————————————结束————————————");
        }
    }


    public void initPool() {

    }


    public void returnConnection(Connection conn) {
        // 确保连接池存在，假如连接没有创建（不存在），直接返回
        if (connections == null) {
            System.out.println(" 连接池不存在，无法返回此连接到连接池中 !");
            return;
        }
        DaoConnection pConn = null;
        Enumeration enumerate = connections.elements();
        // 遍历连接池中的所有连接，找到这个要返回的连接对象
        while (enumerate.hasMoreElements()) {
            pConn = (DaoConnection) enumerate.nextElement();
            // 先找到连接池中的要返回的连接对象
            if (conn == pConn.getConnection()) {
                // 找到了 , 设置此连接为空闲状态
                pConn.setUsing(false);
                break;
            }
        }
    }


    public synchronized void refreshPool() throws SQLException {
        // 确保连接池己创新存在
        if (connections == null) {
            System.out.println(" 连接池不存在，无法刷新 !");
            return;
        }
        DaoConnection pConn = null;
        Enumeration enumerate = connections.elements();
        while (enumerate.hasMoreElements()) {
            // 获得一个连接对象
            pConn = (DaoConnection) enumerate.nextElement();
            // 假如对象忙则等 5 秒 ,5 秒后直接刷新
            if (pConn.isUsing()) {
                // 等 5 秒
                wait(5000);
            }
            // 关闭此连接，用一个新的连接代替它。
            closeConnection(pConn.getConnection());
            pConn.setConnection(newConnection());
            pConn.setUsing(false);
        }
    }


    public synchronized void closePool() throws SQLException {
        // 确保连接池存在，假如不存在，返回
        if (connections == null) {
            System.out.println(" 连接池不存在，无法关闭 !");
            return;
        }
        DaoConnection pConn = null;
        Enumeration enumerate = connections.elements();
        while (enumerate.hasMoreElements()) {
            pConn = (DaoConnection) enumerate.nextElement();
            // 假如忙，等 5 秒
            if (pConn.isUsing()) {
                // 等 5 秒
                wait(5000);
            }
            //5 秒后直接关闭它
            closeConnection(pConn.getConnection());
            // 从连接池向量中删除它
            connections.removeElement(pConn);
        }
        // 置连接池为空
        connections = null;
    }
}
