package Brukere;

public class Validering {

    public static boolean Regex(String regex, String... strings) {
        for (String str : strings) {

            if (!str.matches(regex) || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean navn(String navn) {
        String regex = "";
        boolean sjekk = !navn.isEmpty() && Regex(navn, regex);
        return sjekk;
    }

    public static boolean passord(String passord) {
        String regex = "";
        boolean sjekk = !passord.isEmpty() && Regex(passord, regex);
        return sjekk;
    }

    public static boolean Email(String email) {
        String regex = "(?:[A-ZÆØÅa-zæøå0" +
                "-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x" +
                "1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[" +
                "a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9" +
                "]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-" +
                "\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        boolean sjekk = !email.isEmpty() && email.indexOf("@") != -1 && Regex(regex, email);
        return sjekk;
    }

    public static boolean tlf(String tlf) {
        String regex = "[+()\\s\\-0-9][\\s+()\\-0-9][\\s+()\\-0-9]{8,15}+";
        String regex2 = "[+()\\s\\-0-9]{8,15}+";
        boolean sjekk = !tlf.isEmpty() && Regex(regex, tlf);
        if (!sjekk) {
            sjekk = !tlf.isEmpty() && Regex(regex2, tlf);
        }
        return sjekk;
    }
}


