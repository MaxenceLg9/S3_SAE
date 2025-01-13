package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAccueil;
import net.mpvm.saeimmobilier.vue.VueBiensLouables;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CtrlModifierBien {

    @FXML
    public Button BtnModifierBien;
    @FXML
    public Button btnAnnuler;
    @FXML
    public MenuItem menuAccueil;
    @FXML
    public TextField fieldNumeroProprio;
    @FXML
    public ChoiceBox<TypeBien> listTypeBien;
    @FXML
    public TextField fieldNumeroFiscal;
    @FXML
    public TextField fieldAdresse;
    @FXML
    public TextField fieldCodePostal;
    @FXML
    public TextField fieldVille;
    @FXML
    public Label LabelDate;
    @FXML
    public Label LieuImmeuble;
    @FXML
    public TextField fieldLieuImmeuble;
    @FXML
    public TextField fieldSurface;
    @FXML
    public TextField fieldNbPieces;
    @FXML
    public ChoiceBox<Immeuble> listImmeubles;

    private List<TextField> fieldsLogement;
    private BienLouable bien;


    private int idBien;
    private Date datesql;

    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            if (stage != null) {
                setIdBien(stage);
                afficheBien();
                fieldsetup();
                refreshImmeubles();
                typeBiens();
            } else {
                System.out.println("Pas de stage.");
            }
        });
    }


    private void typeBiens() {
        for (TypeBien b : TypeBien.values()) {
            this.listTypeBien.getItems().add(b);
        }
    }

    private void afficheBien() {
        this.bien = (BienLouable) Bien.BBuilder.get(this.idBien);
        this.fieldAdresse.setText(bien.getAdresse());
        this.fieldCodePostal.setText(bien.getCodePostal());
        this.fieldVille.setText(bien.getVille());
        this.fieldLieuImmeuble.setText(bien.getComplementAdresse());
        this.fieldNbPieces.setText(String.valueOf(bien.getNbPieces()));
        this.fieldNumeroFiscal.setText(bien.getNumeroFiscal());
        this.fieldSurface.setText(String.valueOf(bien.getSurface()));
        this.LabelDate.setText(bien.getDateAjout().toString());
        this.listTypeBien.setValue(this.bien.getTypeBien());
        this.datesql = this.bien.getDateAjout();
    }


    public void setIdBien(Stage stage) {
        Object id = stage.getProperties().get("bien");
        if (id instanceof Integer) {
            this.idBien = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bien' manquante ou incorrecte.");
        }
    }

    public void modifierBien(ActionEvent actionEvent) {
        switch (this.listTypeBien.getValue()) {
            case HABITATION:
            case GARAGE:
                // Vérifier si un immeuble est sélectionné
                if (this.listImmeubles.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Sélection requise");
                    alert.setContentText("Veuillez sélectionner un immeuble dans la liste des immeubles !");
                    alert.showAndWait();
                    return;
                }

                // Validation des champs pour Habitation et Garage
                if (fieldsNotEmptyBienLouable() && isCodePostalValid() && isNumeroFiscalValid() && isSurfaceValid() && isNbPiecesValid()) {
                    if (this.listTypeBien.getValue() == TypeBien.HABITATION) {
                        //TODO : ???????????????
                        try {
                            new Habitation.HBuilder(
                                    this.fieldLieuImmeuble.getText(),
                                    Integer.parseInt(this.fieldNbPieces.getText()),
                                    this.fieldNumeroFiscal.getText(),
                                    this.listImmeubles.getValue(),
                                    Float.parseFloat(this.fieldSurface.getText()),
                                    this.bien.getIdProprio(),
                                    this.datesql
                            ).build().modify();
                        } catch (Bien.BienException e) {
                            e.printStackTrace();
                        }

                        JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Modification du bien", "Le bien de type Habitation a été modifié avec succès !");
                        Stage stage = (Stage) this.listImmeubles.getScene().getWindow();
                        stage.getProperties().put("bien",this.bien.getImmeuble().getIdBien());
                        JfxUtil.showWindow(stage, VueBiensLouables.class);
                    } else {
                        // Ajout d'un Garage
                        try {
                            //TODO : ???????????????????????
                            new Garage.GBuilder(
                                    this.fieldLieuImmeuble.getText(),
                                    Integer.parseInt(this.fieldNbPieces.getText()),
                                    this.fieldNumeroFiscal.getText(),
                                    this.listImmeubles.getValue(),
                                    Float.parseFloat(this.fieldSurface.getText()),
                                    this.bien.getIdProprio(),
                                    this.datesql
                            ).build().modify();

                        } catch (Bien.BienException e) {
                            e.printStackTrace();
                        }

                        JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Modification du bien", "Le bien de type Garage a été modifié avec succès !");
                        Stage stage = (Stage) this.listImmeubles.getScene().getWindow();
                        stage.getProperties().put("bien",this.bien.getImmeuble().getIdBien());
                        JfxUtil.showWindow(stage, VueBiensLouables.class);
                    }
                } else {
                    // Champs invalides
                    alertFieldsEmptybienLouable();
                }
                break;
        }

    }

    public void Accueil(ActionEvent actionEvent) {
        Stage stage = (Stage) this.listImmeubles.getScene().getWindow();
        JfxUtil.showWindow(stage, VueAccueil.class);
    }

    public void Annuler(ActionEvent actionEvent) {
        Stage stage = (Stage) this.listImmeubles.getScene().getWindow();
        stage.getProperties().put("bien",this.bien.getImmeuble().getIdBien());
        JfxUtil.showWindow(stage, VueBiensLouables.class);
    }


    private void fieldsetup() {
        fieldsLogement = new ArrayList<>() {
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
                change.getControlNewText().length() <= 5 && change.getControlNewText().matches("\\d*") ? change : null
        ));

        fieldNumeroFiscal.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 13 && change.getControlNewText().matches("\\d*") ? change : null
        ));

    }

    private void refreshImmeubles() {
        this.listImmeubles.setOnAction(actionEvent -> {
            if (listImmeubles.getValue() != null) {
                this.fieldAdresse.setText(listImmeubles.getValue().getAdresse());
                this.fieldAdresse.setDisable(true);
                this.fieldCodePostal.setText(listImmeubles.getValue().getCodePostal());
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
        try {
            listImmeubles.getItems().clear();
            listImmeubles.getItems().addAll(Immeuble.findAll());
            this.listImmeubles.setValue(this.bien.getImmeuble());
        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des immeubles", "");
        }
    }

    private boolean isSurfaceValid() {
        try {
            float surface = Float.parseFloat(this.fieldSurface.getText());
            if (surface < 2 || surface > 2000) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Surface invalide");
                alert.setContentText("La surface doit être comprise entre 2 et 2000 !");
                alert.showAndWait();
                return false;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format invalide");
            alert.setContentText("Veuillez entrer une surface valide (nombre) !");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean isNbPiecesValid() {
        try {
            int nbPieces = Integer.parseInt(this.fieldNbPieces.getText());
            if (nbPieces < 1 || nbPieces > 100) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Nombre de pièces invalide");
                alert.setContentText("Le nombre de pièces doit être compris entre 1 et 100 !");
                alert.showAndWait();
                return false;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Format invalide");
            alert.setContentText("Veuillez entrer un nombre de pièces valide (nombre entier) !");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean isCodePostalValid() {
        String codePostal = this.fieldCodePostal.getText();
        if (codePostal.length() != 5 || !codePostal.matches("\\d{5}")) {
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
        for (TextField textField : fieldsLogement) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean fieldsNotEmptyImmeuble() {
        for (int i = 0; i < 3; i++) {
            if (this.fieldsLogement.get(i).getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void alertFieldsEmptybienLouable() {
        if (!fieldsNotEmptyBienLouable()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs vides");
            alert.setContentText("Veuillez remplir tous les champs pour un bien louable");
            alert.showAndWait();
        }
    }

    private void alertFieldsEmpty() {
        if (!fieldsNotEmptyImmeuble()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs vides");
            alert.setContentText("Veuillez remplir tous les champs pour un Immeuble");
            alert.showAndWait();
        }
    }

    private void alertTypeEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs vides");
        alert.setContentText("Veuillez choisir un type de bien !");
        alert.showAndWait();
    }
}
