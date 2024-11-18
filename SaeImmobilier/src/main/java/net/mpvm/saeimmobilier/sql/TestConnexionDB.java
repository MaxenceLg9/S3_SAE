package net.mpvm.saeimmobilier.sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnexionDB {

    public static void connect() {
        // connection string
        var url = "jdbc:sqlite:bdd/BDImmobilier.db";

        try (var conn = DriverManager.getConnection(url)) {
            //requête select sur la table Immeuble + affichage

            String sql = "SELECT * FROM BiensLouable";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                System.out.println(rs.getString(0));
            }
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        connect();
    }
}