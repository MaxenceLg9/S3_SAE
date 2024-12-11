package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Connection.BD;
import net.mpvm.saeimmobilier.sql.Query.Queryable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public abstract class Bien implements Queryable {

    private int IdBien;
    private String adresse;
    private String ville;
    private int codePostal;
    private Assurance assurance;
    private float iR; // Taux d'intérêt ou autre valeur


    Bien(String ville,int CodePostal,String adresse, int idBien) {
        this.IdBien = idBien;
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
        //TODO : utiliser les findALl des sous classes
        List<Bien> biens = new ArrayList<>();
        String query = "SELECT * FROM bien"; // Assurez-vous que cette table existe dans votre BDD.
        try (Connection connection = BD.getConnection(true);
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                switch (TypeBien.valueOf(rs.getString("TypeBien"))){
                    case TypeBien.HABITATION :
                        biens.add(new Habitation(rs.getString("Lieu_Immeuble"),
                                rs.getString("Ville"),
                                rs.getInt("CodePostal"),
                                rs.getString("Adresse"),
                                rs.getInt("NombrebPieces"),
                                rs.getString("NumeroFiscal"),
                                (Immeuble) rs.getObject("Immeuble"),
                                rs.getFloat("Surface"),
                                rs.getDate("DateAjout"))
                        );
                        break;
                    case TypeBien.GARAGE:
                        biens.add(new Garage(rs.getString("Lieu_Immeuble"),rs.getString("Ville"),
                                rs.getInt("CodePostal"),
                                rs.getString("Adresse"),
                                rs.getInt("NombrebPieces"),
                                rs.getString("NumeroFiscal"),
                                (Immeuble) rs.getObject("Immeuble"),
                                rs.getFloat("Surface"),
                                rs.getDate("DateAjout")));
                        break;
                    case TypeBien.IMMEUBLE:
                        biens.add(new Immeuble(rs.getString("Ville"),
                                rs.getInt("CodePostal"),
                                rs.getString("Adresse")));
                        break;

                }
            }
        } catch (Exception e) {
            throw new BienException("Erreur lors de la récupération des biens", e);
        }
        return biens;
    }

    public abstract TypeBien getTypeBien();
    public abstract String getTypeBienString();

    public abstract float getSurface();

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
