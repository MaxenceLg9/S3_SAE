package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CtrlNewBien {

    @FXML
    public Button btnajouterLocataire;
    @FXML
    public ComboBox<Locataire> comboLocataires;
    @FXML
    private ChoiceBox<Immeuble> listImmeubles;

    @FXML
    private Label LabelDate;
    @FXML
    private TextField FieldAdresse;
    @FXML
    private TextField FieldVille;
    @FXML
    private TextField FieldCodePostal;
    @FXML
    private TextField FieldNumFisc;
    @FXML
    private TextField FieldNbPieces;
    @FXML
    private TextField FieldSurface;
    @FXML
    private ChoiceBox<TypeBien> ListTypeBien;
    @FXML
    private List<TextField> fieldsLogement;


    @FXML
    public void initialize() {

        fieldsetup();

        LocalDate currentDate = LocalDate.now();

        // Formater la date au format désiré (par exemple, dd/MM/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        // Afficher la date dans le TextField
        this.LabelDate.setText(formattedDate);

        // Initialize the list of Immeubles
        if (listImmeubles != null) {
            listImmeubles.getItems().add(new Immeuble("Toulouse", 31400, "Rue de la paix"));
            System.out.println(listImmeubles.getItems().getFirst().getAdresse());
        } else {
            System.out.println("ChoiceBox listImmeubles is not injected");
        }

        List<Locataire> locataires = null;
        try {
            locataires = Locataire.findALl();
        } catch (Locataire.LocataireException e) {
            locataires = new ArrayList<>();
        }
        for (Locataire loc : locataires) {
            this.comboLocataires.getItems().add(loc);
        }

        this.ListTypeBien.getItems().add(TypeBien.BIEN_LOUABLE);
        this.ListTypeBien.getItems().add(TypeBien.IMMEUBLE);



    }

    private void fieldsetup() {
        fieldsLogement = new ArrayList<>(){
            {
                add(FieldVille);
                add(FieldCodePostal);
                add(FieldAdresse);
                add(FieldNbPieces);
                add(FieldNumFisc);
                add(FieldSurface);
            }
        };
    }


    @FXML
    public void ajouterBien(ActionEvent actionEvent) {
        if (fieldsNotEmptyBienLouable()) {
            if (this.ListTypeBien.getItems().getFirst().getDesignation() == TypeBien.BIEN_LOUABLE.getDesignation()) {
                new BienLouable(this.FieldVille.getText(), Integer.valueOf(this.FieldCodePostal.getText()), this.FieldAdresse.getText(),
                        Integer.valueOf(this.FieldNbPieces.getText()), Integer.valueOf(this.FieldNumFisc.getText()), Float.valueOf(this.FieldSurface.getText()),
                        this.listImmeubles.getItems().getFirst());
            } else {
                if(fieldsNotEmptyBien()) {
                    new Bien(this.FieldVille.getText(), Integer.valueOf(this.FieldCodePostal.getText()), this.FieldAdresse.getText());
                }
            }

        } else {
            alertFieldsEmpty();
        }
    }



    private boolean fieldsNotEmptyBienLouable() {
        for(TextField textField : fieldsLogement){
            if(textField.getText().isEmpty()){
                return false;
            }
        }
        return true;
        }

    private boolean fieldsNotEmptyBien(){
        for (int i = 0; i < 3; i++) {
            if (this.fieldsLogement.get(i).getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void alertFieldsEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez remplir tous les champs");
        alert.showAndWait();
    }


    @FXML
    public void AddLocataire(ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            // Initialiser la fenêtre avec l'utilitaire existant
            JfxUtil.applicationInit(stage, "newlocataire.fxml", "Ajouter un Locataire");

            Stage stage2 = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            // Fermer la fenêtre
            stage2.close();

            // Afficher la fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Annuler(ActionEvent actionEvent) {
        // Obtenir la fenêtre actuelle (Stage) à partir de l'événement
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        // Fermer la fenêtre
        stage.close();
    }

}

