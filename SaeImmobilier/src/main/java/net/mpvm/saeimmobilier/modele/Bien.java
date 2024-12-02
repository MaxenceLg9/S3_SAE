package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Connection.BD;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Bien implements Queryable {

    private int IdBien;
    private String adresse;
    private String ville;
    private int codePostal;
    private Assurance assurance;
    private float iR; // Taux d'intérêt ou autre valeur
    private Bien(int IdBien, String ville,int CodePostal,String adresse) {
        this.IdBien = IdBien;
        this.ville = ville;
        this.codePostal = CodePostal;
        this.adresse = adresse;
    }

    public Bien(String ville, int CodePostal, String adresse) {
        this.ville = ville;
        this.codePostal = CodePostal;
        this.adresse = adresse;
    }
    public int getIdBien() {
        return IdBien;
    }
    public void setIdBien(int IdBien) {
        this.IdBien = IdBien;
    }
    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }
    public Assurance getAssurance() {
        return this.assurance;
    }
    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public float getiR() {
        return iR;
    }

    public void setiR(float iR) {
        this.iR = iR;
    }
    public static List<Bien> findAll() throws BienException {
        List<Bien> biens = new ArrayList<>();
        String query = "SELECT * FROM bienlouable"; // Assurez-vous que cette table existe dans votre BDD.
        try (Connection connection = BD.getConnection(true);
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Bien bien = new Bien(
                        rs.getInt("IdBien"),
                        rs.getString("Ville"),
                        rs.getInt("CodePostal"),
                        rs.getString("Adresse")
                );
                biens.add(bien);
            }
        } catch (Exception e) {
            throw new BienException("Erreur lors de la récupération des biens", e);
        }
        return biens;
    }

    @Override
    public void save() throws QueryableException {
    }

    @Override
    public void modify() throws QueryableException {

    }

    public void delete() throws QueryableException {
        String query = "DELETE FROM bienlouable WHERE IdBienLouable = ?";

        try (Connection connection = BD.getConnection(true);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.IdBien);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new QueryableException("Erreur lors de la suppression du bien");
        }
    }

    public String getTypeBien() {
        Bien bien = this;

        if (bien instanceof BienLouable) {
            return "Bien Louable"; // Si l'objet est une instance de BienLouable
        } else {
            return "Immeuble"; // Si l'objet est une instance de Immeuble
        }
    }


    public float getSurface() {
        Bien bien = this;
        if (bien instanceof BienLouable){
            return bien.getSurface();
        }else{
            return 0;}
    }

    public int getNombrePieces() {
        Bien bien = this;

        if (bien instanceof BienLouable) {
            return ((BienLouable) bien).getNbPieces(); // Appeler la méthode getNbPieces() si c'est un BienLouable
        } else if (bien instanceof Immeuble) {
            return 0; // Par exemple, un immeuble n'a pas de nombre de pièces défini de cette manière, vous pouvez ajuster
        } else {
            return -1; // Retourner une valeur indiquant que le nombre de pièces n'est pas disponible
        }
    }





    // Classe d'exception personnalisée
    public static class BienException extends Exception {
        public BienException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
