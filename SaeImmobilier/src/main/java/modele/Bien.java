package modele;

import java.util.ArrayList;

public class Bien {
    private int IdBien;
    private String adresse;
    private String ville;
    private int codePostal;
    private Assurance assurance;
    public Bien(String ville, int CodePostal, String adresse) {
        this.ville = ville;
        this.codePostal = CodePostal;
        this.adresse = adresse;
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


}
