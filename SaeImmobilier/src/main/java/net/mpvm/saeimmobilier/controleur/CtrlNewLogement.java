package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.mpvm.saeimmobilier.modele.Proprietaire;
import net.mpvm.saeimmobilier.modele.Bien;

import java.util.ArrayList;

public class CtrlNewBien {

    @FXML
    private ComboBox<Proprietaire> comboProprietaire;
    @FXML
    private TextField fieldAdresse;
    @FXML
    private TextField fieldCodePostal;
    @FXML
    private TextField fieldVille;
    @FXML
    private TextArea areaDescription;
    @FXML
    private TextField fieldPrix;

    private ArrayList<Proprietaire> proprietaires;

    public void initialize() {
        // Remplir la liste des propriétaires
        proprietaires = // Charger depuis une source de données
        comboProprietaire.getItems().addAll(proprietaires);
    }

    @FXML
    private void ajouterBien() {
        try {
            Proprietaire proprietaire = comboProprietaire.getValue();
            String adresse = fieldAdresse.getText();
            int codePostal = Integer.parseInt(fieldCodePostal.getText());
            String ville = fieldVille.getText();
            String description = areaDescription.getText();
            float prix = Float.parseFloat(fieldPrix.getText());

            // Créer un nouvel objet Bien
            Bien nouveauBien = new Bien(adresse, ville, codePostal, description, prix);
            proprietaire.getBiensPossedes().add(nouveauBien);

            // Sauvegarder dans la base de données ou l'afficher
            System.out.println("Nouveau bien ajouté avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void annuler() {
        // Fermer la fenêtre ou réinitialiser les champs
        System.out.println("Ajout annulé.");
    }
}
