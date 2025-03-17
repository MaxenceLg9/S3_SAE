package net.mpvm.saeimmobilier.controleur;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.AssociationBailLocataires;
import net.mpvm.saeimmobilier.modele.Bail;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueNewLocataire;

public class CtrlGererUnBail {

    public DatePicker dateDebut, dateFin, dateSignature; public TextField fieldMontantLoyer, fieldTotalCharges, fieldDepotGarantie, fieldNbColocataires;
    public CheckBox checkBoxArchive, checkBoxRenouvelable, checkBoxLocationSimple, checkBoxColocation, checkBoxLoyerEuro, checkBoxLoyerPourcentage, checkBoxChargesEuro, checkBoxChargesPourcentage;
    public Label labelResteLoyer, labelResteCharges, labelAdresse, labelComplementAdresse, labelType; public GridPane gridPaneContent;
    private List<ChoiceBox<Locataire>> choiceBoxesLocataires = new LinkedList<>();
    private List<DatePicker> datesDebut = new LinkedList<>(), datesFin = new LinkedList<>();
    private List<TextField> fieldsRepartitionsCharges = new LinkedList<>(), fieldsRepartitionsLoyer = new LinkedList<>();
    boolean isModification; private File selectedFile; private BienLouable bienLouable;

    public void initialize() {
        try {
            addNonNegativeValidation(fieldMontantLoyer); addNonNegativeValidation(fieldTotalCharges); addNonNegativeValidation(fieldDepotGarantie);
            checkBoxLocationSimple.setSelected(true); checkBoxLocationSimple.setDisable(false); fieldNbColocataires.setText("2");
            System.out.println("Début de l'initialisation du contrôleur");

            // Initialisation des listes
            choiceBoxesLocataires = new LinkedList<>();
            fieldsRepartitionsCharges = new LinkedList<>();
            fieldsRepartitionsLoyer = new LinkedList<>();
            datesDebut = new LinkedList<>();
            datesFin = new LinkedList<>();

            // Ajout de la validation des nombres non-négatifs
            addNonNegativeValidation(fieldMontantLoyer);
            addNonNegativeValidation(fieldTotalCharges);
            addNonNegativeValidation(fieldDepotGarantie);

            System.out.println("Listes initialisées");

            // Initialisation des checkboxes de type de location
            checkBoxLocationSimple.setSelected(true);

            checkBoxLocationSimple.setDisable(false);
            fieldNbColocataires.setText("2"); // Initialisation par défaut à 2 colocataires
            System.out.println("Checkbox location simple initialisée");


            checkBoxLocationSimple.setOnAction(e -> {
                checkBoxColocation.setSelected(!checkBoxLocationSimple.isSelected());
                fieldNbColocataires.setDisable(!checkBoxColocation.isSelected());
                updateColocationFields();
                if (!checkBoxColocation.isSelected()) {
                    labelResteLoyer.setText("");
                    labelResteCharges.setText("");
                    // Réinitialiser à un seul locataire
                    updateNombreColocataires(2);
                }
            });

            checkBoxColocation.setOnAction(e -> {
                if (checkBoxColocation.isSelected()) {
                    checkBoxLocationSimple.setSelected(false);
                } else {
                    checkBoxLocationSimple.setSelected(true);
                }
                fieldNbColocataires.setDisable(!checkBoxColocation.isSelected());
                if (checkBoxColocation.isSelected()) {
                    // Forcer au moins 2 colocataires quand on active la colocation
                    int nbColocataires = Math.max(2, Integer.parseInt(fieldNbColocataires.getText()));
                    fieldNbColocataires.setText(String.valueOf(nbColocataires));
                    updateNombreColocataires(nbColocataires);
                    updateRepartitionLabels();
                } else {
                    updateNombreColocataires(2);
                    labelResteLoyer.setText("");
                    labelResteCharges.setText("");
                }
            });

            System.out.println("Checkboxes de type de location initialisées");

            // Ajout du listener sur le champ nombre de colocataires
            fieldNbColocataires.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty() && checkBoxColocation.isSelected()) {
                    try {
                        int nbColocataires = Integer.parseInt(newValue);
                        if (nbColocataires < 2) {
                            fieldNbColocataires.setText("2");
                            nbColocataires = 2;
                        } else if (nbColocataires > 8) {
                            fieldNbColocataires.setText("8");
                            nbColocataires = 8;
                        }
                        updateNombreColocataires(nbColocataires);
                    } catch (NumberFormatException ex) {
                        fieldNbColocataires.setText(oldValue);
                    }
                }
            });

            // Ajout du premier locataire par défaut
            addLocataireToBail(null);
            // Mise à jour des champs pour le mode location simple
            updateColocationFields();

            // Initialisation des checkboxes de répartition
            checkBoxLoyerEuro.setSelected(true);
            checkBoxLoyerEuro.setOnAction(e -> {
                checkBoxLoyerPourcentage.setSelected(!checkBoxLoyerEuro.isSelected());
                checkBoxLoyerEuro.setSelected(checkBoxLoyerEuro.isSelected());
                updateRepartitionLabels();
                updatePromptTexts();
            });

            checkBoxLoyerPourcentage.setOnAction(e -> {
                checkBoxLoyerEuro.setSelected(!checkBoxLoyerPourcentage.isSelected());
                checkBoxLoyerPourcentage.setSelected(checkBoxLoyerPourcentage.isSelected());
                updateRepartitionLabels();
                updatePromptTexts();
            });

            checkBoxChargesEuro.setSelected(true);

            checkBoxChargesEuro.setOnAction(e -> {
                checkBoxChargesPourcentage.setSelected(!checkBoxChargesEuro.isSelected());
                checkBoxChargesEuro.setSelected(checkBoxChargesEuro.isSelected());
                updateRepartitionLabels();
                updatePromptTexts();
            });

            checkBoxChargesPourcentage.setOnAction(e -> {
                checkBoxChargesEuro.setSelected(!checkBoxChargesPourcentage.isSelected());
                checkBoxChargesPourcentage.setSelected(checkBoxChargesPourcentage.isSelected());
                updateRepartitionLabels();
                updatePromptTexts();
            });

            // Initial update of remaining amounts based on Euro being selected by default
            updateRepartitionLabels();

            checkBoxChargesPourcentage.setOnAction(e -> {
                checkBoxChargesEuro.setSelected(!checkBoxChargesPourcentage.isSelected());
                checkBoxChargesPourcentage.setSelected(checkBoxChargesPourcentage.isSelected());
                updateRepartitionLabels();
            });

            System.out.println("Checkboxes de répartition initialisées");

            // Désactivation initiale du champ nombre de colocataires
            fieldNbColocataires.setDisable(true);
            System.out.println("Champ nombre de colocataires initialisé");

            // Ajout d'un listener sur le champ nombre de colocataires
            fieldNbColocataires.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    try {
                        int nbColocataires = Integer.parseInt(newValue);
                        // En mode colocation, forcer entre 2 et 8 colocataires
                        if (checkBoxColocation.isSelected()) {
                            if (nbColocataires < 2) {
                                fieldNbColocataires.setText("2");
                                return;
                            }
                            if (nbColocataires > 8) {
                                fieldNbColocataires.setText("8");
                                return;
                            }
                        } else if (nbColocataires != 2) {
                            // En mode location simple, forcer à 1
                            fieldNbColocataires.setText("2");
                            return;
                        }
                        updateNombreColocataires(nbColocataires);
                    } catch (NumberFormatException ex) {
                        fieldNbColocataires.setText(oldValue);
                    }
                }
            });

            // Ajout des listeners sur les champs de montant
            fieldMontantLoyer.textProperty().addListener((observable, oldValue, newValue) -> {
                if (checkBoxColocation.isSelected()) {
                    updateRepartitionLabels();
                }
            });

            fieldTotalCharges.textProperty().addListener((observable, oldValue, newValue) -> {
                if (checkBoxColocation.isSelected()) {
                    updateRepartitionLabels();
                }
            });

            // Ajout des listeners sur les DatePicker du bail
            dateDebut.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateLocataireDates();
            });

            dateFin.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateLocataireDates();
            });

            // Récupération du bien louable depuis les propriétés du stage
            Platform.runLater(() -> {
                System.out.println("Début de l'initialisation dans Platform.runLater");
                Stage currentStage = (Stage) labelAdresse.getScene().getWindow();
                System.out.println("Stage récupéré");

                try {
                    System.out.println("Propriétés du stage : " + currentStage.getProperties());

                    if (currentStage.getProperties().containsKey("idBien")) {
                        System.out.println("Récupération du bien via idBien");
                        bienLouable = BienLouable.BLBuilder.getBienLouable((int) currentStage.getProperties().get("idBien"));
                    } else if (currentStage.getProperties().containsKey("bienLouable")) {
                        System.out.println("Récupération du bien via bienLouable");
                        bienLouable = (BienLouable) currentStage.getProperties().get("bienLouable");
                    } else {
                        System.out.println("Aucune propriété de bien trouvée");
                    }

                    if (bienLouable != null) {
                        System.out.println("Bien louable trouvé : " + bienLouable.getIdBien());
                        // Mise à jour des labels avec les informations du bien
                        labelAdresse.setText(bienLouable.getAdresse());
                        labelComplementAdresse.setText(bienLouable.getComplementAdresse());
                        labelType.setText(bienLouable.getTypeBien().toString());

                    } else {
                        System.out.println("Bien louable non trouvé");
                    }
                } catch (Bien.BienException e) {
                    System.out.println("Erreur lors de la récupération du bien : " + e.getMessage());
                }
            });

            System.out.println("Fin de l'initialisation du contrôleur");
        } catch (Exception e) {
            System.out.println("Exception inattendue : " + e.getMessage());
            e.printStackTrace();
            JfxUtil.displayError("Erreur inattendue",
                    "Une erreur inattendue est survenue :\n" +
                            "Type : " + e.getClass().getName() + "\n" +
                            "Message : " + e.getMessage() + "\n" +
                            "Cause : " + (e.getCause() != null ? e.getCause().getMessage() : "Aucune cause"));
        }
    }
    private void updateLocataireDates() {
        for (int i = 0; i < datesDebut.size(); i++) {
            datesDebut.get(i).setValue(dateDebut.getValue());
            datesFin.get(i).setValue(dateFin.getValue());
        }
    }

    private void updateColocationFields() {
        boolean isColocation = checkBoxColocation.isSelected();
        for (int index = 0; index < choiceBoxesLocataires.size(); index++) {
            final int i = index;
            final int rowIndex = i + 2;
            if (i == 0) {
                final int firstLocataireCol = 3;
                gridPaneContent.getChildren().stream()
                        .filter(node -> GridPane.getRowIndex(node) == rowIndex && GridPane.getColumnIndex(node) == firstLocataireCol)
                        .forEach(node -> node.setVisible(true));
            }
            // Mettre à jour la visibilité des autres éléments
            for (int col = 3; col <= 7; col++) {
                final int finalCol = col;
                gridPaneContent.getChildren().stream()
                        .filter(node -> GridPane.getRowIndex(node) == rowIndex &&
                                GridPane.getColumnIndex(node) == finalCol &&
                                !(i == 0 && finalCol == 3)) // Ne pas cacher la première ChoiceBox
                        .forEach(node -> node.setVisible(isColocation));
            }
        }
        updateRepartitionLabels();
    }

    private void updateRepartitionLabels() {
        float totalLoyer = 0;
        float totalCharges = 0;
        float totalRepartitionLoyer = 0;
        float totalRepartitionCharges = 0;
        try {
            if (!fieldMontantLoyer.getText().isEmpty()) {
                totalLoyer = Float.parseFloat(fieldMontantLoyer.getText());
            }
            if (!fieldTotalCharges.getText().isEmpty()) {
                totalCharges = Float.parseFloat(fieldTotalCharges.getText());
            }

            for (int i = 0; i < fieldsRepartitionsLoyer.size(); i++) {
                if (!fieldsRepartitionsLoyer.get(i).getText().isEmpty()) {
                    totalRepartitionLoyer += Float.parseFloat(fieldsRepartitionsLoyer.get(i).getText());
                }
            }

            for (int i = 0; i < fieldsRepartitionsCharges.size(); i++) {
                if (!fieldsRepartitionsCharges.get(i).getText().isEmpty()) {
                    totalRepartitionCharges += Float.parseFloat(fieldsRepartitionsCharges.get(i).getText());
                }
            }

            if (checkBoxLoyerEuro.isSelected()) {
                labelResteLoyer.setText(String.format("Reste à répartir : %.2f €", totalLoyer - totalRepartitionLoyer));
            } else {
                labelResteLoyer.setText(String.format("Reste à répartir : %.2f %%", 100 - totalRepartitionLoyer));
            }

            if (checkBoxChargesEuro.isSelected()) {
                labelResteCharges.setText(String.format("Reste à répartir : %.2f €", totalCharges - totalRepartitionCharges));
            } else {
                labelResteCharges.setText(String.format("Reste à répartir : %.2f %%", 100 - totalRepartitionCharges));
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur de conversion des valeurs numériques");
        }
    }

    public void valider(ActionEvent event) {
        try {
            // Vérification des champs obligatoires
            if (fieldsEmpty()) {
                JfxUtil.displayError("Erreur de validation",
                        "Veuillez remplir tous les champs obligatoires :\n" +
                                "- Dates du bail (début, fin, signature)\n" +
                                "- Montant du loyer\n" +
                                "- Total des charges\n" +
                                "- Dépôt de garantie\n" +
                                "- Document du bail (PDF)");
                return;
            }

            // Vérification de la présence du bien louable
            if (bienLouable == null) {
                JfxUtil.displayError("Erreur de bien louable",
                        "Le bien louable n'a pas été trouvé.\n" +
                                "Veuillez fermer puis réouvrir la page.");
                return;
            }

            // Vérification de la présence d'au moins un locataire
            if (choiceBoxesLocataires.isEmpty()) {
                JfxUtil.displayError("Erreur de locataire",
                        "Veuillez ajouter au moins un locataire au bail.");
                return;
            }

            // Vérification de la sauvegarde du fichier
            if (trySavingFile() == -1) {
                return;
            }

            // Vérification des dates
            if (!validateDates()) {
                return;
            }

            // Vérification des répartitions
            if (!validateRepartition()) {
                return;
            }

            // Si toutes les vérifications sont passées, procéder à la sauvegarde du bail
            trySavingBail();
            JfxUtil.setAlert(Alert.AlertType.INFORMATION,
                    "Sauvegarde confirmée",
                    "Bail enregistré avec succès",
                    "Le bail a été enregistré avec succès dans la base de données.");
        } catch (Exception e) {
            JfxUtil.displayError("Erreur lors de la validation",
                    "Une erreur est survenue lors de la validation du bail :\n" +
                            "Type : " + e.getClass().getSimpleName() + "\n" +
                            "Message : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean fieldsEmpty() {
        for (int i = 0; i < choiceBoxesLocataires.size(); i++) {
            if (choiceBoxesLocataires.size() > 2) {
                if (choiceBoxesLocataires.get(i).getValue() == null || fieldsRepartitionsCharges.get(i).getText().isEmpty() || datesDebut.get(i).getValue() == null) {
                    System.out.println("Champ locataire vide à l'index " + i);
                    return true;
                }
            } else if (choiceBoxesLocataires.get(i).getValue() == null) {
                System.out.println("Locataire non sélectionné à l'index " + i);
                return true;
            }
            if (isModification && datesFin.get(i).getValue() == null) {
                System.out.println("Date de fin vide pour le locataire à l'index " + i);
                return true;
            }
        }
        if (dateDebut.getValue() == null) {
            System.out.println("Date de début vide");
            return true;
        }
        if (dateFin.getValue() == null) {
            System.out.println("Date de fin vide");
            return true;
        }
        if (fieldMontantLoyer.getText().isEmpty()) {
            System.out.println("Montant du loyer vide");
            return true;
        }
        if (fieldTotalCharges.getText().isEmpty()) {
            System.out.println("Total des charges vide");
            return true;
        }
        if (fieldDepotGarantie.getText().isEmpty()) {
            System.out.println("Dépôt de garantie vide");
            return true;
        }
        if (dateSignature.getValue() == null) {
            System.out.println("Date de signature vide");
            return true;
        }
        System.out.println("Fichier non sélectionné");
        return selectedFile == null;
    }



    private void trySavingBail() {
        try {
            // Vérification explicite des dates avant conversion
            if (dateDebut.getValue() == null || dateFin.getValue() == null || dateSignature.getValue() == null) {
                JfxUtil.displayError("Erreur de dates",
                        "Certaines dates sont manquantes :\n" +
                                (dateDebut.getValue() == null ? "- Date de début\n" : "") +
                                (dateFin.getValue() == null ? "- Date de fin\n" : "") +
                                (dateSignature.getValue() == null ? "- Date de signature\n" : ""));
                return;
            }

            // Vérification des dates
            if (!validateDates()) {
                return;
            }

            // Conversion sécurisée des dates
            Date dateDebutSQL = Date.valueOf(dateDebut.getValue());
            Date dateFinSQL = Date.valueOf(dateFin.getValue());
            Date dateSignatureSQL = Date.valueOf(dateSignature.getValue());

            // Vérification de la répartition des montants ou pourcentages
            if (!validateRepartition()) {
                return;
            }

            // Création du bail avec les dates converties
            Bail b = new Bail(dateDebutSQL,
                    Float.parseFloat(fieldMontantLoyer.getText()),
                    checkBoxRenouvelable.isSelected(),
                    Float.parseFloat(fieldTotalCharges.getText()),
                    Float.parseFloat(fieldDepotGarantie.getText()),
                    dateFinSQL,
                    dateSignatureSQL,
                    bienLouable,
                    selectedFile.getName());
            b.save();

            Map<Locataire, AssociationBailLocataires> locataireAssociations;
            if (choiceBoxesLocataires.size() < 2) {
                // Vérification du locataire sélectionné
                ChoiceBox<Locataire> firstChoiceBox = choiceBoxesLocataires.getFirst();
                if (firstChoiceBox == null || firstChoiceBox.getValue() == null) {
                    JfxUtil.displayError("Erreur de locataire", "Veuillez sélectionner un locataire");
                    return;
                }
                Locataire locataire = firstChoiceBox.getValue();
                Date dateDebutLocataireSQL = Date.valueOf(datesDebut.get(0).getValue());
                Date dateFinLocataireSQL = isModification && datesFin.get(0).getValue() != null ?
                        Date.valueOf(datesFin.get(0).getValue()) : null;

                float repartitionLoyer = checkBoxLoyerPourcentage.isSelected() ?
                        Float.parseFloat(fieldsRepartitionsLoyer.get(0).getText()) :
                        (Float.parseFloat(fieldsRepartitionsLoyer.get(0).getText()) / Float.parseFloat(fieldMontantLoyer.getText())) * 100;

                float repartitionCharges = checkBoxChargesPourcentage.isSelected() ?
                        Float.parseFloat(fieldsRepartitionsCharges.get(0).getText()) :
                        (Float.parseFloat(fieldsRepartitionsCharges.get(0).getText()) / Float.parseFloat(fieldTotalCharges.getText())) * 100;

                locataireAssociations = Map.of(locataire,
                        new AssociationBailLocataires(locataire, b,
                                repartitionCharges, repartitionCharges, repartitionCharges, repartitionCharges, repartitionLoyer,
                                dateDebutLocataireSQL,
                                dateFinLocataireSQL
                        ));
            } else {
                // Vérification que la somme des répartitions est égale à 100 pour chaque type
                if (!checkBoxLoyerEuro.isSelected() || !checkBoxChargesEuro.isSelected()) {
                    float totalLoyer = 0;
                    float totalCharges = 0;

                    for (int i = 0; i < choiceBoxesLocataires.size(); i++) {
                        if (!fieldsRepartitionsLoyer.get(i).getText().isEmpty()) {
                            totalLoyer += Float.parseFloat(fieldsRepartitionsLoyer.get(i).getText());
                        }
                        if (!fieldsRepartitionsCharges.get(i).getText().isEmpty()) {
                            totalCharges += Float.parseFloat(fieldsRepartitionsCharges.get(i).getText());
                        }
                    }

                    if (Math.abs(totalLoyer - 100) > 0.01 || Math.abs(totalCharges - 100) > 0.01) {
                        JfxUtil.displayError("Erreur de répartition",
                                "La somme des répartitions doit être égale à 100% pour chaque type de charge.\n" +
                                        String.format("Total Loyer: %.2f%%\n", totalLoyer) +
                                        String.format("Total Charges: %.2f%%\n", totalCharges));
                        return;
                    }
                }

                // Vérification des doublons de locataires
                Set<Locataire> uniqueLocataires = new HashSet<>();
                for (ChoiceBox<Locataire> cb : choiceBoxesLocataires) {
                    if (!uniqueLocataires.add(cb.getValue())) {
                        throw new IllegalArgumentException("Des locataires en double ont été détectés. Chaque locataire doit être unique.");
                    }
                }

                locataireAssociations = choiceBoxesLocataires.stream()
                        .collect(Collectors.toMap(ChoiceBox::getValue,
                                x -> {
                                    int index = choiceBoxesLocataires.indexOf(x);
                                    if (x.getValue() == null) {
                                        throw new IllegalArgumentException("Le locataire " + (index + 1) + " n'est pas sélectionné");
                                    }
                                    if (datesDebut.get(index).getValue() == null) {
                                        throw new IllegalArgumentException("La date de début du locataire " + (index + 1) + " est manquante");
                                    }
                                    Date dateDebutLocataireSQL = Date.valueOf(datesDebut.get(index).getValue());
                                    Date dateFinLocataireSQL = isModification && datesFin.get(index).getValue() != null ?
                                            Date.valueOf(datesFin.get(index).getValue()) : null;

                                    float repartitionLoyer = checkBoxLoyerPourcentage.isSelected() ?
                                            Float.parseFloat(fieldsRepartitionsLoyer.get(index).getText()) :
                                            (Float.parseFloat(fieldsRepartitionsLoyer.get(index).getText()) / Float.parseFloat(fieldMontantLoyer.getText())) * 100;

                                    float repartitionCharges = checkBoxChargesPourcentage.isSelected() ?
                                            Float.parseFloat(fieldsRepartitionsCharges.get(index).getText()) :
                                            (Float.parseFloat(fieldsRepartitionsCharges.get(index).getText()) / Float.parseFloat(fieldTotalCharges.getText())) * 100;

                                    return new AssociationBailLocataires(x.getValue(), b,
                                            repartitionCharges, repartitionCharges, repartitionCharges, repartitionCharges, repartitionLoyer,
                                            dateDebutLocataireSQL,
                                            dateFinLocataireSQL
                                    );
                                }
                        ));
            }
            b.setLocatairesAssociation(locataireAssociations);
        } catch (IllegalArgumentException e) {
            JfxUtil.displayError("Erreur de validation", e.getMessage());
        } catch (Queryable.QbleException e) {
            JfxUtil.displayError("Erreur de sauvegarde",
                    "Une erreur est survenue lors de la sauvegarde du bail : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JfxUtil.displayError("Erreur inattendue",
                    "Une erreur inattendue est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }


    private boolean validateDates() {
        // Vérifier que la date de fin du bail est postérieure à la date de début
        if (dateFin.getValue().isBefore(dateDebut.getValue())) {
            JfxUtil.displayError("Erreur de dates", "La date de fin du bail doit être postérieure à la date de début.");
            return false;
        }

        // Vérifier que les dates des colocataires sont comprises entre les dates de début et de fin du bail
        for (int i = 0; i < datesDebut.size(); i++) {
            if (datesDebut.get(i).getValue().isBefore(dateDebut.getValue()) ||
                    datesFin.get(i).getValue().isAfter(dateFin.getValue())) {
                JfxUtil.displayError("Erreur de dates",
                        "Les dates de début et de fin des colocataires doivent être comprises entre les dates de début et de fin du bail.");
                return false;
            }
        }

        return true;
    }


    private boolean validateRepartition() {
        if (checkBoxLocationSimple.isSelected()) {
            // En mode location simple, on ignore la vérification de répartition
            return true;
        }

        float totalLoyer = 0;
        float totalCharges = 0;

        try {
            if (!fieldMontantLoyer.getText().isEmpty()) {
                totalLoyer = Float.parseFloat(fieldMontantLoyer.getText());
            }
            if (!fieldTotalCharges.getText().isEmpty()) {
                totalCharges = Float.parseFloat(fieldTotalCharges.getText());
            }

            for (int i = 0; i < fieldsRepartitionsLoyer.size(); i++) {
                if (!fieldsRepartitionsLoyer.get(i).getText().isEmpty()) {
                    totalLoyer -= Float.parseFloat(fieldsRepartitionsLoyer.get(i).getText());
                }
            }

            for (int i = 0; i < fieldsRepartitionsCharges.size(); i++) {
                if (!fieldsRepartitionsCharges.get(i).getText().isEmpty()) {
                    totalCharges -= Float.parseFloat(fieldsRepartitionsCharges.get(i).getText());
                }
            }

            if (checkBoxLoyerEuro.isSelected() && Math.abs(totalLoyer) > 0.01) {
                float originalLoyer = Float.parseFloat(fieldMontantLoyer.getText());
                float sumLoyer = 0;
                for (TextField field : fieldsRepartitionsLoyer) {
                    if (!field.getText().isEmpty()) {
                        sumLoyer += Float.parseFloat(field.getText());
                    }
                }
                if (Math.abs(originalLoyer - sumLoyer) > 0.01) {
                    JfxUtil.displayError("Erreur de répartition",
                            "La somme des répartitions du loyer doit être égale au montant total du loyer.\n" +
                                    String.format("Reste à répartir : %.2f €\n", originalLoyer - sumLoyer));
                    return false;
                }
            }

            if (checkBoxChargesEuro.isSelected() && Math.abs(totalCharges) > 0.01) {
                float originalCharges = Float.parseFloat(fieldTotalCharges.getText());
                float sumCharges = 0;
                for (TextField field : fieldsRepartitionsCharges) {
                    if (!field.getText().isEmpty()) {
                        sumCharges += Float.parseFloat(field.getText());
                    }
                }
                if (Math.abs(originalCharges - sumCharges) > 0.01) {
                    JfxUtil.displayError("Erreur de répartition",
                            "La somme des répartitions des charges doit être égale au montant total des charges.\n" +
                                    String.format("Reste à répartir : %.2f €\n", originalCharges - sumCharges));
                    return false;
                }
            }

            if (!checkBoxLoyerEuro.isSelected() && Math.abs(totalLoyer - 100) > 0.01) {
                JfxUtil.displayError("Erreur de répartition",
                        "La somme des répartitions du loyer doit être égale à 100%.\n" +
                                String.format("Reste à répartir : %.2f %%\n", 100 - totalLoyer));
                return false;
            }

            if (!checkBoxChargesEuro.isSelected() && Math.abs(totalCharges - 100) > 0.01) {
                JfxUtil.displayError("Erreur de répartition",
                        "La somme des répartitions des charges doit être égale à 100%.\n" +
                                String.format("Reste à répartir : %.2f %%\n", 100 - totalCharges));
                return false;
            }

        } catch (NumberFormatException e) {
            JfxUtil.displayError("Erreur de conversion des valeurs numériques",
                    "Veuillez vérifier que toutes les valeurs numériques sont correctes.");
            return false;
        }

        return true;
    }


    private int trySavingFile() {
        if (selectedFile != null) {
            try {
                File destinationFolder = new File("./baux/");
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs();
                }
                File destinationFile = new File(destinationFolder, "bail_" + bienLouable.getNumeroFiscal() + "_" + dateDebut.getValue().toString() + ".pdf");
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                selectedFile.createNewFile();
                selectedFile = destinationFile;
                String savedFilePath = destinationFile.getAbsolutePath();
                System.out.println("File saved at: " + savedFilePath);
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File Save Error");
                alert.setHeaderText("Failed to save the file.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return -1;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText("No file was selected.");
            alert.setContentText("Please select a PDF file to save.");
            alert.showAndWait();
            return -1;
        }
    }

    public void annuler(ActionEvent event) {
        Stage stage = (Stage) labelAdresse.getScene().getWindow();
        stage.close();
    }

    public void addLocataireToBail(ActionEvent event) {
        try {
            int rowIndex = choiceBoxesLocataires.size() + 2; // +2 car on commence à la ligne 3

            // Création du ChoiceBox pour le locataire
            ChoiceBox<Locataire> choiceBox = new ChoiceBox<>();
            choiceBox.getItems().addAll(Locataire.findAll());
            choiceBox.setPrefSize(200, 30);
            if(choiceBox.getItems().isEmpty()) {
                JfxUtil.displayError("Aucun locataire trouvé", "Veuillez ajouter un locataire avant de continuer");
            }
            else {
                choiceBox.setValue(choiceBox.getItems().getFirst());
            }

            // Création du TextField pour la répartition des charges
            TextField repartitionCharges = new TextField();
            repartitionCharges.setPromptText("Répartition Charges (€)");
            repartitionCharges.setPrefSize(200, 30);
            repartitionCharges.setVisible(checkBoxColocation.isSelected());
            repartitionCharges.textProperty().addListener((obs, oldVal, newVal) -> updateRepartitionLabels());

            // Création du TextField pour la répartition du loyer
            TextField repartitionLoyer = new TextField();
            repartitionLoyer.setPromptText("Répartition Loyer (€)");
            repartitionLoyer.setPrefSize(200, 30);
            repartitionLoyer.setVisible(checkBoxColocation.isSelected());
            repartitionLoyer.textProperty().addListener((obs, oldVal, newVal) -> updateRepartitionLabels());

            DatePicker datePickerDebut = new DatePicker();
            datePickerDebut.setPromptText("Date Début");
            datePickerDebut.setPrefSize(200, 30);
            datePickerDebut.setValue(dateDebut.getValue());
            datePickerDebut.setVisible(true);

            DatePicker datePickerFin = new DatePicker();
            datePickerDebut.setPromptText("Date Fin");
            datePickerFin.setValue(dateFin.getValue());
            datePickerFin.setPrefSize(200, 30);
            datePickerFin.setVisible(checkBoxColocation.isSelected());

            // Ajout des éléments au GridPane principal dans le nouvel ordre
            gridPaneContent.add(choiceBox, 3, rowIndex);
            gridPaneContent.add(repartitionLoyer, 4, rowIndex);
            gridPaneContent.add(repartitionCharges, 5, rowIndex);
            gridPaneContent.add(datePickerDebut, 6, rowIndex);
            gridPaneContent.add(datePickerFin, 7, rowIndex);

            // Ajout aux listes pour la gestion
            addToLists(choiceBox, datePickerDebut, datePickerFin, repartitionLoyer, repartitionCharges);
        }
        catch (Locataire.LocataireException e) {
            JfxUtil.displayError("Impossible de récupérer les locataires", "Vérifier votre connexion");
        }
    }

    private void addToLists(ChoiceBox<Locataire> choiceBox, DatePicker datePickerDebut, DatePicker datePickerFin, TextField repartitionLoyer, TextField repartitionCharges) {
        choiceBoxesLocataires.add(choiceBox);
        datesDebut.add(datePickerDebut);
        datesFin.add(datePickerFin);

        // Ajout de la validation des nombres non-négatifs pour les champs de répartition
        addNonNegativeValidation(repartitionLoyer);
        addNonNegativeValidation(repartitionCharges);

        fieldsRepartitionsLoyer.add(repartitionLoyer);
        fieldsRepartitionsCharges.add(repartitionCharges);
    }

    private void updateNombreColocataires(int nbColocataires) {
        // Supprimer les lignes en trop
        while (choiceBoxesLocataires.size() > nbColocataires) {
            int lastIndex = choiceBoxesLocataires.size() - 1;
            int rowIndex = lastIndex + 2; // +2 car on commence à la ligne 3

            // Supprimer les éléments du GridPane
            gridPaneContent.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) == rowIndex &&
                            (GridPane.getColumnIndex(node) == 3 || GridPane.getColumnIndex(node) == 4 || GridPane.getColumnIndex(node) == 5 || GridPane.getColumnIndex(node) == 6 || GridPane.getColumnIndex(node) == 7)
            );

            // Supprimer des listes
            choiceBoxesLocataires.remove(lastIndex);
            fieldsRepartitionsCharges.remove(lastIndex);
            fieldsRepartitionsLoyer.remove(lastIndex);
        }

        // Ajouter les lignes manquantes
        while (choiceBoxesLocataires.size() < nbColocataires) {
            addLocataireToBail(null);
        }

        updateColocationFields();
    }

    public void refreshChoiceboixLocataires(){
        choiceBoxesLocataires.forEach(cb -> {
            try {
                cb.getItems().clear();
                cb.getItems().addAll(Locataire.findAll());
            } catch (Locataire.LocataireException e) {
                JfxUtil.displayError("Impossible de récupérer les locataires", "Vérifier votre connexion");
            }
        });
    }

    public void newLocataire(ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("controleur",this);
        JfxUtil.showWindow(s, VueNewLocataire.class);
    }

    public void saveBailDoc(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(pdfFilter);
        selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                File tempFile = File.createTempFile("temp-", ".pdf");
                Files.copy(selectedFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                selectedFile = tempFile;
                System.out.println("Temporary file created at: " + tempFile.getAbsolutePath());
                JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Fichier sélectionné", "Fichier PDF sélectionné avec succès", "Le fichier a été sélectionné avec succès. Cliquez sur \"Valider\" pour continuer.");
            } catch (IOException e) {
                JfxUtil.displayError("Erreur lors de la sélection du fichier", "Veuillez réessayer. Détails: " + e.getMessage());
            }
        } else {
            JfxUtil.displayError("Aucun fichier sélectionné", "Veuillez choisir un fichier PDF.");
        }
    }

    private void addNonNegativeValidation(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    float value = Float.parseFloat(newValue);
                    if (value < 0) {
                        field.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    field.setText(oldValue);
                }
            }
        });
    }

    private void updatePromptTexts() {
        String loyerPrompt = checkBoxLoyerEuro.isSelected() ? "Répartition Loyer (€)" : "Répartition Loyer (%)";
        String chargesPrompt = checkBoxChargesEuro.isSelected() ? "Répartition Charges (€)" : "Répartition Charges (%)";
        
        for (TextField field : fieldsRepartitionsLoyer) {
            field.setPromptText(loyerPrompt);
        }
        
        for (TextField field : fieldsRepartitionsCharges) {
            field.setPromptText(chargesPrompt);
        }
    }

}
