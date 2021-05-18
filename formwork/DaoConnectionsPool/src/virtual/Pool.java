package virtual;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhouyang
 * @date 2019/8/17
 * @description 池子对外提供的方法
 */
public interface Pool {

    /**
     * 获取池子初始化时的大小
     */
    int getInitialConnections();

    /**
     * 设置池子初始化时的大小
     */
    void setInitialConnections(int initialConnections);

    /**
     * 获取池子每次扩容的大小
     */
    int getIncrementalConnections();

    /**
     * 设置池子每次扩容的大小
     */
    void setIncrementalConnections(int incrementalConnections);

    /**
     * 获取池子最大容量
     */
    int getMaxConnections();

    /**
     * 设置池子最大容量
     */
    void setMaxConnections(int maxConnections);

    /**
     * 初始化池子
     */
    void initPool();

    /**
     * 获取连接
     */
    Connection getConnection();

    /**
     * 返回连接到池子里
     */

    void returnConnection(Connection connection);

    /**
     * 刷新池子
     */
    void refreshPool();

    /**
     * 关闭池子
     */
    void closePool();


}
