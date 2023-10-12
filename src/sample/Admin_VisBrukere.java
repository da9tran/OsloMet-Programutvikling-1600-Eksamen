package sample;

import Brukere.*;
import Brukere.Register;
import filbehandling.FiledataTxt;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import produkter.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Admin_VisBrukere {

    private Produkter produkter = new Produkter();

    private int IDs;

    @FXML
    private Button btnFjernKunder;

    @FXML
    private Button btnRedigerKunder;

    @FXML
    private Button btnFjernProdukter;


    @FXML
    private TableView tableView;

    @FXML
    private Label labelError;

    @FXML
    private TextField txtSubmit;

    @FXML
    private Label labelSøk;

    @FXML
    private TextField txtSøk;

    @FXML
    private Button btnSubmit;


    private boolean showFjern = false;

    private boolean showRediger = false;

    private boolean showProdukter = false;

    private boolean showProdukter2 = false;

    private boolean showFjernK = false;

    private boolean ShowIndividuelleProdukt = false;

    private boolean ShowIndividuelleProdukter2 = false;

    private Register brukere;

    private final Register brukere2 = new Register();


    @FXML
    private TableColumn<Bruker, Integer> IDKolonne;

    @FXML
    private TableColumn<Bruker, String> brukerKolonne;

    @FXML
    private TableColumn<Bruker, String> passordKolonne;

    @FXML
    private TableColumn<Bruker, String> mailKolonne;

    @FXML
    private TableColumn<Bruker, Boolean> adminKolonne;


    @FXML
    private TableColumn<Bruker, String> tlfKolonne;


    public void start() {
        if (brukere != null) {


            tableView.setEditable(false);


            IDKolonne.setCellValueFactory(new PropertyValueFactory<Bruker, Integer>("ID"));
            brukerKolonne.setCellValueFactory(new PropertyValueFactory<Bruker, String>("Brukernavn"));
            passordKolonne.setCellValueFactory(new PropertyValueFactory<Bruker, String>("Passord"));
            tlfKolonne.setCellValueFactory(new PropertyValueFactory<Bruker, String>("Tlf"));
            mailKolonne.setCellValueFactory(new PropertyValueFactory<Bruker, String>("Email"));
            adminKolonne.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bruker, Boolean>, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Bruker, Boolean> param) {
                    return new SimpleBooleanProperty(param.getValue().isAdmin());
                }
            });

            tlfKolonne.setCellFactory(TextFieldTableCell.forTableColumn());
            mailKolonne.setCellFactory(TextFieldTableCell.forTableColumn());

            tlfKolonne.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Bruker, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Bruker, String> event) {
                    try {
                        event.getRowValue().setTlf(event.getNewValue());
                    } catch (InvalidStringException e) {
                        labelError.setText(e.getMessage());
                    }
                    saveBrukere();
                    tlfKolonne.getTableView().refresh();
                }
            });
            mailKolonne.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Bruker, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Bruker, String> event) {
                    try {
                        event.getRowValue().setEmail(event.getNewValue());
                    } catch (InvalidStringException e) {
                        labelError.setText(e.getMessage());
                    }
                    saveBrukere();
                    mailKolonne.getTableView().refresh();
                }
            });

            tableView.setItems(brukere.getArray());

            txtSøk.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    Predicate<Bruker> Navn = bruker -> {
                        boolean sjekk = bruker.getBrukernavn().indexOf(txtSøk.getText()) != -1;
                        return sjekk;
                    };

                    brukere2.setArray(brukere.getArray().stream().filter(Navn)
                            .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                    tableView.setItems(brukere2.getArray());
                }
            });
        } else if (this.brukere == null) {
            labelError.setText("Brukere er null");
        }
    }

    public void saveBrukere() {
        FiledataTxt data = new FiledataTxt();
        Path path = Paths.get("src/filbehandling/Brukerinfo.csv");

        try {
            data.save(brukere.toStringTxt(), path);
        } catch (IOException e) {
            labelError.setText("#ERROR!");
        }
    }

    private FiledataTxt lagreTxt;

    private void saveBruekesIndividuelleVarer() {
        lagreTxt = new FiledataTxt();
        Path path = Paths.get("src/filbehandling/KundensHandlekurv.csv");
        try {
            lagreTxt.save(brukere.toStringTxtMedAntall(), path);
        } catch (IOException e) {
            labelError.setText("#ERROR! Det oppstod en feil!");
        }
    }

    @FXML
    void BtnFjernKunder_OnClick(ActionEvent event) {
        if (showProdukter2) {
            tableView.setItems(((Kunde) brukere.getArray().get(IDs)).getHandlekurv().getMainArray());
        } else if (!ShowIndividuelleProdukter2) {
            btnFjernProdukter.setText("Vis en kundes\nprodukter");
            ShowIndividuelleProdukter2 = false;
            fjernerBruker();
            tableView.setItems(brukere.getArray());
        } else if (ShowIndividuelleProdukter2) {
            fjernKundensProdukt();
            tableView.setItems(((Kunde) brukere.getArray().get(IDs)).getIndividuelleVarer().getMainArray());
        }
    }




    @FXML
    void BtnRedigerKunder_OnClick(ActionEvent event) {
        btnFjernKunder.setDisable(false);
        btnRedigerKunder.setDisable(false);
        tableView.refresh();
        if (!showRediger) {
            tableView.setEditable(true);
            btnRedigerKunder.setText("Stopp redigering");
            showRediger = true;
            showFjern = false;
            showFjernK = false;
            btnFjernProdukter.setText("Vis en kundes\nprodukter");

            btnFjernKunder.setText("Fjern brukere");
            labelError.setText("Rediger telefonnummer og email, ved å dobbeltrykke cellen. Husk å trykke 'enter' for å lagre.");
        } else if (showRediger) {
            tableView.setEditable(false);
            btnRedigerKunder.setText("Rediger brukere");
            showRediger = false;
        }
    }

    @FXML
    void btnTilbake_OnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/Admin_Meny.fxml"));
            Parent Superbruker = loader.load();

            Admin_Meny controller = loader.getController();
            controller.initBrukere(brukere, produkter);

            Scene Mellom_side = new Scene(Superbruker);
            Stage Scene_4 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene_4.setScene(Mellom_side);
            Scene_4.setHeight(750);
            Scene_4.setWidth(500);
            Scene_4.centerOnScreen();
            Scene_4.show();
        } catch (IOException e) {
            labelError.setText("#ERROR! Klarte ikke å bytte side!");
        }
    }

    public void initBrukere(Register brukere, Produkter produkter) {
        this.brukere = brukere;
        this.produkter = produkter;
    }

    public void BtnFjernProdukter_OnClick(ActionEvent actionEvent) {
        btnRedigerKunder.setText("Rediger bruker");
        if (!ShowIndividuelleProdukt) {
            ShowIndividuelleProdukt = true;
            btnFjernProdukter.setText("Tilbake");

            showFjernK = false;
            showProdukter = false;
            showRediger = false;

            btnRedigerKunder.setDisable(true);


            btnSubmit.setVisible(true);
            txtSubmit.setVisible(true);

            txtSubmit.setText("");
            txtSubmit.setPromptText("Velg en bruker via ID");


            btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
                @Override

                public void handle(ActionEvent event) {
                    int valgtBruker;
                    try {
                        valgtBruker = Integer.parseInt(txtSubmit.getText());
                    } catch (Exception e) {
                        labelError.setText("Vennligst skriv inn et gyldig tall");
                        valgtBruker = -1;
                    }

                    if (valgtBruker >= 0 && brukere.getArray().get(valgtBruker) instanceof Kunde
                            && valgtBruker < brukere.getArray().size()) {

                        IDs = valgtBruker;
                        tableView.getColumns().clear();
                        labelError.setText("Viser kundens individuelle handlekurv");
                        btnFjernKunder.setText("Fjern kundens\nprodukt");
                        ShowIndividuelleProdukter2 = true;

                        TableColumn<Produkt, Integer> IDKolonne2 = new TableColumn<>("ID");
                        TableColumn<Produkt, String> navnKolonne2 = new TableColumn<>("Produktnavn");
                        TableColumn<Produkt, String> typeKolonne2 = new TableColumn<>("Type");
                        TableColumn<Produkt, Double> prisKolonne2 = new TableColumn<>("Pris");
                        TableColumn<Produkt, Integer> antallKolonne2 = new TableColumn<>("Antall");

                        IDKolonne2.setCellValueFactory(new PropertyValueFactory<Produkt, Integer>("ID"));
                        navnKolonne2.setCellValueFactory(new PropertyValueFactory<Produkt, String>("navn"));
                        typeKolonne2.setCellValueFactory(new PropertyValueFactory<Produkt, String>("type"));
                        prisKolonne2.setCellValueFactory(new PropertyValueFactory<Produkt, Double>("pris"));
                        antallKolonne2.setCellValueFactory(new PropertyValueFactory<Produkt, Integer>("antall"));


                        tableView.getColumns().addAll(IDKolonne2, navnKolonne2, typeKolonne2, prisKolonne2, antallKolonne2);
                        tableView.setItems(((Kunde) brukere.getArray().get(valgtBruker))
                                .getIndividuelleVarer().getMainArray());

                        txtSubmit.setVisible(false);
                        btnSubmit.setVisible(false);

                        txtSøk.setText("");
                        txtSøk.setPromptText("Søk produktnavn");

                        txtSøk.setVisible(false);
                        labelSøk.setVisible(false);

                        txtSøk.setOnKeyTyped(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                Predicate<Produkt> Navn = Produkt -> {
                                    boolean sjekk = Produkt.getNavn().indexOf(txtSøk.getText()) != -1;
                                    return sjekk;
                                };
                                brukere2.setArray(brukere.getArray());
                                ((Kunde) brukere2.getArray().get(IDs)).getIndividuelleVarer()
                                        .setMainArray(((Kunde) brukere.getArray().get(IDs)).
                                                getIndividuelleVarer().getMainArray().stream().filter(Navn)
                                                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
                                tableView.setItems(((Kunde) brukere2.getArray().get(IDs))
                                        .getIndividuelleVarer().getMainArray());
                            }
                        });

                    } else if (valgtBruker >= brukere.getArray().size()) {
                        labelError.setText("Vennligst velg en bruker som eksisterer");
                    } else if (!(brukere.getArray().get(valgtBruker) instanceof Kunde)) {
                        labelError.setText("Vennligst velg en kunde");
                    } else if (valgtBruker < 0) {
                        labelError.setText("Vennligst skriv inn en gyldig ID");
                    }
                }
            });


        } else {
            btnFjernProdukter.setText("Vis en kundes\nprodukter");
            btnFjernKunder.setText("Fjern brukere");
            ShowIndividuelleProdukt = false;
            txtSubmit.setVisible(false);
            btnSubmit.setVisible(false);
            tableView.getColumns().clear();
            tableView.getColumns().addAll(IDKolonne, brukerKolonne, passordKolonne, tlfKolonne, mailKolonne);
            tableView.setItems(brukere.getArray());
            ShowIndividuelleProdukter2 = false;
            btnRedigerKunder.setDisable(false);
        }

    }



    public void fjernerBruker() {
        btnFjernKunder.setDisable(false);
        btnRedigerKunder.setDisable(false);
        showRediger = false;
        showProdukter = false;
        showFjernK = false;
        if (!showFjern) {
            showFjern = true;
            txtSubmit.setVisible(true);
            btnSubmit.setVisible(true);
            txtSubmit.setText("");
            txtSubmit.setPromptText("Skriv inn gylidg ID");
            btnFjernKunder.setText("Tilbake");
            btnRedigerKunder.setText("Rediger brukere");

            btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int valgtBruker;
                    try {
                        valgtBruker = Integer.parseInt(txtSubmit.getText());
                    } catch (Exception e) {
                        labelError.setText("Vennligst skriv inn en gyldig ID");
                        valgtBruker = -1;
                    }
                    if (valgtBruker != -1) {

                        brukere.remove(valgtBruker);
                        brukere2.setArray(brukere.getArray());

                        tableView.setItems(brukere.getArray());
                        labelError.setText("En bruker har blitt fjernet!");
                        saveBrukere();
                    }
                }
            });

        } else if (showFjern) {
            showFjern = false;
            txtSubmit.setVisible(false);
            btnSubmit.setVisible(false);
            txtSubmit.setText("");
            labelError.setText("");
            btnFjernKunder.setText("Fjern brukere");
        }
    }


    public void fjernKundensProdukt() {
        if (!showFjern) {
            showFjern = true;
            txtSubmit.setText("");
            txtSubmit.setPromptText("Velg produkt via ID");
            btnRedigerKunder.setText("Rediger bruker");
            txtSubmit.setVisible(true);
            btnSubmit.setVisible(true);
            showRediger = false;
            tableView.refresh();
            btnFjernKunder.setText("Tilbake");

            btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int valgtProdukt;
                    try {
                        valgtProdukt = Integer.parseInt(txtSubmit.getText());
                    } catch (Exception e) {
                        labelError.setText("Vennligst skriv inn et gyldig tall.");
                        valgtProdukt = -1;
                    }

                    if (valgtProdukt >= 0 && brukere.getArray().get(IDs) instanceof Kunde &&
                            ((Kunde) brukere.getArray().get(IDs)).getIndividuelleVarer()
                                    .getMainArray().size() > valgtProdukt) {

                        ((Kunde) brukere.getArray().get(IDs)).getIndividuelleVarer().remove(valgtProdukt);
                        saveBruekesIndividuelleVarer();
                        labelError.setText("Kundens produkt har nå blitt fjernet!");

                        txtSubmit.setText("");
                        txtSubmit.setPromptText("Velg produkt via ID");

                        txtSubmit.setVisible(false);
                        btnSubmit.setVisible(false);
                        btnFjernProdukter.setText("Vis en kundes\nprodukter");
                    } else {
                        labelError.setText("Dette er en Admin, vennligst velg en kunde!");
                    }
                }
            });
        } else {
            showFjern = false;
            btnFjernKunder.setText("Fjern kundens\nprodukt");
            labelError.setText("");
            txtSubmit.setVisible(false);
            btnSubmit.setVisible(false);
        }

    }

}
