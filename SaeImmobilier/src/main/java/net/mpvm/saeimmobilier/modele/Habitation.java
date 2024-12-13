package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public final class Habitation extends BienLouable {

    public static final String INSERT_QUERY = "INSERT INTO bien (Lieu_Immeuble, Adresse, Ville, CodePostal, TypeBien, Surface, NombrePieces, NumeroFiscal, DateAjout) VALUES (?, ?, ?, ?, ?,?,?,?,?)";

    @Override
    public TypeBien getTypeBien() {
        return TypeBien.HABITATION;
    }


    public Habitation(String complementAdresse,String ville, int codePostal, String adresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout) {
        this(complementAdresse,ville,codePostal,adresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, -1);
    }

    private Habitation(String complementAdresse,String ville, int codePostal, String adresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, int idBien) {
        super(complementAdresse,ville,codePostal,adresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idBien);
    }

    Habitation(ResultSet rs) throws SQLException, Immeuble.ImmeubleException {
        this(rs.getString("complementAdresse"),
                rs.getString("Ville"),
                rs.getInt("CodePostal"),
                rs.getString("Adresse"),
                rs.getInt("NombrePieces"),
                rs.getString("NumeroFiscal"),
                Immeuble.getFromId(rs.getInt("IdImmeuble")),
                rs.getFloat("Surface"),
                rs.getDate("DateAjout"),
                rs.getInt("IdBien"));
    }

    @Override
    public void save() throws QueryableException {
        if(this.getIdBien() != -1)
            throw new Queryable.QueryableException("Le bien existe déjà !");
        try(UpdateQueryElement query = new UpdateQueryElement(INSERT_QUERY, true)){
            query.setArgs(
                    Map.of(1,this.getLieuImmeuble(),
                            2, this.getAdresse(),
                            3, this.getVille(),
                            4, this.getCodePostal(),
                            5, this.getTypeBienString(),
                            6, this.getSurface(),
                            7, this.getNbPieces(),
                            8, this.getNumeroFiscal(),
                            9, this.getDateAjout()
                    )).execute();
        }
        catch (QueryElement.QueryException queryException){
            queryException.getSqlException().printStackTrace();
            throw new Bien.BienException("Erreur lors de l'ajout du bien", queryException.getSqlException());
        }
    }

    public static class HabitationException extends BienException{

        public HabitationException(String message) {
            this(message,null);
        }

        public HabitationException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }
}


