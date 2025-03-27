package net.mpvm.saeimmobilier.controleur;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Assurance;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueNewAssurance;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.mpvm.saeimmobilier.util.JfxUtil.STYLE_CELL;
import static net.mpvm.saeimmobilier.util.JfxUtil.*;

public class CtrlAttribuerAssurance {
    @FXML
    public VBox vBoxContent;

    @FXML
    private TableView<Assurance> tableAssurances;

    private List<Assurance> assurances;
    private Bien bien;


    public void initialize() {
        Platform.runLater(() -> {
            setIdBien((Stage) vBoxContent.getScene().getWindow());
            creerTableView();
            try {
                afficheAssurances();
            } catch (Assurance.AssuranceException e) {
                JfxUtil.displayError("Erreur lors de la récupération des assurances","Nous n'avons pas pu récupérer les assurances");
            }
        });
    }
    public void setIdBien(Stage stage) {
        Object id = stage.getProperties().get("bien");
        if (id instanceof Integer idBien) {
            this.bien = Bien.BBuilder.get(idBien);
        } else {
            throw new IllegalStateException("Propriété 'bien' manquante ou incorrecte.");
        }
    }
    private void afficheAssurances() throws Assurance.AssuranceException {
        try {
            assurances = Assurance.findAll().stream().sorted((a1,a2) -> a1.getIdAssurance()-a2.getIdAssurance()).toList();
        } catch (Assurance.AssuranceException assuranceException) {
            JfxUtil.displayError("Erreur lors de la récupération des assurances", "Vérifiez votre connexion Internet");
            return;
        }
        tableAssurances.setItems(FXCollections.observableArrayList(assurances));
    }

    private void creerTableView() {
        TableColumn<Assurance, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNomAssurance()));


        TableColumn<Assurance, String> colAdresse = new TableColumn<>("Prime");
        colAdresse.setCellValueFactory(cell -> new SimpleStringProperty(Float.toString(cell.getValue().getPrime())));


        TableColumn<Assurance, String> colCodePostal = new TableColumn<>("Annee");
        colCodePostal.setCellValueFactory(cell -> new SimpleStringProperty(Integer.toString(cell.getValue().getAnnee())));


        TableColumn<Assurance, String> colVille = new TableColumn<>("Type");
        colVille.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTypeContrat().toString()));

        TableColumn<Assurance, String> colProtectionJuridique = new TableColumn<>("Protection Juridique");
        colProtectionJuridique.setCellValueFactory(cell -> new SimpleStringProperty(Float.toString(cell.getValue().getProtectionJuridique())));

        TableColumn<Assurance, String> colTotalPrime = new TableColumn<>("Prime Totale");
        colTotalPrime.setCellValueFactory(cell -> new SimpleStringProperty(Float.toString(cell.getValue().getTotalPrime())));

        TableColumn<Assurance, String> colActions = new TableColumn<>("Actions");

        colActions.setCellFactory(param -> new TableCell<>() {


            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                setStyle(STYLE_CELL);
                if (empty) {
                    setGraphic(null);
                } else {
                    Assurance assurance = getTableView().getItems().get(getIndex());
                    final Button deleteButton = new Button("Supprimer l'assurance");
                    final GridPane actionsPane = new GridPane();


                    try {
                        if (assurance.getBienAssure() == null && bien.getAssurance().isEmpty()) {
                            final Button chooseButton = new Button("Choisir");
                            chooseButton.setPrefWidth(200);
                            actionsPane.add(chooseButton, 0, 0);
                            ColumnConstraints c1 = new ColumnConstraints();
                            c1.setMinWidth(220);
                            c1.setHalignment(HPos.CENTER);
                            c1.setHgrow(Priority.ALWAYS);
                            actionsPane.getColumnConstraints().add(c1);
                            chooseButton.setOnAction(event -> attribuerAssurance(bien, assurance));
                            chooseButton.getStyleClass().add(BUTTON_VALIDER);
                        }
                    }catch(Assurance.AssuranceException e){
                        e.getSqlException().printStackTrace();
                        JfxUtil.displayError("Erreur lors de la récupération des biens assurés", "AAAAAA");
                    }
                    deleteButton.setPrefWidth(300);
                    actionsPane.setHgap(5);
                    actionsPane.setVgap(5);
                    actionsPane.add(deleteButton, actionsPane.getColumnCount(), 0);
                    actionsPane.setAlignment(Pos.CENTER);
                    ColumnConstraints c2 = new ColumnConstraints();
                    c2.setMinWidth(320);
                    c2.setHalignment(HPos.CENTER);
                    c2.setHgrow(Priority.ALWAYS);
                    actionsPane.getColumnConstraints().add(c2);
                    RowConstraints rc = new RowConstraints();
                    rc.setValignment(VPos.CENTER);
                    actionsPane.getRowConstraints().addAll(rc);
                    deleteButton.setOnAction(event -> {
                        if(JfxUtil.askForDelete("Voulez-vous vraiment supprimer cette assurance ?") == 1) {
                            try {
                                supprimerAssurance(assurance);
                            } catch (Assurance.AssuranceException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    deleteButton.getStyleClass().add(BUTTON_SUPPRIMER);
                    setGraphic(actionsPane);
                    setAlignment(Pos.CENTER);
                }
            }
        });
        List<TableColumn<Assurance,String>> columns = List.of(colNom,colAdresse,colCodePostal,colVille);
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
            tableAssurances.getColumns().add(column);
        });
        tableAssurances.getColumns().add(colActions);
        tableAssurances.setRowFactory(tv -> new TableRow<>() {

            private static final List<TableRow<?>> register = new LinkedList<>();

            {
                register.add(this);
            }

            @Override
            protected void updateItem(Assurance item, boolean empty) {
                super.updateItem(item, empty);
                JfxUtil.updateRow(item, empty, register, this);
            }
        });
        colNom.setPrefWidth(100);
        colAdresse.setPrefWidth(150);
        colCodePostal.setPrefWidth(100);
        colVille.setPrefWidth(100);
        colActions.setPrefWidth(500);
        colActions.setMaxWidth(550);
    }


    @FXML
    private void ajouterAssurance(ActionEvent event) {
        Stage s = new Stage();
        JfxUtil.showWindow(s, VueNewAssurance.class);
    }

    @FXML
    private void retourImmeubles(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    void attribuerAssurance(Bien bien, Assurance assurance) {
        if (assurance == null) {
            JfxUtil.displayError("Erreur", "L'assurance sélectionnée est invalide.");
            return;
        }

        try {
            assurance.attribuerUneAssurance(bien);
            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Succès", "Attribution réussie", "L'assurance a été attribuée avec succès au bien !");
            // Actualisation de la liste des assurances
            afficheAssurances();

        } catch (Assurance.AssuranceException assuranceException) {
            // Affichage de l'erreur avec des détails pertinents
            JfxUtil.displayError(
                    "Erreur lors de l'attribution de l'assurance",
                    "ID Bien : " + bien.getIdBien() + "\nID Assurance : " + assurance.getIdAssurance() +
                            "\nOn ne peut pas associer 2 assurances différentes \nsur un même bien la même année. "
            );

        }
    }

    private void supprimerAssurance(Assurance assurance) throws Assurance.AssuranceException {
        try {
            assurance.delete();
            afficheAssurances();
            JfxUtil.setAlert(Alert.AlertType.INFORMATION, "Suppression réussie", null, "L'assurance a été supprimée avec succès.");
        } catch (Assurance.AssuranceException e) {
            JfxUtil.displayError("Erreur lors de la suppression de l'assurance", e.getMessage());
        }
        afficheAssurances();
    }

}