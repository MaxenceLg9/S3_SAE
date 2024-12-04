package net.mpvm.saeimmobilier.modele;


public class Garage extends BienLouable {

	@Override
	public TypeBien getTypeBien() {
		return TypeBien.GARAGE;
	}

	public Garage(String ville, int codePostal, String adresse, int nbPieces, int NumeroFiscal, Immeuble immeuble,float surface,Date dateAjout) {
		super(ville,codePostal,adresse, nbPieces,NumeroFiscal,immeuble,surface,dateAjout);
	}

}
