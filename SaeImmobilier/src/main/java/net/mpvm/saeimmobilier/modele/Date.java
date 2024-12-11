package net.mpvm.saeimmobilier.modele;


import java.time.LocalDate;
import java.time.ZoneId;

public class Date {
	private Integer annee;
	private Integer mois;
	private Integer jour;
	private String dateComplete;

	// Constructeur avec validation
	public Date(Integer annee, Integer mois, Integer jour) {
		if (isNotValidDate(annee, mois, jour)) {
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
		if (isNotValidDate(annee, this.mois, this.jour)) {
			throw new IllegalArgumentException("Date invalide avec cette année : " + jour + "/" + mois + "/" + annee);
		}
		this.annee = annee;
		updateDateComplete();
	}

	public void setMois(Integer mois) {
		if (isNotValidDate(this.annee, mois, this.jour)) {
			throw new IllegalArgumentException("Date invalide avec ce mois : " + jour + "/" + mois + "/" + annee);
		}
		this.mois = mois;
		updateDateComplete();
	}

	public void setJour(Integer jour) {
		if (isNotValidDate(this.annee, this.mois, jour)) {
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
	private boolean isNotValidDate(Integer annee, Integer mois, Integer jour) {
		if (mois < 1 || mois > 12 || jour < 1) {
			return true;
		}
		int maxJour = getDaysInMonth(annee, mois);
		return jour > maxJour;
	}

	// Retourne le nombre de jours dans un mois donné
	private int getDaysInMonth(Integer annee, Integer mois) {
        return switch (mois) {
            case 2 -> isLeapYear(annee) ? 29 : 28;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };
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
        return annee % 100 != 0 || annee % 400 == 0;
    }

	public String getDateComplete() {
		return dateComplete;
	}

	public void setDateComplete(String dateComplete) {
		this.dateComplete = dateComplete;
	}

	// Représentation de l'objet sous forme de chaîne
	@Override
	public String toString() {
		return this.dateComplete;
	}

	public static Date getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		int annee = currentDate.getYear();
		int mois = currentDate.getMonthValue();
		int jour = currentDate.getDayOfMonth();
		return new Date(annee, mois, jour);
	}

	public long getCurrentDateAsLong() {
		// Obtenir la date actuelle
		LocalDate currentDate = LocalDate.now();
		// Convertir en Instant (à minuit de ce jour-là, par défaut UTC)
        return currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

}
