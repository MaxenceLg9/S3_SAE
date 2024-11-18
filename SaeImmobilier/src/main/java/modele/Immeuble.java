package modele;

import java.util.ArrayList;
import java.util.List;

public class Immeuble {
	private String adresse;
	private int nouvelIndice;
	private int ancienIndice;
	private float partieFixe;
	private float iR;
	private String ville;
	private int codePostal;
	private String lieuImmeuble;
	private float repartitionElectricité;
	private float repartitionOrduresMenageres;
	private float repartitionEntretien;
	private int iDLocation;
	private String typeBien;
	private List<Logement> biensAssocies;

	public Immeuble(int iDLocation, int codePostal, String adresse) {
		this.iDLocation = iDLocation;
		this.codePostal = codePostal;
		this.adresse = adresse;
		this.biensAssocies = new ArrayList<Logement>();
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public int getNouvelIndice() {
		return this.nouvelIndice;
	}

	public void setNouvelIndice(int nouvelIndice) {
		this.nouvelIndice = nouvelIndice;
	}

	public int getAncienIndice() {
		return this.ancienIndice;
	}

	public void setAncienIndice(int ancienIndice) {
		this.ancienIndice = ancienIndice;
	}

	public float getPartieFixe() {
		return this.partieFixe;
	}

	public void setPartieFixe(float partieFixe) {
		this.partieFixe = partieFixe;
	}

	public float getiR() {
		return this.iR;
	}

	public void setiR(float iR) {
		this.iR = iR;
	}

	public String getVille() {
		return this.ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public int getCodePostal() {
		return this.codePostal;
	}

	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}

	public String getLieuImmeuble() {
		return this.lieuImmeuble;
	}

	public void setLieuImmeuble(String lieuImmeuble) {
		this.lieuImmeuble = lieuImmeuble;
	}

	public float getRepartitionElectricité() {
		return this.repartitionElectricité;
	}

	public void setRepartitionElectricité(float repartitionElectricité) {
		this.repartitionElectricité = repartitionElectricité;
	}

	public float getRepartitionOrduresMenageres() {
		return this.repartitionOrduresMenageres;
	}

	public void setRepartitionOrduresMenageres(float repartitionOrduresMenageres) {
		this.repartitionOrduresMenageres = repartitionOrduresMenageres;
	}

	public float getRepartitionEntretien() {
		return this.repartitionEntretien;
	}

	public void setRepartitionEntretien(float repartitionEntretien) {
		this.repartitionEntretien = repartitionEntretien;
	}

	public int getiDLocation() {
		return this.iDLocation;
	}

	public void setiDLocation(int iDLocation) {
		this.iDLocation = iDLocation;
	}

	public void setTypeBien(String typeBien) throws IllegalArgumentException {
		if (!typeBien.equalsIgnoreCase("bâtiment") && !typeBien.equalsIgnoreCase("logement") && !typeBien.equalsIgnoreCase("garage")) {
			throw new IllegalArgumentException("Le type de bien doit être 'bâtiment', 'logement', ou 'garage'.");
		}
		this.typeBien = typeBien;
	}
	public void associerBien(Immeuble bien) throws IllegalArgumentException {
		if (this.typeBien.equals("bâtiment") && bien.typeBien.equals("bâtiment")) {
			throw new IllegalArgumentException("Les bâtiments ne peuvent pas être associés entre eux.");
		}
		if (!this.typeBien.equals("bâtiment") && bien.typeBien.equals("bâtiment")) {
			throw new IllegalArgumentException("Un bien non bâtiment doit être associé à un bâtiment.");
		}
	}

	public void associerBien(Immeuble bien) {
		if (this instanceof Immeuble && bien instanceof Immeuble) {
			throw new IllegalArgumentException("Un bâtiment ne peut pas être associé à un autre bâtiment.");
		}
		if (!(bien instanceof Immeuble)) {
			this.biensAssocies.add(bien);
		}
	}


}