package modele;

import java.util.Date;

public class Caution {
    private int idCaution;
    private Date dateNaissance;
    private String lieuNaissance;
    private String situationFamiliale;
    private String profession;
    private String employeur;
    private String typeContratTravail;
    private float remuneration;
    private float autresRevenus;
    private float totalRevenus;

    // Constructeur privé pour idCaution
    private Caution(int idCaution, Date dateNaissance, String lieuNaissance, String situationFamiliale,
                   String profession, String employeur, String typeContratTravail,
                   float remuneration, float autresRevenus, float totalRevenus) {
        this.idCaution = idCaution;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.situationFamiliale = situationFamiliale;
        this.profession = profession;
        this.employeur = employeur;
        this.typeContratTravail = typeContratTravail;
        this.remuneration = remuneration;
        this.autresRevenus = autresRevenus;
        this.totalRevenus = totalRevenus;
    }

    // Constructeur public (sans idCaution)
    public Caution(Date dateNaissance, String lieuNaissance, String situationFamiliale,
                   String profession, String employeur, String typeContratTravail,
                   float remuneration, float autresRevenus, float totalRevenus) {
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.situationFamiliale = situationFamiliale;
        this.profession = profession;
        this.employeur = employeur;
        this.typeContratTravail = typeContratTravail;
        this.remuneration = remuneration;
        this.autresRevenus = autresRevenus;
        this.totalRevenus = totalRevenus;
    }

    // Getters
    public Date getDateNaissance() {
        return dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public String getSituationFamiliale() {
        return situationFamiliale;
    }

    public String getProfession() {
        return profession;
    }

    public String getEmployeur() {
        return employeur;
    }

    public String getTypeContratTravail() {
        return typeContratTravail;
    }

    public float getRemuneration() {
        return remuneration;
    }

    public float getAutresRevenus() {
        return autresRevenus;
    }

    public float getTotalRevenus() {
        return totalRevenus;
    }

    // Setters
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public void setSituationFamiliale(String situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    public void setTypeContratTravail(String typeContratTravail) {
        this.typeContratTravail = typeContratTravail;
    }

    public void setRemuneration(float remuneration) {
        this.remuneration = remuneration;
    }

    public void setAutresRevenus(float autresRevenus) {
        this.autresRevenus = autresRevenus;
    }

    public void setTotalRevenus(float totalRevenus) {
        this.totalRevenus = totalRevenus;
    }
}
