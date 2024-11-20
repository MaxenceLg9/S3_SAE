package net.mpvm.saeimmobilier.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

public class BD {

    private static Connection conn = null;

    private static Connection getConnection(){
        String url = "jdbc:sqlite:bdd/bdImmo";
        if(conn != null){
            return conn;
        }
        try {
            conn = DriverManager.getConnection(url);
            return conn;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Statement createStatement(){
        try {
            return Objects.requireNonNull(getConnection()).createStatement();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static PreparedStatement prepareStatement(String sql){
        try {
            return Objects.requireNonNull(getConnection()).prepareStatement(sql);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
