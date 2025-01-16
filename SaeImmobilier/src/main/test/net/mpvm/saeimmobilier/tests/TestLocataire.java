package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
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
    public static void setUp() {
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

    @Test
    public void testGetBaux() throws Locataire.LocataireException {
        Locataire locataire = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
        locataire.save();
        try {
            List<Bail> baux = locataire.getBaux();
            assertNotNull(baux);
        } catch (Locataire.LocataireException e) {
            fail("Exception occurred while fetching baux: " + e.getMessage());
        }
        locataire.delete();
    }

    @Test
    public void testAssociationWithBail() throws Locataire.LocataireException, Bail.BailException {
        Locataire locataire = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
        locataire.save();
        TestBail.BAIL.save();

        Map<Locataire, AssociationBailLocataires> associationMap = Map.of(
                locataire, new AssociationBailLocataires(locataire, TestBail.BAIL, 40.0f, 30.0f, 30.0f,30f,30f)
        );

        Locataire.setLocatairesAssociation(associationMap);

        Map<Locataire, AssociationBailLocataires> fetchedAssociations = Locataire.getLocatairesAssociation(TestBail.BAIL);
        assertTrue(fetchedAssociations.containsKey(locataire));

        TestBail.BAIL.delete();
        locataire.delete();
    }

    @AfterEach
    public void cleanUp() throws Locataire.LocataireException {
        Locataire locataire = new Locataire(NOM, PRENOM, EMAIL, SEXE, TELEPHONE);
        if (locataire.getIdLocataire() != -1) {
            locataire.delete();
        }
    }

    @AfterAll
    public static void setDown() {
        QueryElement.rollBackStaticConnection();
        QueryElement.removeStaticConnection();
    }

}
