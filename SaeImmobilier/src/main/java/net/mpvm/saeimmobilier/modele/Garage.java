package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public final class Garage extends BienLouable {

	public static final String INSERT_QUERY = "INSERT INTO bien (complementAdresse, Adresse, Ville, CodePostal, TypeBien, Surface, NombrePieces, NumeroFiscal, DateAjout) VALUES (?, ?, ?, ?, ?,?,?,?,?)";

	Garage(ResultSet rs) throws SQLException, Immeuble.ImmeubleException {
		this(rs.getString("complementAdresse"),
				rs.getInt("NombrePieces"),
				rs.getString("NumeroFiscal"),
				Immeuble.getFromId(rs.getInt("IdImmeuble")),
				rs.getFloat("Surface"),
				rs.getDate("DateAjout"),
				rs.getInt("IdBien"));
	}

	public Garage(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout) {
		this(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout,-1);
	}

	Garage(String complementAdresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout, int idBien) {
		super(complementAdresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout,idBien);
	}

	@Override
	public TypeBien getTypeBien() {
		return TypeBien.GARAGE;
	}

	public static class GarageException extends Bien.BienException {

		public GarageException(String message) {
			this(message,null);
		}

		public GarageException(String message, SQLException sqlException) {
			super(message, sqlException);
		}
	}
}


