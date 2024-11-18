package net.mpvm.saeimmobilier.sql;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.jdbc4.*;

public class DB {

    public static void connect() {
        // connection string
        var url = "jdbc:sqlite:bdd/BDImmobilier.db";

        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        connect();
    }
}