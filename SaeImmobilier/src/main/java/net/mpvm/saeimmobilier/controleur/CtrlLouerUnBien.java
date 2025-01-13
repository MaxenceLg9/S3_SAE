package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Bail;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.util.JfxUtil;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CtrlLouerUnBien {

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

    private List<ChoiceBox<Locataire>> choiceBoxesLocataires;
    private List<TextField> fieldsRepartitionsElec;
    private List<TextField> fieldsRepartitionsEau;
    private List<TextField> fieldsOrduresMenageres;

    private int idBien;

    public void initialize(){
        choiceBoxesLocataires = new LinkedList<>();
        fieldsRepartitionsElec = new LinkedList<>();
        fieldsRepartitionsEau = new LinkedList<>();
        fieldsOrduresMenageres = new LinkedList();
        Platform.runLater(() -> {
            Stage stage = (Stage) labelAdresse.getScene().getWindow();

            BienLouable bienLouable = stage.getProperties().containsKey("idBien") ? (BienLouable) BienLouable.BLBuilder.get((int) stage.getProperties().get("idBien")) : (BienLouable) stage.getProperties().get("bienLouable");
            if(bienLouable == null) {
                stage.close();
                return;
            }
            labelNomBienL.setText("Nom " + bienLouable.getIdProprio());
            labelAdresse.setText(bienLouable.getComplementAdresse() + ", " + bienLouable.getAdresse() + ", " + bienLouable.getCodePostal() + ", " + bienLouable.getVille());
            labelNumeroFiscal.setText("N° fiscal " + bienLouable.getNumeroFiscal());
            labelType.setText("Type " + bienLouable.getTypeBien().name());
            labelSurface.setText("Surface " + bienLouable.getSurface());
            labelNbPieces.setText(bienLouable.getNbPieces() + " pièces");
            labelDateAjout.setText("Ajouté le " + bienLouable.getDateAjout().toString());
            idBien = bienLouable.getIdBien();
        });
    }

    public void valider(ActionEvent event) {
        Bail b = new Bail(Date.valueOf(dateDebut.getValue()),Float.parseFloat(fieldMontantLoyer.getText()), checkBoxRenouvelable.isSelected(), Float.parseFloat(fieldTotalCharges.getText()), Float.parseFloat(fieldDepotGarantie.getText()) ,Date.valueOf(dateFin.getValue()), Date.valueOf(dateSignature.getValue()),idBien);

        List<Locataire> locataires = choiceBoxesLocataires.stream().map(ChoiceBox::getValue).toList();
        Map<Locataire,Float> repartitionsElec = collectToMap(fieldsRepartitionsElec, "Invalid number format for electricity repartition: ");
        Map<Locataire,Float> repartitionsEau = collectToMap(fieldsRepartitionsEau, "Invalid number format for water repartition: ");
        Map<Locataire,Float> orduresMenageres = collectToMap(fieldsOrduresMenageres, "Invalid number format for garbage repartition: ");
        try {
            b.save();
            b.setLocataires(locataires, repartitionsElec, repartitionsEau, orduresMenageres);
        } catch (Bail.BailException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Map<Locataire, Float> collectToMap(List<TextField> fieldsRepartitionsElec, String string) {
        return IntStream.range(0, fieldsRepartitionsElec.size())
                .filter(x -> !fieldsRepartitionsElec.get(x).getText().isBlank()) // Skip empty TextFields
                .boxed()
                .collect(Collectors.toMap(
                        x -> choiceBoxesLocataires.get(x).getValue(), // Get the Locataire from the ChoiceBox
                        x -> {
                            try {
                                return Float.parseFloat(fieldsRepartitionsElec.get(x).getText()); // Parse the TextField value to Float
                            } catch (NumberFormatException e) {
                                throw new IllegalArgumentException(
                                        x + fieldsRepartitionsElec.get(x).getText());
                            }
                        }
                ));
    }

    public void annuler(ActionEvent event) {
        Stage stage = (Stage) labelAdresse.getScene().getWindow();
        stage.close();
    }

    public void addLocataireToBail(ActionEvent event) {
        ChoiceBox<Locataire> choiceBox = new ChoiceBox<>();
        try {
            Label locataire = new Label("Locataire");
            locataire.setFont(Font.font("Arial", FontWeight.BOLD,14));
            locataire.setStyle("-fx-text-fill: white;");

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

            TextField repartitionEau = new TextField();
            repartitionEau.setPromptText("Répartition eau");
            repartitionEau.setPrefSize(200, 30);

            TextField orduresMenageres = new TextField();
            orduresMenageres.setPromptText("Ordures ménagères");
            orduresMenageres.setPrefSize(200, 30);

            GridPane gridPaneLine = new GridPane();

            Button supprimerLigne = new Button("Supprimer");
            supprimerLigne.setOnAction(e -> {
                int index = choiceBoxesLocataires.indexOf(choiceBox);
                choiceBoxesLocataires.remove(index);
                fieldsRepartitionsElec.remove(index);
                fieldsRepartitionsEau.remove(index);
                fieldsOrduresMenageres.remove(index);
                gridPaneContent.getChildren().removeAll(gridPaneLine);
            });
            supprimerLigne.setPrefSize(100, 30);


            gridPaneLine.setHgap(10);
            gridPaneLine.setVgap(5);

            gridPaneLine.add(locataire, 0, 0);
            gridPaneLine.add(choiceBox, 1, 0);
            gridPaneLine.add(repartitionElec, 2, 0);
            gridPaneLine.add(repartitionEau, 3, 0);
            gridPaneLine.add(orduresMenageres, 4, 0);
            gridPaneLine.add(supprimerLigne, 5, 0);

            ColumnConstraints c = new ColumnConstraints();

            gridPaneLine.getColumnConstraints().add(c);
            gridPaneLine.getColumnConstraints().add(c);
            gridPaneLine.getColumnConstraints().add(c);
            gridPaneLine.getColumnConstraints().add(c);


            choiceBoxesLocataires.add(choiceBox);
            fieldsRepartitionsElec.add(repartitionElec);
            fieldsRepartitionsEau.add(repartitionEau);
            fieldsOrduresMenageres.add(orduresMenageres);


            int rows = gridPaneContent.getRowCount();

            gridPaneContent.add(gridPaneLine, 0, rows);
            GridPane.setColumnSpan(gridPaneLine, 3);
            GridPane.setHalignment(gridPaneLine, HPos.CENTER);


            //get stage and resize it
            Stage stage = (Stage) labelAdresse.getScene().getWindow();
            if(!stage.isMaximized())
                stage.sizeToScene();
        }
        catch (Locataire.LocataireException e) {
            JfxUtil.displayError("Impossible de récupérer les locataires", "Vérifier votre connexion");
        }
    }
}
