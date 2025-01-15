package net.mpvm.saeimmobilier.controleur;

import com.dlsc.formsfx.model.structure.Element;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Travaux;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CtrlNewTravaux {
    @FXML
    public Button btnAjouter;

    @FXML
    public Button btnAnnuler;
    @FXML
    public Button btnRetour;
    @FXML
    private TextField fieldNumeroFacture;
    @FXML
    private TextField fieldEntreprise;
    @FXML
    private TextField fieldMontant;
    @FXML
    private TextField fieldMontantNonDeductible;
    @FXML
    private TextField fieldReduction;
    @FXML
    private TextField fieldNature;
    @FXML
    private TextField fieldNumeroDevis;
    @FXML
    private DatePicker dateTravauxPicker;
    @FXML
    private GridPane bottomGridPane;
    @FXML

    private List<TextField> fieldsTravaux;


    @FXML
    public void initialize() {
        fieldSetup();
        setupButtons();
    }

    private void fieldSetup() {
        this.fieldsTravaux = new ArrayList<>() {
            {
                add(fieldNumeroFacture);
                add(fieldEntreprise);
                add(fieldMontant);
                add(fieldMontantNonDeductible);
                add(fieldReduction);
                add(fieldNature);
                add(fieldNumeroDevis);
            }
        };

        applyCharacterLimit(fieldNumeroFacture, 10);
        applyCharacterLimit(fieldNumeroDevis, 10);
    }
    private void applyCharacterLimit(TextField textField, int maxLength) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange() && change.getControlNewText().length() > maxLength) {
                return null;
            }
            return change;
        }));
    }


    private void validateFormat(TextField field, String regex, String fieldName, String exampleFormat) throws IllegalArgumentException {
        if (!field.getText().matches(regex)) {
            throw new IllegalArgumentException(String.format("Le champ '%s' doit respecter le format suivant : %s", fieldName, exampleFormat));
        }
    }

    private void setupButtons() {
        btnAjouter.getStyleClass().add("button-valider");
        btnAnnuler.getStyleClass().add("button-supprimer");
        btnRetour.getStyleClass().add("button-supprimer");
    }

    @FXML
    public void ajouterTravaux(ActionEvent event) {
        if (fieldsNotEmpty()) {
            try {
                validateFields();

                // Validation des formats des champs
                validateFormat(fieldNumeroFacture, "^F\\d{4}-\\d{4}$", "Numéro Facture", "F1111-1111");
                validateFormat(fieldNumeroDevis, "^D\\d{4}-\\d{4}$", "Numéro Devis", "D1111-1111");

                String numeroFacture = fieldNumeroFacture.getText();
                String entreprise = fieldEntreprise.getText();
                Float montant = Float.parseFloat(fieldMontant.getText());
                Float montantNonDeductible = Float.parseFloat(fieldMontantNonDeductible.getText());
                Float reduction = Float.parseFloat(fieldReduction.getText());
                String nature = fieldNature.getText();
                String numeroDevis = fieldNumeroDevis.getText();
                Date dateTravaux = Date.valueOf(dateTravauxPicker.getValue());

                // Vérification de l'existence des numéros dans la base de données
                if (Travaux.numeroFactureExiste(numeroFacture)) {
                    alertError("Numéro de Facture existant",
                            "Un travail avec ce numéro de facture existe déjà. Veuillez en saisir un autre.");
                    return;
                }

                if (Travaux.numeroDevisExiste(numeroDevis)) {
                    alertError("Numéro de Devis existant",
                            "Un travail avec ce numéro de devis existe déjà. Veuillez en saisir un autre.");
                    return;
                }

                // Création et sauvegarde des travaux
                Travaux travaux = new Travaux.TBuilder(numeroFacture, entreprise, dateTravaux, numeroDevis, montant, montantNonDeductible, nature, reduction)
                        .build();

                travaux.save();
                JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout de Travaux", "Les travaux ont été ajoutés avec succès !");
            } catch (NumberFormatException e) {
                alertError("Format des champs invalide", "Veuillez saisir des valeurs numériques pour les champs appropriés.");
            } catch (IllegalArgumentException e) {
                alertError("Format incorrect", e.getMessage());
            } catch (Travaux.TravauxException e) {
                handleTravauxException(e);
            }
        } else {
            alertFieldsEmpty();
        }
    }


    private void handleTravauxException(Travaux.TravauxException e) {
        if (e.getMessage().contains("Le numéro de facture est obligatoire")) {
            alertError("Erreur de validation", "Le numéro de facture est obligatoire.");
        } else if (e.getMessage().contains("L'entreprise est obligatoire")) {
            alertError("Erreur de validation", "Le champ entreprise est obligatoire.");
        } else if (e.getMessage().contains("Les champs 'Montant', 'MontantNonDeductible' et 'Reduction' doivent être définis")) {
            alertError("Erreur de validation", "Tous les champs financiers doivent être remplis.");
        } else if (e.getMessage().contains("Le montant non déductible ne peut pas être supérieur au montant total")) {
            alertError("Erreur de validation", "Le montant non déductible ne peut pas excéder le montant total.");
        } else if (e.getMessage().contains("La réduction doit être comprise entre 0 et 1")) {
            alertError("Erreur de validation", "La réduction doit être un pourcentage valide (entre 0 et 1).");
        } else {
            alertError("Erreur lors de l'enregistrement", "Une erreur est survenue lors de l'ajout des travaux.");
            e.printStackTrace();
        }
    }



    @FXML
    public void effacer(ActionEvent actionEvent) {
        fieldNumeroFacture.clear();
        fieldEntreprise.clear();
        fieldMontant.clear();
        fieldMontantNonDeductible.clear();
        fieldReduction.clear();
        fieldNature.clear();
        fieldNumeroDevis.clear();
        dateTravauxPicker.setValue(null);
    }

    @FXML
    public void retourAccueil(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


    private void validateFields() throws NumberFormatException {
        for (TextField field : fieldsTravaux) {
            String text = field.getText().replace(",", ".");
            if (field == fieldMontant || field == fieldMontantNonDeductible || field == fieldReduction) {
                if (!text.matches("^[0-9]*\\.?[0-9]+$")) {
                    throw new NumberFormatException("Les champs de montant, montant non déductible ou réduction doivent être des nombres.");
                }
            }
        }
    }

    private void alertFieldsEmpty() {
        alertError("Champs vides", "Veuillez remplir tous les champs avant de valider.");
    }

    private void alertError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean fieldsNotEmpty() {
        if (fieldsTravaux == null) {
            fieldSetup();
        }

        for (TextField textField : fieldsTravaux) {
            if (textField.getText().trim().isEmpty()) {
                return false;
            }
        }
        return dateTravauxPicker.getValue() != null;
    }


}