package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class Bien extends Queryable {

    private static final String SELECT_QUERY = "SELECT * FROM bien";
    private static final String SELECT_ID_QUERY = "SELECT IdBien FROM bien WHERE NumeroFiscal = ?";
    private int idBien;
    private Optional<Assurance> assurance;
    private float iR; // Taux d'intérêt ou autre valeur
    private String numeroFiscal;
    private final java.sql.Date dateAjout;


    public Bien(int idBien, String numeroFiscal, java.sql.Date dateAjout) {
        this.idBien = idBien;
        this.numeroFiscal = numeroFiscal;
        this.dateAjout = dateAjout;
        this.assurance = Optional.empty();
    }

    public static Bien findById(int idBien) throws BienException {
        try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_ID_QUERY)) {
            selectQueryElement.setArgs(Map.of(1, idBien));
            selectQueryElement.execute();
            List<Map<String, Object>> results = selectQueryElement.getResult();



            Map<String, Object> result = results.get(0);
            TypeBien typeBien = TypeBien.valueOf(result.get("TypeBien").toString().toUpperCase());

            return switch (typeBien) {
                case HABITATION -> new Habitation.HBuilder(result).build();
                case GARAGE -> new Garage.GBuilder(result).build();
                case IMMEUBLE -> new Immeuble.IBuilder(result).build();

            };
        } catch (QueryElement.QEltException e) {
            throw new BienException("Erreur lors de la récupération du bien avec ID " + idBien, e.getSqlException());
        }
    }


    public int getIdBien() {
        return idBien;
    }

    public abstract String getVille();

    public abstract void setVille(String ville);

    public abstract int getCodePostal();

    public abstract void setCodePostal(int codePostal);

    public Optional<Assurance> getAssurance() {
        return this.assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = Optional.of(assurance);
    }

    public abstract String getAdresse();

    public abstract void setAdresse(String adresse);
    public float getiR() {
        return iR;
    }

    public void setiR(float iR) {
        this.iR = iR;
    }

    public void save() throws BienException {
        try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_ID_QUERY)){
            selectQueryElement.setArgs(Map.of(1,numeroFiscal));
            selectQueryElement.execute();
            Map<String,Object> result = selectQueryElement.getResult().getFirst();
            this.idBien = (int) result.get("IdBien");
        } catch (QueryElement.QEltException e) {
            throw new BienException("Erreur lors de la récupération de l'ID du bien", e.getSqlException());
        }
    }

    public static List<? extends Bien> findAll() throws BienException {
        List<Bien> biens = new ArrayList<>();
        try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)){
            sortResult(biens, selectQueryElement);
        } catch (QueryElement.QEltException qEltException) {
            throw new BienException("Erreur lors de la récupération des biens", qEltException.getSqlException());
        }
        return biens;
    }

    private static void sortResult(List<Bien> biens, SelectQueryElement selectQueryElement) throws QueryElement.QEltException {
        selectQueryElement.execute();
        List<Map<String, Object>> result = selectQueryElement.getResult();
        for (Map<String, Object> args : result) {
            switch (TypeBien.valueOf(args.get("TypeBien").toString().toUpperCase())) {
                case TypeBien.HABITATION:
                    biens.add(new Habitation.HBuilder(args).build());
                    break;
                case TypeBien.GARAGE:
                    biens.add(new Garage.GBuilder(args).build());
                    break;
                case TypeBien.IMMEUBLE:
                    biens.add(new Immeuble.IBuilder(args).build());
                    break;
                default:
                    break;
            }
        }
    }

    public abstract TypeBien getTypeBien();

    public String getTypeBienString(){
        return getTypeBien().name();
    }

    public float getSurface() {
        Bien bien = this;
        if (bien instanceof BienLouable){
            return bien.getSurface();
        }else{
            return 0;}
    }

    public abstract int getNbPieces();


    public static List<Bien> findByImmeuble(int idImmeuble) throws Bien.BienException {
        List<Bien> biens = new ArrayList<>();
        String query = "SELECT * FROM immeuble WHERE idImmeuble = ?";

        try (SelectQueryElement selectQueryElement = new SelectQueryElement(query)) {

            // Remplacez le paramètre par l'id de l'immeuble
            selectQueryElement.setArgs(Map.of(1, idImmeuble));

            sortResult(biens, selectQueryElement);
        } catch (QueryElement.QEltException e) {
            throw new BienException("Erreur lors de la récupération des biens pour l'immeuble ID " + idImmeuble, e.getSqlException());
        }

        return biens;
    }

    public Optional<Assurance> getAssuranceActuelle() {
        return this.assurance;
    }

    public String getNumeroFiscal() {
        return this.numeroFiscal;
    }

    public java.sql.Date getDateAjout() {
        return this.dateAjout;
    }

    public void setNumeroFiscal(String numeroFiscal) {
        this.numeroFiscal = numeroFiscal;
    }

    @Override
    public void archiver() {

    }

    public void update() {
    }

    public abstract static class BBuilder extends Queryable.Builder{

        private final int IdBien;
        private final String numeroFiscal;
        private final java.sql.Date dateAjout;

        public BBuilder(int idBien, String numeroFiscal, java.sql.Date dateAjout) {
            this.IdBien = idBien;
            this.dateAjout = dateAjout;
            this.numeroFiscal = numeroFiscal;
        }

        int getIdBien(){
            return IdBien;
        }

        String getNumeroFiscal(){
            return numeroFiscal;
        }

        java.sql.Date getDateAjout(){
            return dateAjout;
        }
    }


    // Classe d'exception personnalisée
    public static class BienException extends QbleException {
        public BienException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }
}
