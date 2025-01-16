package net.mpvm.saeimmobilier.modele;


import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.util.Unfinished;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Charges {

    private int idCharges;
    private float montant;
    private Date dateReleve;
    private int idBail;

    // Constructeur
    private Charges(int idCharges, Date dateReleve) {
        this.idCharges = idCharges;
        this.dateReleve = dateReleve;
    }

    public Charges(Date dateReleve) {
        this.dateReleve=dateReleve;
    }

    public static List<Charges> getChargesFromBail(Bail bail) {
        ArrayList<Charges> liste=new ArrayList<Charges>();
        return liste;
    }

    // Getters et Setters

    @Unfinished
    public Bail getBail() {
        //TODO : query
        return null;
    }


    public int getIdCharges() {
        return idCharges;
    }

    public void setIdCharges(int idCharges) {
        this.idCharges = idCharges;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant des charges ne peut pas être négatif.");
        }
        this.montant = montant;
    }
    public static double calculerChargesProprietaire() throws Exception {
        final String SELECT_QUERY = """
        SELECT
            SUM(C.Montant) AS Total
            FROM Charges C
            JOIN Bail B ON C.IdBail = B.IdBail
            WHERE B.Archive = FALSE
            """;

        double totalCharges = 0.0;

        try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
            selectQueryElement.execute();
            List<Map<String, Object>> result = selectQueryElement.getResult();

            if (!result.isEmpty() && result.get(0).get("TotalCharges") != null) {
                totalCharges = (double) result.get(0).get("TotalCharges");
            }
        } catch (QueryElement.QEltException qEltException) {
            qEltException.getSqlException().printStackTrace();
            throw new Exception("Erreur lors du calcul des charges pour le propriétaire.", qEltException.getSqlException());
        }

        return totalCharges;
    }
    public static List<Charges> getChargesDetails() throws ChargesException {
        List<Charges> chargesDetails = new ArrayList<>();
        final String SELECT_QUERY = """
        SELECT IdCharges,DateCharge, Montant, TypeCharges
        FROM Charges
    """;

        try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
            query.execute();
            List<Map<String, Object>> result = query.getResult();

            // Parcourir chaque ligne de résultat et ajouter à la liste
            for (Map<String, Object> row : result) {
                switch((String) row.get("TypeCharges")){
                    case "Eau":
                        Charges ce=new ChargeEau((int) row.get("IdCharges"),(Date) row.get("DateCharge")) ;
                        ce.setMontant((float) row.get("Montant"));
                        chargesDetails.add(ce);
                        break;
                    case "Provision sur charge":
                        Charges pc=new ProvisionCharge((int) row.get("IdCharges"),(Date) row.get("DateCharge")) ;
                        pc.setMontant((float) row.get("Montant"));
                        chargesDetails.add(pc);
                        break;
                    case "Ordures Ménagères":
                        Charges om=new ChargeOrduresMenageres((int) row.get("IdCharges"),(Date) row.get("DateCharge")) ;
                        om.setMontant((float) row.get("Montant"));
                        chargesDetails.add(om);
                        break;
                    case "Électricité":
                        Charges cel=new ChargeElectricite((int) row.get("IdCharges"),(Date) row.get("DateCharge")) ;
                        cel.setMontant((float) row.get("Montant"));
                        chargesDetails.add(cel);
                        break;
                    case "Entretien":
                        Charges cen=new ChargeEntretien((int) row.get("IdCharges"),(Date) row.get("DateCharge")) ;
                        cen.setMontant((float) row.get("Montant"));
                        chargesDetails.add(cen);
                        break;
                }


            }
        } catch (QueryElement.QEltException qEltException) {
            throw new ChargesException(
                    "Erreur lors de la récupération des détails des charges.",
                    qEltException.getSqlException()
            );
        }

        return chargesDetails;
    }

    @Unfinished
    public static List<Charges> getChargesFromLocataire(Locataire locataire) throws ChargesException {
        List<Charges> chargesList = new LinkedList<>();
        final String SELECT_QUERY = """
        SELECT C.Montant 
        FROM Charges C
        JOIN Bail B ON C.IdBail = B.IdBail
        JOIN AssocieBailLocataire ABL ON B.IdBail = ABL.IdBail
        WHERE ABL.IdLocataire = ?""";

        try (SelectQueryElement selectQueryElement = new SelectQueryElement(SELECT_QUERY)) {
            selectQueryElement.setArgs(Map.of(1, locataire.getIdLocataire())); // Assuming getId() retrieves the current Locataire's ID.
            selectQueryElement.execute();
            List<Map<String, Object>> result = selectQueryElement.getResult();
            for (Map<String, Object> row : result) {
                //TODO : unfinished constructor de merde
//                chargesList.add(new Charges((int) row.get("IdCharges"),(Date) row.get("DateCharge")));
            }
        } catch (QueryElement.QEltException qEltException) {
            qEltException.getSqlException().printStackTrace();
            throw new ChargesException("Erreur lors de la récupération des charges du locataire", qEltException.getSqlException());
        }
        return chargesList;
    }

    public Date getDateReleve() {
        return dateReleve;
    }

    public void setDateReleve(Date dateReleve) {
        this.dateReleve = dateReleve;
    }

    // Méthode pour valider la régularisation des charges
    public boolean validerRegularisationCharges(float montantVerse, float montantDu) {
        return Math.abs(montantVerse - montantDu) <= 0.01; // Tolérance d'arrondi
    }

    public void save() throws ChargesException {
        // Vérifier si la charge existe déjà dans la base de données (id différent de -1)
        if (this.idCharges != -1) {
            throw new ChargesException("Cette charge existe déjà dans la table.", null);
        }

        // Validation des données
        if (this.montant < 0) {
            throw new ChargesException("Le montant de la charge ne peut pas être négatif.", null);
        }
        if (this.dateReleve == null) {
            throw new ChargesException("La date de la charge est obligatoire.", null);
        }

        // Préparer la requête SQL
        String INSERT_QUERY = """
        INSERT INTO Charges (Montant, DateCharge, TypeCharges, NouvelIndice, AncienIndice, PartieFixe, PartieVariable, IdBail)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (UpdateQueryElement query = new UpdateQueryElement(INSERT_QUERY, true)) {
            // Déterminer les valeurs spécifiques selon le type de charge
            Map<Integer, Object> args = switch (this) {
                case ProvisionCharge pc -> Map.of(
                        1, this.getMontant(),
                        2, this.getDateReleve(),
                        3, "Provision sur charge",
                        4, null, // NouvelIndice
                        5, null, // AncienIndice
                        6, null, // PartieFixe
                        7, null, // PartieVariable
                        8, this.getIdBail()
                );
                case ChargeEau ce -> Map.of(
                        1, this.getMontant(),
                        2, this.getDateReleve(),
                        3, "Eau",
                        4, ce.getNouvelIndice(),
                        5, ce.getAncienIndice(),
                        6, ce.getPartieFixe(),
                        7, ce.getPartieVariable(),
                        8, this.getIdBail()
                );
                case ChargeEntretien ce -> Map.of(
                        1, this.getMontant(),
                        2, this.getDateReleve(),
                        3, "Entretien",
                        4, null, // NouvelIndice
                        5, null, // AncienIndice
                        6, null, // PartieFixe
                        7, null, // PartieVariable
                        8, this.getIdBail()
                );
                case ChargeOrduresMenageres com -> Map.of(
                        1, this.getMontant(),
                        2, this.getDateReleve(),
                        3, "Ordures ménagères",
                        4, null, // NouvelIndice
                        5, null, // AncienIndice
                        6, null, // PartieFixe
                        7, null, // PartieVariable
                        8, this.getIdBail()
                );
                case ChargeElectricite ce -> Map.of(
                        1, this.getMontant(),
                        2, this.getDateReleve(),
                        3, "Électricité",
                        4, null, // NouvelIndice
                        5, null, // AncienIndice
                        6, null, // PartieFixe
                        7, null, // PartieVariable
                        8, this.getIdBail()
                );
                default -> throw new ChargesException("Type de charge inconnu ou non supporté.", null);
            };

            // Exécuter la requête avec les arguments
            query.setArgs(args).execute();

            System.out.println("Charge ajoutée avec succès.");
        } catch (QueryElement.QEltException qEltException) {
            throw new ChargesException(
                    "Erreur lors de l'ajout de la charge : " + qEltException.getSqlException().getMessage(),
                    qEltException.getSqlException()
            );
        }
    }

    private int getIdBail() {
        return this.idBail;
    }


    public void setIdBail(int idBail) {
        this.idBail=idBail;
    }
    public static class ProvisionCharge extends Charges {
        private float provision;
        public ProvisionCharge(int idCharges, Date dateReleve) {
            super(idCharges,dateReleve);
        }
        public ProvisionCharge(Date dateReleve) {
            this(-1,dateReleve);
        }
        public void setProvision(float provision){
            this.provision=provision;
        }
        public float getProvision(){
            return provision;
        }


    }
    public static class ChargeOrduresMenageres extends Charges {

        public ChargeOrduresMenageres(int idCharges, Date dateReleve) {
            super(idCharges,dateReleve);
        }
        public ChargeOrduresMenageres(Date dateReleve) {
            this(-1,dateReleve);
        }

    }

    public static class ChargeEntretien extends Charges {

        private ChargeEntretien(int idCharges,Date dateReleve) {
            super(idCharges,dateReleve);
        }
        // Constructeur
        public ChargeEntretien(Date dateReleve) {
            this(-1,dateReleve);
        }

    }

    public static class ChargeElectricite extends Charges {

        private ChargeElectricite(int idCharges,Date dateReleve) {
            super(idCharges,dateReleve);
        }
        // Constructeur
        public ChargeElectricite(Date dateReleve) {
            this(-1,dateReleve);
        }

    }

    public static class ChargeEau extends Charges {
        private int NouvelIndice;
        private int AncienIndice; // Ajouté pour le calcul
        private float PartieFixe;
        private float PartieVariable;

        // Constructeur privé
        private ChargeEau(int idCharges, Date dateReleve) {
            super(idCharges, dateReleve);
        }

        // Constructeur public
        public ChargeEau(Date dateReleve) {
            this(-1, dateReleve);
        }

        // Calculer le montant à partir des indices et des parties fixe/variable
        public double calculerMontant() {
            if (NouvelIndice < AncienIndice) {
                throw new IllegalArgumentException("Le nouvel indice ne peut pas être inférieur à l'ancien indice.");
            }

            // Calcul du volume consommé
            int volumeConsommé = NouvelIndice - AncienIndice;

            // Calcul du montant
            float montant = PartieFixe + (volumeConsommé * PartieVariable);

            // Appliquer le montant calculé à l'objet
            this.setMontant(montant);
            return 0;
        }

        // Getters et setters
        public int getNouvelIndice() {
            return NouvelIndice;
        }

        public void setNouvelIndice(int NouvelIndice) throws IllegalArgumentException {
            if (NouvelIndice < 0) {
                throw new IllegalArgumentException("L'indice doit être un entier positif.");
            }
            this.NouvelIndice = NouvelIndice;
        }

        public int getAncienIndice() {
            return AncienIndice;
        }

        public void setAncienIndice(int AncienIndice) throws IllegalArgumentException {
            if (AncienIndice < 0) {
                throw new IllegalArgumentException("L'indice doit être un entier positif.");
            }
            this.AncienIndice = AncienIndice;
        }

        public float getPartieFixe() {
            return PartieFixe;
        }

        public void setPartieFixe(float PartieFixe) {
            if (PartieFixe < 0) {
                throw new IllegalArgumentException("La partie fixe ne peut pas être négative.");
            }
            this.PartieFixe = PartieFixe;
        }

        public float getPartieVariable() {
            return PartieVariable;
        }

        public void setPartieVariable(float PartieVariable) {
            if (PartieVariable < 0) {
                throw new IllegalArgumentException("La partie variable ne peut pas être négative.");
            }
            this.PartieVariable = PartieVariable;
        }
    }


    public static class ChargesException extends Queryable.QbleException {
        public ChargesException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }

}
