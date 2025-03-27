package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.vue.*;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.util.LinkedList;
import java.util.List;

import static net.mpvm.saeimmobilier.util.JfxUtil.*;

public class CtrlViewImmeubles {

    @FXML
    private VBox vBoxImmeubles;

    @FXML
    private TableView<Immeuble> tableImmeubles;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            creerTableView();
            afficheImmeubles();
        });
    }

    private void creerTableView() {
        TableColumn<Immeuble, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdProprio()));

        TableColumn<Immeuble, String> colAdresse = new TableColumn<>("Adresse");
        colAdresse.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAdresse()));


        TableColumn<Immeuble, String> colCodePostal = new TableColumn<>("Code Postal");
        colCodePostal.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCodePostal()));


        TableColumn<Immeuble, String> colVille = new TableColumn<>("Ville");
        colVille.getStyleClass().add(COL_VILLE);
        colVille.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVille()));

        TableColumn<Immeuble, String> colActions = new TableColumn<>("Actions");

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button voirBiensButton = new Button("Voir les biens");
            private final Button attribuerAssuranceButton = new Button("Attribuer Assurance");
            private final Button faireTravaux = new Button("Attribuer Travaux");
            private final Button supprimerButton = new Button("Supprimer");

            private final GridPane actionsPane = new GridPane();

            {
                // Ensures buttons are centered inside the GridPane
                actionsPane.setAlignment(Pos.CENTER);
                actionsPane.setHgap(10);
                actionsPane.setVgap(10);

                ColumnConstraints cc = new ColumnConstraints();
                cc.setHalignment(HPos.CENTER);
                cc.setHgrow(Priority.ALWAYS);

                RowConstraints rc = new RowConstraints();
                rc.setValignment(VPos.CENTER);
                rc.setVgrow(Priority.ALWAYS);

                actionsPane.getColumnConstraints().addAll(cc, cc); // Apply to both columns
                actionsPane.getRowConstraints().addAll(rc, rc); // Apply to both rows

                voirBiensButton.setPrefWidth(200);
                attribuerAssuranceButton.setPrefWidth(200);
                faireTravaux.setPrefWidth(200);
                supprimerButton.setPrefWidth(200);

                actionsPane.add(voirBiensButton, 0, 0);
                actionsPane.add(attribuerAssuranceButton, 1, 0);
                actionsPane.add(faireTravaux, 0, 1);
                actionsPane.add(supprimerButton, 1, 1);
            }


            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setStyle(STYLE_CELL);
                if (empty) {
                    setGraphic(null);
                } else {
                    Immeuble immeuble = getTableView().getItems().get(getIndex());
                    voirBiensButton.getStyleClass().add(BUTTON_VALIDER);
                    attribuerAssuranceButton.getStyleClass().add(BUTTON_VALIDER);
                    faireTravaux.getStyleClass().add(BUTTON_VALIDER);
                    supprimerButton.getStyleClass().add(BUTTON_SUPPRIMER);

                    voirBiensButton.setOnAction(event -> afficheBiensPourImmeuble(immeuble.getIdBien(), event));
                    attribuerAssuranceButton.setOnAction(event -> attribuerAssurance(immeuble.getIdBien(), event));
                    faireTravaux.setOnAction(event -> attribuerTravaux(immeuble.getIdBien(), event));
                    supprimerButton.setOnAction(event -> {
                        if (JfxUtil.askForDelete("Voulez vous supprimer l'immeuble") == 1)
                            supprimerImmeuble(immeuble);
                    });
                    setGraphic(actionsPane);
                }
            }
        });
        List<TableColumn<Immeuble,String>> columns = List.of(colNom,colAdresse,colCodePostal,colVille);
        columns.forEach(column -> {
            column.setCellFactory(c -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setStyle(STYLE_CELL);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                        setAlignment(Pos.CENTER); // Ensures the text is centered
                    }
                }
            });
            tableImmeubles.getColumns().add(column);
        });

        tableImmeubles.getColumns().add(colActions);
        tableImmeubles.setRowFactory(tv -> new TableRow<Immeuble>() {

            private static final List<TableRow<?>> register = new LinkedList<>();

            {
                register.add(this);
            }

            @Override
            protected void updateItem(Immeuble item, boolean empty) {
                super.updateItem(item, empty);
                JfxUtil.updateRow(item, empty,register,this);
            }
        });
        colNom.setPrefWidth(100);
        colAdresse.setPrefWidth(150);
        colCodePostal.setPrefWidth(100);
        colVille.setPrefWidth(100);
        colActions.setPrefWidth(300);
        colActions.setMaxWidth(500);
    }


    public void afficheImmeubles() {
        try {
            List<Immeuble> immeubles = Immeuble.findAll();

            if (immeubles.isEmpty()) {
                Label label = new Label("Aucun immeuble trouvé.");
                label.getStyleClass().add(ASSURANCE_LABEL);
                vBoxImmeubles.getChildren().add(label);
                return;
            }
            ObservableList<Immeuble> lesimmeubles = FXCollections.observableArrayList(immeubles);
            tableImmeubles.setItems(lesimmeubles);

        } catch (Immeuble.ImmeubleException e) {
            JfxUtil.displayError("Erreur lors du chargement des immeubles", e.getMessage());
            e.printStackTrace();
        }
    }


    private void supprimerImmeuble(Immeuble immeuble) {
        try {
            immeuble.delete();
            afficheImmeubles();
            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Suppression réussie", null, "L'immeuble a été supprimé avec succès.");
        } catch (Bien.BienException e) {
            JfxUtil.displayError("Erreur lors de la suppression de l'immeuble", e.getMessage());
        }
    }

    private void afficheBiensPourImmeuble(int idBien,ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s, VueBiensLouables.class);
    }


    private void attribuerAssurance(int idBien,ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s, VueAttribuerAssurance.class);
    }
    private void attribuerTravaux(int idBien, ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s, VueAttribuerTravaux.class);
    }
    @FXML
    private void retourAccueil(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void ajouterBien(ActionEvent event){
        Stage s = new Stage();
        s.getProperties().put("controleur",this);
        s.setOnHidden(e -> afficheImmeubles());
        JfxUtil.showWindow(s, VueNewBien.class);

    }
}
