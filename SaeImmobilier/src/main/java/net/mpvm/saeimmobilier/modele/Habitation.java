package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.Date;
import java.util.Map;

public class Habitation extends BienLouable {

    public static final String INSERT_QUERY = "INSERT INTO bien (Lieu_Immeuble, Adresse, Ville, CodePostal, TypeBien, Surface, NombrePieces, NumeroFiscal, DateAjout) VALUES (?, ?, ?, ?, ?,?,?,?,?)";

    @Override
    public TypeBien getTypeBien() {
        return TypeBien.HABITATION;
    }

    @Override
    public String getTypeBienString() {
        return "GARAGE";
    }


    public Habitation(String lieuImmeuble,String ville, int codePostal, String adresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout) {
        super(lieuImmeuble,ville,codePostal,adresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout);
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
            throw new BienLouableException("Erreur lors de l'ajout du bien", queryException.getSqlException());
        }
    }
}


