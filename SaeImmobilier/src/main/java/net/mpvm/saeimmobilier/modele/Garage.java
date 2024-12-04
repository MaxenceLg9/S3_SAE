package net.mpvm.saeimmobilier.modele;


import java.sql.Date;

public class Garage extends BienLouable {

	@Override
	public TypeBien getTypeBien() {
		return TypeBien.GARAGE;
	}

	@Override
	public String getTypeBienString() {
		return "GARAGE";
	}


	public Garage(String ville, int codePostal, String adresse, int nbPieces, String NumeroFiscal, Immeuble immeuble, float surface, Date dateAjout) {
		super(ville,codePostal,adresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout);
	}

}
