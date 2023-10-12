package produkter;

import Brukere.InvalidNumberException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Produkt {
    private transient SimpleIntegerProperty ID;
    private transient SimpleIntegerProperty antall;
    private transient SimpleStringProperty navn;
    private transient SimpleDoubleProperty pris;
    private transient ObservableList<String> detaljer;
    private transient SimpleStringProperty type;

    public Produkt(String navn, double pris, String type, String... strings) {
        setNavn(navn);
        setPris(pris);
        setType(type);
        antall = new SimpleIntegerProperty(0);

        detaljer = FXCollections.observableArrayList();
        for (String s : strings) {
            detaljer.add(s);
        }
    }

    public String getNavn() {
        return navn.getValue();
    }

    public void setNavn(String navn) {
        this.navn = new SimpleStringProperty(navn);
    }

    public double getPris() {
        return pris.getValue();
    }

    public void setPris(double pris) {
        this.pris = new SimpleDoubleProperty(pris);
    }


    public ObservableList<String> getSpecs() {
        return detaljer;
    }

    public void setSpecs(ObservableList<String> specs2) {
        detaljer = specs2;
    }

    public String listSpecs() {
        String ut = "";
        for (String line : detaljer) {
            ut += line + "\n";
        }
        return ut;
    }

    public String getType() {

        return type.getValue();
    }

    public void setType(String type) {
        this.type = new SimpleStringProperty(type);
    }


    public int getID() {

        return ID.getValue();
    }

    public void setID(int ID) {
        this.ID = new SimpleIntegerProperty(ID);
    }

    public int getAntall() {

        return antall.getValue();
    }

    public void setAntall(int antall) throws InvalidNumberException {
        if(antall > 0)
            this.antall.setValue(antall);
        else{
            throw new InvalidNumberException("Vennligst velg et antall over 0");
        }
    }


    public void addSpec(String detalj) {

        detaljer.add(detalj);
    }

    public void addSpecs(String... strings) {
        for (String spec : strings) {
            detaljer.add(spec);
        }
    }


    public abstract String toString();
    public abstract String toStringTxt();
    public abstract String toStringTxtMedAntall();
}
