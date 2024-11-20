package net.mpvm.saeimmobilier.sql;

import java.sql.*;


public class TestConnexionDB {

    public static void connect() {
        // connection string

        try {
            //requête select sur la table Immeuble + affichage
            /*String insert = "INSERT INTO Locataires VALUES (0,'Doe', 'John', 'Monsieur', '0601020304', 'johndoe@prankex.rizz', 'false')";
            Statement iStmt = conn.createStatement();
            iStmt.executeUpdate(insert);
            int nbRow = iStmt.executeUpdate("SELECT * FROM Locataire");
            System.out.println(nbRow + "rows affected");*/

            String select = "SELECT * FROM Locataires";
            Statement stmt = BD.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while(rs.next()){
                System.out.println("Nom :" + rs.getString(2) + ", Prenom :" + rs.getString(3) + ", Tel :" + rs.getString(5) + ", Email :" + rs.getString(6));
            }
            rs.close();
            stmt.close();
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        connect();
    }
}