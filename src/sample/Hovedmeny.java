package sample;


import Brukere.Register;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class Hovedmeny {


    @FXML
    private Label lblError;

    private Register brukere;

    public void initRegister(Register brukere) {
        this.brukere = brukere;
    }

    @FXML
    void btnLogginnClk(ActionEvent event) {
        if (brukere != null) {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/InnLogging.fxml"));
            Parent Hovedmeny;
            boolean value_8 = true;

            try {
                Hovedmeny = loader.load();
            }

            catch (IOException e) {
                lblError.setText("Klarte ikke å bytte side!");
                Hovedmeny = null;
                value_8 = false;
            }

            if (value_8) {
                InnLogging controller = loader.getController();
                controller.setRegister(brukere);

                Scene LoggInn = new Scene(Hovedmeny);
                Stage Scene_8 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene_8.setScene(LoggInn);
                Scene_8.setHeight(617);
                Scene_8.setWidth(380);
                Scene_8.centerOnScreen();
                Scene_8.show();
            }

        }

        else {
            lblError.setText("Mangel på data");
        }
    }

    @FXML
    void btnNyBrukerClk(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/RegistrerNyBruker.fxml"));
        Parent Hovedmeny;
        boolean value_9 = true;
        try {
            Hovedmeny = loader.load();
        } catch (IOException e) {
            lblError.setText("Klarte ikke å bytte side!");
            Hovedmeny = null;
            value_9 = false;
        }
        if (value_9) {
            RegistrerNyBruker controller = loader.getController();
            controller.initRegister(brukere);
            Scene Register_ny_bruker = new Scene(Hovedmeny);
            Stage Scene_9 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_9.setScene(Register_ny_bruker);
            Scene_9.setHeight(700);
            Scene_9.setWidth(700);
            Scene_9.centerOnScreen();
            Scene_9.show();
        }
    }
}
