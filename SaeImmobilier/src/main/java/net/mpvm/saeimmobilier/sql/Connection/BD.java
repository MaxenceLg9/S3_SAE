package net.mpvm.saeimmobilier.sql.Connection;

import java.sql.*;

public class BD{

    public static Connection getConnection(boolean commit) throws SQLException {
        String url = "jdbc:mysql://127.0.0.5:3306/bdimmo";
        Connection connection = DriverManager.getConnection(url,"root","31Mai2005*");
        connection.setAutoCommit(commit);
        return connection;
    }

}
