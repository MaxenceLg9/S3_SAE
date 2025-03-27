package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.*;
import java.util.*;


public abstract class Bien extends Queryable {

    private static final String SELECT_QUERY = "SELECT * FROM bien";
    private static final String SELECT_ID_QUERY = "SELECT IdBien FROM bien WHERE NumeroFiscal = ?";
    private static final String SELECT_QUERY_BY_ID = "SELECT * FROM bien WHERE IdBien = ?";
    public static final String SELECT_FROM_ID = "SELECT * FROM Bien WHERE IdBien = ?";
    public static final String DELETE_QUERY = "DELETE FROM Bien WHERE IdBien = ? AND TypeBien = ?";

    private int idBien;
    private String numeroFiscal;
    private final Date dateAjout;
    private String idProprio;

    private static final Map<Integer,Bien> biens = new HashMap<>();


    Bien(int idBien, String numeroFiscal, Date dateAjout, String idProprio) {
        this.idBien = idBien;
        this.numeroFiscal = numeroFiscal;
        this.dateAjout = dateAjout;
        this.idProprio = idProprio;
    }

    public static Bien findById(int idBien) throws BienException {
        try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY_BY_ID)) {
            selectQueryElement.setArgs(Map.of(1, idBien));
            selectQueryElement.execute();
            List<Map<String, Object>> results = selectQueryElement.getResult();

            Map<String, Object> result = results.getFirst();
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

    public abstract String getCodePostal();

    public abstract String getAdresse();


    public Optional<Assurance> getAssurance() {
        //TODO : query
        return Optional.empty();
    }

    public void setAssurance(Assurance assurance) {
        //TODO : query
    }

    //fonction pour "sauvegarder" l'id générée par
    protected void updateID(UpdateQueryElement updateQueryElement) throws QbleException {
        this.idBien = lastID(updateQueryElement);
    }

    //super méthode pour supprimer le Bien : gère les spécificités des classes filles
    public final void delete() throws BienException {
        //check de la présence du bien dans la BD
        if (this.getIdBien() == -1){
            throw new Immeuble.ImmeubleException("Le bien n'existe pas dans la table", null);
        }

        //Gestion de la suppression des contraintes Travaux & Assurances
        String DELETE_TRAVAUX = "DELETE FROM Travaux WHERE IdBien = ?";
        String UPDATE_ASSURANCES = "UPDATE Assurance SET IdBien = NULL WHERE IdBien = ?";
        //Initialisation des différentes query
        try (
                UpdateQueryElement deleteTravauxQuery = new UpdateQueryElement(DELETE_TRAVAUX, true);
                UpdateQueryElement updateQuery = new UpdateQueryElement(UPDATE_ASSURANCES, true);
                UpdateQueryElement deleteQuery = new UpdateQueryElement(DELETE_QUERY, true);
        ){
        //gestion des spécificités de chaque type
        if(this instanceof Immeuble immeuble){
            for (BienLouable b :immeuble.getBiensAssocies()){
                b.delete();
            }
        }else{
            for(Bail b : Bail.getBauxFromBien((BienLouable) this))
                b.delete();
        }

            //init des paramètres et exécutions des requêtes
            deleteTravauxQuery.setArgs(Map.of(1, this.getIdBien())).execute();
            updateQuery.setArgs(Map.of(1, this.getIdBien())).execute();
            deleteQuery.setArgs(Map.of(1, this.getIdBien(), 2, this.getTypeBien().name())).execute();
            this.idBien = -1;
        } catch (QueryElement.QEltException e){
            e.getSqlException().printStackTrace();
            throw new Bien.BienException("Erreur lors de la suppression du bienA", e.getSqlException());
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

    abstract Map<String, Object> getArgs();

    public void setIdProprio(String idProprio) {
        this.idProprio = idProprio;
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


    public String getIdProprio() {
        return this.idProprio;
    }

    public abstract static class BBuilder extends Queryable.Builder{

        private final int IdBien;
        private final String numeroFiscal;
        private final java.sql.Date dateAjout;
        private final String idProprio;

        public BBuilder(int idBien, String numeroFiscal, Date dateAjout, String idProprio) {
            this.IdBien = idBien;
            this.dateAjout = dateAjout;
            this.numeroFiscal = numeroFiscal;
            this.idProprio = idProprio;
        }

        int getIdBien(){
            return IdBien;
        }

        String getNumeroFiscal(){
            return numeroFiscal;
        }

        Date getDateAjout(){
            return dateAjout;
        }

        String getIdProprio(){
            return idProprio;
        }

        boolean checkPresentIn(){
            return checkPresentIn(IdBien);
        }

        public static boolean checkPresentIn(int idBien){
            return (biens.containsKey(idBien) && biens.get(idBien) != null);
        }

        public static Bien getFromId(int idBien, TypeBien typeBien) throws Bien.BienException {
            if(checkPresentIn(idBien)){
                if(typeBien.getTClass().isInstance(biens.get(idBien)))
                    return get(idBien);
                else
                    throw new Bien.BienException("Le bien n'est pas du bon type", null);
            }
            return getFromQuery(idBien, typeBien);

        }

        private static Bien getFromQuery(int idBien, TypeBien type) throws BienException {
            try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY_BY_ID)){
                selectQueryElement.setArgs(Map.of(1, idBien));
                selectQueryElement.execute();
                List<Map<String,Object>> result = selectQueryElement.getResult();
                if(result.isEmpty() && !result.getFirst().get("TypeBien").equals(type.name()))
                    throw new Bien.BienException("Le bien n'existe pas dans la base de données", null);
                return switch (type) {
                    case HABITATION -> new Habitation.HBuilder(result.getFirst()).build();
                    case GARAGE -> new Garage.GBuilder(result.getFirst()).build();
                    case IMMEUBLE -> new Immeuble.IBuilder(result.getFirst()).build();
                };
            }catch (QueryElement.QEltException qEltException){
                throw new Bien.BienException("Erreur lors de la récupération de l'immeuble, il n'existe peut-être pas dans la base de données", qEltException.getSqlException());
            }
        }

        public static void add(Bien bien) {
            biens.put(bien.getIdBien(), bien);
        }

        public static Bien get(int idBien) {
            return biens.get(idBien);
        }
    }

    // Classe d'exception personnalisée
    public static class BienException extends QbleException {
        public BienException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }
}
