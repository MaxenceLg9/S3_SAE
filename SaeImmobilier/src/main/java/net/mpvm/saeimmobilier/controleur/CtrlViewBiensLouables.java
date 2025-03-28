package net.mpvm.saeimmobilier.controleur;

import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueAttribuerAssurance;
import net.mpvm.saeimmobilier.vue.VueBails;
import net.mpvm.saeimmobilier.vue.VueModifierBien;
import net.mpvm.saeimmobilier.vue.VueNewBien;

import static net.mpvm.saeimmobilier.util.JfxUtil.*;
import static net.mpvm.saeimmobilier.util.JfxUtil.STYLE_CELL;


public class CtrlViewBiensLouables {

    public static final String STYLE_CELL = "-fx-text-fill: white; -fx-font-size: 14px; -fx-background-color: #1e2d3e;";
    public static final String STYLE_CELL_HOVER = "-fx-text-fill: black; -fx-font-size: 14px; -fx-background-color: white;";
    @FXML
    private TableView<BienLouable> tableBiensLouables;
    @FXML
    private VBox vBoxBiensLouables;

    private int idImmeuble;

    // Initialise le contrôleur et configure les listeners pour la scène
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            creerTableView();
            setIdImmeuble((Stage) vBoxBiensLouables.getScene().getWindow());
            afficheBiens();
        });
    }

    // Récupère l'identifiant de l'immeuble à partir des propriétés de la fenêtre
    public void setIdImmeuble(Stage stage) {
        Object id = stage.getProperties().get("bien");
        if (id instanceof Integer) {
            this.idImmeuble = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bien' manquante ou incorrecte.");
        }
    }


    // Met à jour les biens affichés
    public void afficheBiens() {
        try {

            List<BienLouable> biens = BienLouable.findByImmeuble(idImmeuble);
            if (biens.isEmpty()) {
                Label label = new Label("Aucun bien trouvé.");
                label.getStyleClass().add(ASSURANCE_TITLE);
                vBoxBiensLouables.getChildren().add(label);
            }
            else {
                ObservableList<BienLouable> lesBiens = FXCollections.observableArrayList(biens);
                tableBiensLouables.setItems(lesBiens);
            }
        } catch (Bien.BienException e) {
            JfxUtil.displayError("Erreur lors du chargement des biens", e.getMessage());
            e.printStackTrace();
        }
    }

    // Crée et configure le tableau d'affichage des biens louables
    private void creerTableView() {

        TableColumn<BienLouable, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdProprio()));


        TableColumn<BienLouable, String> colAdresse = new TableColumn<>("Adresse");
        colAdresse.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAdresse() + " " + cell.getValue().getComplementAdresse()));


        TableColumn<BienLouable, String> colCodePostal = new TableColumn<>("Code Postal");
        colCodePostal.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCodePostal()));


        TableColumn<BienLouable, String> colVille = new TableColumn<>("Ville");
        colVille.getStyleClass().add(COL_VILLE);
        colVille.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVille()));

        TableColumn<BienLouable, String> colActions = new TableColumn<>("Actions");

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button voirBiensButton = new Button("Voir les baux");
            private final Button attribuerAssuranceButton = new Button("Attribuer Assurance");
            private final Button modifier = new Button("Modifier");
            private final Button supprimerButton = new Button("Supprimer");

            private final GridPane actionsPane = new GridPane();

            {
                voirBiensButton.setPrefWidth(200);
                attribuerAssuranceButton.setPrefWidth(200);
                modifier.setPrefWidth(200);
                supprimerButton.setPrefWidth(200);
                actionsPane.setHgap(5);
                actionsPane.setVgap(5);
                actionsPane.add(voirBiensButton, 0, 0);
                actionsPane.add(attribuerAssuranceButton, 1, 0);
                actionsPane.add(modifier, 0, 1);
                actionsPane.add(supprimerButton, 1, 1);
                actionsPane.setAlignment(Pos.CENTER);
                ColumnConstraints cc = new ColumnConstraints();
                cc.setMinWidth(200);
                cc.setHalignment(HPos.CENTER);
                actionsPane.getColumnConstraints().addAll(cc,cc);
                RowConstraints rc = new RowConstraints();
                rc.setValignment(VPos.CENTER);
                actionsPane.getRowConstraints().addAll(rc,rc);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setStyle(STYLE_CELL);
                if (empty) {
                    setGraphic(null);
                } else {
                    BienLouable bienLouable = getTableView().getItems().get(getIndex());
                    voirBiensButton.getStyleClass().add(BUTTON_VALIDER);
                    attribuerAssuranceButton.getStyleClass().add(BUTTON_VALIDER);
                    modifier.getStyleClass().add(BUTTON_VALIDER);
                    supprimerButton.getStyleClass().add(BUTTON_SUPPRIMER);

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
        List<TableColumn<BienLouable,String>> columns = List.of(colNom,colAdresse,colCodePostal,colVille);
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
            tableBiensLouables.getColumns().add(column);
        });

        tableBiensLouables.getColumns().add(colActions);
        tableBiensLouables.setRowFactory(tv -> new TableRow<>() {

            private static final List<TableRow<?>> register = new LinkedList<>();

            @Override
            protected void updateItem(BienLouable item, boolean empty) {
                super.updateItem(item, empty);
                JfxUtil.updateRow(item, empty, register, this);
            }
        });
        colNom.setPrefWidth(100);
        colAdresse.setPrefWidth(150);
        colCodePostal.setPrefWidth(100);
        colVille.setPrefWidth(100);
        colActions.setPrefWidth(300);
        colActions.setPrefWidth(500);
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
    public void ajouterBien(ActionEvent event) {
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
        s.setOnHidden(e -> afficheBiens());
        JfxUtil.showWindow(s, VueModifierBien.class);


    }
}
