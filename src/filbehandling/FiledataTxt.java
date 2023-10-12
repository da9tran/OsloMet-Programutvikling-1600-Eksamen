package filbehandling;

import Brukere.*;
import javafx.concurrent.Task;
import produkter.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static javax.swing.JOptionPane.showMessageDialog;

public class FiledataTxt extends Task<Void> {

    private int intervaler = 0;
    private Bruker bruker;

    private Register register;
    private Path pathTxt;
    private Path pathTxt2 = Paths.get("src/filbehandling/KundensHandlekurv.csv");

    public void save(String data, Path path) throws IOException {
        Files.write(path, data.getBytes());
    }


    public Register loadBruker(Register brukere, Path path) throws Exception {
        try (BufferedReader Reader = Files.newBufferedReader(path)) {
            String line = "";
            while ((line = Reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strings = line.split(";");
                    if (intervaler == 0) {
                        boolean sjekk = true;
                        boolean Admin = Boolean.parseBoolean(strings[4]);
                        if (Admin) {
                            bruker = new Admin();
                        } else {
                            bruker = new Kunde();
                        }

                        bruker.setBrukernavn(strings[0]);
                        bruker.setPassord(strings[1]);

                        try {
                            bruker.setEmail(strings[2]);
                        } catch (InvalidStringException e) {
                            showMessageDialog(null, e.getMessage());
                            sjekk = false;
                        }
                        try {
                            bruker.setTlf(strings[3]);
                        } catch (InvalidStringException e) {
                            showMessageDialog(null, e.getMessage());
                            sjekk = false;
                        }


                        if (Admin) {
                            brukere.add(bruker);
                        } else if (!Admin) {
                            int ant;

                            try{
                                ant = Integer.parseInt(strings[6]);
                            }catch (Exception e){
                                ant = -1;
                                sjekk = false;
                            }

                            try {
                                intervaler = Integer.parseInt(strings[7]);
                            } catch (Exception e) {
                                intervaler = -1;
                                sjekk = false;
                            }

                            if (intervaler == 0 && sjekk) {
                                ((Kunde)bruker).setAntallKjøp(ant);
                                brukere.add(bruker);
                            }
                        }
                    } else if (intervaler > 0) {


                        String navn = strings[0];
                        double pris;

                        try {
                            pris = Double.parseDouble(strings[1]);
                        } catch (Exception e) {
                            pris = 0;
                            showMessageDialog(null, "klarte ikke å laste inn produkter");
                        }
                        String type = strings[2];

                        String[] detaljer = new String[strings.length - 3];

                        for (int i = 3; i < strings.length; i++) {
                            int teller = i - 3;
                            detaljer[teller] = strings[i];
                        }

                        if (bruker instanceof Kunde) {
                            if (type.equals("Korn")) {
                                ((Kunde) bruker).leggTilHandlekurv(new Korn(navn, pris, type, detaljer));
                            } else if (type.equals("Arbeidsklær")) {
                                ((Kunde) bruker).leggTilHandlekurv(new Arbeidsklær(navn, pris, type, detaljer));
                            } else if (type.equals("Gjødsel")) {
                                ((Kunde) bruker).leggTilHandlekurv(new Gjødsel(navn, pris, type, detaljer));
                            } else if (type.equals("Traktor")) {
                                ((Kunde) bruker).leggTilHandlekurv(new Traktor(navn, pris, type, detaljer));
                            }
                            intervaler--;
                            if (intervaler == 0) {
                                brukere.add(bruker);
                            }
                        }

                    }
                }
            }
        }
        return brukere;
    }

    public void lesIndividuelleVarer() throws IOException{
        try (BufferedReader Reader = Files.newBufferedReader(pathTxt2)) {
            String line = "";
            boolean funnet = false;
            while ((line = Reader.readLine()) != null) {
                String[] strings = line.split(";");
                if(!funnet && intervaler == 0) {
                    for (int i = 0; i < register.getArray().size(); i++) {
                        if (register.getArray().get(i).getBrukernavn().equals(strings[0]) &&
                                register.getArray().get(i).getPassord().equals(strings[1])) {
                            bruker = register.getArray().get(i);
                            funnet = true;

                            try{
                               intervaler = Integer.parseInt(strings[3]);
                            }catch (Exception e){
                                intervaler = -1;
                            }

                            int antallKjøp;
                            try{
                                antallKjøp = Integer.parseInt(strings[2]);
                            }catch (Exception e){
                                antallKjøp = -1;
                            }
                            if(((Kunde)register.getArray().get(i)).getAntallKjøp() < antallKjøp){
                                ((Kunde)register.getArray().get(i)).setAntallKjøp(antallKjøp);
                            }
                        }
                    }
                }else {
                    if (intervaler > 0) {
                        boolean sjekk = true;
                        String navn = strings[0];
                        double pris;

                        try {
                            pris = Double.parseDouble(strings[1]);
                        } catch (Exception e) {
                            pris = 0;
                            sjekk = false;

                        }

                        String type = strings[2];

                        int antall;
                        try{
                            antall = Integer.parseInt(strings[3]);
                        }catch (Exception e){
                            antall = -1;
                            sjekk = false;
                        }

                        String[] detaljer = new String[strings.length - 4];

                        for (int i = 4; i < strings.length; i++) {
                            int teller = i - 4;
                            detaljer[teller] = strings[i];
                        }
                        sjekk = detaljer.length % 2 == 0;

                        if (bruker instanceof Kunde && sjekk) {
                            if (type.equals("Korn")) {
                                Korn p = new Korn(navn, pris, type, detaljer);
                                p.setAntall(antall);
                                ((Kunde) bruker).leggTilIndividuelleHandlekurv(p);
                            } else if (type.equals("Arbeidsklær")) {
                                Arbeidsklær s = new Arbeidsklær(navn, pris, type, detaljer);
                                s.setAntall(antall);
                                ((Kunde) bruker).leggTilIndividuelleHandlekurv(s);
                            } else if (type.equals("Gjødsel")) {
                                Gjødsel m = new Gjødsel(navn, pris, type, detaljer);
                                m.setAntall(antall);
                                ((Kunde) bruker).leggTilIndividuelleHandlekurv(m);
                            } else if (type.equals("Trakotr")) {
                                Traktor s = new Traktor(navn, pris, type, detaljer);
                                s.setAntall(antall);
                                ((Kunde) bruker).leggTilIndividuelleHandlekurv(s);
                            }
                            intervaler--;
                        }

                    }
                }
            }
        }
    }

    public void setRegister(Register brukere) {
        this.register = brukere;
    }

    public void setPathTxt(Path pathTxt) {
        this.pathTxt = pathTxt;
    }

    public void setPathTxt2(Path pathTxt2){
        this.pathTxt2 = pathTxt2;
    }

    @Override
    protected Void call() throws Exception {
        if(pathTxt != null && register != null) {
            register.setArray(loadBruker(register, pathTxt).getArray());
            if(pathTxt2 != null){
                lesIndividuelleVarer();
            }
        }
        return null;
    }
}