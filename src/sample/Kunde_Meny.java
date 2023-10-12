package sample;


import Brukere.Register;
import Brukere.Kunde;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import produkter.Produkter;
import java.io.IOException;

public class Kunde_Meny {

    private Produkter produkter;

    private Register brukere;

    private Kunde bruker;

    @FXML
    private Label labelError;


    @FXML
    void Btn_Tilbake_OnClick(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/InnLogging.fxml"));
        Parent Innlogging;
        boolean value_1 = true;

        try {
            Innlogging = loader.load();
        }

        catch (IOException e) {
            labelError.setText("Klarte ikke å gå tilbake til LoggInn siden!");
            Innlogging = null;
            value_1 = false;
        }

        if (value_1) {
            Scene LoggInn = new Scene(Innlogging);
            Stage Scene_1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_1.setScene(LoggInn);
            Scene_1.setHeight(617);
            Scene_1.setWidth(418);
            Scene_1.centerOnScreen();
            Scene_1.show();
        }
    }

    @FXML
    void Btn_Produkter_OnClick(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/Kunde_Handleside.fxml"));
        Parent Kunde_meny;
        boolean value_3 = true;
        try {
            Kunde_meny = loader.load();
        } catch (IOException e) {

            labelError.setText("Handlesiden ble ikke lastet opp!");
            Kunde_meny = null;
            value_3 = false;
        }
        if (value_3) {
            Kunde_Handleside controller = loader.getController();
            controller.start(bruker, brukere, produkter);
            Scene handleside = new Scene(Kunde_meny);
            Stage Scene_1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_1.setScene(handleside);
            Scene_1.setHeight(720);
            Scene_1.setWidth(877);
            Scene_1.centerOnScreen();
            Scene_1.show();
        }
    }

    public void setInfo(Register r, Produkter k, Kunde b) {
        brukere = r;
        produkter = k;
        bruker = b;
    }
}
