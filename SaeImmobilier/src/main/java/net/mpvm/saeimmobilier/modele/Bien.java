package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;

import java.sql.Date;
import java.sql.*;
import java.util.*;


public abstract class Bien extends Queryable {

    private static final String SELECT_QUERY = "SELECT * FROM bien";
    private static final String SELECT_ID_QUERY = "SELECT IdBien FROM bien WHERE NumeroFiscal = ?";
    private static final String SELECT_QUERY_BY_ID = "SELECT * FROM bien WHERE IdBien = ?";
    public static final String SELECT_FROM_ID = "SELECT * FROM Bien WHERE IdBien = ?";

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

    public void delete() throws BienException {
        this.idBien = -1;
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

    public Optional<Assurance> getAssuranceActuelle() {
        //TODO JE SAIS PAS
        return Optional.empty();
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
            try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_FROM_ID)){
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
