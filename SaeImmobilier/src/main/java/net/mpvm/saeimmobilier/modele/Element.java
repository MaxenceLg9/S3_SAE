package net.mpvm.saeimmobilier.modele;
import java.util.ArrayList;

public class Element {

    private Etat etat;
    private int IdElement;
    private ArrayList<EtatDesLieux> etatsLieux;

    private Element(int IdElement, Etat etat) {
        this.etat = etat;
        this.IdElement = IdElement;
        this.etatsLieux = new ArrayList<>();
    }

    public Element(Etat etat) {
        this.etat = etat;
        this.etatsLieux = new ArrayList<>();
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public int getIdElement() {
        return IdElement;
    }
    public void setIdElement(int IdElement) {
        this.IdElement = IdElement;
    }
    public ArrayList<EtatDesLieux> getEtatsLieux() {
        return etatsLieux;
    }
    public void setEtatsLieux(ArrayList<EtatDesLieux> etatsLieux) {
        this.etatsLieux = etatsLieux;
    }
    public void addEtatDesLieux(EtatDesLieux etat) {
        etatsLieux.add(etat);
    }

    public enum Etat {
        NEUF,
        BON_ETAT,
        ETAT_D_USAGE,
        MAUVAIS_ETAT
    }
}
