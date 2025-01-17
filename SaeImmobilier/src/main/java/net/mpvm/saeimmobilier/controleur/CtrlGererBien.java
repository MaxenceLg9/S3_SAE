package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CtrlGererBien {

    @FXML
    private ChoiceBox<Immeuble> listImmeubles;

    private Date datesql;

    @FXML
    private TextField fieldComplementAdresse;
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

    private List<TextField> fieldsBien;
    private List<TextField> fieldsBienLouables;
    private List<TextField> fieldsImmeubles;

    @FXML
    private TextField fieldIdProprio;

    private Date currentDate;

    private Bien bien;
    private CtrlViewImmeubles controleur;


    @FXML
    public void initialize() {

        fieldsetup();
        refreshImmeubles();

        Platform.runLater(() -> {
            Stage stage = (Stage) this.fieldAdresse.getScene().getWindow();
            if (stage.getProperties().containsKey("bien")) {
                if (setBien(stage) == -1) {
                    JfxUtil.displayError("Erreur lors de la récupération du bien", "Impossible de récupérer le bien à modifier !");
                    stage.close();
                    return;
                }
                afficheBien();
                this.listTypeBien.getItems().add(this.bien.getTypeBien());
            } else {
                this.datesql = Date.valueOf(LocalDate.now());
                String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                this.LabelDate.setText(formattedDate);
                listTypeBienSetup();
            }
            if(stage.getProperties().containsKey("controleur")){
                this.controleur = (CtrlViewImmeubles) stage.getProperties().get("controleur");
            }

        });
    }

    private void listTypeBienSetup() {
        this.listTypeBien.getItems().addAll(TypeBien.values());
        this.listTypeBien.setOnAction(actionEvent -> {
            TypeBien selectedType = this.listTypeBien.getValue();
            if (selectedType == TypeBien.IMMEUBLE) {
                this.fieldIdProprio.setText("");
                this.fieldNumeroFiscal.setText("");
                enableFieldsForImmeuble();
            } else if (selectedType == TypeBien.GARAGE || selectedType == TypeBien.HABITATION) {
                // Activer uniquement les champs pour GARAGE ou HABITATION
                enableFieldsForGarageOrHabitation();
            }
        });
    }

    private void enableFieldsForImmeuble() {
        resetFields(true);
        resetFields(false);

        // Activer les champs spécifiques à IMMEUBLE
        for (TextField textField : fieldsImmeubles) {
            textField.setDisable(false);
        }

        fieldIdProprio.setDisable(false);
        fieldNumeroFiscal.setDisable(false);

        // Désactiver les champs pour GARAGE ou HABITATION
        for (TextField textField : fieldsBienLouables) {
            textField.setDisable(true);
            textField.setText("");
        }
        this.listImmeubles.setValue(null);
        this.listImmeubles.setDisable(true);
    }

    private void enableFieldsForGarageOrHabitation() {
        resetFields(true);
        resetFields(false);

        // Activer les champs spécifiques à GARAGE ou HABITATION
        for (TextField textField : fieldsBienLouables) {
            textField.setDisable(false);
        }

        fieldIdProprio.setDisable(false);
        fieldNumeroFiscal.setDisable(false);

        // Désactiver et effacer les champs pour IMMEUBLE
        for (TextField textField : fieldsImmeubles) {
            textField.setDisable(true);
            textField.setText("");
        }
        this.listImmeubles.setDisable(false);
    }

    private void refreshImmeubles() {
        listImmeubles.setOnAction(actionEvent -> {
            if (listImmeubles.getValue() != null) {
                fieldAdresse.setText(listImmeubles.getValue().getAdresse());
                fieldAdresse.setDisable(true);
                fieldCodePostal.setText(listImmeubles.getValue().getCodePostal());
                fieldCodePostal.setDisable(true);
                fieldVille.setText(listImmeubles.getValue().getVille());
                fieldVille.setDisable(true);
                listTypeBien.getItems().remove(TypeBien.IMMEUBLE);
            } else {
                fieldAdresse.setText("");
                fieldAdresse.setDisable(false);
                fieldCodePostal.setText("");
                fieldCodePostal.setDisable(false);
                fieldVille.setText("");
                fieldVille.setDisable(false);
                listTypeBien.getItems().add(TypeBien.IMMEUBLE);
            }
        });
        try {
            listImmeubles.getItems().clear();
            listImmeubles.getItems().addAll(Immeuble.findAll());
        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des immeubles", "");
        }
    }

    private void fieldsetup() {
        fieldsBien = new ArrayList<>() {
            {
                add(fieldNumeroFiscal);
                add(fieldIdProprio);
            }
        };
        fieldsBienLouables = new ArrayList<>() {
            {
                add(fieldNbPieces);
                add(fieldSurface);
                add(fieldComplementAdresse);
            }
        };
        fieldsImmeubles = new ArrayList<>() {
            {
                add(fieldAdresse);
                add(fieldVille);
                add(fieldCodePostal);
                add(fieldIdProprio);
                add(fieldNumeroFiscal);
            }
        };
        fieldCodePostal.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 5 && change.getControlNewText().matches("\\d*") ? change : null
        ));

        fieldNumeroFiscal.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 13 && change.getControlNewText().matches("\\d*") ? change : null
        ));
    }

    private void resetFields(boolean isImmeuble) {
        if (isImmeuble) {
            for (TextField textField : fieldsImmeubles) {
                textField.setText("");
                textField.setDisable(false);
            }
            this.listImmeubles.setValue(null);
        } else {
            for (TextField textField : fieldsBienLouables) {
                textField.setText("");
            }
        }
    }

    @FXML
    public void Clear(ActionEvent actionEvent) {
        for (TextField textField : fieldsBien) {
            textField.setText("");
        }
        for (TextField textField : fieldsBienLouables) {
            textField.setText("");
        }
        for (TextField textField : fieldsImmeubles) {
            textField.setText("");
        }
        this.listTypeBien.setValue(null);
        this.listImmeubles.setValue(null);
    }

    // Other methods remain unchanged...



    @FXML
    public void ajouterBien(ActionEvent actionEvent) {
        try {
            // Vérification du type de bien sélectionné
            if (this.listTypeBien.getValue() == null) {
                alertTypeEmpty(); // Si aucun type de bien n'est sélectionné
                return;
            }

            if(this.listTypeBien.getValue() == TypeBien.IMMEUBLE)
                if (fieldsNotEmptyImmeuble() && isCodePostalValid() && isNumeroFiscalValid()) {
                    // Ajout d'un Immeuble
                    new Immeuble.IBuilder(
                            this.fieldVille.getText(),
                            this.fieldCodePostal.getText(),
                            this.fieldAdresse.getText(),
                            this.fieldNumeroFiscal.getText(),
                            this.datesql,
                            this.fieldIdProprio.getText()
                    ).build().save();

                    refreshImmeubles();
                    JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout du bien", "Le bien de type Immeuble a été ajouté avec succès !");
                } else {
                    alertFieldsEmpty();
                }
            else{
                // Vérifier si un immeuble est sélectionné
                if (this.listImmeubles.getValue() == null) {
                    JfxUtil.displayError("Sélection d'un immeuble requise", "Veuillez sélectionner un immeuble dans la liste des immeubles !");
                    return;
                }

                // Validation des champs pour Habitation et Garage
                if (fieldsNotEmptyBienLouable() && isCodePostalValid() && isNumeroFiscalValid() && isSurfaceValid() && isNbPiecesValid()) {
                    if (this.listTypeBien.getValue() == TypeBien.HABITATION) {
                        new Habitation.HBuilder(
                                this.fieldComplementAdresse.getText(),
                                Integer.parseInt(this.fieldNbPieces.getText()),
                                this.fieldNumeroFiscal.getText(),
                                this.listImmeubles.getValue(),
                                Float.parseFloat(this.fieldSurface.getText()),
                                this.fieldIdProprio.getText(),
                                this.datesql
                        ).build().save();

                        JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout du bien", "Le bien de type Habitation a été ajouté avec succès !");
                    } else {
                        // Ajout d'un Garage
                        new Garage.GBuilder(
                                this.fieldComplementAdresse.getText(),
                                Integer.parseInt(this.fieldNbPieces.getText()),
                                this.fieldNumeroFiscal.getText(),
                                this.listImmeubles.getValue(),
                                Float.parseFloat(this.fieldSurface.getText()),
                                this.fieldIdProprio.getText(),
                                this.datesql
                        ).build().save();
                        if(controleur != null){
                            controleur.afficheImmeubles();
                        }
                        JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout du bien", "Le bien de type Garage a été ajouté avec succès !");
                    }
                } else {
                    // Champs invalides
                    alertFieldsEmptyBienLouable();
                }
            }
        } catch (Queryable.QbleException e) {
            // Gérer les exceptions SQL
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter le bien", e.getMessage());
            e.getSqlException().printStackTrace();
        } catch (NumberFormatException e) {
            // Gérer les erreurs de conversion de chaîne en nombre
            JfxUtil.setAlert(Alert.AlertType.ERROR, "Erreur", "Format invalide", "Veuillez vérifier les valeurs numériques des champs !");
        }
    }

    private void afficheBien() {
        this.fieldAdresse.setText(bien.getAdresse());
        this.fieldCodePostal.setText(bien.getCodePostal());
        this.fieldVille.setEditable(true);
        this.fieldVille.setText(bien.getVille());
        this.fieldNumeroFiscal.setText(bien.getNumeroFiscal());
        this.fieldIdProprio.setText(bien.getIdProprio());
        this.LabelDate.setText(bien.getDateAjout().toString());
        this.listTypeBien.setValue(this.bien.getTypeBien());
        this.datesql = this.bien.getDateAjout();
        if(bien instanceof BienLouable bienLouable) {
            this.fieldComplementAdresse.setText(bienLouable.getComplementAdresse());
            this.fieldNbPieces.setText(String.valueOf(bienLouable.getNbPieces()));
            this.fieldSurface.setText(String.valueOf(bienLouable.getSurface()));
            this.listImmeubles.setValue(bienLouable.getImmeuble());
        }else{
            this.fieldComplementAdresse.setDisable(true);
            this.fieldNbPieces.setDisable(true);
            this.fieldSurface.setDisable(true);
            this.listImmeubles.setDisable(true);
        }
    }

    public int setBien(Stage stage) {
        if(stage.getProperties().get("bien") instanceof Bien) {
            this.bien = (Bien) stage.getProperties().get("bien");
            return 0;
        }
        return -1;
    }

    public void modifierBien(ActionEvent actionEvent) {
        try {
            this.bien.setNumeroFiscal(this.fieldNumeroFiscal.getText());
            this.bien.setIdProprio(this.fieldIdProprio.getText());
            if(this.bien instanceof BienLouable bienLouable) {
                bienLouable.setSurface(Float.parseFloat(this.fieldSurface.getText()));
                bienLouable.setNbPieces(Integer.parseInt(this.fieldNbPieces.getText()));
                bienLouable.setComplementAdresse(this.fieldComplementAdresse.getText());

                bienLouable.setImmeuble(this.listImmeubles.getValue());
                bienLouable.modify();

            } else if(this.bien instanceof Immeuble immeuble) {
                immeuble.setVille(this.fieldVille.getText());
                immeuble.setCodePostal(this.fieldCodePostal.getText());
                immeuble.setAdresse(this.fieldAdresse.getText());
                immeuble.modify();
            }
            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Modification du bien", "Le bien a été modifié avec succès !");
            alertFieldsEmptyBienLouable();
        }catch (Bien.BienException e){
            JfxUtil.displayError("Erreur lors de la récupération de l'immeuble", e.getMessage());
        }
    }

    private boolean isSurfaceValid() {
        try {
            float surface = Float.parseFloat(this.fieldSurface.getText());
            if (surface < 2 || surface > 2000) {
                JfxUtil.displayError("Erreur", "La surface doit être comprise entre 2 et 2000 !");
                return false;
            }
        } catch (NumberFormatException e) {
            JfxUtil.displayError("Erreur", "Veuillez entrer une surface valide (nombre) !");
            return false;
        }
        return true;
    }

    private boolean isNbPiecesValid() {
        try {
            int nbPieces = Integer.parseInt(this.fieldNbPieces.getText());
            if (nbPieces < 1 || nbPieces > 100) {
                JfxUtil.displayError("Erreur", "Le nombre de pièces doit être compris entre 1 et 100 !");
                return false;
            }
        } catch (NumberFormatException e) {
            JfxUtil.displayError("Erreur", "Veuillez entrer un nombre de pièces valide (nombre entier) !");
            return false;
        }
        return true;
    }

    private boolean isCodePostalValid() {
        String codePostal = this.fieldCodePostal.getText();
        if (codePostal.length() != 5 || !codePostal.matches("\\d{5}")) {
            JfxUtil.displayError("Erreur", "Le code postal doit comporter exactement 5 chiffres !");
            return false;
        }
        return true;
    }

    private boolean isNumeroFiscalValid() {
        String numeroFiscal = this.fieldNumeroFiscal.getText();

        // Vérifier la longueur
        if (numeroFiscal.length() != 13) {
            JfxUtil.displayError("Erreur", "Le numéro fiscal doit comporter exactement 13 chiffres !");
            return false;
        }

        // Vérifier la première valeur
        char firstChar = numeroFiscal.charAt(0);
        if (firstChar != '0' && firstChar != '1' && firstChar != '2' && firstChar != '3') {
            JfxUtil.displayError("Erreur", "Le numéro fiscal doit commencer par 0, 1, 2 ou 3 !");
            return false;
        }

        return true;
    }

    private boolean fieldsNotEmptyBienLouable() {
        for (TextField textField : fieldsBien) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean fieldsNotEmptyImmeuble() {
        for (TextField textField : fieldsImmeubles) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void alertFieldsEmptyBienLouable() {
        if (!fieldsNotEmptyBienLouable()) {
            JfxUtil.displayError("Erreur", "Veuillez remplir tous les champs pour un bien louable !");
        }
    }

    private void alertFieldsEmpty() {
        if (!fieldsNotEmptyImmeuble()) {
            JfxUtil.displayError("Erreur", "Veuillez remplir tous les champs pour un Immeuble !");
        }
    }

    private void alertTypeEmpty() {
        JfxUtil.displayError("Erreur", "Veuillez choisir un type de bien !");
    }

    @FXML
    public void RetourAccueil() {
        Stage stage = (Stage) this.listImmeubles.getScene().getWindow();
        stage.close();
    }


}
