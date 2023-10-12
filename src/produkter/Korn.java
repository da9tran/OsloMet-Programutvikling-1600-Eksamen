package produkter;

public class Korn extends Produkt {


    public Korn(String navn, double pris, String kategori, String... strings) {
        super(navn, pris, kategori, strings);
    }

    @Override
    public String toString() {
        return "Navn: " + super.getNavn();
    }

    @Override
    public String toStringTxt() {
        String ut = getNavn() + ";" + getPris() + ";" + getType() + ";";
        for (int i = 0; i < getSpecs().size(); i++) {
            ut += getSpecs().get(i) + ";";
        }
        return ut;
    }
    @Override
    public String toStringTxtMedAntall() {
        String ut = getNavn() + ";" + getPris() + ";" + getType() + ";" + getAntall() + ";";
        for (int i = 0; i < getSpecs().size(); i++) {
            ut += getSpecs().get(i) + ";";
        }
        return ut;
    }
}
