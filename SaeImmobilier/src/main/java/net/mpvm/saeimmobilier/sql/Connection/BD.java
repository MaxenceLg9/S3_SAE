package net.mpvm.saeimmobilier.sql.Connection;

import java.sql.*;

public class BD{

    public static Connection getConnection(boolean commit) throws SQLException {
        String url = "jdbc:mysql://localhost/bdimmo";
        Connection connection = DriverManager.getConnection(url,"root","iutinfo");
        connection.setAutoCommit(commit);
        return connection;
    }

}
