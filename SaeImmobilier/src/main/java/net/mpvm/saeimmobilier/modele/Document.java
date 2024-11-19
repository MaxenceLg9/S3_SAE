package modele;

import java.util.Date;

public class Document {
    private int idDocument;
    private String cheminDocument;
    private Date dateAjout;

    // Constructeur privé qui demande tous les attributs
    private Document(int idDocument, String cheminDocument, Date dateAjout) {
        this.idDocument = idDocument;
        this.cheminDocument = cheminDocument;
        this.dateAjout = dateAjout;
    }

    // Constructeur public qui demande tous les attributs sauf idDocument
    public Document(String cheminDocument, Date dateAjout) {
        this.cheminDocument = cheminDocument;
        this.dateAjout = dateAjout;
    }

    // Getters
    public int getIdDocument() {
        return idDocument;
    }

    public String getCheminDocument() {
        return cheminDocument;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    // Setters
    public void setCheminDocument(String cheminDocument) {
        this.cheminDocument = cheminDocument;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }
}