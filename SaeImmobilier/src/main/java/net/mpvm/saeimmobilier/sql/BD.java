package net.mpvm.saeimmobilier.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.*;

public class BD{

    private static Connection conn = null;

    private static Connection getConnection(){
        String url = "jdbc:sqlite:bdd/bdImmo";
        if(conn != null){
            return conn;
        }
        createConn();
        return conn;
    }

    private static void createConn(){
        String url = "jdbc:sqlite:bdd/bdImmo";
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
        } catch (Exception e){
            System.out.println(e.getMessage());
            conn = null;
        }
    }

    public static Statement createStatement(){
        try {
            createConn();
            return Objects.requireNonNull(getConnection()).createStatement();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    private static PreparedStatement prepareStatement(String sql) throws SQLException {
        createConn();
        return Objects.requireNonNull(getConnection()).prepareStatement(sql);
    }

    public static void executeUpdate(@NotNull PreparedStatement preparedStatement, boolean commit) throws SQLException {
        int row = preparedStatement.executeUpdate();
        System.out.println(row + " rows affected");
        if (commit)
            getConnection().commit();
        preparedStatement.close();
        getConnection().close();
    }


    public static void insertInto(String table, Map<String,String> args, boolean commit) throws SQLException {
        String query = createInsertQuery(table, args);
        System.out.println(query);
        System.out.println(Arrays.toString(args.values().toArray(new String[0])));
        PreparedStatement pSt = prepareStatement(query);
        for (int i = 0; i < args.size(); i++) {
            pSt.setString(i + 1, args.values().toArray(new String[0])[i]);
        }
        executeUpdate(pSt, commit);
    }

    @NotNull
    private static String createInsertQuery(String table, @Nullable Map<String, String> args) {
        String query = "INSERT INTO "+ table;
        String columns = "";
        String params = "";
        if( args != null)
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

    public static ResultSet select(String table, @Nullable Map<String,String> args, @Nullable List<String> columns){
        Statement st = createStatement();
        if(st == null){
            throw new NullPointerException("Statement is null");
        }
        try {
            return st.executeQuery(createSelectQuery(table,args,columns));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String createSelectQuery(String table,@Nullable Map<String, String> args, @Nullable List<String> columns) {
        String query = "SELECT ";
        if(columns == null)
            query += "* FROM " + table;
        else{
            for(int i = 0; i < columns.size(); i++){
                query += columns.get(i);
                if(i < columns.size() - 1){
                    query += ", ";
                }
                else
                    query += " FROM " + table;

            }
        }
        if(args == null)
            return query;
        else{
            query += " WHERE ";
            for(int i = 0; i < args.size(); i++){
                String key = String.valueOf(args.keySet().toArray()[i]);

                query += key + " = " + args.get(key);
                if(i < args.size() - 1){
                    query += " AND ";
                }
            }
        }
        System.out.println(query);
        return query;
    }

    public static void delete(String table, HashMap<String, Integer> id, boolean commit) throws SQLException {
        PreparedStatement pSt = prepareStatement(createDeleteQuery(table, id));
        executeUpdate(pSt,commit);
    }

    private static String createDeleteQuery(String table, @NotNull Map<String,Integer> args){
        String query = "DELETE FROM " + table + " WHERE ";
        for(int i = 0; i < args.size(); i++){
            String key = args.keySet().stream().toList().get(i);
            query += key + " = " + args.get(key);
            if(i < args.size() - 1){
                query += " AND ";
            }
        }
        return query;
    }
}
