package net.mpvm.saeimmobilier.modele;

public class Bien {
    private int IdBien;
    private String adresse;
    private String ville;
    private int codePostal;
    private Assurance assurance;
    private float iR; // Taux d'intérêt ou autre valeur
    private Bien(int IdBien, String ville,int CodePostal,String adresse) {
        this.IdBien = IdBien;
        this.ville = ville;
        this.codePostal = CodePostal;
        this.adresse = adresse;
    }

    public Bien(String ville, int CodePostal, String adresse) {
        this.ville = ville;
        this.codePostal = CodePostal;
        this.adresse = adresse;
    }
    public int getIdBien() {
        return IdBien;
    }
    public void setIdBien(int IdBien) {
        this.IdBien = IdBien;
    }
    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }
    public Assurance getAssurance() {
        return this.assurance;
    }
    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public float getiR() {
        return iR;
    }

    public void setiR(float iR) {
        this.iR = iR;
    }

}
