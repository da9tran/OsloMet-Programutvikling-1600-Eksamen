package sample;

import Brukere.*;
import filbehandling.FiledataJOBJ;
import filbehandling.FiledataTxt;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import produkter.Produkter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class InnLogging implements Initializable {

    @FXML
    private TextField txtBrukernavn;

    @FXML
    private PasswordField txtPassord;

    @FXML
    private Button btnLogginn, btnRegistrer, btnAvslutt;

    @FXML
    private Label lblError;

    private Register brukere;
    private Produkter produkter;

    private void vellykedeProdukter(WorkerStateEvent event) {
        txtBrukernavn.setDisable(false);
        txtPassord.setDisable(false);
        btnLogginn.setDisable(false);
        btnRegistrer.setDisable(false);
        btnAvslutt.setDisable(false);
    }

    private void mislykkedeProdukter(WorkerStateEvent event) {
        txtBrukernavn.setDisable(false);
        txtPassord.setDisable(false);
        btnLogginn.setDisable(false);
        btnRegistrer.setDisable(false);
        btnAvslutt.setDisable(false);

        lblError.setText("Lasting av varer mislyktes");
    }

    public void lastOppProdukter() {

        if (produkter == null) {
            produkter = new Produkter();
            FiledataJOBJ data = new FiledataJOBJ();
            Path path = Paths.get("src/filbehandling/LagreProdukter.JOBJ");

            data.setProdukt(produkter);
            data.setPath(path);

            data.setOnSucceeded(this::vellykedeProdukter);
            data.setOnFailed(this::mislykkedeProdukter);

            txtBrukernavn.setDisable(true);
            txtPassord.setDisable(true);
            btnLogginn.setDisable(true);
            btnRegistrer.setDisable(true);

            Thread tr = new Thread(data);
            tr.start();

            try {
                Thread.sleep(3000);
            }

            catch (InterruptedException e) {
                lblError.setText("Stopping av tråden mislyktes");
            }
        }
    }

    public void setRegister(Register reg, Produkter produkter) {
        this.brukere = reg;
        this.produkter = produkter;
    }

    public void setRegister(Register reg) {
        this.brukere = reg;
    }

    private void vellykkedeBrukere(WorkerStateEvent event) {
        txtBrukernavn.setDisable(false);
        txtPassord.setDisable(false);
        btnLogginn.setDisable(false);
        btnRegistrer.setDisable(false);
        btnAvslutt.setDisable(false);
    }

    private void mislykkedeBrukere(WorkerStateEvent event) {
        txtBrukernavn.setDisable(false);
        txtPassord.setDisable(false);
        btnLogginn.setDisable(false);
        btnRegistrer.setDisable(false);
        btnAvslutt.setDisable(false);

        lblError.setText("Lagrede brukere ble ikke funnet");
    }

    private void lastOppBrukere() {
        if (brukere == null) {
            brukere = new Register();
            FiledataTxt lese = new FiledataTxt();
            Path path = Paths.get("src/filbehandling/Brukerinfo.csv");

            lese.setPathTxt(path);
            lese.setRegister(brukere);

            txtBrukernavn.setDisable(true);
            txtPassord.setDisable(true);
            btnLogginn.setDisable(true);
            btnRegistrer.setDisable(true);

            lese.setOnSucceeded(this::vellykkedeBrukere);
            lese.setOnFailed(this::mislykkedeBrukere);

            Thread tr = new Thread(lese);
            tr.start();

            try {
                Thread.sleep(5000);
            }

            catch (InterruptedException e) {
                lblError.setText("Stopping av tråden mislyktes");
            }
        }
    }

    @FXML
    void BtnRegNyBruker(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/RegistrerNyBruker.fxml"));
        Parent Logg_inn;
        boolean value_7 = true;
        try {
            Logg_inn = loader.load();
        }

        catch (IOException e) {
            lblError.setText("Klarte ikke å bytte side!");
            Logg_inn = null;
            value_7 = false;

        }

        if (value_7) {
            RegistrerNyBruker controller = loader.getController();
            controller.initRegister(brukere);
            Scene Register_ny_bruker = new Scene(Logg_inn);
            Stage Scene_7 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_7.setScene(Register_ny_bruker);
            Scene_7.setHeight(700);
            Scene_7.setWidth(700);
            Scene_7.centerOnScreen();
            Scene_7.show();
        }

    }

    @FXML
    void BtnLoggInnClk(ActionEvent event) {
        boolean vellykketInnlogging = false;

        for (int i = 0; i < brukere.getArray().size(); i++) {
            boolean verdi = true;

            if (brukere.getArray().get(i).getBrukernavn().equals(txtBrukernavn.getText().trim())
                    && brukere.getArray().get(i).getPassord().equals(txtPassord.getText().trim())) {

                vellykketInnlogging = true;

                if (brukere.getArray().get(i).isAdmin()) {

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/FXML/Admin_Meny.fxml"));
                    Parent Logg_inn;
                    try {
                        Logg_inn = loader.load();
                    }

                    catch (IOException e) {
                        lblError.setText("Klarer ikke å bytte side");
                        Logg_inn = null;
                        verdi = false;
                    }

                    if (verdi) {

                        Admin_Meny controller = loader.getController();
                        controller.initBrukere(brukere, produkter);

                        Scene Kundebruker = new Scene(Logg_inn);
                        Stage Scene_5 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene_5.setScene(Kundebruker);
                        Scene_5.setHeight(619);
                        Scene_5.setWidth(420);
                        Scene_5.centerOnScreen();
                        Scene_5.show();
                    }
                }

                else {

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/FXML/Kunde_Meny.fxml"));
                    Parent Logg_inn;

                    try {
                        Logg_inn = loader.load();
                    }

                    catch (IOException e) {
                        lblError.setText("Lasting av data mislyktes ");
                        Logg_inn = null;
                        verdi = false;
                    }

                    if (verdi) {
                        Kunde_Meny controller = loader.getController();
                        controller.setInfo(brukere, produkter, (Kunde) brukere.getArray().get(i));

                        Scene Kundebruker = new Scene(Logg_inn);
                        Stage Scene_2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene_2.setScene(Kundebruker);
                        Scene_2.setHeight(629);
                        Scene_2.setWidth(420);
                        Scene_2.centerOnScreen();
                        Scene_2.show();

                    }
                }

            }
        }

        if (!vellykketInnlogging) {
            lblError.setText("Ugyldig brukernavn eller passord");
        }
    }


    @FXML
    void BtnAvslutt(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lastOppProdukter();
        lastOppBrukere();

    }
}
