/**
 * @author zhouyang
 * @date 2019/8/17
 */

import virtual.Pool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: zhouyang
 * @Date: 2019/8/17 19:44
 * @Description:
 **/
public class PoolUtils implements Pool {

    /**
     * 数据库驱动
     */
    private static String jdbcDriver = "com.mysql.jdbc.Driver";
    /**
     * 数据库URL
     */
    private static String dbUrl = "jdbc:mysql://localhost:3306/test";
    /**
     * 数据库用户名
     */
    private static String dbUsername = "admin";
    /**
     * 数据库密码
     */
    private static String dbPassword = "admin";

    private static DaoConnectionsPool connPool ;

    /**
     * 设置成单例模式 防止多次实例化连接池 非延迟加载
     */

    private static PoolUtils instance;

    private PoolUtils() {
    }

    public static PoolUtils getInstance() {

        return instance;
    }

    static {

        connPool = new DaoConnectionsPool(jdbcDriver, dbUrl, dbUsername, dbPassword);
        instance = new PoolUtils();

    }

    @Override
    public int getInitialConnections() {

        return connPool.getInitialConnections();
    }

    @Override
    public void setInitialConnections(int initialConnections) {

        connPool.setInitialConnections(initialConnections);
    }

    @Override
    public int getIncrementalConnections() {

        return connPool.getIncrementalConnections();
    }

    @Override
    public void setIncrementalConnections(int incrementalConnections) {

        connPool.setIncrementalConnections(incrementalConnections);
    }

    @Override
    public int getMaxConnections() {

        return connPool.getMaxConnections();
    }

    @Override
    public void setMaxConnections(int maxConnections) {

        connPool.setMaxConnections(maxConnections);
    }

    @Override
    public void initPool() {
        try {
            connPool.createPool();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {

        Connection conn = null;
        try {
            conn = connPool.getConnection();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void returnConnection(Connection conn) {

        connPool.returnConnection(conn);
    }

    @Override
    public void refreshPool() {

        try {
            connPool.refreshPool();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void closePool() {

        try {
            connPool.closePool();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

}

