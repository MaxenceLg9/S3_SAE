package net.mpvm.saeimmobilier.modele;
public class EtatDesLieux extends Document {
    private int idEtatDesLieux;
    private ModeleDate dateSignature;


    // Constructeur privé qui demande tous les attributs (y compris ceux de la classe parente)
    private EtatDesLieux(int idEtatDesLieux, String cheminDocument, ModeleDate dateAjout, ModeleDate dateSignature) {
        super(cheminDocument, dateAjout); // Appelle le constructeur de la classe parente
        this.idEtatDesLieux = idEtatDesLieux;
        this.dateSignature = dateSignature;
    }

    // Constructeur public qui demande tous les attributs sauf idEtatDesLieux
    public EtatDesLieux(String cheminDocument, ModeleDate dateAjout, ModeleDate dateSignature) {
        super(cheminDocument, dateAjout); // Appelle le constructeur de la classe parente
        this.dateSignature = dateSignature;
    }

    // Getters
    public int getIdEtatDesLieux() {
        return idEtatDesLieux;
    }

    public ModeleDate getDateSignature() {
        return dateSignature;
    }

    // Setters
    public void setDateSignature(ModeleDate dateSignature) {
        this.dateSignature = dateSignature;
    }
    public void setIdEtatDesLieux(int idEtatDesLieux) {
        this.idEtatDesLieux = idEtatDesLieux;
    }

}
