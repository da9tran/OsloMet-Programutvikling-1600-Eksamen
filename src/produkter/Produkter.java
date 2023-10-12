package produkter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Produkter {

    private static final  String[] typer = {"Korn", "Arbeidsklær", "Gjødsel", "Traktor"};

    private static final Produkt[] typer2 = {
            new Korn("", 00, ""),
            new Arbeidsklær("", 00, ""),
            new Gjødsel("", 00, ""),
            new Traktor("", 00, "")};



    ObservableList<Produkt> main = FXCollections.observableArrayList();

    public static String[] getTyper() {
        return typer;
    }
    public static Produkt[] getTyper2() {
        return typer2;
    }

    public ObservableList<Produkt> getMainArray() {
        return main;
    }

    public void setMainArray(ObservableList<Produkt> elems) {
        main = elems;
    }


    public String toStringTxt() {
        String ut = "";
        for (int i = 0; i < main.size(); i++) {
            ut += "\n" + main.get(i).toStringTxt();
        }
        return ut;
    }
    public String toStringTxtMedAntall() {
        String ut = "";
        for (int i = 0; i < main.size(); i++) {
            ut += "\n" + main.get(i).toStringTxtMedAntall();
        }
        return ut;
    }

    public void sort() {
        ObservableList<Produkt> newMain = FXCollections.observableArrayList();

        for (int i = 0; i < typer2.length; i++) {
            for (int j = 0; j < main.size(); j++) {
                if (typer2[i].getClass().equals(main.get(j).getClass())) {
                    main.get(j).setID(newMain.size());
                    newMain.add(main.get(j));
                }
            }
        }
        main = newMain;
    }

    public <T extends Produkt> boolean add(T elem) {
        boolean sjekk = false;
        for (int i = 0; i < typer2.length; i++) {
            if (typer2[i].getClass().equals(elem.getClass())) {
                sjekk = true;
            }
        }

        if (sjekk) {
            main.add(elem);
            sort();
        }
        return sjekk;
    }

    public void remove(int index) {
        main.remove(index);
        sort();
    }

    public <T extends Produkt> Produkt getFromMain(int index) {
        return main.get(index);
    }

    public void writeObject(ObjectOutputStream stream) throws IOException {
        for (int i = 0; i < main.size(); i++) {
            stream.writeUTF(main.get(i).getNavn());
            stream.writeDouble(main.get(i).getPris());
            stream.writeUTF(main.get(i).getType());

            String str = "";
            for (String s : main.get(i).getSpecs()) {
                str += s + ":";
            }
            stream.writeUTF(str);

            stream.writeBoolean(i != main.size() - 1);
        }
    }

    public void readObject(ObjectInputStream stream) throws Exception {
        boolean fortsett = true;
        while (fortsett) {
            String navn = stream.readUTF();
            double pris = stream.readDouble();
            String type = stream.readUTF();
            String string = stream.readUTF();
            String[] strings = string.split(":");

            if (type.equals("Korn")) {
                add(new Korn(navn, pris, type, strings));
            }

            else if (type.equals("Arbeidsklær")) {
                add(new Arbeidsklær(navn, pris, type, strings));
            }

            else if (type.equals("Gjødsel")) {
                add(new Gjødsel(navn, pris, type, strings));
            }
            else if (type.equals("Traktor")) {
                add(new Traktor(navn, pris, type, strings));
            }

            else {
                throw new Exception("Lasting av data mislyktes");
            }
            fortsett = stream.readBoolean();
        }
    }
}
