package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.TypeContrat;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.util.ArrayList;
import java.util.List;

public class CtrlNewAssurance {

    public Button btnAnnuler;
    public Button btnAjouterAssurance;
    @FXML
    private TextField fieldProtectionJuridique;
    @FXML
    private TextField fieldQuotiteJuridique;
    @FXML
    private ComboBox<TypeContrat> comboTypeContrat;
    @FXML
    private TextField fieldPrime;
    @FXML
    private List<TextField> fieldsAssurance;
    @FXML
    private TextField fieldAnnee;
    public CtrlNewAssurance() {
        System.out.println("Constructeur de CtrlNewAssurance appele");
    }

    @FXML
    public void initialize() {
        fieldSetup();
        setupComboBox();
    }

    private void setupComboBox() {
        comboTypeContrat.getItems().addAll(TypeContrat.values());
    }

    private void fieldSetup() {
        fieldsAssurance = new ArrayList<>() {
            {
                add(fieldAnnee);
                add(fieldProtectionJuridique);
                add(fieldQuotiteJuridique);
                add(fieldPrime);
            }
        };
    }


    @FXML
    public void ajouterAssurance(ActionEvent event) {
        if (fieldsNotEmpty()) {
            try {
                validateFields();
                int annee = Integer.parseInt(fieldAnnee.getText());
                float protectionJuridique = Float.parseFloat(fieldProtectionJuridique.getText());
                float quotiteJuridique = Float.parseFloat(fieldQuotiteJuridique.getText());
                float prime = Float.parseFloat(fieldPrime.getText());
                TypeContrat typeContrat = comboTypeContrat.getValue();

                if (typeContrat == null) {
                    alertError("Type de contrat manquant", "Veuillez selectionner un type de contrat.");
                    return;
                }
                if (annee < 1950 || annee > 2050) {
                    alertError("Année invalide", "L'année doit être comprise entre 1950 et 2050.");
                    return;
                }
                if (quotiteJuridique < 0 || quotiteJuridique > 100) {
                    alertError("Quotité juridique invalide", "La quotité juridique doit être un nombre entre 0 et 100.");
                    return;
                }
                // Enregistrement de l'assurance dans la base de donnees
                new Assurance.ABuilder(typeContrat,annee,protectionJuridique,quotiteJuridique,prime).build().save();

            } catch (NumberFormatException e) {
                alertError("Format des champs invalide", "Veuillez saisir des valeurs numeriques pour les champs appropriés.");
            } catch (Assurance.AssuranceException e) {
                System.out.println(e.getMessage());
                alertError("Erreur lors de l'enregistrement", "Une erreur est survenue lors de l'ajout de l'assurance.");
            }
        } else {
            alertFieldsEmpty();
        }

        try {
            Stage stage = new Stage();
            JfxUtil.updateStage(stage, "home.fxml", "Page Home",700,800);
            stage.setMinWidth(1300);
            stage.setMinHeight(900);


            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void validateFields() throws NumberFormatException {
        for (TextField field : fieldsAssurance) {
            String text = field.getText().replace(",", "."); // Convertir les virgules en points pour Java
            if (!text.matches("^[0-9]*\\.?[0-9]+$")) { // Vérifie si le champ contient uniquement des nombres
                throw new NumberFormatException("Champs contenant des caractères invalides.");
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
        for (TextField textField : fieldsAssurance) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }


    @FXML
    public void Annuler(ActionEvent actionEvent) {
        fieldAnnee.clear();
        fieldQuotiteJuridique.clear();
        fieldProtectionJuridique.clear();
        comboTypeContrat.getItems().clear();
        fieldsAssurance.clear();
        fieldPrime.clear();
    }
}
