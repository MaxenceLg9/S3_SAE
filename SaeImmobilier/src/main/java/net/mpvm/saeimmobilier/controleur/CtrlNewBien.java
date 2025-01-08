package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;

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
    private TextField fieldLieuImmeuble;
    @FXML
    private Label LabelDate;
    @FXML
    private TextField fieldAdresse;
    @FXML
    private TextField fieldVille;
    @FXML
    private TextField fieldCodePostal;
    @FXML
    private TextField fieldNumeroFiscal;
    @FXML
    private TextField fieldNbPieces;
    @FXML
    private TextField fieldSurface;
    @FXML
    private ChoiceBox<TypeBien> listTypeBien;
    @FXML
    private List<TextField> fieldsLogement;
    @FXML
    private TextField fieldNumeroProprio;

    private ModeleDate currentDate;


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
        try {
            listImmeubles.getItems().addAll(Immeuble.findAll());
        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des immeubles", "");
        }


        for (TypeBien b : TypeBien.values()){
            this.listTypeBien.getItems().add(b);
        }

        this.listTypeBien.setOnAction(actionEvent -> {
            if(this.listTypeBien.getValue()==TypeBien.IMMEUBLE){
                this.fieldNbPieces.setDisable(true);
                this.fieldSurface.setDisable(true);
                this.listImmeubles.setDisable(true);
                this.fieldLieuImmeuble.setDisable(true);

            } else {
                this.fieldNbPieces.setDisable(false);
                this.fieldSurface.setDisable(false);
                this.listImmeubles.setDisable(false);
                this.fieldLieuImmeuble.setDisable(false);
            }
        });

        this.listImmeubles.setOnAction(actionEvent -> {
            if (listImmeubles.getValue() != null) {
                this.fieldAdresse.setText(listImmeubles.getValue().getAdresse());
                this.fieldAdresse.setDisable(true);
                this.fieldCodePostal.setText(String.valueOf(listImmeubles.getValue().getCodePostal()));
                this.fieldCodePostal.setDisable(true);
                this.fieldVille.setText(listImmeubles.getValue().getVille());
                this.fieldVille.setDisable(true);
                this.listTypeBien.getItems().remove(TypeBien.IMMEUBLE);
            } else {
                this.fieldAdresse.setText("");
                this.fieldAdresse.setDisable(false);
                this.fieldCodePostal.setText("");
                this.fieldCodePostal.setDisable(false);
                this.fieldVille.setText("");
                this.fieldVille.setDisable(false);
                this.listTypeBien.getItems().add(TypeBien.IMMEUBLE);
            }
        });


    }

    private void fieldsetup() {
        fieldsLogement = new ArrayList<>(){
            {
                add(fieldVille);
                add(fieldCodePostal);
                add(fieldAdresse);
                add(fieldNbPieces);
                add(fieldNumeroFiscal);
                add(fieldSurface);
            }
        };
    }


    @FXML
    public void ajouterBien(ActionEvent actionEvent) {
        try {
            switch (this.listTypeBien.getValue()) {
                case TypeBien.HABITATION:
                    if (fieldsNotEmptyBienLouable()) {
                        new Habitation.HBuilder(this.fieldLieuImmeuble.getText(),
                                Integer.parseInt(this.fieldNbPieces.getText()),
                                this.fieldNumeroFiscal.getText(),
                                this.listImmeubles.getItems().getFirst(),
                                Float.parseFloat(this.fieldSurface.getText()), this.fieldNumeroProprio.getText(), this.datesql).build().save();
                    }else {
                        alertFieldsEmptybienLouable();
                    }
                    break;

                case TypeBien.GARAGE:
                    if (fieldsNotEmptyBienLouable()) {
                        new Garage.GBuilder(this.fieldLieuImmeuble.getText(),
                                Integer.parseInt(this.fieldNbPieces.getText()),
                                this.fieldNumeroFiscal.getText(),
                                this.listImmeubles.getItems().getFirst(),
                                Float.parseFloat(this.fieldSurface.getText()), this.fieldNumeroProprio.getText(), this.datesql).build().save();
                    } else {
                        alertFieldsEmptybienLouable();
                    }
                    break;

                case TypeBien.IMMEUBLE:
                    if (fieldsNotEmptyImmeuble()){
                        new Immeuble.IBuilder(
                                this.fieldVille.getText(),
                                Integer.parseInt(this.fieldCodePostal.getText()),
                                this.fieldAdresse.getText(),
                                this.fieldNumeroFiscal.getText(),
                                this.datesql,
                                this.fieldNumeroProprio.getText()).build().save();
                    }else {
                        alertFieldsEmpty();
                    }
                    break;
            }

        } catch (Queryable.QbleException e) {
            e.getSqlException().printStackTrace();
        }
        JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout du bien", "Le bien a été ajouté! Vous pouvez maintenant retourner sur la page d'accueil");
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
    public void Annuler(ActionEvent event) {
        Stage stage = (Stage) this.listImmeubles.getScene().getWindow();
        stage.close();
    }


    public void Accueil(ActionEvent event) {
        Stage stage = (Stage) this.listImmeubles.getScene().getWindow();
        JfxUtil.showWindow(stage, VueAccueil.class);
    }

    public void Clear(ActionEvent actionEvent) {
        for(TextField textField : fieldsLogement){
            if(!textField.getText().isEmpty()){
                textField.setText("");
                textField.setEditable(true);
            }
        }
        this.listImmeubles.setValue(null);
        this.listTypeBien.setValue(null);
    }
}
