package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.vue.VueAccueil;
import net.mpvm.saeimmobilier.vue.VueConnexion;
import net.mpvm.saeimmobilier.vue.VueHome;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CtrlNewBien {

    @FXML
    private ChoiceBox<Immeuble> listImmeubles;

    private java.sql.Date datesql;

    @FXML
    private TextField FieldLieuImmeuble;
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


    private Date currentDate;


    @FXML
    public void initialize() {
        this.datesql = new Date(new ModeleDate(1, 1, 1).getCurrentDateAsLong());
        fieldsetup();

        LocalDate currentDate = LocalDate.now();

        // Formater la date au format désiré (par exemple, dd/MM/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        // Afficher la date dans le TextField
        this.LabelDate.setText(formattedDate);

        // Initialize the list of Immeubles
        if (listImmeubles != null) {
            Immeuble immeuble = null;
            try {
                immeuble = new Immeuble.IBuilder("Toulouse", 31400, "Rue de la paix", "AAAAAAAAAA", Date.valueOf(LocalDate.now())).build();
            } catch (Bien.BienException e) {
                e.printStackTrace();
            }
            listImmeubles.getItems().add(immeuble);
            try {
                List<Immeuble> immeubles = Immeuble.findAll();
                for (Immeuble i : immeubles) {
                    listImmeubles.getItems().add(i);
                }
            } catch (Immeuble.ImmeubleException e) {
                throw new RuntimeException(e);
            }
            System.out.println(listImmeubles.getItems().getFirst().getAdresse());
        } else {
            System.out.println("ChoiceBox listImmeubles is not injected");
        }

        for (TypeBien b : TypeBien.values()){
            this.ListTypeBien.getItems().add(b);
        }

        this.ListTypeBien.setOnAction(actionEvent -> {
            if(this.ListTypeBien.getValue()==TypeBien.IMMEUBLE){
                this.FieldNbPieces.setDisable(true);
                this.FieldNumFisc.setDisable(true);
                this.FieldSurface.setDisable(true);
                this.listImmeubles.setDisable(true);
                this.FieldLieuImmeuble.setDisable(true);

            } else {
                this.FieldNbPieces.setDisable(false);
                this.FieldNumFisc.setDisable(false);
                this.FieldSurface.setDisable(false);
                this.listImmeubles.setDisable(false);
                this.FieldLieuImmeuble.setDisable(false);
            }
        });

        this.listImmeubles.setOnAction(actionEvent -> {
            if (listImmeubles.getValue().getTypeBien() == TypeBien.IMMEUBLE) {
                this.FieldAdresse.setText(listImmeubles.getValue().getAdresse());
                this.FieldAdresse.setEditable(false);
                this.FieldCodePostal.setText(String.valueOf(listImmeubles.getValue().getCodePostal()));
                this.FieldCodePostal.setEditable(false);
                this.FieldVille.setText(listImmeubles.getValue().getVille());
                this.FieldVille.setEditable(false);
            } else {
                this.FieldAdresse.setText("");
                this.FieldAdresse.setEditable(true);
                this.FieldCodePostal.setText("");
                this.FieldCodePostal.setEditable(true);
                this.FieldVille.setText("");
                this.FieldVille.setEditable(true);
            }
        });


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
        try {
            switch (this.ListTypeBien.getValue()) {
                case TypeBien.HABITATION:
                    if (fieldsNotEmptyBienLouable()) {
                        new Habitation.HBuilder(this.FieldLieuImmeuble.getText(),
                                Integer.parseInt(this.FieldNbPieces.getText()),
                                this.FieldNumFisc.getText(),
                                this.listImmeubles.getItems().getFirst(),
                                Float.parseFloat(this.FieldSurface.getText()), this.datesql).build().save();
                    }else {
                        alertFieldsEmptybienLouable();
                    }
                    break;

                case TypeBien.GARAGE:
                    if (fieldsNotEmptyBienLouable()) {
                        new Garage.GBuilder(this.FieldLieuImmeuble.getText(),
                                Integer.parseInt(this.FieldNbPieces.getText()),
                                this.FieldNumFisc.getText(),
                                this.listImmeubles.getItems().getFirst(),
                                Float.parseFloat(this.FieldSurface.getText()), this.datesql).build().save();
                    } else {
                        alertFieldsEmptybienLouable();
                    }
                    break;

                case TypeBien.IMMEUBLE:
                    if (fieldsNotEmptyImmeuble()){
                        new Immeuble.IBuilder(
                                this.FieldVille.getText(),
                                Integer.parseInt(this.FieldCodePostal.getText()),
                                this.FieldAdresse.getText(),
                                this.FieldNumFisc.getText(),
                                this.datesql).build().save();
                    }else {
                        alertFieldsEmpty();
                    }
                    break;
            }

        } catch (Queryable.QbleException e) {
            e.getSqlException().printStackTrace();
        }
        System.out.print("Bouh ! ");
    }




    private boolean fieldsNotEmptyBienLouable() {
        for(TextField textField : fieldsLogement){
            if(textField.getText().isEmpty()){
                return false;
            }
        }
        return true;
    }


    private boolean fieldsNotEmptyImmeuble(){
        for (int i = 0; i < 3; i++) {
            if (this.fieldsLogement.get(i).getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void alertFieldsEmptybienLouable() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez remplir tous les champs pour un bien louable");
        alert.showAndWait();
    }

    private void alertFieldsEmpty(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez remplir tous les champs pour un Immeuble");
        alert.showAndWait();
    }

    private void alertTypeEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez choisir un type de bien !");
        alert.showAndWait();
    }




    @FXML
    public void Annuler(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            VueHome.showWindow(stage);
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stageActu.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void Accueil(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            VueHome.showWindow(stage);
            Stage stageActu = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stageActu.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void Deconnexion(ActionEvent actionEvent) {
        try {
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();

            VueConnexion.showWindow(stage);
            Stage stage2 = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            // Fermer la fenêtre
            stage2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Clear(ActionEvent actionEvent) {
        for(TextField textField : fieldsLogement){
            if(!textField.getText().isEmpty()){
                textField.setText("");
                textField.setEditable(true);
            }
        }
    }

}
