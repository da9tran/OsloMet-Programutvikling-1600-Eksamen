package Brukere;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import produkter.*;

public class Kunde extends Bruker {
    private final SimpleDoubleProperty sum;
    private Produkter handlekurv;
    private Produkter individuelleVarer;
    private static final SimpleBooleanProperty ADMIN = new SimpleBooleanProperty(false);
    private int antallKjøp;

    public Kunde() {
        sum = new SimpleDoubleProperty(0);
        handlekurv = new Produkter();
        individuelleVarer = new Produkter();
    }

    public String getBrukernavn() {

        return super.getBrukernavn();
    }

    public String getPassord() {

        return super.getPassord();
    }

    public String getEmail() {

        return super.getEmail();
    }

    public Produkter getHandlekurv() {

        return handlekurv;
    }

    public Produkter getIndividuelleVarer() {

        return individuelleVarer;
    }

    public int getAntallKjøp(){

        return antallKjøp;
    }
    public String getTlf() {

        return super.getTlf();
    }

    public double getSum() {

        return sum.getValue();
    }

    public double getIndividuellevarerSum(){
        double s = 0;
        for(int i = 0; i < individuelleVarer.getMainArray().size(); i++){
            s += individuelleVarer.getMainArray().get(i).getPris()*individuelleVarer.getMainArray().get(i).getAntall();
        }
        return s;
    }

    public boolean isAdmin() {

        return ADMIN.getValue();
    }

    public void setBrukernavn(String brukernavn) {
        super.setBrukernavn(brukernavn);
    }

    public void setPassord(String passord) {
        super.setPassord(passord);
    }

    public void setEmail(String email) throws InvalidStringException {
        super.setEmail(email);
    }

    public void setAntallKjøp(int antallKjøp){
        this.antallKjøp = antallKjøp;
    }

    public void setHandlekurv(Produkter handlekurv) {
        this.handlekurv = handlekurv;
    }

    public void setIndividuelleVarer(Produkter individuelleVarer) {
        this.individuelleVarer = individuelleVarer;
    }

    public void setTlf(String tlf) throws InvalidStringException {
        super.setTlf(tlf);
    }


    public void setSum() {
        this.sum.setValue(0);
        double sum2 = 0;
        for (int i = 0; i < handlekurv.getMainArray().size(); i++) {
            sum2 += handlekurv.getMainArray().get(i).getPris();
        }
        this.sum.setValue(sum2);
    }

    public <T extends Produkt> void leggTilHandlekurv(T elem) {
        boolean check = true;

        for (int i = 0; i < handlekurv.getMainArray().size(); i++) {
            if (handlekurv.getMainArray().get(i).getType().equals(elem.getType())) {
                handlekurv.remove(i);
                handlekurv.add(elem);
                check = false;

                setSum();
            }
        }
        if (check) {
            handlekurv.add(elem);


            setSum();
        }
    }
    public <T extends Produkt> void leggTilIndividuelleHandlekurv(T elem) {
        boolean sjekk = true;

        for (int i = 0; i < individuelleVarer.getMainArray().size(); i++) {

            if (individuelleVarer.getMainArray().get(i).getNavn().equals(elem.getNavn())) {
                sjekk = false;
            }
        }
        if (sjekk) {
            individuelleVarer.add(elem);
        }
    }

    public String toStringFormat() {
        return super.toStringFormat() + isAdmin() + ";" + sum.getValue() + ";"
                + antallKjøp + ";" + handlekurv.getMainArray().size() + ";" + handlekurv.toStringTxt();
    }
    public String toStringFormatMedAntall() {
        return getBrukernavn() + ";" + getPassord() + ";" + antallKjøp + ";"
                + individuelleVarer.getMainArray().size() + ";" + individuelleVarer.toStringTxtMedAntall();
    }


}
