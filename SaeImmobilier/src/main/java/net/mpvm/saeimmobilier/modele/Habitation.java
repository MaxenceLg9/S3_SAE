package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Habitation extends BienLouable {

    public static final Habitation HABITATION;

    static {
        try {
            HABITATION = new HBuilder("RATATA", 1, "1", Immeuble.IMMEUBLE, 1, "Appartement du Batiment", Date.valueOf(LocalDate.now())).build();
        } catch (BienException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String SELECT_QUERY = "SELECT * FROM Bien WHERE TypeBien = 'Habitation'";

    private Habitation(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, String idProprio, int idBien) throws BienException {
        super(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idProprio, idBien);
    }

    private Habitation(HBuilder hBuilder) throws BienException {
        this(hBuilder.getComplementAdresse(),hBuilder.getNbPieces(),hBuilder.getNumeroFiscal(),hBuilder.getImmeuble(),hBuilder.getSurface(),hBuilder.getDateAjout(),hBuilder.getIdProprio(),hBuilder.getIdBien());
    }

    public static List<Habitation> findAll() throws HabitationException {
        List<Habitation> habitations = new LinkedList<>();
        try(SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)){
            selectQueryElement.execute();
            List<Map<String,Object>> result = selectQueryElement.getResult();
            for(Map<String, Object> row : result){
                habitations.add(new Habitation.HBuilder(row).build());
            }
            return habitations;
        }catch(QueryElement.QEltException qEltException){
            System.out.println(qEltException.getMessage());
            throw new HabitationException("Impossible de récupérer les habitations : " + qEltException.getSqlException().getMessage(), qEltException.getSqlException());
        }
    }

    @Override
    public TypeBien getTypeBien() {
        return TypeBien.HABITATION;
    }

    public static class HBuilder extends BLBuilder {
        public HBuilder(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface,  String idProprio, @NotNull Date dateAjout) {
            this(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idProprio, -1);
        }

        HBuilder(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, String idProprio, int idBien) {
            super(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idProprio, idBien);
        }

        HBuilder(Map<String, Object> args) throws BienException {
            this(args.get("ComplementAdresse").toString(),
                    (int) args.get("NombrePieces"),
                    args.get("NumeroFiscal").toString(),
                    Immeuble.IBuilder.getImmeuble((int) args.get("IdImmeuble")),
                    ((Double) args.get("Surface")).floatValue(),
                    (Date) args.get("DateAjout"),
                    args.get("IdProprio").toString(),
                    (int) args.get("IdBien"));
        }

        @Override
        public Habitation build() throws BienException {
            if(this.getIdBien() == -1)
                return new Habitation(this);
            if(checkPresentIn()){
                if(get(this.getIdBien()) instanceof Habitation)
                    return (Habitation) get(this.getIdBien());
                else
                    throw new Bien.BienException("Le bien n'est pas du bon type", null);
            }
            Habitation h = new Habitation(this);
            add(h);
            return h;
        }
    }

    public static class HabitationException extends BienLouableException{

        public HabitationException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }
}


