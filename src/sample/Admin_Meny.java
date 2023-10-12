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
import produkter.Produkter;

import java.io.IOException;



public class Admin_Meny {

    private Register brukere;
    private Produkter produkter;

    @FXML
    private Label lblError;

    @FXML
    void BtnVisBrukere_OnClick(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/Admin_VisBrukere.fxml"));
        Parent AdminMenySide;
        boolean value = true;
        try {
            AdminMenySide = loader.load();
        }
        catch (IOException e) {
            lblError.setText("#ERROR! Det oppstod en feil!");
            AdminMenySide = null;
            value = false;

        }
        if (value) {

            Admin_VisBrukere controller = loader.getController();
            controller.initBrukere(brukere, produkter);
            controller.start();

            Scene VisBruker_Super = new Scene(AdminMenySide);
            Stage Scene_12 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_12.setScene(VisBruker_Super);
            Scene_12.setHeight(593);
            Scene_12.setWidth(920);
            Scene_12.centerOnScreen();
            Scene_12.show();
        }
    }

    @FXML
    void BtnVisProdukter_OnClick(ActionEvent event) {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/Admin_VisProdukter.fxml"));
        Parent Mellom_side_Superbruker;
        boolean value2 = true;
        try {
            Mellom_side_Superbruker = loader.load();
        } catch (IOException e) {
            lblError.setText("#ERROR! Det oppstod en feil!");
            Mellom_side_Superbruker = null;
            value2 = false;

        }
        if (value2) {

            Admin_VisProdukter controller = loader.getController();
            controller.setBruker(brukere, produkter);
            controller.start();

            Scene VisProdukt_Admin = new Scene(Mellom_side_Superbruker);
            Stage Scene_13 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_13.setScene(VisProdukt_Admin
            );
            Scene_13.setHeight(629);
            Scene_13.setWidth(1300);
            Scene_13.centerOnScreen();
            Scene_13.show();
        }
    }


    @FXML
    void On_Click_BtnReturnerTilStart(ActionEvent event) {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/InnLogging.fxml"));
        Parent Superbruker;
        boolean value3 = true;
        try {
            Superbruker = loader.load();
        } catch (IOException e) {
            lblError.setText("#ERROR! Det oppstod en feil!");
            Superbruker = null;
            value3 = false;

        }
        if (value3) {

            InnLogging controller = loader.getController();
            controller.setRegister(brukere, produkter);

            Scene LoggInn = new Scene(Superbruker);
            Stage Scene_4 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_4.setScene(LoggInn);
            Scene_4.setHeight(750);
            Scene_4.setWidth(500);
            Scene_4.centerOnScreen();
            Scene_4.show();
        }
    }



    public void initBrukere(Register brukere, Produkter produkter) {
        this.brukere = brukere;
        this.produkter = produkter;
    }

}
