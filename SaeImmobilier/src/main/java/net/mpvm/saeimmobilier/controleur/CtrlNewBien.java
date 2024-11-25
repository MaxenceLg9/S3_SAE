package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import net.mpvm.saeimmobilier.modele.BienLouable;

import java.util.ArrayList;
import java.util.List;

public class CtrlNewBien {

    @FXML
    private AnchorPane anchorPaneRacine;
    @FXML
    private TextField fieldAdresse;
    @FXML
    private TextField fieldVille;
    @FXML
    private TextField fieldCodePostal;
    @FXML
    private ComboBox<String> comboBoxTypeBien;
    @FXML
    private TextField fieldSurface;
    @FXML
    private TextField fieldNombrePieces;
    @FXML
    private TextField fieldNumeroFiscal;

    private List<TextField> fieldsBien;

    @FXML
    public void initialize() {
        fieldSetup();
        setupComboBoxTypeBien();
    }

    private void fieldSetup() {
        setFieldsPromptText();

        fieldsBien = new ArrayList<>() {
            {
                add(fieldAdresse);
                add(fieldVille);
                add(fieldCodePostal);
                add(fieldNumeroFiscal);
            }
        };
    }

    private void setFieldsPromptText() {
        fieldAdresse.setPromptText("Adresse du bien");
        fieldVille.setPromptText("Ville du bien");
        fieldCodePostal.setPromptText("Code postal");
        fieldSurface.setPromptText("Surface (en m²)");
        fieldNombrePieces.setPromptText("Nombre de pièces");
        fieldNumeroFiscal.setPromptText("Numéro fiscal");
    }

    private void setupComboBoxTypeBien() {
        comboBoxTypeBien.getItems().addAll("Logement", "Garage");
        comboBoxTypeBien.getSelectionModel().selectFirst();
    }

    @FXML
    public void ajouterBien() {
        if (fieldsNotEmpty()) {
            try {
                String adresse = fieldAdresse.getText();
                String ville = fieldVille.getText();
                String codePostal = fieldCodePostal.getText();
                String typeBien = comboBoxTypeBien.getSelectionModel().getSelectedItem();
                String numeroFiscal = fieldNumeroFiscal.getText();

                Float surface = fieldSurface.getText().isEmpty() ? null : Float.parseFloat(fieldSurface.getText());
                Integer nombrePieces = fieldNombrePieces.getText().isEmpty() ? null : Integer.parseInt(fieldNombrePieces.getText());

                BienLouable nouveauBien = new BienLouable(adresse, ville, codePostal, typeBien, surface, nombrePieces, numeroFiscal);
                nouveauBien.save(); // Appelle une méthode dans le modèle pour enregistrer l'objet dans la base de données

                showSuccessAlert();
            } catch (NumberFormatException e) {
                showInvalidInputAlert();
            }
        } else {
            alertFieldsEmpty();
        }
    }

    private void alertFieldsEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez remplir tous les champs requis.");
        alert.showAndWait();
    }

    private void showInvalidInputAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Entrée invalide");
        alert.setContentText("Veuillez vérifier les valeurs numériques pour la surface ou le nombre de pièces.");
        alert.showAndWait();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("Bien ajouté");
        alert.setContentText("Le nouveau bien a été ajouté avec succès.");
        alert.showAndWait();
    }

    private boolean fieldsNotEmpty() {
        for (TextField textField : fieldsBien) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void annuler(ActionEvent actionEvent) {
        System.out.println("Ajout annulé.");
    }
}
