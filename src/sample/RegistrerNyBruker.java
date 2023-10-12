package sample;

import Brukere.*;
import filbehandling.FiledataTxt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static javax.swing.JOptionPane.showMessageDialog;

public class RegistrerNyBruker {

    @FXML
    private TextField txtBrukernavn, txtTlfnr, txtEpost;

    @FXML
    private PasswordField txtPassord;

    @FXML
    private CheckBox chkAdmin, chkKunde;

    @FXML
    private Label lblError;

    private Register brukere;
    private FiledataTxt lagreTxt;
    private final Path path = Paths.get("src/filbehandling/Brukerinfo.csv");

    private void save() {
        lagreTxt = new FiledataTxt();
        try {
            lagreTxt.save(brukere.toStringTxt(), path);
        }

        catch (IOException e) {
            lblError.setText("Klarte ikke å lagre data");
        }
    }

    public void initRegister(Register reg) {
        brukere = reg;
    }

    @FXML
    void Btn_Avbryt_OnClick(ActionEvent event) {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/InnLogging.fxml"));
        Parent Avbryt_registrering;
        boolean value_1 = true;

        try {
            Avbryt_registrering = loader.load();
        }

        catch (IOException e) {
            lblError.setText("Klarte ikke å gå tilbake til LoggInn siden");
            Avbryt_registrering = null;
            value_1 = false;
        }

        if (value_1) {

            InnLogging controller = loader.getController();
            controller.setRegister(brukere);

            Scene Avbryt_Registrering = new Scene(Avbryt_registrering);
            Stage Scene_1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_1.setScene(Avbryt_Registrering);
            Scene_1.setHeight(617);
            Scene_1.setWidth(418);
            Scene_1.centerOnScreen();
            Scene_1.show();

        }


    }

    @FXML
    void Btn_Registrer_OnClick(ActionEvent event) {
        if (brukere != null) {
            boolean gyldigBruker = false;
            for (int i = 0; i < brukere.getArray().size(); i++) {

                if (brukere.getArray().get(i).getBrukernavn().equals(txtBrukernavn.getText())
                        && brukere.getArray().get(i).getPassord().equals(txtPassord.getText())) {
                    gyldigBruker = true;
                    i = brukere.getArray().size();
                    lblError.setText("Brukernavn og passordet er allerde tatt\nVennligst velg noe annet");

                }

                else if (brukere.getArray().get(i).getBrukernavn().equals(txtBrukernavn.getText()) &&
                        !(brukere.getArray().get(i).getPassord().equals(txtPassord.getText()))) {
                    lblError.setText("Brukernavn er allerde tatt\nVennligst velg et annet");
                    i = brukere.getArray().size();
                    gyldigBruker = true;
                }

                else if (brukere.getArray().get(i).getPassord().equals(txtPassord.getText()) &&
                        !(brukere.getArray().get(i).getBrukernavn().equals(txtBrukernavn.getText()))) {
                    lblError.setText("Passordet er allerde tatt\nVennligst velg et annet");
                    i = brukere.getArray().size();
                    gyldigBruker = true;
                }
            }
            if (!gyldigBruker) {
                boolean sjekk = true;
                Bruker b;

                if (chkAdmin.isSelected() && !chkKunde.isSelected()) {
                    b = new Admin();
                    b.setBrukernavn(txtBrukernavn.getText());
                    b.setPassord(txtPassord.getText());

                    try {
                        b.setTlf(txtTlfnr.getText());
                    }

                    catch (InvalidStringException e) {
                        txtTlfnr.setText("");
                        txtTlfnr.setPromptText(e.getMessage());
                        sjekk = false;
                    }

                    try {
                        b.setEmail(txtEpost.getText());
                    }

                    catch (InvalidStringException e) {
                        txtEpost.setText("");
                        txtEpost.setPromptText(e.getMessage());
                        sjekk = false;
                    }


                    if (sjekk) {

                        brukere.add(b);

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/FXML/Hovedmeny.fxml"));
                        Parent Registering_ny_Admin;
                        boolean lasteinn = true;
                        try {
                            Registering_ny_Admin = loader.load();
                        }

                        catch (IOException e) {
                            lblError.setText("Klarte ikke å bytte side");
                            Registering_ny_Admin = null;
                            lasteinn = false;
                        }

                        if (lasteinn) {
                            Hovedmeny controller = loader.getController();
                            controller.initRegister(brukere);

                            Scene MellomSide = new Scene(Registering_ny_Admin);
                            Stage Scene_9 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Scene_9.setScene(MellomSide);
                            Scene_9.setHeight(750);
                            Scene_9.setWidth(500);
                            Scene_9.centerOnScreen();
                            Scene_9.show();

                            save();
                        }
                    }

                }

                else if (chkKunde.isSelected() && !chkAdmin.isSelected()) {
                    b = new Kunde();
                    b.setBrukernavn(txtBrukernavn.getText());
                    b.setPassord(txtPassord.getText());
                    try {
                        b.setTlf(txtTlfnr.getText());
                    }

                    catch (InvalidStringException e) {
                        txtTlfnr.setText("");
                        txtTlfnr.setPromptText(e.getMessage());
                        sjekk = false;
                    }
                    try {
                        b.setEmail(txtEpost.getText());
                    }

                    catch (InvalidStringException e) {
                        txtEpost.setText("");
                        txtEpost.setPromptText(e.getMessage());
                        sjekk = false;
                    }

                    if (sjekk) {
                        brukere.add(b);

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/FXML/Hovedmeny.fxml"));
                        Parent Registering_ny_Standarbruker;
                        boolean lasteinn = true;

                        try {
                            Registering_ny_Standarbruker = loader.load();
                        }

                        catch (IOException e) {
                            lblError.setText("Klarte ikke å bytte side");
                            Registering_ny_Standarbruker = null;
                            lasteinn = false;
                        }

                        if (lasteinn) {
                            Hovedmeny controller = loader.getController();
                            controller.initRegister(brukere);

                            Scene MellomSide = new Scene(Registering_ny_Standarbruker);
                            Stage Scene_10 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Scene_10.setScene(MellomSide);
                            Scene_10.setHeight(750);
                            Scene_10.setWidth(500);
                            Scene_10.centerOnScreen();
                            Scene_10.show();
                            save();
                        }
                    }

                }

                else if (chkKunde.isSelected() && chkAdmin.isSelected() || !chkKunde.isSelected() && !chkAdmin.isSelected()) {
                    lblError.setText("Vennligst fyll ut informasjonen din og kryss av på en av boksene.");
                }
            }
        }

        else if (brukere == null) {
            lblError.setText("ERROR");
        }
    }
}

