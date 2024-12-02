package net.mpvm.saeimmobilier.modele;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.mpvm.saeimmobilier.sql.QueryElement.QueryElement;
import static net.mpvm.saeimmobilier.modele.Immeuble.mapResultSetToImmeuble;


public class Bien {
    public static final String INSERT_QUERY = "INSERT INTO Bien (adresse, ville, codePostal, typeBien, surface, nombrePieces) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String DELETE_QUERY = "DELETE FROM Bien WHERE IdBien = ?";
    public static final String SELECT_QUERY = "SELECT * FROM Bien";

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
    public static List<Bien> findByImmeuble(int idImmeuble) {
        String query = "SELECT * FROM Bien WHERE Lieu_Immeuble = ?";
        try (QueryElement<List<Bien>> queryElement = new QueryElement<>(query, true) {
            @Override
            public List<Bien> execute() throws QueryException {
                List<Bien> biens = new ArrayList<>();
                try (ResultSet resultSet = getPreparedStatement().executeQuery()) {
                    while (resultSet.next()) {
                        Bien bien = mapResultSetToBien(resultSet);
                        biens.add(bien);
                    }
                } catch (SQLException e) {
                    throw new QueryException("Error executing query", e);
                }
                return biens;
            }
        }) {
            queryElement.setArgs(Map.of(1, idImmeuble));
            return queryElement.execute();
        } catch (QueryElement.QueryException e) {
            throw new RuntimeException("Failed to fetch biens for immeuble " + idImmeuble, e);
        }
    }
    public static List<Immeuble> findAllImmeubles() {
        String query = "SELECT * FROM Bien WHERE TypeBien = 'Immeuble'";
        try (QueryElement<List<Immeuble>> queryElement = new QueryElement<>(query, true) {
            @Override
            public List<Immeuble> execute() throws QueryException {
                List<Immeuble> immeubles = new ArrayList<>();
                try (ResultSet resultSet = getPreparedStatement().executeQuery()) {
                    while (resultSet.next()) {
                        ResultSet ResultSet = null;
                        Immeuble immeuble = mapResultSetToImmeuble(ResultSet);
                        immeubles.add(immeuble);
                    }
                } catch (SQLException e) {
                    throw new QueryException("Error executing query", e);
                }
                return immeubles;
            }
        }) {
            return queryElement.execute();
        } catch (QueryElement.QueryException e) {
            throw new RuntimeException("Failed to fetch immeubles", e);
        }
    }
    private static Bien mapResultSetToBien(ResultSet resultSet) throws SQLException {
        int idBien = resultSet.getInt("IdBien");
        String ville = resultSet.getString("Ville");
        int codePostal = resultSet.getInt("CodePostal");
        String adresse = resultSet.getString("Adresse");

        return new Bien(idBien, ville, codePostal, adresse);
    }




    // Classe d'exception personnalisée
    public static class BienException extends Exception {
        public BienException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
