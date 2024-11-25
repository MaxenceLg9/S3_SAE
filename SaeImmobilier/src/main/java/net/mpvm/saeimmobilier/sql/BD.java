package net.mpvm.saeimmobilier.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sqlite.JDBC;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4Connection;

import java.sql.*;
import java.util.*;

public class BD{

    public static Connection getConnection(boolean commit) throws SQLException {
        String url = "jdbc:sqlite:bdd/bdImmo";
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(commit);
        return connection;
    }

}
