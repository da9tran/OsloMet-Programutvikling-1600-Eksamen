package sample;

import Brukere.Register;
import filbehandling.FiledataJOBJ;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import produkter.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Admin_VisProdukter {

    //@FXML
    // private AnchorPane pane;

    @FXML
    private TableView tableView;

    private Register Brukere;

    private Produkter produkter = new Produkter();

    private final Produkter prod = new Produkter();

    private int IDs;

    private final Button btnf = new Button();

    @FXML
    private Button btnLeggTilProdukter, btnFjern, btnRedigerProdukter, btnVisDetaljer, btnFullfør, btnAdd;

    @FXML
    private Label lblError, lblSøk;

    @FXML
    private TextField txtProduktnavn, txtPris, txtSøk, txtFullfør;

    @FXML
    private TextArea txtDetaljer;

    @FXML
    private ComboBox kategoriValg;

    @FXML
    private GridPane ProdukterGrid, ProdukterGrid2;

    @FXML
    private TableColumn<Produkt, Integer> idKln;

    @FXML
    private TableColumn<Produkt, String> produktnavnKln;

    @FXML
    private TableColumn<Produkt, String> kategoriKln;

    @FXML
    private TableColumn<Produkt, Double> prisKln;

    private final TableColumn<Detaljer, Integer> detaljIDKln = new TableColumn<>("ID");
    private final TableColumn<Detaljer, String> detaljProduktnavnKln = new TableColumn<>("Detaljer");
    private final TableColumn<Detaljer, String> detaljVerdiKln = new TableColumn<>("Verdi");


    private boolean visLeggTil = false;
    private boolean visFjern = false;
    private boolean visDetaljer = false;
    private boolean visRediger = false;

    private ObservableList<Detaljer> detaljer = FXCollections.observableArrayList();
    private ObservableList<Detaljer> detaljerSøk = FXCollections.observableArrayList();

    public void start() {

        idKln.setCellValueFactory(new PropertyValueFactory<Produkt, Integer>("ID"));
        produktnavnKln.setCellValueFactory(new PropertyValueFactory<Produkt, String>("navn"));
        kategoriKln.setCellValueFactory(new PropertyValueFactory<Produkt, String>("type"));
        prisKln.setCellValueFactory(new PropertyValueFactory<Produkt, Double>("pris"));

        tableView.setItems(produkter.getMainArray());

        txtSøk.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Predicate<Produkt> Navn = Produkt -> {
                    boolean sjekk = Produkt.getNavn().indexOf(txtSøk.getText()) != -1;
                    return sjekk;
                };

                prod.setMainArray(produkter.getMainArray().stream().filter(Navn)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                tableView.setItems(prod.getMainArray());
            }
        });
    }


    public void saveProdukter() {
        FiledataJOBJ data = new FiledataJOBJ();
        Path path = Paths.get("src/filbehandling/LagreProdukter.JOBJ");

        try {
            data.save(produkter, path);
        } catch (IOException e) {
            lblError.setText("#ERROR!");
        }
    }


    @FXML
    void BtnFjernProdukter_OnClick(ActionEvent event) {
        txtFullfør.setVisible(true);
        btnFullfør.setVisible(true);
        txtFullfør.setText("");
        txtFullfør.setPromptText("Skriv inn gydlig ID");
        visRediger = false;
        visLeggTil = false;

        if (!visDetaljer) {
            tableView.setEditable(false);
            btnLeggTilProdukter.setText("Legg til produkter");
            btnFjern.setText("Tilbake");
            btnRedigerProdukter.setText("Rediger produkter");
            if (!visFjern) {
                visFjern = true;
                ProdukterGrid.setVisible(false);
                ProdukterGrid2.setVisible(false);


                if (tableView.isVisible()) {

                    btnFullfør.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            int valgtProdukt;
                            try {
                                valgtProdukt = Integer.parseInt(txtFullfør.getText());
                            }

                            catch (Exception e) {
                                lblError.setText("Vennligst skriv inn gyldig vare ID");
                                valgtProdukt = -1;
                            }

                            if (valgtProdukt != -1) {
                                produkter.remove(valgtProdukt);
                                prod.setMainArray(produkter.getMainArray());

                                tableView.setItems(produkter.getMainArray());

                                saveProdukter();
                                lblError.setText("Et produkt har blitt fjernet!");

                                btnFullfør.setVisible(false);
                                txtFullfør.setVisible(false);
                                visFjern = true;
                                btnFjern.setText("Fjern produkter");
                            }
                        }
                    });

                }

                else {
                    tableView.setVisible(true);
                    lblSøk.setVisible(true);
                    txtSøk.setVisible(true);
                    visFjern = true;

                    btnFullfør.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            int valgtProdukt;
                            try {
                                valgtProdukt = Integer.parseInt(txtFullfør.getText());
                            } catch (Exception e) {
                                lblError.setText("Vennligst skriv inn gyldig vare ID");
                                valgtProdukt = -1;
                            }
                            if (valgtProdukt != -1) {

                                produkter.remove(valgtProdukt);
                                prod.setMainArray(produkter.getMainArray());

                                tableView.setItems(produkter.getMainArray());

                                saveProdukter();
                                lblError.setText("Et produkt har blitt nå fjernet!");

                                visRediger = false;
                                visLeggTil = false;
                                txtFullfør.setVisible(false);
                                btnFullfør.setVisible(false);
                                visFjern = false;
                                btnFjern.setText("Fjern produkter");

                            }
                        }
                    });
                }
            }

            else {
                visFjern = false;
                btnFjern.setText("Fjern produkter");
                txtFullfør.setVisible(false);
                btnFullfør.setVisible(false);
            }
        }

        else if (visDetaljer) {
            if (!visFjern) {
                tableView.setEditable(false);
                btnLeggTilProdukter.setText("Legg til detaljer");
                btnFjern.setText("Tilbake");
                btnRedigerProdukter.setText("Rediger detaljer");

                txtFullfør.setText("");
                txtFullfør.setPromptText("Skriv inn ID");
                visFjern = true;
                visLeggTil = false;
                visRediger = false;

                btnFullfør.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int detaljID;
                        if (txtFullfør.getText() != null) {
                            try {
                                detaljID = Integer.parseInt(txtFullfør.getText());
                            }

                            catch (Exception e) {
                                lblError.setText("Skriv inn en gyldig id");
                                detaljID = -1;
                            }
                        }

                        else {
                            lblError.setText("Skriv inn en gyldig id");
                            detaljID = -1;
                        }

                        if (detaljID >= 0) {
                            produkter.getMainArray().get(IDs).getSpecs().remove(detaljID);
                            tableView.getItems().remove(detaljID);

                            ObservableList<Detaljer> ny = FXCollections.observableArrayList();

                            for (int i = 0; i < detaljer.size(); i++) {
                                detaljer.get(i).setID(i);
                            }
                            tableView.refresh();

                            saveProdukter();
                            lblError.setText("Produktet sin detalje har blitt fjernet!");
                            txtFullfør.setVisible(false);
                            btnFullfør.setVisible(false);
                            btnFjern.setText("Fjern detalje");
                            visFjern = false;
                        }
                    }
                });

            }

            else {
                visFjern = false;
                btnFjern.setText("Fjern detaljer");
                txtFullfør.setVisible(false);
                btnFullfør.setVisible(false);
            }
        }
    }

    @FXML
    void BtnLeggTilProdukter_OnClick(ActionEvent event) {


        ProdukterGrid.setVisible(false);
        ProdukterGrid2.setVisible(false);

        txtFullfør.setVisible(false);
        btnFullfør.setVisible(false);
        txtFullfør.setText("");

        if (!visDetaljer) {
            tableView.setEditable(false);
            btnRedigerProdukter.setText("Rediger produkter");
            btnFjern.setText("Fjern produkter");

            if (!visLeggTil) {
                btnLeggTilProdukter.setText("Tilbake");
                visLeggTil = true;
                visRediger = false;
                visFjern = false;
                tableView.setVisible(false);
                txtSøk.setVisible(false);
                lblSøk.setVisible(false);
                ProdukterGrid.setVisible(true);
                ProdukterGrid2.setVisible(true);
                txtProduktnavn.setText("");
                txtPris.setText("");
                txtDetaljer.setText("");

                ObservableList<String> typer = FXCollections.observableArrayList();

                for (String s : Produkter.getTyper()) {
                    typer.add(s);
                }
                kategoriValg.setItems(typer);

                kategoriValg.setOnAction(new EventHandler<ActionEvent>() {

    @Override
    public void handle(ActionEvent event) {
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String[] lines = txtDetaljer.getText().split("\n");
                double pris;
                boolean sjekk = true;
                try {
                    pris = Double.parseDouble(txtPris.getText());
                } catch (Exception e) {
                    pris = 0;
                    sjekk = false;
                    txtPris.setText("");
                    txtPris.setPromptText("Vennligst skriv inn gyldige verdier!");
                }
                String line = lines[0];
                for (int i = 1; i < lines.length; i++) {
                    line += ":" + lines[i];
                }
                String[] detaljer = line.split(":");


                if (sjekk) {
                    if (kategoriValg.getValue().equals("Korn")) {
                        produkter.add(new Korn(txtProduktnavn.getText(), pris, "Korn", detaljer));
                    } else if (kategoriValg.getValue().equals("Arbeidsklær")) {
                        produkter.add(new Arbeidsklær(txtProduktnavn.getText(), pris, "Arbeidsklær", detaljer));
                    } else if (kategoriValg.getValue().equals("Gjødsel")) {
                        produkter.add(new Gjødsel(txtProduktnavn.getText(), pris, "Gjødsel", detaljer));
                    } else if (kategoriValg.getValue().equals("Traktor")) {
                        produkter.add(new Traktor(txtProduktnavn.getText(), pris, "Traktor", detaljer));
                    }
                    else {
                        lblError.setText("Husk å velg et produkt");
                    }
                    lblError.setText("Et produkt har blitt lagt til");
                    saveProdukter();
                    tableView.setItems(produkter.getMainArray());
                }
            }
        });
    }
});
            } else if (visLeggTil) {
                tableView.setVisible(true);
                lblSøk.setVisible(true);
                txtSøk.setVisible(true);
                btnLeggTilProdukter.setText("Legg til produkter");
                txtFullfør.setVisible(false);
                btnFullfør.setVisible(false);
                ProdukterGrid.setVisible(false);
                ProdukterGrid2.setVisible(false);
                visLeggTil = false;
            }
        } else if (visDetaljer) {
            if (!visLeggTil) {
                visLeggTil = true;
                btnFjern.setText("Fjern detaljer");
                btnRedigerProdukter.setText("Rediger detaljer");
                tableView.setEditable(false);
                txtFullfør.setVisible(true);
                btnFullfør.setVisible(true);
                txtFullfør.setText("");
                txtFullfør.setPromptText("detalj: verdi");

                btnLeggTilProdukter.setText("Tilbake");
                btnFullfør.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String[] strings = txtFullfør.getText().split(":");
                        String navn = strings[0].trim();
                        String verdi = strings[1].trim();
                        Detaljer detaljer = new Detaljer(navn, tableView.getItems().size(), verdi);
                        tableView.getItems().add(detaljer);
                        produkter.getMainArray().get(IDs).getSpecs().add(txtFullfør.getText());
                        saveProdukter();
                        lblError.setText("En detalj har blitt lagt til!");
                        visLeggTil = false;
                        txtFullfør.setVisible(false);
                        btnFullfør.setVisible(false);
                        btnLeggTilProdukter.setText("Legg til detaljer");
                    }
                });
            } else {
                btnLeggTilProdukter.setText("Legg til detaljer");
                visLeggTil = false;
                txtFullfør.setVisible(false);
                btnFullfør.setVisible(false);
            }
        }
    }

    @FXML
    void BtnRedigerPrdukter_OnClick(ActionEvent event) {
        ProdukterGrid.setVisible(false);
        ProdukterGrid2.setVisible(false);
        btnf.setVisible(false);
        tableView.setEditable(true);
        tableView.setVisible(true);
        tableView.refresh();

        txtFullfør.setVisible(false);
        btnFullfør.setVisible(false);
        txtFullfør.setText("");
        txtFullfør.setPromptText("skriv inn ID");

        if (!visDetaljer) {
            if (!visRediger) {
                lblError.setText("Du kan kun redigere på navn og pris etter å ha klikket på disse,\nhusk å klikke på enter etter å ha redigert ferdig.");
                DoubleStringConverter doubleString = new DoubleStringConverter();

                produktnavnKln.setCellFactory(TextFieldTableCell.forTableColumn());
                prisKln.setCellFactory(TextFieldTableCell.forTableColumn(doubleString));

                produktnavnKln.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Produkt, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Produkt, String> event) {
                        event.getRowValue().setNavn(event.getNewValue());

                        produktnavnKln.getTableView().refresh();

                        saveProdukter();
                    }
                });
                prisKln.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Produkt, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Produkt, Double> event) {
                        event.getRowValue().setPris(event.getNewValue());

                        prisKln.getTableView().refresh();
                        saveProdukter();
                    }

                });

                btnRedigerProdukter.setText("Stopp redigering");
                visRediger = true;
                visLeggTil = false;
                visFjern = false;
                btnFjern.setText("Fjern produkter");
                btnLeggTilProdukter.setText("Legg til produkter");
            } else if (visRediger) {
                btnRedigerProdukter.setText("Rediger produkter");
                tableView.setEditable(false);
                visRediger = false;
                lblError.setText("");
            }
        } else if (visDetaljer) {
            if (!visRediger) {
                detaljProduktnavnKln.setCellFactory(TextFieldTableCell.forTableColumn());
                detaljVerdiKln.setCellFactory(TextFieldTableCell.forTableColumn());
                detaljProduktnavnKln.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Detaljer, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Detaljer, String> event) {
                        produkter.getMainArray().get(IDs).getSpecs().add(event.getRowValue().getID(),
                                event.getNewValue());
                        produkter.getMainArray().get(IDs).getSpecs().remove(event.getRowValue().getNavn());
                        produkter.getMainArray().get(IDs).getSpecs().remove(event.getRowValue().getVerdi());
                        produkter.getMainArray().get(IDs).getSpecs().add(event.getRowValue().getID() + 1,
                                event.getRowValue().getVerdi());


                        event.getRowValue().setNavn(event.getNewValue());
                        event.getTableView().refresh();
                        saveProdukter();
                    }
                });
                detaljVerdiKln.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Detaljer, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Detaljer, String> event) {
                        produkter.getMainArray().get(IDs).getSpecs().remove(event.getRowValue().getNavn());
                        produkter.getMainArray().get(IDs).getSpecs().remove(event.getRowValue().getVerdi());
                        produkter.getMainArray().get(IDs).getSpecs().add(event.getRowValue().getID(),
                                event.getNewValue());
                        produkter.getMainArray().get(IDs).getSpecs().add(event.getRowValue().getID() + 1,
                                event.getRowValue().getVerdi());


                        event.getRowValue().setVerdi(event.getNewValue());
                        event.getTableView().refresh();
                        saveProdukter();
                    }
                });
                btnRedigerProdukter.setText("Stopp redigering");
                visRediger = true;
                visLeggTil = false;
                visFjern = false;
                btnFjern.setText("Fjern produkter");
                btnLeggTilProdukter.setText("Legg til produkter");
            } else if (visRediger) {
                tableView.setEditable(false);
                txtFullfør.setVisible(false);
                btnFullfør.setVisible(false);
                visRediger = false;
                txtFullfør.setText("");
                btnRedigerProdukter.setText("Rediger produkter");
            }
        }
    }


    @FXML
    void BtnTilbake_OnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/Admin_Meny.fxml"));
            Parent Superbruker = loader.load();

            Admin_Meny controller = loader.getController();
            controller.initBrukere(Brukere, produkter);

            Scene Mellom_side = new Scene(Superbruker);
            Stage Scene_4 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_4.setScene(Mellom_side);
            Scene_4.setHeight(750);
            Scene_4.setWidth(500);
            Scene_4.centerOnScreen();
            Scene_4.show();
        } catch (IOException e) {
            lblError.setText("Klarte ikke å bytte side");
        }

    }

    public void setBruker(Register brukere, Produkter produkter) {
        this.Brukere = brukere;
        this.produkter = produkter;
    }

    public void BtnVisProdukter_OnClick(ActionEvent event) {
        tableView.setEditable(false);
        tableView.setVisible(true);
        txtSøk.setVisible(true);
        lblSøk.setVisible(true);
        txtFullfør.setVisible(false);
        btnFullfør.setVisible(false);

        ProdukterGrid.setVisible(false);
        ProdukterGrid2.setVisible(false);

        btnf.setVisible(false);

        tableView.getColumns().clear();
        visDetaljer = false;
        visRediger = false;
        visFjern = false;
        visLeggTil = false;

        tableView.getColumns().addAll(idKln, produktnavnKln, kategoriKln, prisKln);
        tableView.setItems(produkter.getMainArray());

        txtSøk.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Predicate<Produkt> Navn = Produkt -> {
                    boolean sjekk = Produkt.getNavn().indexOf(txtSøk.getText()) != -1;
                    return sjekk;
                };

                prod.setMainArray(produkter.getMainArray().stream().filter(Navn)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                tableView.setItems(prod.getMainArray());
            }

        });


        btnFjern.setText("Fjern produkter");
        btnRedigerProdukter.setText("Rediger produkter");
        tableView.setEditable(false);

        btnLeggTilProdukter.setText("Legg til produkter");
        txtSøk.setPromptText("Skriv inn produktnavnet");
    }

    public void BtnVisDetaljer_OnClick(ActionEvent event) {
        if (!visDetaljer) {
            tableView.setVisible(true);
            tableView.setEditable(false);
            txtSøk.setVisible(true);
            lblSøk.setVisible(true);

            ProdukterGrid.setVisible(false);
            ProdukterGrid2.setVisible(false);

            btnf.setVisible(false);

            txtFullfør.setVisible(true);
            btnFullfør.setVisible(true);
            txtFullfør.setText("");
            txtFullfør.setPromptText("Velg produkt (ID)");

            btnFullfør.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int ID;
                    if (txtFullfør.getText() != null || !txtFullfør.getText().isEmpty()) {

                        try {
                            ID = Integer.parseInt(txtFullfør.getText());
                        } catch (Exception e) {
                            lblError.setText("Vennligst skriv inn riktig verdi");
                            ID = -1;
                        }
                    } else {
                        lblError.setText("Vennligst skriv inn riktig verdi");
                        ID = -1;
                    }


                    if (ID >= 0 && ID < produkter.getMainArray().size()) {
                        tableView.getColumns().clear();
                        txtSøk.setPromptText("Skriv inn detalj");

                        detaljer = FXCollections.observableArrayList();
                        IDs = ID;

                        for (int i = 0; i < produkter.getMainArray().get(ID).getSpecs().size(); i += 2) {
                            Detaljer t = new Detaljer(produkter.getMainArray().get(ID).getSpecs().get(i), i
                                    , produkter.getMainArray().get(ID).getSpecs().get(i + 1));
                            detaljer.add(t);
                        }
                        detaljIDKln.setCellValueFactory(new PropertyValueFactory<Detaljer, Integer>("ID"));
                        detaljProduktnavnKln.setCellValueFactory(new PropertyValueFactory<Detaljer, String>("navn"));
                        detaljVerdiKln.setCellValueFactory(new PropertyValueFactory<Detaljer, String>("verdi"));


                        tableView.getColumns().addAll(detaljIDKln, detaljProduktnavnKln, detaljVerdiKln);

                        tableView.setItems(detaljer);
                        visDetaljer = true;
                        visRediger = false;
                        visFjern = false;
                        visLeggTil = false;

                        txtSøk.setOnKeyTyped(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                detaljerSøk = detaljer.stream().filter(s -> s.getNavn().indexOf(txtSøk.getText()) != -1)
                                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                                tableView.setItems(detaljerSøk);
                            }
                        });
                        btnFjern.setText("Fjern produkter");
                        btnRedigerProdukter.setText("Rediger produkter");
                        btnLeggTilProdukter.setText("Legg til produkter");

                        txtFullfør.setVisible(false);
                        btnFullfør.setVisible(false);

                        btnVisDetaljer.setText("Tilbake");
                    }
                }
            });
        }

        else {
            txtFullfør.setVisible(false);
            btnFullfør.setVisible(false);
            btnVisDetaljer.setText("Vis detaljen til et produkt");
            BtnVisProdukter_OnClick(event);
            visDetaljer = false;
        }

    }
}
