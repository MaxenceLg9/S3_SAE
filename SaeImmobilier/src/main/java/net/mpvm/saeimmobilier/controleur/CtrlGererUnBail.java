package net.mpvm.saeimmobilier.controleur;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueNewLocataire;

public class CtrlGererUnBail {
    private static final int MIN_COLOCATAIRES = 2;
    private static final int MAX_COLOCATAIRES = 8;
    private static final float EPSILON = 0.01f;
    private static final String DEFAULT_COLOCATAIRES = "2";
    private static final int GRID_FIRST_LOCATAIRE_COL = 3;
    private static final int GRID_LAST_COL = 7;
    
    public DatePicker dateDebut, dateFin, dateSignature;
    public TextField fieldMontantLoyer, fieldDepotGarantie, fieldNbColocataires;
    public CheckBox checkBoxArchive, checkBoxRenouvelable, checkBoxLocationSimple, checkBoxColocation, checkBoxLoyerEuro, checkBoxLoyerPourcentage;
    public Label labelResteLoyer, labelResteCharges, labelAdresse, labelComplementAdresse, labelType;
    public GridPane gridPaneContent;
    private List<ChoiceBox<Locataire>> choiceBoxesLocataires = new LinkedList<>();
    private List<DatePicker> datesDebut = new LinkedList<>(), datesFin = new LinkedList<>();
    private List<TextField> fieldsRepartitionsCharges = new LinkedList<>(), fieldsRepartitionsLoyer = new LinkedList<>();
    boolean isModification;
    private File selectedFile;
    private BienLouable bienLouable;

    // Initialise l'interface du contrôleur et configure les composants graphiques
    public void initialize() {
        try {
            addNonNegativeValidation(fieldMontantLoyer);
            addNonNegativeValidation(fieldDepotGarantie);
            checkBoxLocationSimple.setSelected(true);
            checkBoxLocationSimple.setDisable(false);
            fieldNbColocataires.setText(DEFAULT_COLOCATAIRES);
            choiceBoxesLocataires = new LinkedList<>();
            fieldsRepartitionsCharges = new LinkedList<>();
            fieldsRepartitionsLoyer = new LinkedList<>();
            datesDebut = new LinkedList<>();
            datesFin = new LinkedList<>();

            checkBoxLocationSimple.setOnAction(e -> {
                checkBoxColocation.setSelected(!checkBoxLocationSimple.isSelected());
                fieldNbColocataires.setDisable(!checkBoxColocation.isSelected());
                updateColocationFields();
                if (checkBoxLocationSimple.isSelected()) {
                    labelResteLoyer.setText("");
                    labelResteCharges.setText("");
                    updateNombreColocataires(1);
                    if (!fieldsRepartitionsLoyer.isEmpty() && !fieldsRepartitionsCharges.isEmpty()) {
                        fieldsRepartitionsLoyer.get(0).setText(fieldMontantLoyer.getText());
                        fieldsRepartitionsCharges.get(0).setText("100");
                        fieldsRepartitionsLoyer.get(0).setDisable(true);
                        fieldsRepartitionsCharges.get(0).setDisable(true);
                    }
                } else {
                    if (!fieldsRepartitionsLoyer.isEmpty() && !fieldsRepartitionsCharges.isEmpty()) {
                        fieldsRepartitionsLoyer.get(0).setDisable(false);
                        fieldsRepartitionsCharges.get(0).setDisable(false);
                    }
                    updateNombreColocataires(2);
                }
            });
            checkBoxColocation.setOnAction(e -> {
                if (checkBoxColocation.isSelected()) {
                    checkBoxLocationSimple.setSelected(false);
                    int nbColocataires = Math.max(2, Integer.parseInt(fieldNbColocataires.getText()));
                    fieldNbColocataires.setText(String.valueOf(nbColocataires));
                    updateNombreColocataires(nbColocataires);
                    if (!fieldsRepartitionsLoyer.isEmpty() && !fieldsRepartitionsCharges.isEmpty()) {
                        fieldsRepartitionsLoyer.get(0).setDisable(false);
                        fieldsRepartitionsCharges.get(0).setDisable(false);
                        fieldsRepartitionsLoyer.forEach(field -> field.setText(""));
                        fieldsRepartitionsCharges.forEach(field -> field.setText(""));
                    }
                    updateRepartitionLabels();
                } else {
                    checkBoxLocationSimple.setSelected(true);
                    updateNombreColocataires(1);
                    if (!fieldsRepartitionsLoyer.isEmpty() && !fieldsRepartitionsCharges.isEmpty()) {
                        fieldsRepartitionsLoyer.get(0).setText(fieldMontantLoyer.getText());
                        fieldsRepartitionsCharges.get(0).setText("100");
                        fieldsRepartitionsLoyer.get(0).setDisable(true);
                        fieldsRepartitionsCharges.get(0).setDisable(true);
                    }
                    labelResteLoyer.setText("");
                    labelResteCharges.setText("");
                }
                fieldNbColocataires.setDisable(!checkBoxColocation.isSelected());
            });

            fieldNbColocataires.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    try {
                        int nbColocataires = Integer.parseInt(newValue);
                        if (checkBoxColocation.isSelected()) {
                            if (nbColocataires < MIN_COLOCATAIRES) {
                                fieldNbColocataires.setText(DEFAULT_COLOCATAIRES);
                                nbColocataires = MIN_COLOCATAIRES;
                            } else if (nbColocataires > MAX_COLOCATAIRES) {
                                fieldNbColocataires.setText(String.valueOf(MAX_COLOCATAIRES));
                                nbColocataires = MAX_COLOCATAIRES;
                            }
                        } else if (nbColocataires != MIN_COLOCATAIRES) {
                            fieldNbColocataires.setText(DEFAULT_COLOCATAIRES);
                            return;
                        }
                        updateNombreColocataires(nbColocataires);
                    } catch (NumberFormatException ex) {
                        fieldNbColocataires.setText(oldValue);
                    }
                }
            });

            addLocataireToBail(null);
            updateColocationFields();

            checkBoxLoyerEuro.setSelected(true);
            checkBoxLoyerEuro.setOnAction(_ -> updateLoyerCheckBoxes(true));
            checkBoxLoyerPourcentage.setOnAction(_ -> updateLoyerCheckBoxes(false));

            updateRepartitionLabels();
            fieldNbColocataires.setDisable(true);

            fieldMontantLoyer.textProperty().addListener((observable, oldValue, newValue) -> {
                updateRepartitionLabels();
            });

            dateDebut.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateLocataireDates();
            });

            dateFin.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateLocataireDates();
            });

            Platform.runLater(() -> {
                Stage currentStage = (Stage) labelAdresse.getScene().getWindow();

                try {
                    if (currentStage.getProperties().containsKey("idBien")) {
                        bienLouable = BienLouable.BLBuilder.getBienLouable((int) currentStage.getProperties().get("idBien"));
                    } else if (currentStage.getProperties().containsKey("bienLouable")) {
                        bienLouable = (BienLouable) currentStage.getProperties().get("bienLouable");
                    }

                    if (bienLouable != null) {
                        labelAdresse.setText(bienLouable.getAdresse());
                        labelComplementAdresse.setText(bienLouable.getComplementAdresse());
                        labelType.setText(bienLouable.getTypeBien().toString());
                    }
                } catch (Exception e) {
                    JfxUtil.displayError("Erreur", "Une erreur s'est produite lors de la récupération du bien louable : " + e.getMessage());
                }
            });
        } catch (Exception e) {
            
            JfxUtil.displayError("Erreur inattendue",
                    "Une erreur inattendue est survenue :\n" +
                            "Type : " + e.getClass().getName() + "\n" +
                            "Message : " + e.getMessage() + "\n" +
                            "Cause : " + (e.getCause() != null ? e.getCause().getMessage() : "Aucune cause"));
        }
    }

    // Met à jour l'affichage des cases à cocher pour le type de loyer (euro ou pourcentage)
    private void updateLoyerCheckBoxes(boolean isEuroSelected) {
        checkBoxLoyerPourcentage.setSelected(!isEuroSelected);
        checkBoxLoyerEuro.setSelected(isEuroSelected);
        if (checkBoxLocationSimple.isSelected()) {
            if (isEuroSelected) {
                fieldsRepartitionsLoyer.get(0).setText(fieldMontantLoyer.getText());
            } else {
                fieldsRepartitionsLoyer.get(0).setText("100");
            }
        }
        updateRepartitionLabels();
        updatePromptTexts();
    }

    // Synchronise les dates de tous les locataires avec les dates principales du bail
    private void updateLocataireDates() {
        for (int i = 0; i < datesDebut.size(); i++) {
            datesDebut.get(i).setValue(dateDebut.getValue());
            datesFin.get(i).setValue(dateFin.getValue());
        }
    }

    // Gère l'affichage des champs spécifiques à la colocation
    private void updateColocationFields() {
        boolean isColocation = checkBoxColocation.isSelected();
        for (int index = 0; index < choiceBoxesLocataires.size(); index++) {
            final int rowIndex = index + 2;
            if (index == 0) {
                gridPaneContent.getChildren().stream()
                        .filter(node -> GridPane.getRowIndex(node) == rowIndex && 
                                GridPane.getColumnIndex(node) == GRID_FIRST_LOCATAIRE_COL)
                        .forEach(node -> node.setVisible(true));
            }
            for (int col = GRID_FIRST_LOCATAIRE_COL; col <= GRID_LAST_COL; col++) {
                final int currentCol = col;
                final int finalIndex = index;
                gridPaneContent.getChildren().stream()
                        .filter(node -> GridPane.getRowIndex(node) == rowIndex &&
                                GridPane.getColumnIndex(node) == currentCol &&
                                !(finalIndex == 0 && currentCol == GRID_FIRST_LOCATAIRE_COL))
                        .forEach(node -> node.setVisible(isColocation));
            }
        }
        updateRepartitionLabels();
    }

    // Convertit une chaîne en nombre flottant en gérant le format local (virgule/point)
    private float parseFloatWithLocale(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0f;
        }
        text = text.trim().replace(',', '.');
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    // Met à jour les labels affichant la répartition des loyers et charges
    private void updateRepartitionLabels() {
        float totalLoyer = 0;
        float totalRepartitionLoyer = 0;
        float totalRepartitionCharges = 0;

        try {
            String montantLoyerText = fieldMontantLoyer.getText().trim();
            if (!montantLoyerText.isEmpty()) {
                totalLoyer = parseFloatWithLocale(montantLoyerText);
                if (totalLoyer < 0) {
                    JfxUtil.displayError("Erreur de saisie", "Le montant du loyer ne peut pas être négatif");
                    return;
                }
            }

            for (TextField field : fieldsRepartitionsLoyer) {
                String text = field.getText().trim();
                if (!text.isEmpty()) {
                    float value = parseFloatWithLocale(text);
                    if (value < 0) {
                        JfxUtil.displayError("Erreur de saisie", "La répartition du loyer ne peut pas être négative");
                        return;
                    }
                    totalRepartitionLoyer += value;
                }
            }

            for (TextField field : fieldsRepartitionsCharges) {
                String text = field.getText().trim();
                if (!text.isEmpty()) {
                    float value = parseFloatWithLocale(text);
                    if (value < 0) {
                        JfxUtil.displayError("Erreur de saisie", "La répartition des charges ne peut pas être négative");
                        return;
                    }
                    totalRepartitionCharges += value;
                }
            }

            if (checkBoxLoyerEuro.isSelected()) {
                labelResteLoyer.setText(String.format("Reste à répartir : %.2f €", totalLoyer - totalRepartitionLoyer));
            } else {
                labelResteLoyer.setText(String.format("Reste à répartir : %.2f %%", 100 - totalRepartitionLoyer));
            }

            labelResteCharges.setText(String.format("Reste à répartir : %.2f %%", 100 - totalRepartitionCharges));

        } catch (Exception e) {
            JfxUtil.displayError("Erreur inattendue", "Une erreur est survenue lors du calcul des répartitions");

        }
    }

    // Valide et enregistre le bail avec toutes ses informations
    public void valider() {
        try {
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
            if (bienLouable == null) {
                JfxUtil.displayError("Erreur de bien louable",
                        "Le bien louable n'a pas été trouvé.\n" +
                                "Veuillez fermer puis réouvrir la page.");
                return;
            }
            if (choiceBoxesLocataires.isEmpty()) {
                JfxUtil.displayError("Erreur de locataire",
                        "Veuillez ajouter au moins un locataire au bail.");
                return;
            }
            if (checkBoxColocation.isSelected()) {
                if (!validateUniqueLocataires()) {
                    return;
                }
            }
            if (validateRepartition() || trySavingFile() == -1 || !validateDates()) {
                return;
            }
            
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

        }
    }
    // Vérifie qu'un même locataire n'est pas sélectionné plusieurs fois
    private boolean validateUniqueLocataires() {
        Set<Integer> uniqueIds = new HashSet<>(); 
        for (ChoiceBox<Locataire> choiceBox : choiceBoxesLocataires) {
            Locataire locataire = choiceBox.getValue();
            if (locataire != null) {
                if (!uniqueIds.add(locataire.getIdLocataire())) { 
                    JfxUtil.displayError("Erreur de locataire",
                            "Le locataire " + locataire.getNom() + " " + locataire.getPrenom() + 
                            " (ID: " + locataire.getIdLocataire() + ") est sélectionné plusieurs fois.");
                    return false;
                }
            }
        }
        return true;
    }

    // Vérifie la validité des informations d'un locataire à l'index spécifié
    private boolean validateLocataire(int index) {
        if (choiceBoxesLocataires.get(index).getValue() == null || 
            fieldsRepartitionsCharges.get(index).getText().isEmpty() || 
            datesDebut.get(index).getValue() == null || (isModification && datesFin.get(index).getValue() == null)) {
            return true;
        }
        return false;
    }

    // Vérifie que toutes les dates obligatoires sont renseignées
    private boolean validateRequiredDates() {
        if (dateDebut.getValue() == null || dateSignature.getValue() == null || dateFin.getValue() == null) {
            return true;
        }
        
        return false;
    }

    // Vérifie que tous les champs obligatoires sont remplis
    private boolean validateRequiredFields() {
        if (fieldMontantLoyer.getText().isEmpty() || fieldDepotGarantie.getText().isEmpty() || selectedFile == null) {
            return true;
        }
        
        return false;
    }

    // Vérifie si des champs obligatoires sont vides
    private boolean fieldsEmpty() {
        if (checkBoxLocationSimple.isSelected()) {
            if (choiceBoxesLocataires.isEmpty() || choiceBoxesLocataires.get(0).getValue() == null) {
                return true;
            }
            if (isModification && datesFin.get(0).getValue() == null) {
                return true;
            }
        } else {
            for (int i = 0; i < choiceBoxesLocataires.size(); i++) {
                if (validateLocataire(i)) return true;
            }
        }
        return validateRequiredDates() || validateRequiredFields();
    }

    // Tente de sauvegarder le bail et ses associations avec les locataires
    public void trySavingBail() {
        try {
            if (dateDebut.getValue() == null || dateFin.getValue() == null || dateSignature.getValue() == null) {
                JfxUtil.displayError("Erreur de dates",
                        "Certaines dates sont manquantes :\n" +
                                (dateDebut.getValue() == null ? "- Date de début\n" : "") +
                                (dateFin.getValue() == null ? "- Date de fin\n" : "") +
                                (dateSignature.getValue() == null ? "- Date de signature\n" : ""));
                return;
            }

            if (!validateDates()) {
                return;
            }

            Date dateDebutSQL = Date.valueOf(dateDebut.getValue());
            Date dateFinSQL = Date.valueOf(dateFin.getValue());
            Date dateSignatureSQL = Date.valueOf(dateSignature.getValue());

            if (validateRepartition()) {
                return;
            }

            String fileName = selectedFile != null ? selectedFile.getName() : null;
            Bail b = new Bail(dateDebutSQL,
                    Float.parseFloat(fieldMontantLoyer.getText()),
                    checkBoxRenouvelable.isSelected(),
                    Float.parseFloat("0"),
                    Float.parseFloat(fieldDepotGarantie.getText()),
                    dateFinSQL,
                    dateSignatureSQL,
                    bienLouable,
                    fileName);
            b.save();

            Map<Locataire, AssociationBailLocataires> locataireAssociations = new HashMap<>();
            if (choiceBoxesLocataires.size() < 2) {
                ChoiceBox<Locataire> firstChoiceBox = choiceBoxesLocataires.getFirst();
                if (firstChoiceBox == null || firstChoiceBox.getValue() == null) {
                    JfxUtil.displayError("Erreur de locataire", "Veuillez sélectionner un locataire");
                    return;
                }
                Locataire locataire = firstChoiceBox.getValue();
                Date dateDebutLocataireSQL = Date.valueOf(datesDebut.get(0).getValue());
                Date dateFinLocataireSQL = (isModification && datesFin.get(0).getValue() != null) ? Date.valueOf(datesFin.get(0).getValue()) : dateFinSQL;

                float repartitionLoyer = 100.0f;
                float repartitionCharges = 100.0f;

                AssociationBailLocataires association = new AssociationBailLocataires(locataire, b, repartitionCharges, repartitionCharges, repartitionCharges, repartitionCharges, (checkBoxColocation.isSelected() && checkBoxLoyerEuro.isSelected()) ? repartitionLoyer * 10000 : repartitionLoyer, dateDebutLocataireSQL, dateFinLocataireSQL);
                locataireAssociations.put(locataire, association);
            } else {

                float totalMontantLoyer = Float.parseFloat(fieldMontantLoyer.getText());
                locataireAssociations = new HashMap<>();
                
                for (ChoiceBox<Locataire> x : choiceBoxesLocataires) {
                    int index = choiceBoxesLocataires.indexOf(x);
                    if (x.getValue() == null) {
                        throw new IllegalArgumentException("Le locataire " + (index + 1) + " n'est pas sélectionné");
                    }
                    if (datesDebut.get(index).getValue() == null) {
                        throw new IllegalArgumentException("La date de début du locataire " + (index + 1) + " est manquante");
                    }
                    Date dateDebutLocataireSQL = Date.valueOf(datesDebut.get(index).getValue());
                    Date dateFinLocataireSQL = isModification && datesFin.get(index).getValue() != null ? Date.valueOf(datesFin.get(index).getValue()) : dateFinSQL;

                    float repartitionLoyer = checkBoxLoyerEuro.isSelected() ? Math.abs((Float.parseFloat(fieldsRepartitionsLoyer.get(index).getText().isEmpty() ? "0" : fieldsRepartitionsLoyer.get(index).getText()) / totalMontantLoyer)) * EPSILON: Float.parseFloat(fieldsRepartitionsLoyer.get(index).getText().isEmpty() ? "0" : fieldsRepartitionsLoyer.get(index).getText());
                    float repartitionCharges = Float.parseFloat(fieldsRepartitionsCharges.get(index).getText().isEmpty() ? "0" : fieldsRepartitionsCharges.get(index).getText());

                    AssociationBailLocataires association = new AssociationBailLocataires(x.getValue(), b, repartitionCharges, repartitionCharges, repartitionCharges, repartitionCharges, (checkBoxColocation.isSelected() && checkBoxLoyerEuro.isSelected()) ? repartitionLoyer * 10000 : repartitionLoyer, dateDebutLocataireSQL, dateFinLocataireSQL
                    );
                    locataireAssociations.put(x.getValue(), association);
                }
            }
            if (locataireAssociations.isEmpty()) {
                throw new IllegalArgumentException("Aucune association locataire-bail n'a été créée");
            }
            b.setLocatairesAssociation(locataireAssociations);
        } catch (IllegalArgumentException e) {
            JfxUtil.displayError("Erreur de validation", e.getMessage());

        } catch (Queryable.QbleException e) {
            float totalRepartitionLoyer = 0;
            float totalRepartitionCharges = 0;
            for (TextField field : fieldsRepartitionsLoyer) {
                totalRepartitionLoyer += parseFloatWithLocale(field.getText().trim());
            }
            for (TextField field : fieldsRepartitionsCharges) {
                totalRepartitionCharges += parseFloatWithLocale(field.getText().trim());
            }


        }
    }

    // Vérifie la cohérence des dates principales du bail
    private boolean validateMainDates() {
        if (dateDebut.getValue() == null || dateFin.getValue() == null) {
            JfxUtil.displayError("Erreur de dates","Les dates de début et de fin du bail sont obligatoires.");
            return false;
        }
        if (dateFin.getValue().isBefore(dateDebut.getValue())) {
            JfxUtil.displayError("Erreur de dates","La date de fin du bail doit être postérieure à la date de début.");
            return false;
        }
        return true;
    }

    // Vérifie la validité des dates pour un locataire spécifique
    private boolean validateTenantDates(int tenantIndex) {
        if (datesDebut.get(tenantIndex).getValue() == null) {JfxUtil.displayError("Erreur de dates","La date de début est manquante pour le locataire " + (tenantIndex + 1));
            return false;
        }
        if (datesFin.get(tenantIndex).getValue() != null && (datesDebut.get(tenantIndex).getValue().isBefore(dateDebut.getValue()) || datesFin.get(tenantIndex).getValue().isAfter(dateFin.getValue()))) {
            JfxUtil.displayError("Erreur de dates","Les dates de début et de fin des colocataires doivent être comprises entre les dates de début et de fin du bail.");
            return false;
        }
        return true;
    }

    // Vérifie la validité de toutes les dates du bail et des locataires
    private boolean validateDates() {
        if (!validateMainDates()) {
            return false;
        }
        for (int i = 0; i < datesDebut.size(); i++) {
            if (!validateTenantDates(i)) {
                return false;
            }
        }
        return true;
    }

    private float calculateSum(List<TextField> fields) {
        return fields.stream()
                .filter(field -> !field.getText().isEmpty())
                .map(field -> parseFloatWithLocale(field.getText()))
                .reduce(0f, Float::sum);
    }

    private void setDefaultValues() {
        if (checkBoxLoyerEuro.isSelected()) {
            fieldsRepartitionsLoyer.get(0).setText(fieldMontantLoyer.getText());
        } else {
            fieldsRepartitionsLoyer.get(0).setText("100");
        }
        fieldsRepartitionsCharges.get(0).setText("100");
    }

    private boolean validateDistribution(float sum, float target, String errorTitle, String errorMessage) {
        if (Math.abs(target - sum) > EPSILON) {
            JfxUtil.displayError(errorTitle, errorMessage);
            return true;
        }
        return false;
    }

    private boolean validateRepartition() {
        if (checkBoxLocationSimple.isSelected()) {
            setDefaultValues();
            return false;
        }

        try {
            float sumCharges = calculateSum(fieldsRepartitionsCharges);
            if (validateDistribution(sumCharges, 100, "Erreur de répartition des charges","La somme des pourcentages des charges doit être égale à 100%.")) {
                return true;
            }

            float sumLoyer = calculateSum(fieldsRepartitionsLoyer);
            if (checkBoxLoyerEuro.isSelected()) {
                float totalLoyer = parseFloatWithLocale(fieldMontantLoyer.getText());
                if (validateDistribution(sumLoyer, totalLoyer, "Erreur de répartition du loyer",
                        String.format("La somme des parts de loyer (%.2f€) doit être égale au montant total du loyer (%.2f€).", 
                        sumLoyer, totalLoyer))) {
                    return true;
                }
            } else {
                if (validateDistribution(sumLoyer, 100, "Erreur de répartition du loyer","La somme des pourcentages du loyer doit être égale à 100%.")) {
                    return true;
                }
            }
            return false;
        } catch (NumberFormatException e) {
            JfxUtil.displayError("Erreur de conversion des valeurs numériques","Veuillez vérifier que toutes les valeurs numériques sont correctes.");
            return true;
        }
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
                return 0;
            } catch (IOException e) {
    
                JfxUtil.setAlert(Alert.AlertType.ERROR, "File Save Error", "Failed to save the file.", e.getMessage());
                return -1;
            }
        } else {
            JfxUtil.setAlert(Alert.AlertType.WARNING, "No File Selected", "No file was selected.", "Please select a PDF file to save.");
            return -1;
        }
    }

    public void annuler(ActionEvent event) {
        Stage stage = (Stage) labelAdresse.getScene().getWindow();
        stage.close();
    }
    public void creerComposant(Node composant, String promptText){
        if (composant instanceof DatePicker) {
            DatePicker datePicker = (DatePicker) composant;
            datePicker.setPromptText(promptText);
            datePicker.setVisible(checkBoxColocation.isSelected());
            datePicker.setPrefSize(200, 30);
        } else if (composant instanceof TextField) {
            TextField textField = (TextField) composant;
            textField.setPromptText(promptText);
            textField.setVisible(checkBoxColocation.isSelected());
            textField.setPrefSize(200, 30);
            textField.textProperty().addListener((obs, oldVal, newVal) -> updateRepartitionLabels());

        }
    }
    public void addLocataireToBail(ActionEvent event) {
        try {
            int rowIndex = choiceBoxesLocataires.size() + 2;

            ChoiceBox<Locataire> choiceBox = new ChoiceBox<>();
            choiceBox.getItems().addAll(Locataire.findAll());
            choiceBox.setPrefSize(200, 30);
            if(choiceBox.getItems().isEmpty()) {
                JfxUtil.displayError("Aucun locataire trouvé", "Veuillez ajouter un locataire avant de continuer");
            }
            else {
                choiceBox.setValue(choiceBox.getItems().getFirst());
            }

            TextField repartitionCharges = new TextField();
            creerComposant(repartitionCharges, "Répartition Charges (%)");
            repartitionCharges.textProperty().addListener((obs, oldVal, newVal) -> updateRepartitionLabels());

            TextField repartitionLoyer = new TextField();
            creerComposant(repartitionLoyer, "Répartition Loyer (€)");
            repartitionLoyer.textProperty().addListener((obs, oldVal, newVal) -> updateRepartitionLabels());

            DatePicker datePickerDebut = new DatePicker();
            creerComposant(datePickerDebut, "Date Début");
            datePickerDebut.setValue(dateDebut.getValue());

            DatePicker datePickerFin = new DatePicker();
            creerComposant(datePickerFin, "Date Fin");
            datePickerFin.setValue(dateFin.getValue());

            gridPaneContent.add(choiceBox, 3, rowIndex);
            gridPaneContent.add(repartitionLoyer, 4, rowIndex);
            gridPaneContent.add(repartitionCharges, 5, rowIndex);
            gridPaneContent.add(datePickerDebut, 6, rowIndex);
            gridPaneContent.add(datePickerFin, 7, rowIndex);
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

        addNonNegativeValidation(repartitionLoyer);
        addNonNegativeValidation(repartitionCharges);

        fieldsRepartitionsLoyer.add(repartitionLoyer);
        fieldsRepartitionsCharges.add(repartitionCharges);
    }

    private void updateNombreColocataires(int nbColocataires) {
        while (choiceBoxesLocataires.size() > nbColocataires) {
            int lastIndex = choiceBoxesLocataires.size() - 1;
            int rowIndex = lastIndex + 2;
            gridPaneContent.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) == rowIndex &&
                            (GridPane.getColumnIndex(node) == 3 || GridPane.getColumnIndex(node) == 4 || GridPane.getColumnIndex(node) == 5 || GridPane.getColumnIndex(node) == 6 || GridPane.getColumnIndex(node) == 7)
            );
            choiceBoxesLocataires.remove(lastIndex);
            fieldsRepartitionsCharges.remove(lastIndex);
            fieldsRepartitionsLoyer.remove(lastIndex);
        }
        while (choiceBoxesLocataires.size() < nbColocataires) {
            addLocataireToBail(null);
        }

        float montantLoyer = 0;
        try {
            if (!fieldMontantLoyer.getText().isEmpty()) {
                montantLoyer = Float.parseFloat(fieldMontantLoyer.getText());
            }
        } catch (NumberFormatException e) {
            JfxUtil.displayError("Erreur de conversion", "Le montant du loyer doit être un nombre valide.");
        }

        float partLoyer = checkBoxLoyerEuro.isSelected() ? montantLoyer / nbColocataires : 100.0f / nbColocataires;
        float partCharges = 100.0f / nbColocataires;

        for (int i = 0; i < nbColocataires; i++) {
            if (!fieldsRepartitionsLoyer.isEmpty() && i < fieldsRepartitionsLoyer.size()) {
                fieldsRepartitionsLoyer.get(i).setText(String.format("%.2f", partLoyer));
            }
            if (!fieldsRepartitionsCharges.isEmpty() && i < fieldsRepartitionsCharges.size()) {
                fieldsRepartitionsCharges.get(i).setText(String.format("%.2f", partCharges));
            }
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
        String chargesPrompt = "Répartition Charges (%)";
        for (TextField field : fieldsRepartitionsLoyer) {
            field.setPromptText(loyerPrompt);
        }
        for (TextField field : fieldsRepartitionsCharges) {
            field.setPromptText(chargesPrompt);
        }
    }
}

