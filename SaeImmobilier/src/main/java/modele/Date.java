package modele;
public class Date {
	private Integer Annee;
	private Integer Mois;
	private Integer Jour;
	private String DateComplete;

	public Date(Integer Annee, Integer Mois, Integer Jour) throws IllegalArgumentException {
		if (Jour < 1 || Jour > 31) {
			throw new IllegalArgumentException("Jour pas compris entre 1 et 31");
		}
		if (Mois == 2 && Jour > 29 && Annee % 4 == 0) {
			if (Annee % 100 == 0) {
				if (Annee % 400 != 0) {
					throw new IllegalArgumentException("L'année n'est pas bissextile");
				}
			}
		}
		if (Mois == 2 && Jour > 28 && !(Annee % 4 == 0)) {
			throw new IllegalArgumentException("Jour trop élevé pour février");
		}
		if (Mois == 2 || Mois == 4 || Mois == 6 || Mois == 9 || Mois == 11 && Jour > 30) {
			throw new IllegalArgumentException("Jour trop élevé pour des mois de 30 jours");
		}
		this.Annee = Annee;
		this.Jour = Jour;
		this.Mois = Mois;
		this.DateComplete = this.Jour + "/" + this.Mois + "/" + this.Annee;
	}

	public Integer getAnnee() {
		return this.Annee;
	}

	public Integer getJour() {
		return this.Jour;
	}

	public Integer getMois() {
		return this.Mois;
	}

	public Date setAnnee(Integer annee) {
		if (Mois == 2 && Jour > 29 && Annee % 4 == 0) {
			if (Annee % 100 == 0) {
				if (Annee % 400 != 0) {
					throw new IllegalArgumentException("L'année n'est pas bissextile");
				}
			}
		}
		if (Mois == 2 && Jour > 28 && !(Annee % 4 == 0)) {
			throw new IllegalArgumentException("Jour trop élevé pour février");
		}
		this.Annee = annee;
		this.DateComplete = this.Jour + "/" + this.Mois + "/" + this.Annee;
		return this;
	}

	public Date setJour(Integer jour) {
		if (Jour < 1 || Jour > 31) {
			throw new IllegalArgumentException("Jour pas compris entre 1 et 31");
		}
		if (Mois == 2 && Jour > 29 && Annee % 4 == 0) {
			if (Annee % 100 == 0) {
				if (Annee % 400 != 0) {
					throw new IllegalArgumentException("L'année n'est pas bissextile");
				}
			}
		}
		if (Mois == 2 && Jour > 28 && !(Annee % 4 == 0)) {
			throw new IllegalArgumentException("Jour trop élevé pour février");
		}
		if (Mois == 2 || Mois == 4 || Mois == 6 || Mois == 9 || Mois == 11 && Jour > 30) {
			throw new IllegalArgumentException("Jour trop élevé pour des mois de 30 jours");
		}
		this.Jour = jour;
		this.DateComplete = this.Jour + "/" + this.Mois + "/" + this.Annee;
		return this;
	}

	public Date setMois(Integer mois) throws IllegalArgumentException{
		if (Mois == 2 && Jour > 29 && Annee % 4 == 0) {
			if (Annee % 100 == 0) {
				if (Annee % 400 != 0) {
					throw new IllegalArgumentException("L'année n'est pas bissextile");
				}
			}
		}
		if (Mois == 2 && Jour > 28 && !(Annee % 4 == 0)) {
			throw new IllegalArgumentException("Jour trop élevé pour février");
		}
		if (Mois == 2 || Mois == 4 || Mois == 6 || Mois == 9 || Mois == 11 && Jour > 30) {
			throw new IllegalArgumentException("Jour trop élevé pour des mois de 30 jours");
		}
		this.Mois = mois;
		this.DateComplete = this.Jour + "/" + this.Mois + "/" + this.Annee;
		return this;
	}

	@Override
	public String toString() {
		return this.DateComplete;
	}

}
