package net.mpvm.saeimmobilier.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sqlite.JDBC;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4Connection;

import java.sql.*;
import java.util.*;

public class BD{

    private static Connection conn = null;

    private static Connection getConnection(){
        if(conn != null){
            return conn;
        }
        createConn();
        return conn;
    }

    private static void createConn(){
        String url = "jdbc:sqlite:bdd/bdImmo";
        if(conn != null)
            return;
        try {
            conn = DriverManager.getConnection(url);
            if(conn instanceof JDBC4Connection){
                System.out.println("Connection to SQLite has been established.");
            }
            conn.setAutoCommit(false);
        } catch (Exception e){
            System.out.println(e.getMessage());
            conn = null;
        }
    }

    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        createConn();
        return Objects.requireNonNull(getConnection()).prepareStatement(sql);
    }

}
