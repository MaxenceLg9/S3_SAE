package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Proprietaire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestProprietaire {

    private static final String NOM = "Doe";
    private static final String PRENOM = "John";
    private static final String EMAIL = "johndoe@gmail.com";
    private static final String PASSWORD = "password";

    @Test
    public void testInstanceProprietaire() throws Proprietaire.ProprietaireException {
        Proprietaire proprietaire = new Proprietaire(EMAIL,PASSWORD);
        assertNotNull(proprietaire);
        assertEquals(EMAIL,proprietaire.getEmail());
        assertEquals(PASSWORD,proprietaire.getPassword());
    }
}
