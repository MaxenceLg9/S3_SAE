package net.mpvm.saeimmobilier.controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.util.JfxUtil;
import net.mpvm.saeimmobilier.vue.VueNewBien;
import net.mpvm.saeimmobilier.vue.VueNewLocataire;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CtrlViewLocataires {

    @FXML
    private VBox vBoxContent;
    @FXML
    private Button retourAccueil;

    private Map<Integer, Locataire> locataires;
    private int idBail;
    @FXML
    public void initialize(){
        vBoxContent.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    setIdBail(stage);
                    afficheLocataires();
                } else {
                    System.out.println("pas de stage");
                }
            } else {
                System.out.println("pas de scène");
            }
        });
    }

    public void setIdBail(Stage stage) {
        Object id = stage.getProperties().get("bail");
        if (id instanceof Integer) {
            this.idBail = (int) id;
        } else {
            throw new IllegalStateException("Propriété 'bail' manquante ou incorrecte.");
        }
    }
    public void afficheLocataires() {

        try {
            locataires = Locataire.findAll().stream().collect(Collectors.toMap(Locataire::getIdLocataire, Function.identity()));
        } catch (Locataire.LocataireException locataireException) {
            JfxUtil.displayError("Erreur lors de la récupération des locataires", "Verifiez votre connexion");
            return;
        }
        vBoxContent.getChildren().clear();

        Label titre = new Label("Liste des Locataires");
        titre.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
        titre.setAlignment(Pos.CENTER);
        vBoxContent.getChildren().add(titre);

        retourAccueil = new Button("Retour à l'accueil");
        retourAccueil.setOnAction(event -> retourAccueil(event));
        retourAccueil.getStyleClass().add("button-supprimer");
        vBoxContent.getChildren().add(retourAccueil);
        Button ajouterLocataire = new Button("Ajouter Locataire");
        ajouterLocataire.setOnAction(event -> ajouterLocataire(event,idBail));
        ajouterLocataire.getStyleClass().add("button-valider");
        vBoxContent.getChildren().add(ajouterLocataire);
        for(Locataire l : locataires.values()) {
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(100);
            col1.setMinWidth(20);
            col1.setHgrow(Priority.SOMETIMES);

            gp.getColumnConstraints().addAll(col1, col1);

            Label nom = new Label("Nom " + l.getNom());
            Label prenom = new Label("Prenom " + l.getPrenom());
            Label email = new Label("Email " + l.getEmail());
            Label telephone = new Label("N° tel. " + l.getTelephone());
            Label sexe = new Label("Sexe " + l.getSexe());
            Button button = new Button("Supprimer le locataire");
            button.setOnAction(event -> askForDelete(l.getIdLocataire()));

            nom.getStyleClass().add("assurance-label");
            nom.getStyleClass().add("assurance-title");
            prenom.getStyleClass().add("assurance-label");
            email.getStyleClass().add("assurance-label");
            telephone.getStyleClass().add("assurance-label");
            sexe.getStyleClass().add("assurance-label");
            button.getStyleClass().add("button-supprimer");

            gp.add(nom, 0, 0);
            gp.add(prenom, 0, 1);
            gp.add(email, 1, 1);
            gp.add(telephone, 1, 0);
            gp.add(sexe, 2, 2);

            button.setId(String.valueOf(l.getIdLocataire()));
            gp.add(button, 2, 1);

            gp.setAlignment(Pos.TOP_CENTER);

            GridPane.setHalignment(nom, HPos.LEFT);
            GridPane.setHalignment(prenom, HPos.LEFT);
            GridPane.setHalignment(email, HPos.LEFT);
            GridPane.setHalignment(telephone, HPos.LEFT);
            GridPane.setHalignment(sexe, HPos.LEFT);
            GridPane.setValignment(nom, VPos.CENTER);
            GridPane.setValignment(prenom, VPos.CENTER);
            GridPane.setValignment(email, VPos.CENTER);
            GridPane.setValignment(telephone, VPos.CENTER);
            GridPane.setValignment(sexe, VPos.CENTER);

            gp.getStyleClass().add("assurance-gridpane");
            gp.setHgap(10);
            gp.setVgap(5);

            gp.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gp.setMaxWidth(Region.USE_COMPUTED_SIZE);

            vBoxContent.getChildren().add(gp);
        }
    }



    @FXML
    public void askForDelete(int id){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de la suppression");
        alert.setHeaderText("Souhaitez-vous réellement supprimer ce locataire? ");
        alert.setContentText("Cette action est irréversible");
        alert.showAndWait()
                .filter(r -> r.equals(ButtonType.OK))
                .ifPresent(r -> deleteLocataire(id));
    }
    public void ajouterLocataire(ActionEvent event, int idBail) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

        Stage s = new Stage();
        s.getProperties().put("bail",idBail);
        JfxUtil.showWindow(s, VueNewLocataire.class);

    }

    private void deleteLocataire(int id){
        try {
            locataires.get(id).delete();
            afficheLocataires();
        } catch (Locataire.LocataireException e) {
            // TODO: handle exception with visual
        }

    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
