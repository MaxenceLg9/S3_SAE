package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestLocataire {

    private static final String NOM = "Dupont";
    private static final String PRENOM = "Jean";
    private static final String EMAIL = "jean.dupont@example.com";
    private static final char SEXE = 'M';
    private static final String TELEPHONE = "0123456789";

    public static final Locataire LOCATAIRE1 = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
    public static final Locataire LOCATAIRE2 = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);

    @BeforeAll
    public static void setUp() throws Bail.BailException {
        QueryElement.newStaticConnection();
    }

    @Test
    public void testCreatingInstance() {
        Locataire newLocataire = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
        assertEquals(NOM, newLocataire.getNom());
        assertEquals(PRENOM, newLocataire.getPrenom());
        assertEquals(EMAIL, newLocataire.getEmail());
        assertEquals(SEXE, newLocataire.getSexe());
        assertEquals(TELEPHONE, newLocataire.getTelephone());
        assertEquals(-1, newLocataire.getIdLocataire());
    }

    @Test
    public void testSavingAndDeletingInstance() throws Locataire.LocataireException {
        Locataire locataire = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
        locataire.save();
        assertNotEquals(-1, locataire.getIdLocataire());

        List<Locataire> locataires = Locataire.findAll();
        assertTrue(locataires.stream().anyMatch(l -> l.getIdLocataire() == locataire.getIdLocataire()));

        locataire.delete();
        locataires = Locataire.findAll();
        assertFalse(locataires.stream().anyMatch(l -> l.getIdLocataire() == locataire.getIdLocataire()));
    }

    @Test
    public void testModifyingFields() throws Locataire.LocataireException {
        Locataire locataire = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
        locataire.save();
        locataire.setNom("Martin");
        locataire.setPrenom("Pierre");
        locataire.setEmail("pierre.martin@example.com");
        locataire.setSexe('M');
        locataire.setTelephone("0987654321");
        locataire.modify();

        List<Locataire> locataires = Locataire.findAll();
        Locataire updatedLocataire = locataires.stream()
                .filter(l -> l.getIdLocataire() == locataire.getIdLocataire())
                .findFirst()
                .orElseThrow();

        assertEquals("Martin", updatedLocataire.getNom());
        assertEquals("Pierre", updatedLocataire.getPrenom());
        assertEquals("pierre.martin@example.com", updatedLocataire.getEmail());
        assertEquals('M', updatedLocataire.getSexe());
        assertEquals("0987654321", updatedLocataire.getTelephone());

        locataire.delete();
    }

    @Test
    public void testFindAll() throws Locataire.LocataireException {
        Locataire locataire = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
        locataire.save();
        List<Locataire> locataires = Locataire.findAll();
        assertTrue(locataires.stream().anyMatch(l -> l.getIdLocataire() == locataire.getIdLocataire()));
        locataire.delete();
    }

    @Test
    public void testGetCharges() throws Locataire.LocataireException {
        Locataire locataire = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
        locataire.save();
        try {
            List<Charges> charges = locataire.getCharges();
            assertNotNull(charges);
        } catch (Locataire.LocataireException e) {
            fail("Exception occurred while fetching charges: " + e.getMessage());
        }
        locataire.delete();
    }

    @AfterAll
    public static void setDown() {
        QueryElement.rollBackStaticConnection();
        QueryElement.removeStaticConnection();
    }

}
