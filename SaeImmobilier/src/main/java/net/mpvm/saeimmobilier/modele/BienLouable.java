package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;
import net.mpvm.saeimmobilier.util.Unfinished;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class BienLouable extends Bien {

    public static final String INSERT_QUERY = "INSERT INTO bien (ComplementAdresse, TypeBien, Surface, NombrePieces, NumeroFiscal, DateAjout, IdImmeuble, IdProprio) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_QUERY = "DELETE FROM bien WHERE IdBien = ?";
    public static final String UPDATE_QUERY = "UPDATE bien SET ComplementAdresse = ?, Surface = ?, NombrePieces = ? , NumeroFiscal = ?, IdProprio = ? WHERE IdBien = ?";
    public static final String SELECT_QUERY = "SELECT * FROM bien WHERE TypeBien in ('HABITATION','GARAGE')";
    public static final String SELECT_QUERY_ID = "SELECT IdImmeuble FROM bien WHERE IdBien = ?";

    private String complementAdresse;
    private int ancienIndex;
    private boolean changementCompteur;
    private float surface;
    private Immeuble immeuble;
    private int nbPieces;


    BienLouable(String complementAdresse,int nbPieces, String numeroFiscal, Immeuble immeuble, float surface, Date dateAjout, String idProprio, int idBien) throws BienException {// Initialisation des attributs hérités de Bien
        super(idBien, numeroFiscal, dateAjout, idProprio);
        this.complementAdresse = complementAdresse;
        setImmeuble(immeuble);
        this.surface = surface;
        this.nbPieces = nbPieces;
    }

    // Getters et Setters pour tous les champs

    public String getComplementAdresse() {
        return complementAdresse;
    }

    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    @Unfinished
    public ArrayList<Travaux> getTravaux() {
        //TODO : make a query
        return null;
    }

    @Unfinished
    public List<Bail> getBaux() throws Bail.BailException {
        return Bail.getBauxFromBien(this);
    }

    @Override
    public String getCodePostal(){
        return this.immeuble.getCodePostal();
    }

    @Override
    public String getAdresse() {
        return this.immeuble.getAdresse();
    }


    @Override
    public String getVille() {
        return this.immeuble.getVille();
    }

    public int getAncienIndex() {
        return ancienIndex;
    }

    public void setAncienIndex(int ancienIndex) {
        this.ancienIndex = ancienIndex;
    }

    public boolean isChangementCompteur() {
        return changementCompteur;
    }

    public void setChangementCompteur(boolean changementCompteur) {
        this.changementCompteur = changementCompteur;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }

    public float getSurface() {
        return surface;
    }

    public Immeuble getImmeuble() {
        return immeuble;
    }

    public void setImmeuble(Immeuble immeuble) throws BienLouableException {
        if(immeuble == null)
            throw new BienLouableException("L'immeuble doit être renseigné", null);
        this.immeuble = immeuble;
    }

    Map<String,Object> getArgs(){
        return Map.of(
                "ComplementAdresse", this.getComplementAdresse(),
                "TypeBien", this.getTypeBienString(),
                "Surface", this.getSurface(),
                "NombrePieces", this.getNbPieces(),
                "NumeroFiscal", this.getNumeroFiscal(),
                "DateAjout", this.getDateAjout(),
                "IdImmeuble", this.getImmeuble().getIdBien(),
                "IdProprio", this.getIdProprio()
        );
    }


    public int getNbPieces() {
        return nbPieces;
    }

    public void setNbPieces(int nbPieces) {
        this.nbPieces = nbPieces;
    }

    @Override
    public void save() throws BienException {
        if(this.getIdBien() != -1)
            throw new BienException("Le bien existe déjà !",null);
        try(UpdateQueryElement updateQueryElement = new UpdateQueryElement(INSERT_QUERY, true)){
            updateQueryElement.setArgs(
                    Map.of(1, this.getComplementAdresse(),
                            2, this.getTypeBienString(),
                            3, this.getSurface(),
                            4, this.getNbPieces(),
                            5, this.getNumeroFiscal(),
                            6, this.getDateAjout(),
                            7, this.getImmeuble().getIdBien(),
                            8, this.getIdProprio()
                    )).execute();
            super.save(updateQueryElement);
            BBuilder.add(this);
        }
        catch (QueryElement.QEltException QEltException){
            throw new BienException("Erreur lors de l'ajout du bien : " + QEltException.getSqlException().getMessage(), QEltException.getSqlException());
        }
    }

    public static List<? extends BienLouable> findAll() throws BienLouableException {
        List<BienLouable> biens = new LinkedList<>();
        try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
            selectQueryElement.execute();
            List<Map<String, Object>> result = selectQueryElement.getResult();
            for (Map<String, Object> args : result) {
                if(TypeBien.valueOf(args.get("TypeBien").toString()) == TypeBien.HABITATION)
                    biens.add(new Habitation.HBuilder(args).build());
                if(TypeBien.valueOf(args.get("TypeBien").toString()) == TypeBien.GARAGE)
                    biens.add(new Garage.GBuilder(args).build());
            }
        }
        catch(QueryElement.QEltException QEltException){
            System.out.println(QEltException.getMessage());
            throw new BienLouableException(STR."Erreur lors de la récupération des biens : \{QEltException.getSqlException().getMessage()}", QEltException.getSqlException());
        }
        return biens;
    }
    public static List<BienLouable> findByImmeuble(int idImmeuble) throws BienException {
        List<BienLouable> biens = new ArrayList<>();
        String query = "SELECT * FROM Bien WHERE IdImmeuble = ? AND (TypeBien = 'HABITATION' OR TypeBien = 'GARAGE')";
        try (SelectQueryElement selectQueryElement = new SelectQueryElement(query)) {
            selectQueryElement.setArgs(Map.of(1, idImmeuble));
            sortResult(biens, selectQueryElement);
        } catch (QueryElement.QEltException e) {
            throw new BienException(STR."Erreur lors de la récupération des biens pour l'immeuble ID \{idImmeuble}", e.getSqlException());
        }

        return biens;
    }



    public static int findIdImmeuble(int idBien) throws BienException {
        try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY_ID)) {
            selectQueryElement.setArgs(Map.of(1, idBien));
            selectQueryElement.execute();
            List<Map<String, Object>> result = selectQueryElement.getResult();
            if (!result.isEmpty()) {
                return Integer.parseInt(result.getFirst().get("IdImmeuble").toString());
            } else {
                throw new BienException("Aucun immeuble trouvé pour le bien avec ID " + idBien, null);
            }
        } catch (QueryElement.QEltException e) {
            throw new BienException("Erreur lors de la récupération de l'ID immeuble pour le bien avec ID " + idBien, e.getSqlException());
        }
    }

    private static void sortResult(List<BienLouable> biens, SelectQueryElement selectQueryElement) throws QueryElement.QEltException {
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
                default:
                    break;
            }
        }
    }

    @Override
    public void modify() throws BienLouableException {
        if(this.getIdBien() == -1)
            throw new BienLouableException("Le bien n'existe pas", null);
        try(UpdateQueryElement updateQueryElement = new UpdateQueryElement(UPDATE_QUERY, true)){
            updateQueryElement.setArgs(
                    Map.of(1, this.getComplementAdresse(),
                            2, this.getSurface(),
                            3, this.getNbPieces(),
                            4, this.getNumeroFiscal(),
                            5, this.getIdProprio(),
                            6, this.getIdBien()
                    )).execute();
        }catch(QueryElement.QEltException QEltException){
            throw new BienLouableException("Erreur lors de la modification du bien : " + QEltException.getMessage(), QEltException.getSqlException());
        }
    }

    @Override
    public void delete() throws BienLouableException {
        try(UpdateQueryElement updateQueryElement = new UpdateQueryElement(DELETE_QUERY, true)){
            for(Bail b : Bail.getBauxFromBien(this))
                b.delete();
            updateQueryElement.setArgs(
                            Map.of(1,this.getIdBien()))
                    .execute();
            super.delete();
        }catch(QueryElement.QEltException QEltException){
            QEltException.getSqlException().printStackTrace();
            throw new BienLouableException("Erreur lors de la suppression du bien", QEltException.getSqlException());
        }
    }
    public abstract static class BLBuilder extends BBuilder{

        private final String complementAdresse;
        private final int nbPieces;
        private final float surface;
        private final Immeuble immeuble;

        public BLBuilder(String complementAdresse,int nbPieces, String numeroFiscal, Immeuble immeuble, float surface, Date dateAjout, String idProprio, int idBien) {
            super(idBien, numeroFiscal, dateAjout, idProprio);
            this.complementAdresse = complementAdresse;
            this.nbPieces = nbPieces;
            this.surface = surface;
            this.immeuble = immeuble;
        }

        public static BienLouable getBienLouable(int idBien) throws BienException {
            try {
                return (BienLouable) getFromId(idBien, TypeBien.GARAGE);
            }catch (BienException e){
                try {
                    return (BienLouable) getFromId(idBien, TypeBien.HABITATION);
                }catch (BienException e1){
                    throw new BienException("Le bien n'existe pas", null);
                }
            }
        }



        public float getSurface() {
            return surface;
        }

        public int getNbPieces() {
            return nbPieces;
        }

        public String getComplementAdresse() {
            return complementAdresse;
        }

        public Immeuble getImmeuble() {
            return immeuble;
        }
    }

    public static class BienLouableException extends BienException{

        public BienLouableException(String message, SQLException sqlException){
            super(message,sqlException);
        }
    }
}
