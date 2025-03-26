package net.mpvm.saeimmobilier.controleur;

import java.util.List;

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
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAttribuerAssurance;
import net.mpvm.saeimmobilier.vue.VueBails;
import net.mpvm.saeimmobilier.vue.VueModifierBien;
import net.mpvm.saeimmobilier.vue.VueNewBien;


public class CtrlViewBiensLouables {

    public static final String STYLE_CELL = "-fx-text-fill: white; -fx-font-size: 14px; -fx-background-color: #1e2d3e;";
    public static final String STYLE_CELL_HOVER = "-fx-text-fill: black; -fx-font-size: 14px; -fx-background-color: white;";
    @FXML
    private TableView<BienLouable> tableBiensLouables;
    @FXML
    private VBox vBoxBiensLouables;
    @FXML
    private Button retourAccueil;

    private int idImmeuble;

    @FXML
    public void initialize() {
        vBoxBiensLouables.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    setIdImmeuble(stage);
                    afficheBiens();
                } else {
                    System.out.println("Pas de stage");
                }
            } else {
                System.out.println("Pas de scène");
            }
        });
    }

    public void setIdImmeuble(Stage stage) {
        Object id = stage.getProperties().get("bien");
        if (id instanceof Integer) {
            this.idImmeuble = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bien' manquante ou incorrecte.");
        }
    }



    public void afficheBiens() {
        try {

            List<BienLouable> biens = BienLouable.findByImmeuble(idImmeuble);
            if (biens.isEmpty()) {
                Label label = new Label("Aucun bien trouvé.");
                label.getStyleClass().add("assurance-title");
                vBoxBiensLouables.getChildren().add(label);
                return;
            }
            else{
                creerTableView();
                ObservableList<BienLouable> lesBiens = FXCollections.observableArrayList(biens);
                tableBiensLouables.setItems(lesBiens);
            }
        } catch (Bien.BienException e) {
            JfxUtil.displayError("Erreur lors du chargement des biens", e.getMessage());
            e.printStackTrace();
        }
    }

    private void creerTableView() {

        TableColumn<BienLouable, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdProprio()));


        TableColumn<BienLouable, String> colAdresse = new TableColumn<>("Adresse");
        colAdresse.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAdresse() + " " + cell.getValue().getComplementAdresse()));


        TableColumn<BienLouable, String> colCodePostal = new TableColumn<>("Code Postal");
        colCodePostal.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCodePostal()));


        TableColumn<BienLouable, String> colVille = new TableColumn<>("Ville");
        colVille.getStyleClass().add("col-ville");
        colVille.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVille()));

        TableColumn<BienLouable, String> colActions = new TableColumn<>("Actions");

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button voirBiensButton = new Button("Voir les baux");
            private final Button attribuerAssuranceButton = new Button("Attribuer Assurance");
            private final Button modifier = new Button("Modifier");
            private final Button supprimerButton = new Button("Supprimer");

            private final GridPane actionsPane = new GridPane();

            {
                actionsPane.setHgap(5);
                actionsPane.add(voirBiensButton, 0, 0);
                actionsPane.add(attribuerAssuranceButton, 1, 0);
                actionsPane.add(modifier, 0, 1);
                actionsPane.add(supprimerButton, 1, 1);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setStyle(STYLE_CELL);
                if (empty) {
                    setGraphic(null);
                } else {
                    BienLouable bienLouable = getTableView().getItems().get(getIndex());
                    voirBiensButton.getStyleClass().add("button-valider");
                    attribuerAssuranceButton.getStyleClass().add("button-valider");
                    modifier.getStyleClass().add("button-valider");
                    supprimerButton.getStyleClass().add("button-supprimer");

                    voirBiensButton.setOnAction(event -> gererBails(bienLouable.getIdBien(), event));
                    attribuerAssuranceButton.setOnAction(event -> attribuerAssurance(bienLouable.getIdBien(), event));
                    modifier.setOnAction(event -> modifierBien(bienLouable, event));
                    supprimerButton.setOnAction(event -> {
                        if (JfxUtil.askForDelete("Voulez vous supprimer l'immeuble") == 1)
                            supprimerBien(bienLouable);
                    });

                    setGraphic(actionsPane);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        tableBiensLouables.getColumns().clear();
        tableBiensLouables.getColumns().addAll(List.of(colNom,colAdresse,colCodePostal,colVille));
        tableBiensLouables.getColumns().forEach(column -> {
            TableColumn<BienLouable, String> col = (TableColumn<BienLouable, String>) column;
            col.setCellFactory(c -> new TableCell<>() {
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

        tableBiensLouables.getColumns().add(colActions);
        tableBiensLouables.setRowFactory(tv -> new TableRow<BienLouable>() {
            @Override
            protected void updateItem(BienLouable item, boolean empty) {
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

    private void supprimerBien(BienLouable bien) {
        try {
            bien.delete(); // Suppression de l'objet Bien
            afficheBiens(); // Mise à jour de l'affichage des biens
            JfxUtil.setAlert(Alert.AlertType.INFORMATION,
                    "Suppression réussie",
                    null,
                    "Le bien a été supprimé avec succès."); // Affichage d'une alerte d'information
        } catch (Bien.BienException e) {
            JfxUtil.displayError("Erreur lors de la suppression", e.getMessage()); // Gestion des erreurs avec l'alerte
        }
    }

    private void gererBails(int idBien,ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("bien", idBien);
        JfxUtil.showWindow(s, VueBails.class);
    }

    private void attribuerAssurance(int idBien,ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("bien",idBien);
        JfxUtil.showWindow(s,VueAttribuerAssurance.class);
    }
    public void ajouterBien(ActionEvent event) throws Exception {
        Stage s = new Stage();
        s.setOnHidden(e -> afficheBiens());
        JfxUtil.showWindow(s, VueNewBien.class);
    }
    @FXML
    private void retourImmeubles(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void modifierBien(BienLouable bien,ActionEvent event) {
        Stage s = new Stage();
        s.getProperties().put("bien",bien);
        JfxUtil.showWindow(s, VueModifierBien.class);
        s.setOnHidden(e -> afficheBiens());

    }
}
