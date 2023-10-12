package sample;

import Brukere.InvalidNumberException;
import Brukere.Register;
import Brukere.Kunde;
import filbehandling.FiledataTxt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import produkter.Produkt;
import produkter.Produkter;
import produkter.Detaljer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Kunde_Handleside {

    @FXML
    private TableView tableView;

    @FXML
    private Button btnKvitt, btnVisHandlekurv, btnVisDetaljer, btnLeggTil, btnFullfør, btnFullKjøp;

    @FXML
    private Label lblError, lblTotSum, lblListe;

    @FXML
    private ComboBox kategoriValg;

    @FXML
    private TextField txtSøk, txtFullfør;

    @FXML
    private TableColumn<Produkt, Integer> idKln;

    @FXML
    private TableColumn<Produkt, String> produktnavnKln;

    @FXML
    private TableColumn<Produkt, String> kategoriKln;

    @FXML
    private TableColumn<Produkt, Double> prisKln;

    @FXML
    private TableColumn<Produkt, Integer> antallKln;


    private Kunde bruker;
    private Kunde bruker2 = new Kunde();
    private Produkter produkter;
    private Produkter produkter2 = new Produkter();
    private Register brukere;

    private boolean visKurv = false;
    private boolean visDetaljer = false;
    private boolean visLeggTil = false;
    private boolean visFullført = false;

    private FiledataTxt lagreTxt;

    private void lagre() {
        lagreTxt = new FiledataTxt();
        Path path = Paths.get("src/filbehandling/KundensHandlekurv.csv");
        try {
            lagreTxt.save(brukere.toStringTxtMedAntall(), path);
        }

        catch (IOException e) {
            lblError.setText("Klarte ikke å lagre data");
        }
    }

    @FXML
    void Btn_Handlekurn_OnClick(ActionEvent event) {
        kategoriValg.setValue("Alle");
        if (!visKurv) {
            if (bruker.getIndividuelleVarer().getMainArray().size() == 0) {
                lblError.setText("Din handlekurv er tom, legg til varer");
            }

            defualt(false);

            Produkter p = new Produkter();
            for (int i = 0; i < bruker.getIndividuelleVarer().getMainArray().size(); i++) {
                p.add(bruker.getIndividuelleVarer().getMainArray().get(i));
            }

            bruker.getIndividuelleVarer().setMainArray(p.getMainArray());


            btnVisHandlekurv.setText("Skjul handlekurv");
            btnVisDetaljer.setText("Tilbake");
            btnLeggTil.setText("Fjern produkt");
            btnVisDetaljer.setText("Vis detaljer");
            lblListe.setText("Viser varene i din handlekurv");
            visKurv = true;
            visDetaljer = false;
            visLeggTil = false;
            tableView.setItems(bruker.getIndividuelleVarer().getMainArray());

            btnFullfør.setVisible(false);
            txtFullfør.setVisible(false);
            btnFullKjøp.setVisible(true);
            kategoriValg.setValue("Alle");
            kategoriValg.setDisable(true);
            txtSøk.setVisible(true);
            søk(false);

            txtFullfør.setText("");
        }

        else {
            btnVisHandlekurv.setText("Vis handlekurv");
            btnVisDetaljer.setText("Vis detaljer");
            btnLeggTil.setText("Legg til produkt");
            lblListe.setText("Vareoversikt");
            visKurv = false;
            defualt(true);

        }

    }

    @FXML
    void Btn_LeggTill_OnClick(ActionEvent event) {
        btnFullfør.setVisible(true);
        txtFullfør.setVisible(true);
        txtFullfør.setText("");
        visDetaljer = false;
        visFullført = false;
        btnLeggTil.setText("Tilbake");
        btnVisDetaljer.setText("Vis detaljer");

        if (!visKurv) {
            btnFullKjøp.setVisible(false);

            if (!visLeggTil) {
                visLeggTil = true;
                txtFullfør.setPromptText("Velg produkt (ID)");
                lblError.setText("For å legge til varer så må du velge antall varer som du vil ha ved å dobbelklikke" +
                        " på varenes antall \n og deretter klikke enter for at antallet skal bli registrert!");

                defualt(true);

                btnFullfør.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int valgtprodukt;

                        try {
                            valgtprodukt = Integer.parseInt(txtFullfør.getText());
                        } catch (Exception e) {
                            lblError.setText("Vennligst skriv inn en gyldig ID på varen");
                            valgtprodukt = -1;
                        }

                        if (valgtprodukt >= 0) {
                            if (produkter.getMainArray().get(valgtprodukt).getAntall() > 0) {
                                boolean funnet = false;
                                for (int i = 0; i < bruker.getIndividuelleVarer().getMainArray().size(); i++) {
                                    if (bruker.getIndividuelleVarer().getMainArray().get(i).getNavn().
                                            equals(produkter.getMainArray().get(valgtprodukt).getNavn())) {
                                        funnet = true;
                                    }
                                }
                                if (!funnet) {
                                    bruker.getIndividuelleVarer().add(produkter.getMainArray().get(valgtprodukt));
                                    lblTotSum.setText("Totalpris: " + bruker.getIndividuellevarerSum());
                                    lblError.setText("Varen har blitt lagt til i handlekurven.");
                                    lagre();
                                } else {
                                    lblError.setText("Varen har blitt lagt til i handlekurven");
                                }
                            } else {
                                lblError.setText("Antall varer har ikke blitt registrert! Vennligst prøv på nytt");
                            }
                        }
                    }
                });
            }

            else {
                visLeggTil = false;
                btnLeggTil.setText("Legg til i handlekurv");
                btnFullfør.setVisible(false);
                txtFullfør.setVisible(false);
            }
        }

        else {
            if (!visLeggTil) {
                visLeggTil = true;
                txtFullfør.setPromptText("Velg et produkt ved å taste inn ID");
                btnFullfør.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int valgtprodukt;
                        try {
                            valgtprodukt = Integer.parseInt(txtFullfør.getText());
                        }

                        catch (Exception e) {
                            lblError.setText("Vennligst skriv inn en gyldig ID");
                            valgtprodukt = -1;
                        }

                        if (valgtprodukt >= 0) {
                            bruker.getIndividuelleVarer().remove(valgtprodukt);
                            tableView.setItems(bruker.getIndividuelleVarer().getMainArray());
                            lblError.setText("Et produkt har blitt fjernet fra handlekurven");
                            lblTotSum.setText("Totalpris: " + bruker.getIndividuellevarerSum());
                            lagre();
                        }
                    }
                });
            }

            else {
                visLeggTil = false;
                btnLeggTil.setText("Fjern et produkt");
                txtFullfør.setVisible(false);
                btnFullfør.setVisible(false);

            }
        }


    }

    @FXML
    void Btn_Detaljer_OnClick(ActionEvent event) {
        visDetaljer();
    }

    public void visDetaljer() {
        btnKvitt.setVisible(false);
        btnFullKjøp.setVisible(false);

        if (!visDetaljer) {
            defualt(true);
            btnFullfør.setVisible(true);
            txtFullfør.setVisible(true);
            visDetaljer = true;
            visKurv = false;
            visFullført = false;
            btnVisDetaljer.setText("Tilbake");
            btnLeggTil.setText("Legg til produkt");
            btnVisDetaljer.setText("Vis detaljer");


            txtFullfør.setText("");
            txtFullfør.setPromptText("Velg et produkt ved å taste inn ID");

            btnFullfør.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    int valgtProdukt;
                    try {
                        valgtProdukt = Integer.parseInt(txtFullfør.getText());
                    }

                    catch (Exception e) {
                        lblError.setText("Vennligst skriv inn en gyldig ID!");
                        valgtProdukt = -1;
                    }

                    if (valgtProdukt >= 0) {
                        lblError.setText("En vare sin detalje har blitt vist");
                        lblListe.setText("Viser detaljer for produktet");
                        tableView.getColumns().clear();

                        TableColumn<Detaljer, String> detaljerKolonne = new TableColumn<>("Detaljer");
                        TableColumn<Detaljer, String> detaljerVerdiKolonne = new TableColumn<>("Verdi");
                        detaljerKolonne.setCellValueFactory(new PropertyValueFactory<>("navn"));
                        detaljerVerdiKolonne.setCellValueFactory(new PropertyValueFactory<>("verdi"));


                        ObservableList<Detaljer> detaljer = FXCollections.observableArrayList();
                        ObservableList<Detaljer> detaljer2 = FXCollections.observableArrayList();

                        for (int i = 0; i < produkter.getMainArray().get(valgtProdukt).getSpecs().size(); i += 2) {
                            detaljer.add(new Detaljer(produkter.getMainArray().get(valgtProdukt)
                                    .getSpecs().get(i), i, produkter.getMainArray()
                                    .get(valgtProdukt).getSpecs().get(i + 1)));
                        }

                        tableView.getColumns().addAll(detaljerKolonne, detaljerVerdiKolonne);
                        tableView.setItems(detaljer);

                        txtSøk.setText("");
                        txtSøk.setPromptText("Søk detaljer");

                        kategoriValg.setDisable(true);

                        txtSøk.setOnKeyTyped(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                Predicate<Detaljer> Navn = Detaljer -> {
                                    boolean sjekk = Detaljer.getNavn().toLowerCase().indexOf(txtSøk.getText().toLowerCase()) != -1;
                                    return sjekk;
                                };

                                detaljer2.clear();
                                detaljer2.addAll(detaljer.stream().filter(Navn).collect
                                (Collectors.toCollection(FXCollections::observableArrayList)));
                                tableView.setItems(detaljer2);
                            }
                        });

                    }
                }
            });
        }

        else {
            defualt(true);
            kategoriValg.setDisable(false);
            btnVisDetaljer.setText("Vis detaljer");
            visDetaljer = false;

            btnFullfør.setVisible(false);
            txtFullfør.setVisible(false);
        }
    }

    @FXML
    void BtnTilbake_OnClick(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/Kunde_Meny.fxml"));
        Parent Kunde_Handleside;
        boolean value = true;

        try {
            Kunde_Handleside = loader.load();
        }

        catch (IOException e) {
            lblError.setText("Klarte ikke å bytte side");
            Kunde_Handleside = null;
            value = false;
        }

        if (value) {
            Kunde_Meny controller = loader.getController();
            controller.setInfo(brukere, produkter, bruker);
            Scene LoggInn = new Scene(Kunde_Handleside);
            Stage Scene_3 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_3.setScene(LoggInn);
            Scene_3.setHeight(617);
            Scene_3.setWidth(418);
            Scene_3.centerOnScreen();
            Scene_3.show();
        }
    }

    public void start(Kunde bruker, Register brukere, Produkter produkter) {
        this.bruker = bruker;
        this.brukere = brukere;
        this.produkter = produkter;

        lblTotSum.setText("Totalsum: " + bruker.getIndividuellevarerSum() + "Kr");

        ObservableList<String> kategoriValgs = FXCollections.observableArrayList(FXCollections.observableArrayList(
                "Alle", "Korn", "Arbeidsklær", "Gjødsel", "Traktor"));

        kategoriValg.setItems(kategoriValgs);

        lblTotSum.setText("Totalpris: " + bruker.getIndividuellevarerSum());

        defualt(true);
    }

    private void defualt(boolean view) {

        btnKvitt.setVisible(false);
        kategoriValg.setDisable(false);
        tableView.setEditable(true);
        btnFullKjøp.setVisible(false);
        txtSøk.setPromptText("Søk produktnavn");

        idKln.setCellValueFactory(new PropertyValueFactory<Produkt, Integer>("ID"));
        produktnavnKln.setCellValueFactory(new PropertyValueFactory<Produkt, String>("navn"));
        kategoriKln.setCellValueFactory(new PropertyValueFactory<Produkt, String>("type"));
        prisKln.setCellValueFactory(new PropertyValueFactory<Produkt, Double>("pris"));
        antallKln.setCellValueFactory(new PropertyValueFactory<Produkt, Integer>("antall"));

        IntegerStringConverter intStr = new IntegerStringConverter();
        antallKln.setCellFactory(TextFieldTableCell.forTableColumn(intStr));

        antallKln.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Produkt, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Produkt, Integer> event) {
                boolean sjekk = true;
                int antall;
                try {
                    antall = event.getNewValue();
                }

                catch (InvalidNumberException e) {
                    lblError.setText("Vennligst skriv inn gyldig antall");
                    sjekk = false;
                }

                try {
                    event.getRowValue().setAntall(event.getNewValue());
                }

                catch (InvalidNumberException e) {
                    lblError.setText(e.getMessage());
                    sjekk = false;
                }

                if (sjekk) {
                    lagre();
                    lblTotSum.setText("Totalpris: " + bruker.getIndividuellevarerSum());
                }

            }
        });

        tableView.getColumns().clear();
        tableView.getColumns().addAll(idKln, produktnavnKln, kategoriKln, prisKln, antallKln);

        if (view) {
            Produkter k = new Produkter();
            for (int i = 0; i < produkter.getMainArray().size(); i++) {
                k.add(produkter.getMainArray().get(i));
            }
            produkter = k;
            tableView.setItems(produkter.getMainArray());
        }

        else {
            tableView.setItems(bruker.getIndividuelleVarer().getMainArray());
        }

        kategoriValg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (kategoriValg.getValue().equals("Alle")) {
                    tableView.setItems(produkter.getMainArray());
                }

                else {
                    Predicate<Produkt> type = Produkt -> {
                        boolean sjekk = Produkt.getNavn().toLowerCase().indexOf(txtSøk.getText().toLowerCase()) != -1;
                        return sjekk;
                    };
                    produkter2.setMainArray(produkter.getMainArray().stream().filter(type)
                            .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                    tableView.setItems(produkter2.getMainArray());

                }

                txtSøk.setVisible(true);
                søk(view);
            }
        });
    }

    public void søk(boolean view) {
        txtSøk.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Predicate<Produkt> Navn = Produkt -> {

                    if (!kategoriValg.getValue().equals("Alle")) {
                        boolean sjekk = Produkt.getNavn().toLowerCase().indexOf(txtSøk.getText().toLowerCase()) != -1
                                && kategoriValg.getValue().equals(Produkt.getType());
                        return sjekk;
                    }

                    boolean sjekk = Produkt.getNavn().toLowerCase().indexOf(txtSøk.getText().toLowerCase()) != -1;
                    return sjekk;
                };

                if (view) {
                    produkter2.setMainArray(produkter.getMainArray().stream().filter(Navn)
                            .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                    tableView.setItems(produkter2.getMainArray());
                }

                else {
                    bruker2.getIndividuelleVarer().setMainArray(bruker.getIndividuelleVarer().getMainArray().stream().filter(Navn)
                            .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                    tableView.setItems(bruker2.getIndividuelleVarer().getMainArray());
                }
            }
        });
    }


    public void On_Click_BtnKjøp(ActionEvent event) {
        if (bruker.getIndividuelleVarer().getMainArray().size() > 0) {
            if (!visFullført) {
                visFullført = true;
                btnKvitt.setVisible(true);
                lblListe.setText("Ditt kjøp er fullført");
                btnFullKjøp.setText("Avbryt");
                btnKvitt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        DirectoryChooser fc = new DirectoryChooser();
                        bruker.setAntallKjøp(bruker.getAntallKjøp() + 1);

                        File f = fc.showDialog(null);
                        if (f != null) {
                            Path path = Paths.get(f.getAbsolutePath() + "\\Kvittering(" + bruker.getAntallKjøp() + ").csv");
                            String s = f.getAbsolutePath();

                            FiledataTxt lagre = new FiledataTxt();
                            String brukerInfo = bruker.getBrukernavn() + ";" + bruker.getTlf() + ";" + bruker.getEmail() + "\n";
                            String produkter = bruker.getIndividuelleVarer().toStringTxtMedAntall() + "\nTotalsum;"
                            + bruker.getIndividuellevarerSum();
                            bruker.getIndividuelleVarer().getMainArray().clear();

                            try {
                                lagre.save(brukerInfo + produkter, path);
                            }

                            catch (IOException e) {
                                lblError.setText(e.getMessage());
                            }

                            lagre();
                            bruker.getIndividuelleVarer().getMainArray().clear();
                            lblError.setText("Kjøpet er fullført");
                            tableView.getItems().clear();
                        }

                    }
                });

            }

            else {
                visFullført = false;
                btnKvitt.setVisible(false);
                btnFullKjøp.setText("Fullfør kjøp");
            }
        }

        else {
            lblError.setText("Du må ha minst en vare i handlekurven for å foreta kjøpet");
        }
    }
}
