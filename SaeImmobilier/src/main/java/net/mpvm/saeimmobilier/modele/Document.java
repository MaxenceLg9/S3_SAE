package net.mpvm.saeimmobilier.modele;

public class Document {
    private int idDocument;
    private String cheminDocument;
    private Date dateAjout;
    private Bail bail;
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
    public Bail getBail() {
        return bail;
    }

    // Setters
    public void setCheminDocument(String cheminDocument) {
        this.cheminDocument = cheminDocument;
    }
    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }
    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }
    public void setBail(Bail bail) {
        this.bail = bail;
    }
}

