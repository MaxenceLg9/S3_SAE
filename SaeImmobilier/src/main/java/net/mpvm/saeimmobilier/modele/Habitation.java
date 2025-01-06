package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Habitation extends BienLouable {

    public static final String SELECT_QUERY = "SELECT * FROM Bien WHERE TypeBien = 'Habitation'";

    private static final Map<Integer,Habitation> habitations = new HashMap<>();

    @Override
    public TypeBien getTypeBien() {
        return TypeBien.HABITATION;
    }

    private Habitation(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, int idBien) throws BienException {
        super(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idBien);
        if((!habitations.containsKey(this.getIdBien()) || habitations.get(this.getIdBien()) == null) && idBien != -1){
            habitations.put(this.getIdBien(),this);
        }
    }

    private Habitation(HBuilder hBuilder) throws BienException {
        this(hBuilder.getComplementAdresse(),hBuilder.getNbPieces(),hBuilder.getNumeroFiscal(),hBuilder.getImmeuble(),hBuilder.getSurface(),hBuilder.getDateAjout(),hBuilder.getIdBien());
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

    public void save() throws BienException {
        super.save();
        habitations.put(this.getIdBien(), this);
    }

    public static class HBuilder extends BLBuilder {
        public HBuilder(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout) {
            this(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, -1);
        }

        HBuilder(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, int idBien) {
            super(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idBien);
        }

        HBuilder(Map<String, Object> args) throws BienException {
            this(args.get("ComplementAdresse").toString(),
                    (int) args.get("NombrePieces"),
                    args.get("NumeroFiscal").toString(),
                    new Immeuble.IBuilder((int) args.get("IdImmeuble")).build(),
                    ((Double) args.get("Surface")).floatValue(),
                    (Date) args.get("DateAjout"),
                    (int) args.get("IdBien"));
        }

        @Override
        public Habitation build() throws BienException {
            if(habitations.containsKey(this.getIdBien()) && habitations.get(this.getIdBien()) != null)
                return habitations.get(this.getIdBien());
            return new Habitation(this);
        }
    }

    public static class HabitationException extends BienLouableException{

        public HabitationException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }
}


