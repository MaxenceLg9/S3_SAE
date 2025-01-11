package net.mpvm.saeimmobilier.controleur;

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

    private List<TextField> fieldsTravaux;

    public CtrlNewTravaux() {
        System.out.println("Constructeur de CtrlNewTravaux appelé");
    }

    @FXML
    public void initialize() {
        fieldSetup();
        setupButtons();
    }

    private void fieldSetup() {
        fieldsTravaux = new ArrayList<>() {
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
    }

    private void setupButtons() {
        Button btnAjouterTravaux = new Button("Ajouter Travaux");
        btnAjouterTravaux.getStyleClass().add("button-valider");
        btnAjouterTravaux.setOnAction(this::ajouterTravaux);

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.getStyleClass().add("button-supprimer");
        btnAnnuler.setOnAction(this::effacer);

        Button btnRetourAccueil = new Button("Retour à l'Accueil");
        btnRetourAccueil.getStyleClass().add("button-supprimer");
        btnRetourAccueil.setOnAction(this::retourAccueil);

        bottomGridPane.add(btnAjouterTravaux, 2, 0);
        bottomGridPane.add(btnAnnuler, 1, 0);
        bottomGridPane.add(btnRetourAccueil, 0, 0);
    }

    @FXML
    public void ajouterTravaux(ActionEvent event) {
        if (fieldsNotEmpty()) {
            try {
                validateFields();
                String numeroFacture = fieldNumeroFacture.getText();
                String entreprise = fieldEntreprise.getText();
                Float montant = Float.parseFloat(fieldMontant.getText());
                Float montantNonDeductible = Float.parseFloat(fieldMontantNonDeductible.getText());
                Float reduction = Float.parseFloat(fieldReduction.getText());
                String nature = fieldNature.getText();
                String numeroDevis = fieldNumeroDevis.getText();
                Date dateTravaux = Date.valueOf(dateTravauxPicker.getValue());

                Travaux travaux = new Travaux.TBuilder(numeroFacture, entreprise, dateTravaux)
                        .setMontant(montant)
                        .setMontantNonDeductible(montantNonDeductible)
                        .setReduction(reduction)
                        .setNature(nature)
                        .setNumeroDevis(numeroDevis)
                        .build();

                travaux.save();
                JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout de Travaux", "Les travaux ont été ajoutés avec succès !");
            } catch (NumberFormatException e) {
                alertError("Format des champs invalide", "Veuillez saisir des valeurs numériques pour les champs appropriés.");
            } catch (Travaux.TravauxException e) {
                System.out.println(e.getMessage());
                alertError("Erreur lors de l'enregistrement", "Une erreur est survenue lors de l'ajout des travaux.");
            }
        } else {
            alertFieldsEmpty();
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
        for (TextField textField : fieldsTravaux) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return dateTravauxPicker.getValue() != null;
    }
}
