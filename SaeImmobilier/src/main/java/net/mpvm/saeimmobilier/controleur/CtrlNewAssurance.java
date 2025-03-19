package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.TypeContrat;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;

import java.util.ArrayList;
import java.util.List;

public class CtrlNewAssurance {

    @FXML
    private TextField fieldProtectionJuridique;
    @FXML
    private TextField fieldNumeroDeContrat;
    @FXML
    private ComboBox<TypeContrat> comboTypeContrat;
    @FXML
    private TextField fieldPrime;
    @FXML
    private List<TextField> fieldsAssurance;
    @FXML
    private TextField fieldAnnee;
    @FXML
    private TextField fieldNom;

    @FXML
    private GridPane bottomGridPane;

    public CtrlNewAssurance() {
        System.out.println("Constructeur de CtrlNewAssurance appele");
    }

    @FXML
    public void initialize() {
        fieldSetup();
        setupComboBox();
        setupButtons();
    }

    private void setupComboBox() {
        comboTypeContrat.getItems().addAll(TypeContrat.values());
    }

    private void fieldSetup() {
        fieldsAssurance = new ArrayList<>() {
            {
                add(fieldAnnee);
                add(fieldNom);
                add(fieldProtectionJuridique);
                add(fieldNumeroDeContrat);
                add(fieldPrime);
            }
        };
    }

    private void setupButtons() {
        Button btnAjouterAssurance = new Button("Ajouter Assurance");
        btnAjouterAssurance.getStyleClass().add("button-valider");
        btnAjouterAssurance.setOnAction(this::ajouterAssurance);

        Button btnAnnuler = new Button("Effacer la saisie");
        btnAnnuler.getStyleClass().add("button");
        btnAnnuler.setOnAction(this::Effacer);

        Button btnRetourAccueil = new Button("Retour à l'Accueil");
        btnRetourAccueil.getStyleClass().add("button-supprimer");
        btnRetourAccueil.setOnAction(this::retourAccueil);

        bottomGridPane.add(btnAjouterAssurance, 2, 0);
        bottomGridPane.add(btnAnnuler, 1, 0);
        bottomGridPane.add(btnRetourAccueil, 0, 0);

    }

    @FXML
    public void ajouterAssurance(ActionEvent event) {
        if (fieldsNotEmpty()) {
            try {
                validateFields();
                int annee = Integer.parseInt(fieldAnnee.getText());
                float protectionJuridique = Float.parseFloat(fieldProtectionJuridique.getText());
                String numeroContrat = fieldNumeroDeContrat.getText();
                float prime = Float.parseFloat(fieldPrime.getText());
                TypeContrat typeContrat = comboTypeContrat.getValue();
                String nomAssurance = fieldNom.getText();

                if (typeContrat == null) {
                    JfxUtil.displayError("Type de contrat manquant", "Veuillez sélectionner un type de contrat.");
                    return;
                }
                if (annee < 1950 || annee > 2050) {
                    JfxUtil.displayError("Année invalide", "L'année doit être comprise entre 1950 et 2050.");
                    return;
                }
                new Assurance.ABuilder(typeContrat, annee, protectionJuridique, prime, numeroContrat, nomAssurance)
                        .build()
                        .save();
                JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout de l'Assurance", "L'assurance a été ajoutée avec succès !");
            } catch (NumberFormatException e) {
                JfxUtil.displayError("Format des champs invalide", "Veuillez saisir des valeurs numériques pour les champs appropriés.");
            } catch (Assurance.AssuranceException e) {
                System.out.println(e.getMessage());
                JfxUtil.displayError("Erreur lors de l'enregistrement", "Une erreur est survenue lors de l'ajout de l'assurance.");            }
        } else {
            JfxUtil.displayError("Champs vides","Veuillez remplir tous les champs avant de valider.");
        }
    }

    @FXML
    public void Effacer(ActionEvent actionEvent) {
        fieldAnnee.clear();
        fieldNom.clear();
        fieldNumeroDeContrat.clear();
        fieldProtectionJuridique.clear();
        comboTypeContrat.getItems().clear();
        fieldsAssurance.clear();
        fieldPrime.clear();
    }

    public void retourAccueil(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void validateFields() throws NumberFormatException {
        for (TextField field : fieldsAssurance) {
            String text = field.getText().replace(",", ".");
            if (field == fieldAnnee) {
                if (!text.matches("^[0-9]+$")) {
                    throw new NumberFormatException("Le champ 'Année' doit contenir uniquement des chiffres.");
                }
            } else if (field == fieldProtectionJuridique) {
                if (!text.matches("^[0-9]*\\.?[0-9]+$")) {
                    throw new NumberFormatException("Le champ 'Protection Juridique' doit être un nombre.");
                }
            } else if (field == fieldPrime) {
                if (!text.matches("^[0-9]*\\.?[0-9]+$")) {
                    throw new NumberFormatException("Le champ 'Prime' doit être un nombre.");
                }
            } else if (field == fieldNumeroDeContrat) {
                if (!text.matches("^[a-zA-Z0-9]+$")) {
                    throw new NumberFormatException("Le champ 'Numéro de Contrat' doit contenir uniquement des lettres et/ou des chiffres.");
                }
            }
        }
    }

    private boolean fieldsNotEmpty() {
        for (TextField textField : fieldsAssurance) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
