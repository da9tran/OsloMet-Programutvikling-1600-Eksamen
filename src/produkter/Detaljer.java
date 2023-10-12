package produkter;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Detaljer {
    private final SimpleIntegerProperty ID;
    private final SimpleStringProperty navn;
    private final SimpleStringProperty verdi;


    public Detaljer(String navn, int ID, String verdi) {
        this.ID = new SimpleIntegerProperty(ID);
        this.navn = new SimpleStringProperty(navn);
        this.verdi = new SimpleStringProperty(verdi);

    }

    public int getID(){
        return ID.getValue();
    }

    public void setID(int ID) {
        this.ID.setValue(ID);
    }

    public String getNavn(){
        return navn.getValue();
    }

    public void setNavn(String navn) {
        this.navn.setValue(navn);
    }

    public String getVerdi(){
        return verdi.getValue();
    }


    public void setVerdi(String verdi) {
        this.verdi.setValue(verdi);
    }


}

