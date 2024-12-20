package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Garage extends BienLouable {

	public static final String INSERT_QUERY = "INSERT INTO bien (complementAdresse, Adresse, Ville, CodePostal, TypeBien, Surface, NombrePieces, NumeroFiscal, DateAjout) VALUES (?, ?, ?, ?, ?,?,?,?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM bien WHERE TypeBien = 'GARAGE'";

	private static final Map<Integer,Garage> garages = new HashMap<>();

	Garage(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, int idBien) throws BienException {
		super(complementAdresse, nbPieces, NumeroFiscal, immeuble, surface, dateAjout, idBien);
		if((!garages.containsKey(this.getIdBien()) || garages.get(this.getIdBien()) == null) && idBien != -1){
			garages.put(this.getIdBien(),this);
		}
	}

	private Garage(GBuilder gBuilder) throws BienException {
		this(gBuilder.getComplementAdresse(),gBuilder.getNbPieces(),gBuilder.getNumeroFiscal(),gBuilder.getImmeuble(),gBuilder.getSurface(),gBuilder.getDateAjout(),gBuilder.getIdBien());
	}

	public void save() throws BienException {
		super.save();
		garages.put(this.getIdBien(), this);
	}

	public static List<Garage> findAll() throws GarageException {
		List<Garage> garages = new LinkedList<>();
		try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
			selectQueryElement.execute();
			List<Map<String, Object>> result = selectQueryElement.getResult();
			for(Map<String,Object> row : result){
				garages.add(new Garage.GBuilder(row).build());
			}
		} catch (QueryElement.QEltException qEltException) {
			qEltException.printStackTrace();
			throw new GarageException("Erreur lors de la récupération des garages : " + qEltException.getSqlException().getMessage(), qEltException.getSqlException());
		}
		return garages;
	}

	@Override
	public TypeBien getTypeBien() {
		return TypeBien.GARAGE;
	}

	public static class GBuilder extends BLBuilder {
		public GBuilder(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout) {
			this(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, -1);
		}

		GBuilder(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, int idBien) {
			super(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idBien);
		}

		GBuilder(Map<String, Object> args) throws BienException {
			this(args.get("ComplementAdresse").toString(),
					(int) args.get("NombrePieces"),
					args.get("NumeroFiscal").toString(),
					new Immeuble.IBuilder((int) args.get("IdImmeuble")).build(),
					((Double) args.get("Surface")).floatValue(),
					(Date) args.get("DateAjout"),
					(int) args.get("IdBien"));
		}

		@Override
		public Garage build() throws BienException {
			if(garages.containsKey(this.getIdBien()) && garages.get(this.getIdBien()) != null)
				return garages.get(this.getIdBien());
			return new Garage(this);
		}
	}

	public static class GarageException extends Bien.BienException {

		public GarageException(String message) {
			this(message, null);
		}

		public GarageException(String message, SQLException sqlException) {
			super(message, sqlException);
		}
	}
}


