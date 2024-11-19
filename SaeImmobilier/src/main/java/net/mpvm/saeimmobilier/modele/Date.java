package net.mpvm.saeimmobilier.modele;

public class Date {
	private Integer annee;
	private Integer mois;
	private Integer jour;
	private String dateComplete;

	// Constructeur avec validation
	public Date(Integer annee, Integer mois, Integer jour) {
		if (!isValidDate(annee, mois, jour)) {
			throw new IllegalArgumentException("Date invalide : " + jour + "/" + mois + "/" + annee);
		}
		this.annee = annee;
		this.mois = mois;
		this.jour = jour;
		updateDateComplete();
	}

	// Getters
	public Integer getAnnee() {
		return this.annee;
	}

	public Integer getMois() {
		return this.mois;
	}

	public Integer getJour() {
		return this.jour;
	}

	// Setters avec validation
	public void setAnnee(Integer annee) {
		if (!isValidDate(annee, this.mois, this.jour)) {
			throw new IllegalArgumentException("Date invalide avec cette année : " + jour + "/" + mois + "/" + annee);
		}
		this.annee = annee;
		updateDateComplete();
	}

	public void setMois(Integer mois) {
		if (!isValidDate(this.annee, mois, this.jour)) {
			throw new IllegalArgumentException("Date invalide avec ce mois : " + jour + "/" + mois + "/" + annee);
		}
		this.mois = mois;
		updateDateComplete();
	}

	public void setJour(Integer jour) {
		if (!isValidDate(this.annee, this.mois, jour)) {
			throw new IllegalArgumentException("Date invalide avec ce jour : " + jour + "/" + mois + "/" + annee);
		}
		this.jour = jour;
		updateDateComplete();
	}

	// Mise à jour de la date complète sous forme de chaîne
	private void updateDateComplete() {
		this.dateComplete = this.jour + "/" + this.mois + "/" + this.annee;
	}

	// Méthode de validation des dates
	private boolean isValidDate(Integer annee, Integer mois, Integer jour) {
		if (mois < 1 || mois > 12 || jour < 1) {
			return false;
		}
		int maxJour = getDaysInMonth(annee, mois);
		return jour <= maxJour;
	}

	// Retourne le nombre de jours dans un mois donné
	private int getDaysInMonth(Integer annee, Integer mois) {
		switch (mois) {
			case 2:
				return isLeapYear(annee) ? 29 : 28;
			case 4: case 6: case 9: case 11:
				return 30;
			default:
				return 31;
		}
	}
	public Date addMonths(int months) {
		int newMois = this.mois + months;
		int newAnnee = this.annee;

		// Ajuster l'année et le mois si le mois dépasse 12 ou est négatif
		while (newMois > 12) {
			newMois -= 12;
			newAnnee++;
		}
		while (newMois < 1) {
			newMois += 12;
			newAnnee--;
		}

		// Ajuster le jour si nécessaire
		int maxJour = getDaysInMonth(newAnnee, newMois);
		int newJour = Math.min(this.jour, maxJour);

		return new Date(newAnnee, newMois, newJour);
	}
	// Vérifie si une année est bissextile
	private boolean isLeapYear(Integer annee) {
		if (annee % 4 != 0) {
			return false;
		}
		if (annee % 100 == 0 && annee % 400 != 0) {
			return false;
		}
		return true;
	}

	// Représentation de l'objet sous forme de chaîne
	@Override
	public String toString() {
		return this.dateComplete;
	}
}
