package net.mpvm.saeimmobilier.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class CtrlViewLocation {
    @FXML
    private Label Titre;

    @FXML
    private GridPane gridPaneBiensLouables;

    // Exemple de données des locations (à remplacer par les données réelles)
    private List<Location> locations = List.of(
            new Location("Location 1"),
            new Location("Location 2"),
            new Location("Location 3")
    );

    @FXML
    public void initialize() {
        afficherLocations();
    }

    private void afficherLocations() {
        int row = 0;
        for (Location location : locations) {
            HBox hbox = new HBox(10);
            hbox.getStyleClass().add("locationBox");

            Label label = new Label(location.getNom());
            label.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
            hbox.getChildren().add(label);

            // Bouton Calcul des charges
            Button btnCharges = new Button("Calcul des Charges");
            btnCharges.setOnAction(e -> viewCharges(location));
            hbox.getChildren().add(btnCharges);

            // Bouton Locataires
            Button btnLocataires = new Button("Locataires");
            btnLocataires.setOnAction(e -> viewLocataires(location));
            hbox.getChildren().add(btnLocataires);

            // Bouton Bien concerné
            Button btnBien = new Button("Bien Concerné");
            btnBien.setOnAction(e -> viewBienLouable(location));
            hbox.getChildren().add(btnBien);

            // Bouton Documents
            Button btnDocuments = new Button("Documents");
            btnDocuments.setOnAction(e -> viewDocuments(location));
            hbox.getChildren().add(btnDocuments);

            gridPaneBiensLouables.add(hbox, 0, row++);
        }
    }

    // Méthodes pour chaque action (à implémenter)
    private void viewCharges(Location location) {
        System.out.println("Afficher les charges pour : " + location.getNom());
    }

    private void viewLocataires(Location location) {
        System.out.println("Afficher les locataires pour : " + location.getNom());
    }

    private void viewBienLouable(Location location) {
        System.out.println("Afficher le bien pour : " + location.getNom());
    }

    private void viewDocuments(Location location) {
        System.out.println("Afficher les documents pour : " + location.getNom());
    }

    // Classe interne représentant une location (à remplacer par votre modèle réel)
    public static class Location {
        private String nom;

        public Location(String nom) {
            this.nom = nom;
        }

        public String getNom() {
            return nom;
        }
    }
}
