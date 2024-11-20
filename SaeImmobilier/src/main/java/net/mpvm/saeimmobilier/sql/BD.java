package net.mpvm.saeimmobilier.sql;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class BD{

    private static Connection conn = null;

    private static Connection getConnection(){
        String url = "jdbc:sqlite:bdd/bdImmo";
        if(conn != null){
            return conn;
        }
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
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
    private static void prepareStatement(String sql, String[] args, boolean commit){
        try {
            PreparedStatement pSt = Objects.requireNonNull(getConnection()).prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                pSt.setString(i+1, args[i]);
            }
            int row = pSt.executeUpdate();
            if(commit)
                getConnection().commit();
            pSt.close();
            getConnection().close();
            System.out.println(row + " rows affected");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void insertInto(String table, Map<String,String> args, boolean commit){
        String query = createInsertQuery(table, args);
        System.out.println(query);
        System.out.println(Arrays.toString(args.values().toArray(new String[0])));
        prepareStatement(query, args.values().toArray(new String[0]), commit);
    }

    @NotNull
    private static String createInsertQuery(String table, Map<String, String> args) {
        String query = "INSERT INTO "+ table;
        String columns = "";
        String params = "";
        for(int i = 0; i < args.size(); i++){
            columns += args.keySet().toArray()[i];
            params += "?";
            if(i < args.size() - 1) {
                columns += ",";
                params += ",";
            }
        }
        query += " (" + columns + ") values(" + params + ")";
        return query;
    }
}
