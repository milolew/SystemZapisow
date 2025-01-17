package model;

public class FasadaWeryfikacja implements Weryfikacja {
    private static final String HASLO = "password";
    private static final String PREFIX_STUDENT = "student";
    private static final String LOGIN_PRACOWNIK = "pracownik";
    private final PobranieDanych pobranieDanych;

    public FasadaWeryfikacja(PobranieDanych pobranieDanych) {
        this.pobranieDanych = pobranieDanych;
    }

    @Override
    public boolean zaloguj(String login, String haslo) {
        if (haslo.equals(HASLO)) {
            if (login.equals(LOGIN_PRACOWNIK)) {
                return true;
            } else if (login.startsWith(PREFIX_STUDENT)) {
                try {
                    String nrIndeksuStr = login.substring(PREFIX_STUDENT.length());
                    int nrIndeksu = Integer.parseInt(nrIndeksuStr);
                    return sprawdzUprawnienia(nrIndeksu);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public String getRola(String login) {
        if (login.equals(LOGIN_PRACOWNIK)) {
            return "PRACOWNIK";
        } else if (login.startsWith(PREFIX_STUDENT)) {
            return "STUDENT";
        }
        return null;
    }

    @Override
    public int getNrIndeksu(String login) {
        if (login.startsWith(PREFIX_STUDENT)) {
            try {
                return Integer.parseInt(login.substring(PREFIX_STUDENT.length()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Nieprawidłowy format loginu studenta");
            }
        }
        throw new IllegalArgumentException("To nie jest login studenta");
    }

    @Override
    public boolean sprawdzUprawnienia(int nrIndeksu) {
        Student student = pobranieDanych.pobierzStudenta(nrIndeksu);
        return student != null;
    }
}