package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Proprietaire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestProprietaire {

    private static final String EMAIL = "johndoe@gmail.com";
    private static final String PASSWORD = "A1!password";

    @Test
    public void testInstanceProprietaire() throws Proprietaire.ProprietaireException {
        Proprietaire proprietaire = new Proprietaire(EMAIL,PASSWORD);
        assertNotNull(proprietaire);
        assertEquals(EMAIL,proprietaire.getEmail());
        assertEquals(PASSWORD,proprietaire.getPassword());
    }

    @Test
    public void testThrowsPasswordInvalid(){

        // Invalid passwords - missing requirements
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "password")); // Only lowercase
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "PASSWORD")); // Only uppercase
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "12345678")); // Only numbers
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "!&=()$%")); // Only special characters

        // Invalid passwords - too short
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "Pas1!")); // Too short with valid chars
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "12345!")); // Too short numbers and special char
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "Ab1!")); // Too short with mixed chars

        // Invalid passwords - missing one of the requirements
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "Password1")); // Missing special char
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "Password!")); // Missing number
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "12345!a")); // Missing uppercase
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire(EMAIL, "12345!A")); // Missing lowercase

        // Valid passwords - should NOT throw exceptions
        assertDoesNotThrow(() -> new Proprietaire(EMAIL, "Pass1!word")); // Valid mixed password
        assertDoesNotThrow(() -> new Proprietaire(EMAIL, "1!Password")); // Valid mixed password
        assertDoesNotThrow(() -> new Proprietaire(EMAIL, "!Password1")); // Valid mixed password
    }

    @Test
    public void testExceptionProprietaire(){
        assertThrows(Proprietaire.ProprietaireException.class, () -> new Proprietaire("mama","eaaa"));
    }
}
