/**
 * @author zhouyang
 * @date 2019/8/17
 */

import java.sql.Connection;

/**
 * @Author: zhouyang
 * @Date: 2019/8/17 19:05
 * @Description:
 **/
public class DaoConnection {

    /**
     * 连接对象
     */

    Connection connection;

    /**
     * 该对象是否正在被使用
     */
    boolean isUsing = false;

    public DaoConnection(Connection connection) {
        this.connection = connection;

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void setUsing(boolean using) {
        isUsing = using;
    }
}
