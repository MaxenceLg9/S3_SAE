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

    private Date datesql;

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

    private Date currentDate;


    @FXML
    public void initialize() {
        this.datesql = Date.valueOf(LocalDate.now());
        fieldsetup();

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        this.LabelDate.setText(formattedDate);
        refreshImmeubles();

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

    private void refreshImmeubles() {
        try {
            listImmeubles.getItems().clear();
            listImmeubles.getItems().addAll(Immeuble.findAll());
        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des immeubles", "");
        }
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
                add(fieldNumeroProprio);
            }
        };
        fieldCodePostal.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 5 ? change : null
        ));

        fieldNumeroFiscal.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 13 ? change : null
        ));
    }


    @FXML
    public void ajouterBien(ActionEvent actionEvent) {
        try {
            // Vérification du type de bien sélectionné
            if (this.listTypeBien.getValue() == null) {
                alertTypeEmpty(); // Si aucun type de bien n'est sélectionné
                return;
            }

            switch (this.listTypeBien.getValue()) {
                case HABITATION:
                case GARAGE:
                    // Validation des champs pour Habitation et Garage
                    if (fieldsNotEmptyBienLouable() && isCodePostalValid() && isNumeroFiscalValid()) {
                        if (this.listTypeBien.getValue() == TypeBien.HABITATION) {
                            // Ajout d'une Habitation
                            new Habitation.HBuilder(
                                    this.fieldLieuImmeuble.getText(),
                                    Integer.parseInt(this.fieldNbPieces.getText()),
                                    this.fieldNumeroFiscal.getText(),
                                    this.listImmeubles.getValue(),
                                    Float.parseFloat(this.fieldSurface.getText()),
                                    this.fieldNumeroProprio.getText(),
                                    this.datesql
                            ).build().save();

                            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout du bien", "Le bien de type Habitation a été ajouté avec succès !");
                        } else {
                            // Ajout d'un Garage
                            new Garage.GBuilder(
                                    this.fieldLieuImmeuble.getText(),
                                    Integer.parseInt(this.fieldNbPieces.getText()),
                                    this.fieldNumeroFiscal.getText(),
                                    this.listImmeubles.getValue(),
                                    Float.parseFloat(this.fieldSurface.getText()),
                                    this.fieldNumeroProprio.getText(),
                                    this.datesql
                            ).build().save();

                            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout du bien", "Le bien de type Garage a été ajouté avec succès !");
                        }
                    } else {
                        // Champs invalides
                        alertFieldsEmptybienLouable();
                    }
                    break;

                case IMMEUBLE:
                    // Validation des champs pour Immeuble
                    if (fieldsNotEmptyImmeuble() && isCodePostalValid() && isNumeroFiscalValid()) {
                        // Ajout d'un Immeuble
                        new Immeuble.IBuilder(
                                this.fieldVille.getText(),
                                Integer.parseInt(this.fieldCodePostal.getText()),
                                this.fieldAdresse.getText(),
                                this.fieldNumeroFiscal.getText(),
                                this.datesql,
                                this.fieldNumeroProprio.getText()
                        ).build().save();

                        refreshImmeubles(); // Mettre à jour la liste des immeubles
                        JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout du bien", "Le bien de type Immeuble a été ajouté avec succès !");
                    } else {
                        alertFieldsEmpty();
                    }
                    break;

                default:
                    alertTypeEmpty();
                    break;
            }
        } catch (Queryable.QbleException e) {
            // Gérer les exceptions SQL
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Erreur SQL", "Une erreur est survenue lors de l'ajout du bien : " + e.getMessage());
            e.getSqlException().printStackTrace();
        } catch (NumberFormatException e) {
            // Gérer les erreurs de conversion de chaîne en nombre
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Format invalide", "Veuillez vérifier les valeurs numériques des champs !");
        }
    }
    private boolean isCodePostalValid() {
        if (this.fieldCodePostal.getText().length() != 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Code Postal invalide");
            alert.setContentText("Le code postal doit comporter exactement 5 chiffres !");
            alert.showAndWait();
            return false;
        }
        return true;
    }
    private boolean isNumeroFiscalValid() {
        String numeroFiscal = this.fieldNumeroFiscal.getText();

        // Vérifier la longueur
        if (numeroFiscal.length() != 13) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Numéro Fiscal invalide");
            alert.setContentText("Le numéro fiscal doit comporter exactement 13 chiffres !");
            alert.showAndWait();
            return false;
        }

        // Vérifier la première valeur
        char firstChar = numeroFiscal.charAt(0);
        if (firstChar != '0' && firstChar != '1' && firstChar != '2' && firstChar != '3') {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Numéro Fiscal invalide");
            alert.setContentText("Le numéro fiscal doit commencer par 0, 1, 2 ou 3 !");
            alert.showAndWait();
            return false;
        }

        return true;
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
