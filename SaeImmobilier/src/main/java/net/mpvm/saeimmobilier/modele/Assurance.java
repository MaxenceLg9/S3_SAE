package net.mpvm.saeimmobilier.modele;

import net.mpvm.saeimmobilier.sql.Query.*;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.util.Unfinished;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.lang.annotation.*;

public class Assurance extends Queryable{

    private int idAssurance;
    private int annee;
    private float protectionJuridique;
    private float prime;
    private TypeContrat typeContrat; // Type Propriétaire ou aide juridique
    private String numeroContrat;
    private String nomAssurance;
    private Assurance(int idAssurance, TypeContrat typeContrat, int annee, float protectionJuridique, float prime, String numeroContrat, String nomAssurance) {
        this.nomAssurance = nomAssurance;
        if (typeContrat == null) {
            throw new IllegalArgumentException("Le type de contrat est obligatoire.");
        }
        this.idAssurance = idAssurance;
        this.typeContrat = typeContrat;
        this.annee = annee;
        this.protectionJuridique = protectionJuridique;
        this.prime = prime;
        this.numeroContrat = numeroContrat;
    }

    public Assurance(ABuilder aBuilder) {
        this(aBuilder.id, aBuilder.typeContrat, aBuilder.annee, aBuilder.protectionJuridique, aBuilder.prime, aBuilder.numeroContrat, aBuilder.nomAssurance);
    }

    // Méthode pour récupérer toutes les assurances
    public static List<Assurance> findAll() throws AssuranceException {
        List<Assurance> assurances = new ArrayList<>();
        String SELECT_QUERY = "SELECT IdAssurance,ProtectionJuridique,Prime,TypeContrat,Annee,NomAssurance,NumeroContrat FROM Assurance";

        try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
            Result rs = query.execute();
            for(Map<String,Object> row : rs){
                Assurance assurance = new Assurance.ABuilder(row).build();
                assurances.add(assurance);
            }
        } catch (QueryElement.QEltException qEltException) {
            throw new AssuranceException("Erreur lors de la récupération des assurances", qEltException.getSqlException());
        }

        return assurances;
    }


    public float getTotalPrime() {
        return this.prime + this.protectionJuridique;
    }

    public int getIdAssurance() {
        return idAssurance;
    }

    public float getProtectionJuridique() {
        return protectionJuridique;
    }

    public void setProtectionJuridique(float protectionJuridique) {
        this.protectionJuridique = protectionJuridique;
    }

    public float getPrime() {
        return prime;
    }

    public void setPrime(float prime) {
        this.prime = prime;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    public Bien getBien() {
        //TODO : Use a query
        return null;
    }

    public float getAugmentationAnnuelle() {
        return 0;
    }

    @Override
    public void save() throws AssuranceException {
        // Vérifie si l'assurance existe déjà (id différent de -1)
        if (this.idAssurance != -1) {
            throw new AssuranceException("Cette assurance existe déjà dans la table.", null);
        }

        // Validation des données
        if (this.getProtectionJuridique() < 0) {
            throw new AssuranceException("La protection juridique ne peut pas être négative.");
        }
        if (this.getPrime() < 0) {
            throw new AssuranceException("La prime ne peut pas être négative.");
        }
        if (this.getTypeContrat() == null) {
            throw new AssuranceException("Le type de contrat est obligatoire.");
        }
        if (this.getNomAssurance() == null || this.getNomAssurance().isEmpty()) {
            throw new AssuranceException("Le nom de l'assurance est obligatoire.");
        }

        String INSERT_QUERY = """
        INSERT INTO Assurance (ProtectionJuridique, Prime, TypeContrat, NomAssurance, Annee, NumeroContrat)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (UpdateQueryElement query = new UpdateQueryElement(INSERT_QUERY, true)) {
            query.setArgs(Map.of(
                    1, this.getProtectionJuridique(),
                    2, this.getPrime(),
                    3, this.getTypeContrat().name(),
                    4, this.getNomAssurance(),
                    5, this.getAnnee(),
                    6, this.getNumeroContrat()
            )).execute();

            System.out.println("Assurance ajoutée avec succès.");
        } catch (QueryElement.QEltException qEltException) {
            throw new AssuranceException(
                    "Erreur lors de l'ajout de l'assurance : " + qEltException.getSqlException().getMessage(),
                    qEltException.getSqlException()
            );
        }
    }



    @Override
    public void modify() throws QbleException {
        update();
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getNomAssurance() {
        return nomAssurance;
    }
    public void setNomAssurance(String nomAssurance) {
        this.nomAssurance = nomAssurance;
    }
    public void delete() throws AssuranceException {
        if (this.idAssurance <= 0) {
            throw new AssuranceException("L'ID de l'assurance est invalide pour une suppression.");
        }

        String DELETE_QUERY = """
        DELETE FROM Assurance
        WHERE IdAssurance = ?
        """;

        try (UpdateQueryElement query = new UpdateQueryElement(DELETE_QUERY, true)) {
            query.setArgs(Map.of(
                    1, this.idAssurance
            ));

            int rowsAffected = query.execute();
            if (rowsAffected == 0) {
                throw new AssuranceException("Aucune assurance correspondante trouvée pour la suppression.");
            }
        } catch (QueryElement.QEltException e) {
            throw new AssuranceException("Erreur lors de la suppression de l'assurance avec ID " + this.idAssurance, e.getSqlException());
        }
    }

    @Override
    public void archiver() throws QbleException {

    }
    public void attribuerUneAssurance(int idBien, int idAssurance) throws AssuranceException {
        if (idBien <= 0 || idAssurance <= 0) {
            throw new AssuranceException("L'ID du bien et de l'assurance doivent être valides.");
        }

        String CHECK_QUERY = """
    SELECT COUNT(*) as count FROM Assurance
    WHERE IdBien = ? AND Annee = ?
    """;

        try (SelectQueryElement checkQuery = new SelectQueryElement(CHECK_QUERY)) {
            checkQuery.setArgs(Map.of(
                    1, idBien,
                    2, this.annee
            ));
            Result rs = checkQuery.execute();

            if (!rs.isEmpty()) {
                int count = ((Number) rs.get(0).get("count")).intValue();
                if (count > 0) {
                    throw new AssuranceException(
                            "Une assurance pour le bien avec la même année existe déjà. Association refusée."
                    );
                }
            }
        } catch (QueryElement.QEltException e) {
            throw new AssuranceException(
                    "Erreur lors de la vérification des assurances existantes : " + e.getSqlException().getMessage(),
                    e.getSqlException()
            );
        }

        String UPDATE_QUERY = """
                              UPDATE Assurance
                              SET IdBien = ?
                              WHERE IdAssurance = ?
                              """;

        try (UpdateQueryElement query = new UpdateQueryElement(UPDATE_QUERY, true)) {
            query.setArgs(Map.of(
                    1, idBien,
                    2, idAssurance
            ));

            int rowsAffected = query.execute();
            if (rowsAffected == 0) {
                throw new AssuranceException("Aucune assurance correspondante trouvée pour l'attribution.");
            }

        } catch (QueryElement.QEltException e) {
            throw new AssuranceException(
                    "Erreur lors de l'attribution de l'assurance avec ID " + idAssurance + " au bien avec ID " + idBien,
                    e.getSqlException()
            );
        }
    }




    @Unfinished
    public float getPrimePrecedente() {
        return 0;
    }


    protected void setId(int id) throws QbleException {
        this.idAssurance = id;
    }

    public String selectIdBien() throws AssuranceException {
        if (this.idAssurance <= 0) {
            throw new AssuranceException("L'ID de l'assurance est invalide.");
        }

        String SELECT_QUERY = "SELECT IdBien FROM Assurance WHERE IdAssurance = ?";
        try (SelectQueryElement query = new SelectQueryElement(SELECT_QUERY)) {
            query.setArgs(Map.of(1, this.idAssurance));
            Result rs = query.execute();

            if (rs.isEmpty()) {
                // Renvoie "aucun" si aucun résultat n'est trouvé
                return "aucun";
            }

            // Récupère le premier résultat et retourne l'ID du bien sous forme de chaîne
            Map<String, Object> row = rs.get(0);
            return String.valueOf(row.get("IdBien"));
        } catch (QueryElement.QEltException qEltException) {
            throw new AssuranceException(
                    "Erreur lors de la récupération de l'ID du bien associé à l'assurance avec ID " + this.idAssurance,
                    qEltException.getSqlException()
            );
        }
    }



    public String toString(){
        return this.nomAssurance+" "+this.typeContrat + " " + this.annee + " " + this.prime + " " + this.numeroContrat;
    }

    public void update() throws AssuranceException {
        if (this.idAssurance <= 0) {
            throw new AssuranceException("L'ID de l'assurance est invalide pour une mise à jour.");
        }

        String UPDATE_QUERY = """
        UPDATE Assurance SET
        ProtectionJuridique = ?,
        QuotitéJuridique = ?,
        Prime = ?,
        TypeContrat = ?,
        TotalPrime = ?
        WHERE IdAssurance = ?
        """;

        try (UpdateQueryElement query = new UpdateQueryElement(UPDATE_QUERY, true)) {
            query.setArgs(Map.of(
                    1, this.protectionJuridique,
                    3, this.prime,
                    4, this.typeContrat.toString(),
                    6, this.idAssurance
            ));

            int rowsAffected = query.execute();
            if (rowsAffected == 0) {
                throw new AssuranceException("Aucune assurance correspondante trouvée pour la mise à jour.");
            }
            System.out.println("Mise à jour réussie pour l'assurance ID = " + this.idAssurance);
        } catch (QueryElement.QEltException e) {
            throw new AssuranceException("Erreur lors de la mise à jour de l'assurance avec ID " + this.idAssurance, e.getSqlException());
        }
    }

    public void setIdAssurance(int idAssurance) {
        this.idAssurance = idAssurance;
    }

    public String getNumeroContrat() {
        return numeroContrat;
    }

    public void setNumeroContrat(String numeroContrat) {
        this.numeroContrat = numeroContrat;
    }

    public static class ABuilder extends Queryable.Builder{

        private final int id;
        private final TypeContrat typeContrat;
        private final int annee;
        private final float protectionJuridique;
        private final float prime;
        private final String numeroContrat;
        public final String nomAssurance;

        ABuilder(Map<String, Object> args) {
            this((int) args.get("IdAssurance"),
                    TypeContrat.valueOf(args.get("TypeContrat").toString()),
                    (int) args.get("Annee"),
                    JfxUtil.doubleToFloat((double) args.get("ProtectionJuridique")),
                    JfxUtil.doubleToFloat((double) args.get("Prime")),
                    args.get("NumeroContrat").toString(),
                    args.get("NomAssurance").toString());
        }

        private ABuilder(int id, TypeContrat typeContrat, int annee, float protectionJuridique, float prime, String numeroContrat, String nomAssurance) {
            this.id = id;
            this.typeContrat = typeContrat;
            this.annee = annee;
            this.protectionJuridique = protectionJuridique;
            this.prime = prime;
            this.numeroContrat = numeroContrat;
            this.nomAssurance = nomAssurance;
        }

        public ABuilder(TypeContrat typeContrat, int annee, float protectionJuridique, float prime, String numeroContrat,String nomAssurance) {
            this(-1,typeContrat, annee, protectionJuridique, prime, numeroContrat, nomAssurance);
        }

        @Override
        public Assurance build() {
            return new Assurance(this);
        }
    }

    public static class AssuranceException extends Queryable.QbleException {
        public AssuranceException(String message){
            super(message);
        }

        public AssuranceException(String message, SQLException sqlException) {
            super(message, sqlException);
        }
    }
}
