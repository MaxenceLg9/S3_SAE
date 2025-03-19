package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueNewLocataire;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CtrlGererUnBail {

    public DatePicker dateDebut;
    public DatePicker dateFin;
    public TextField fieldMontantLoyer;
    public TextField fieldTotalCharges;
    public TextField fieldDepotGarantie;
    public CheckBox checkBoxArchive;
    public CheckBox checkBoxRenouvelable;

    public Label labelAdresse;
    public Label labelNumeroFiscal;
    public Label labelType;
    public Label labelSurface;
    public Label labelNbPieces;
    public Label labelDateAjout;
    public Label labelNomBienL;
    public DatePicker dateSignature;

    public GridPane gridPaneContent;
    public GridPane gridPaneLocataires;

    private List<ChoiceBox<Locataire>> choiceBoxesLocataires;
    private List<TextField> fieldsRepartitionsElec;
    private List<TextField> fieldsRepartitionsEntretien;
    private List<TextField> fieldsOrduresMenageres;
    private List<DatePicker> datesDebut;
    private List<DatePicker> datesFin;
    private List<TextField> fieldsRepartitionsEau;
    private List<TextField> fieldsRepartitionsLoyer;

    boolean isModification;
    private File selectedFile;
    private BienLouable bienLouable;


    public void initialize(){
        choiceBoxesLocataires = new LinkedList<>();
        fieldsRepartitionsElec = new LinkedList<>();
        fieldsRepartitionsEntretien = new LinkedList<>();
        fieldsOrduresMenageres = new LinkedList<>();
        fieldsRepartitionsEau = new LinkedList<>();
        fieldsRepartitionsLoyer = new LinkedList<>();
        datesDebut = new LinkedList<>();
        datesFin = new LinkedList<>();
        Platform.runLater(() -> {
            Stage stage = (Stage) labelAdresse.getScene().getWindow();

            bienLouable = stage.getProperties().containsKey("idBien") ? (BienLouable) BienLouable.BLBuilder.get((int) stage.getProperties().get("idBien")) : (BienLouable) stage.getProperties().get("bienLouable");
            if(bienLouable == null) {
                stage.close();
                return;
            }

            if(stage.getProperties().containsKey("bail")){
                isModification = true;
            }
            labelNomBienL.setText("Nom " + bienLouable.getIdProprio());
            labelAdresse.setText(bienLouable.getComplementAdresse() + ", " + bienLouable.getAdresse() + ", " + bienLouable.getCodePostal() + ", " + bienLouable.getVille());
            labelNumeroFiscal.setText("N° fiscal " + bienLouable.getNumeroFiscal());
            labelType.setText("Type " + bienLouable.getTypeBien().name());
            labelSurface.setText("Surface " + bienLouable.getSurface());
            labelNbPieces.setText(bienLouable.getNbPieces() + " pièces");
            labelDateAjout.setText("Ajouté le " + bienLouable.getDateAjout().toString());
        });
    }

    public void valider(ActionEvent event) {
        if(fieldsEmpty()) {
            JfxUtil.displayError("Champs vides", "Veuillez remplir tous les champs");
            return;
        }
        if(bienLouable == null)
            JfxUtil.displayError("Bien non trouvé", "Veuillez fermer puis réouvrir la page");
        if(choiceBoxesLocataires.isEmpty()) {
            JfxUtil.displayError("Pas de locataire", "Veuillez ajouter un locataire");
            return;
        }
        if(trySavingFile() == -1){
            return;
        }
        trySavingBail();
        JfxUtil.setAlert(Alert.AlertType.INFORMATION,"Sauvegarde confirmé","Bail enregistré", "Le bail a été enregistré avec succès");
    }

    private boolean fieldsEmpty() {
        for(int i = 0; i < choiceBoxesLocataires.size(); i++){
            if(choiceBoxesLocataires.size()>1)
                if(choiceBoxesLocataires.get(i).getValue() == null || fieldsRepartitionsElec.get(i).getText().isEmpty() || fieldsRepartitionsEntretien.get(i).getText().isEmpty() || fieldsOrduresMenageres.get(i).getText().isEmpty() || datesDebut.get(i).getValue() == null){
                    System.out.println("fields locataire null");
                    return true;
                }
                else if(choiceBoxesLocataires.get(i).getValue() == null) {
                    System.out.println("locataire null");
                    return true;
                }
            if(isModification && datesFin.get(i).getValue() == null){
                System.out.println("modif & date fin null");
                return true;
            }
        }
        if(dateDebut.getValue() == null || dateFin.getValue() == null || fieldMontantLoyer.getText().isEmpty() || fieldTotalCharges.getText().isEmpty() || fieldDepotGarantie.getText().isEmpty() || dateSignature.getValue() == null){
            System.out.println("locataire null");
            return true;
        }
        System.out.println("Files null");
        return selectedFile == null;
    }

    private void trySavingBail() {
        try {
            Bail b = new Bail(Date.valueOf(dateDebut.getValue()),Float.parseFloat(fieldMontantLoyer.getText()), checkBoxRenouvelable.isSelected(), Float.parseFloat(fieldTotalCharges.getText()), Float.parseFloat(fieldDepotGarantie.getText()) ,Date.valueOf(dateFin.getValue()), Date.valueOf(dateSignature.getValue()),bienLouable, selectedFile.getName());
            b.save();

            Map<Locataire,AssociationBailLocataires> locataireAssociations;
            if(choiceBoxesLocataires.size() < 2){
                locataireAssociations = Map.of(choiceBoxesLocataires.getFirst().getValue(),
                        new AssociationBailLocataires(choiceBoxesLocataires.getFirst().getValue(), b,
                                100,100,100,100,100
                        ));
            }else {
                locataireAssociations = choiceBoxesLocataires.stream()
                        .collect(Collectors.toMap(ChoiceBox::getValue,
                                x -> new AssociationBailLocataires(x.getValue(), b,
                                        Float.parseFloat(fieldsRepartitionsElec.get(choiceBoxesLocataires.indexOf(x)).getText()),
                                        Float.parseFloat(fieldsRepartitionsEntretien.get(choiceBoxesLocataires.indexOf(x)).getText()),
                                        Float.parseFloat(fieldsOrduresMenageres.get(choiceBoxesLocataires.indexOf(x)).getText()),
                                        Float.parseFloat(fieldsRepartitionsEau.get(choiceBoxesLocataires.indexOf(x)).getText()),
                                        Float.parseFloat(fieldsRepartitionsLoyer.get(choiceBoxesLocataires.indexOf(x)).getText()),
                                        Date.valueOf(datesDebut.get(choiceBoxesLocataires.indexOf(x)).getValue())
                                )));
            }
            b.setLocatairesAssociation(locataireAssociations);
        } catch (Queryable.QbleException e) {
            e.printStackTrace();
        }
    }

    private int trySavingFile() {
        if (selectedFile != null) {
            try {
                // Specify the destination folder where the file will be saved

                File destinationFolder = new File("./baux/");

                // Ensure the destination folder exists
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs(); // Create the folder if it doesn't exist
                }

                // Create the destination file path
                File destinationFile = new File(destinationFolder, "bail_" + bienLouable.getNumeroFiscal() + "_" + dateDebut.getValue().toString() + ".pdf");

                // Copy the selected file to the destination folder
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                selectedFile.createNewFile();
                selectedFile = destinationFile;

                // Store the path to the saved file
                String savedFilePath = destinationFile.getAbsolutePath();
                System.out.println("File saved at: " + savedFilePath);
                return 0;

            } catch (IOException e) {
                // Handle any errors that occur during file saving
                e.printStackTrace();
                JfxUtil.setAlert(Alert.AlertType.ERROR, "File Save Error", "Failed to save the file.", e.getMessage());
                return -1;
            }
        } else {
            // Show an alert if no file was selected
            JfxUtil.setAlert(Alert.AlertType.WARNING, "No File Selected", "No file was selected.", "Please select a PDF file to save.");
            return -1;
        }
    }

    public void annuler(ActionEvent event) {
        Stage stage = (Stage) labelAdresse.getScene().getWindow();
        stage.close();
    }

    public void addLocataireToBail(ActionEvent event) {
        GridPane gridPaneLine = new GridPane();

        try {
            Label locataire = new Label("Locataire");
            locataire.setFont(Font.font("Arial", FontWeight.BOLD,14));
            locataire.setStyle("-fx-text-fill: white;");

            ChoiceBox<Locataire> choiceBox = new ChoiceBox<>();
            choiceBox.getItems().addAll(Locataire.findAll());
            choiceBox.setPrefSize(200, 30);
            if(choiceBox.getItems().isEmpty()) {
                JfxUtil.displayError("Aucun locataire trouvé", "Veuillez ajouter un locataire avant de continuer");
            }
            else {
                choiceBox.setValue(choiceBox.getItems().getFirst());
            }
            TextField repartitionElec = new TextField();
            repartitionElec.setPromptText("Répartition électricité");
            repartitionElec.setPrefSize(200, 30);

            TextField repartitionsEntretien = new TextField();
            repartitionsEntretien.setPromptText("Répartition entretien");
            repartitionsEntretien.setPrefSize(200, 30);

            TextField orduresMenageres = new TextField();
            orduresMenageres.setPromptText("Ordures ménagères");
            orduresMenageres.setPrefSize(200, 30);

            TextField repartitionEau = new TextField();
            repartitionEau.setPromptText("Repartition de la facture d'eau");
            repartitionEau.setPrefSize(200, 30);

            TextField repartitionLoyer = new TextField();
            repartitionLoyer.setPromptText("Repartition du loyer");
            repartitionLoyer.setPrefSize(200, 30);

            DatePicker dateDebut = new DatePicker();
            dateDebut.setPromptText("Date de début");
            dateDebut.setPrefSize(200, 30);

            DatePicker dateFin = new DatePicker();
            dateFin.setPromptText("Date de fin");
            dateFin.setPrefSize(200, 30);
            dateFin.setVisible(isModification);

            Button supprimerLigne = new Button("Supprimer");
            setSupprimerLineAction(supprimerLigne, choiceBox, gridPaneLine);
            supprimerLigne.setPrefSize(200, 30);
            supprimerLigne.setStyle("-fx-background-color: red; -fx-text-fill: white;");

            gridPaneLine.setHgap(10);
            gridPaneLine.setVgap(5);

            addToLine(gridPaneLine, locataire, choiceBox, repartitionElec, repartitionsEntretien, repartitionLoyer, repartitionEau, orduresMenageres, dateDebut, dateFin, supprimerLigne);
            makeConstraints(gridPaneLine);
            addtToLists(choiceBox, repartitionElec, repartitionsEntretien, repartitionLoyer, repartitionEau, orduresMenageres, dateDebut, dateFin);


            int rows = gridPaneLocataires.getRowCount();

            setVisibleFieldsColocations(rows > 1);

            gridPaneLine.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gridPaneLine.setMaxWidth(Double.MAX_VALUE);
            gridPaneLine.setAlignment(javafx.geometry.Pos.CENTER);
            gridPaneLocataires.add(gridPaneLine, 0, rows);
            System.out.println(gridPaneLine.getColumnCount());
            GridPane.setHalignment(gridPaneLine, HPos.CENTER);
        }
        catch (Locataire.LocataireException e) {
            JfxUtil.displayError("Impossible de récupérer les locataires", "Vérifier votre connexion");
        }
    }

    private void setSupprimerLineAction(Button supprimerLigne, ChoiceBox<Locataire> choiceBox, GridPane gridPaneLine) {
        supprimerLigne.setOnAction(e -> {
            int index = choiceBoxesLocataires.indexOf(choiceBox);
            choiceBoxesLocataires.remove(index);
            fieldsRepartitionsElec.remove(index);
            fieldsRepartitionsEntretien.remove(index);
            fieldsOrduresMenageres.remove(index);
            fieldsRepartitionsEau.remove(index);
            fieldsRepartitionsLoyer.remove(index);
            datesDebut.remove(index);
            datesFin.remove(index);
            gridPaneLocataires.getChildren().removeAll(gridPaneLine);
        });
    }

    private static void addToLine(GridPane gridPaneLine, Label locataire, ChoiceBox<Locataire> choiceBox, TextField repartitionElec, TextField repartitionsEntretien, TextField repartitionLoyer, TextField repartitionEau, TextField orduresMenageres, DatePicker dateDebut, DatePicker dateFin, Button supprimerLigne) {
        gridPaneLine.add(locataire, 0, 0);
        gridPaneLine.add(choiceBox, 1, 0);
        gridPaneLine.add(repartitionElec, 2, 0);
        gridPaneLine.add(repartitionsEntretien, 3, 0);
        gridPaneLine.add(repartitionLoyer, 4, 0);
        gridPaneLine.add(repartitionEau, 5, 0);
        gridPaneLine.add(orduresMenageres, 6, 0);
        gridPaneLine.add(dateDebut, 7, 0);
        gridPaneLine.add(dateFin, 8, 0);
        gridPaneLine.add(supprimerLigne, 9, 0);
    }

    private static void makeConstraints(GridPane gridPaneLine) {
        ColumnConstraints c = new ColumnConstraints();
        c.setPrefWidth(200);
        c.setMaxWidth(Region.USE_COMPUTED_SIZE);
        c.setFillWidth(true);
        c.setHgrow(Priority.NEVER);
        c.setHalignment(HPos.CENTER);

        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
        gridPaneLine.getColumnConstraints().add(c);
    }

    private void addtToLists(ChoiceBox<Locataire> choiceBox, TextField repartitionElec, TextField repartitionsEntretien, TextField repartitionLoyer, TextField repartitionEau, TextField orduresMenageres, DatePicker dateDebut, DatePicker dateFin) {
        choiceBoxesLocataires.add(choiceBox);
        fieldsRepartitionsElec.add(repartitionElec);
        fieldsRepartitionsEntretien.add(repartitionsEntretien);
        fieldsOrduresMenageres.add(orduresMenageres);
        fieldsRepartitionsEau.add(repartitionEau);
        fieldsRepartitionsLoyer.add(repartitionLoyer);
        datesDebut.add(dateDebut);
        datesFin.add(dateFin);
    }

    private void setVisibleFieldsColocations(boolean visible) {
        for (int i = 0; i < choiceBoxesLocataires.size(); i++) {
            fieldsRepartitionsElec.get(i).setVisible(visible);
            fieldsRepartitionsEntretien.get(i).setVisible(visible);
            fieldsOrduresMenageres.get(i).setVisible(visible);
            fieldsRepartitionsEau.get(i).setVisible(visible);
            fieldsRepartitionsLoyer.get(i).setVisible(visible);
            datesDebut.get(i).setVisible(visible);
        }
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
        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set file extension filters to only allow PDF files
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(pdfFilter);

        // Open the FileChooser dialog
        selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                // Create a temporary file in the system's default temporary file location
                File tempFile = File.createTempFile("temp-", ".pdf");

                // Copy the selected file to the temporary file
                Files.copy(selectedFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                selectedFile = tempFile;

                // Log the temporary file location
                System.out.println("Temporary file created at: " + tempFile.getAbsolutePath());
                JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Fichier sélectionné", "Fichier PDF sélectionné avec succès", "Le fichier a été sélectionné avec succès. Cliquez sur \"Valider\" pour continuer.");
            } catch (IOException e) {
                // Display error if there is an issue creating the temporary file or copying
                JfxUtil.displayError("Erreur lors de la sélection du fichier", "Veuillez réessayer. Détails: " + e.getMessage());
            }
        } else {
            // Handle the case where no file was selected
            JfxUtil.displayError("Aucun fichier sélectionné", "Veuillez choisir un fichier PDF.");
        }
    }
}
