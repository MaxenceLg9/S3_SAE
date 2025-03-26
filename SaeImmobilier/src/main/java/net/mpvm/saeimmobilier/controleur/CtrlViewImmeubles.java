package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.vue.*;
import net.mpvm.saeimmobilier.util.JfxUtil;

import java.util.LinkedList;
import java.util.List;

public class CtrlViewImmeubles {

    public static final String STYLE_CELL = "-fx-text-fill: white; -fx-font-size: 14px; -fx-background-color: #1e2d3e;";
    public static final String STYLE_CELL_HOVER = "-fx-text-fill: black; -fx-font-size: 14px; -fx-background-color: white;";
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
        colVille.getStyleClass().add("col-ville");
        colVille.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVille()));

        TableColumn<Immeuble, String> colActions = new TableColumn<>("Actions");

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button voirBiensButton = new Button("Voir les biens");
            private final Button attribuerAssuranceButton = new Button("Attribuer Assurance");
            private final Button faireTravaux = new Button("Attribuer Travaux");
            private final Button supprimerButton = new Button("Supprimer");

            private final GridPane actionsPane = new GridPane();

            {
                actionsPane.setHgap(5);
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
                    voirBiensButton.getStyleClass().add("button-valider");
                    attribuerAssuranceButton.getStyleClass().add("button-valider");
                    faireTravaux.getStyleClass().add("button-valider");
                    supprimerButton.getStyleClass().add("button-supprimer");

                    voirBiensButton.setOnAction(event -> afficheBiensPourImmeuble(immeuble.getIdBien(), event));
                    attribuerAssuranceButton.setOnAction(event -> attribuerAssurance(immeuble.getIdBien(), event));
                    faireTravaux.setOnAction(event -> attribuerTravaux(immeuble.getIdBien(), event));
                    supprimerButton.setOnAction(event -> {
                        if (JfxUtil.askForDelete("Voulez vous supprimer l'immeuble") == 1)
                            supprimerImmeuble(immeuble);
                    });

                    setGraphic(actionsPane);
                    setAlignment(Pos.CENTER);
                }
            }
        });
        tableImmeubles.getColumns().addAll(List.of(colNom,colAdresse,colCodePostal,colVille));
        tableImmeubles.getColumns().forEach(column -> {
            TableColumn<Immeuble, String> col = (TableColumn<Immeuble, String>) column;
            col.setCellFactory(c -> new TableCell<Immeuble, String>() {
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
        });

        tableImmeubles.getColumns().add(colActions);
        tableImmeubles.setRowFactory(tv -> new TableRow<Immeuble>() {

            private static final List<TableRow<Immeuble>> register = new LinkedList<>();

            {
                register.add(this);
            }

            @Override
            protected void updateItem(Immeuble item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle(""); // Reset style for empty rows
                } else {
                    if (isSelected()) {
                        setStyle("-fx-background-color: #336699; -fx-text-fill: white;"); // Apply hover style
                    } else {
                        setStyle(""); // Reset style for unselected rows
                    }

                    setOnMouseEntered(event -> {
                        if (!isSelected()) {
                            for (int i = 0; i < getChildrenUnmodifiable().size(); i++) {
                                if (getChildrenUnmodifiable().get(i) instanceof TableCell<?, ?> cell) {
                                    cell.setStyle(STYLE_CELL_HOVER);
                                }
                            }
                        }
                    });
                    setOnMouseExited(event -> {
                        if (!isSelected()) {
                            for (int i = 0; i < getChildrenUnmodifiable().size(); i++) {
                                if (getChildrenUnmodifiable().get(i) instanceof TableCell<?, ?> cell) {
                                    cell.setStyle(STYLE_CELL);
                                }
                            }
                        }
                    });
                }
            }
        });
        colNom.setPrefWidth(100);
        colAdresse.setPrefWidth(150);
        colCodePostal.setPrefWidth(100);
        colVille.setPrefWidth(100);
        colActions.setPrefWidth(200);
    }


    public void afficheImmeubles() {
        try {
            List<Immeuble> immeubles = Immeuble.findAll();

            if (immeubles.isEmpty()) {
                Label label = new Label("Aucun immeuble trouvé.");
                label.getStyleClass().add("assurance-label");
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
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
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
