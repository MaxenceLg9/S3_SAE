package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Garage extends BienLouable {

	public static final String SELECT_QUERY = "SELECT * FROM bien WHERE TypeBien = 'GARAGE'";


	Garage(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, String idProprio, int idBien) throws BienException {
		super(complementAdresse, nbPieces, NumeroFiscal, immeuble, surface, dateAjout, idProprio, idBien);
	}

	private Garage(GBuilder gBuilder) throws BienException {
		this(gBuilder.getComplementAdresse(),gBuilder.getNbPieces(),gBuilder.getNumeroFiscal(),gBuilder.getImmeuble(),gBuilder.getSurface(),gBuilder.getDateAjout(), gBuilder.getIdProprio(), gBuilder.getIdBien());
	}

	public void save() throws BienException {
		super.save();
		BBuilder.add(this);
	}

	@NotNull
	public static List<Garage> findAll() throws GarageException {
		List<Garage> garages = new LinkedList<>();
		try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
			selectQueryElement.execute();
			List<Map<String, Object>> result = selectQueryElement.getResult();
			for(Map<String,Object> row : result){
				garages.add(new Garage.GBuilder(row).build());
			}
		} catch (QueryElement.QEltException qEltException) {
			System.out.println(qEltException.getMessage());
			throw new GarageException("Erreur lors de la récupération des garages : " + qEltException.getSqlException().getMessage(), qEltException.getSqlException());
		}
		return garages;
	}

	@Override
	public TypeBien getTypeBien() {
		return TypeBien.GARAGE;
	}

	public static class GBuilder extends BLBuilder {
		
		public GBuilder(String complementAdresse, int nbPieces, String NumeroFiscal, @NotNull Immeuble immeuble, float surface, String idProprio, @NotNull Date dateAjout) {
			this(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idProprio, -1);
		}

		GBuilder(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, String idProprio, int idBien) {
			super(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout, idProprio, idBien);
		}

		GBuilder(Map<String, Object> args) throws BienException {
			this(args.get("ComplementAdresse").toString(),
					(int) args.get("NombrePieces"),
					args.get("NumeroFiscal").toString(),
					new Immeuble.IBuilder((int) args.get("IdImmeuble")).build(),
					((Double) args.get("Surface")).floatValue(),
					(Date) args.get("DateAjout"),
					args.get("IdProprio").toString(),
					(int) args.get("IdBien"));
		}

		@Override
		public Garage build() throws BienException {
			if(this.getIdBien() == -1)
				return new Garage(this);
			if(checkNotPresentIn(Garage.class))
				return (Garage) get(this.getIdBien());
			Garage garage = new Garage(this);
			add(garage);
			return garage;
		}
	}

	public static class GarageException extends BienLouable.BienLouableException {

		public GarageException(String message, SQLException sqlException) {
			super(message, sqlException);
		}
	}
}


