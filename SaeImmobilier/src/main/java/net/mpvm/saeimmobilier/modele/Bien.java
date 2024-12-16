package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Connection.BD;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class Bien implements Queryable {

    private static final String SELECT_QUERY = "SELECT * FROM bien";
    private int IdBien;
    private Assurance assurance;
    private float iR; // Taux d'intérêt ou autre valeur


    Bien(int idBien) {
        this.IdBien = idBien;
    }


    public int getIdBien() {
        return IdBien;
    }

    public abstract String getVille();

    public abstract void setVille(String ville);

    public abstract int getCodePostal();

    public abstract void setCodePostal(int codePostal);

    public Assurance getAssurance() {
        return this.assurance;
    }
    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }

    public abstract String getAdresse();

    public abstract void setAdresse(String adresse);
    public float getiR() {
        return iR;
    }

    public void setiR(float iR) {
        this.iR = iR;
    }

    public static List<? extends Bien> findAll() throws BienException {
        List<Bien> biens = new ArrayList<>();
        try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)){
            ResultSet rs = selectQueryElement.execute();
            while (rs.next()) {
                switch (TypeBien.valueOf(rs.getString("TypeBien").toUpperCase())) {
                    case TypeBien.HABITATION:
                        biens.add(new Habitation(rs));
                        break;
                    case TypeBien.GARAGE:
                        biens.add(new Garage(rs));
                        break;
                    case TypeBien.IMMEUBLE:
                        biens.add(new Immeuble(
                                rs.getString("Ville"),
                                rs.getInt("CodePostal"),
                                rs.getString("Adresse"),
                                rs.getInt("IdBien")));
                        break;
                }
            }
        } catch (SQLException | QueryElement.QueryException e) {
            throw new BienException("Erreur lors de la récupération des biens", e instanceof SQLException ? (SQLException) e : ((QueryElement.QueryException) e).getSqlException());
        }
        return biens;
    }

    public abstract TypeBien getTypeBien();

    public String getTypeBienString(){
        return getTypeBien().name();
    };

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


    public static List<Bien> findByImmeuble(int idImmeuble) throws Bien.BienException {
        List<Bien> biens = new ArrayList<>();
        String query = "SELECT * FROM immeuble WHERE idImmeuble = ?";

        try (SelectQueryElement selectQueryElement = new SelectQueryElement(query)) {

            // Remplacez le paramètre par l'id de l'immeuble
            selectQueryElement.setArgs(Map.of(1, idImmeuble));

            ResultSet rs = selectQueryElement.execute();
            while (rs.next()) {
                // Identifiez le type de bien et créez l'objet correspondant
                String typeBien = rs.getString("TypeBien");

                switch (TypeBien.valueOf(typeBien)) {
                    case HABITATION:
                        biens.add(new Habitation(rs));
                        break;

                    case GARAGE:
                        biens.add(new Garage(rs));
                        break;

                    case IMMEUBLE:
                        biens.add(new Immeuble(
                                rs.getString("Ville"),
                                rs.getInt("CodePostal"),
                                rs.getString("Adresse"),
                                rs.getInt("IdBIen")
                        ));
                        break;

                    default:
                        throw new BienException("Type de bien inconnu : " + typeBien, null);
                }
            }
        } catch (SQLException | QueryElement.QueryException e) {
            throw new BienException("Erreur lors de la récupération des biens pour l'immeuble ID " + idImmeuble, e instanceof SQLException ? (SQLException) e : ((QueryElement.QueryException) e).getSqlException());
        }

        return biens;
    }


    // Classe d'exception personnalisée
    public static class BienException extends QueryableException {
        public BienException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }
}
