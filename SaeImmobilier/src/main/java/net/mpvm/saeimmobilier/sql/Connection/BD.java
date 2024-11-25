package net.mpvm.saeimmobilier.sql.Connection;

import java.sql.*;

public class BD{

    public static Connection getConnection(boolean commit) throws SQLException {
        String url = "jdbc:sqlite:bdd/bdImmo";
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(commit);
        return connection;
    }

}
