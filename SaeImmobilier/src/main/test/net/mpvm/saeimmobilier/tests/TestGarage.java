package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.*;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestGarage {

    public static final String COMPLEMENT_ADRESSE = "Batiment A, Appartement 86";
    public static final String VILLE = "Toulouse";
    public static final int CODE_POSTAL= 31000;
    public static final String ADRESSE = "1 rue de la paix";
    public static final String NUMERO_FISCAL = "1234567890";
    public static final String NUMERO_FISCAL_IMMEUBLE = "6789012345";
    public static final int NBPIECES = 2;
    public static final float SURFACE = 2;
    public static final Date DATE = Date.valueOf(LocalDate.now());
    private static Immeuble immeuble;


    @BeforeAll
    public static void setUp() throws Bien.BienException {
        immeuble = new Immeuble.IBuilder(VILLE, CODE_POSTAL, ADRESSE, NUMERO_FISCAL_IMMEUBLE, DATE).build();
        immeuble.save();
    }

    @AfterAll
    public static void setDown() throws Immeuble.ImmeubleException {
        immeuble.delete();
    }

    @Test
    public void testCreatingInstance() throws Bien.BienException {
        Immeuble immeuble = new Immeuble.IBuilder(VILLE, CODE_POSTAL, ADRESSE, "0123456789", DATE).build();
        Garage garage = new Garage.GBuilder(COMPLEMENT_ADRESSE, NBPIECES, "2345678901", immeuble, SURFACE, DATE).build();
        assertEquals(-1,garage.getIdBien());
    }

    @Test
    public void testFindAll() throws Bien.BienException{
        Garage garage = new Garage.GBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, immeuble, SURFACE, DATE).build();
        garage.save();
        assertEquals(1, Garage.findAll().stream().filter(g -> g.getIdBien() == garage.getIdBien()).count());
        Garage.findAll().stream().filter(g -> g.getNumeroFiscal().equals(garage.getNumeroFiscal())).findAny().ifPresent(g -> assertEquals(garage.getIdBien(), g.getIdBien()));
        Garage.findAll().stream().filter(g -> g.getNumeroFiscal().equals(garage.getNumeroFiscal())).findAny().ifPresent(g -> assertEquals(garage, g));
        //assertEquals(garage.getNumeroFiscal(), Garage.findAll().getFirst().getNumeroFiscal());
        garage.delete();
    }

    @Test
    public void testFactoryPatternInstance() throws Bien.BienException {
        Garage garage = new Garage.GBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, immeuble, SURFACE, DATE).build();
        garage.save();
        Garage garage2 = Garage.findAll().stream().filter(g -> g.getNumeroFiscal().equals(NUMERO_FISCAL)).findAny().get();
        assertEquals(garage, garage2);
        garage.delete();
    }

    @Test
    public void testSaveModifyId() throws Bien.BienException {
        Garage garage = new Garage.GBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, immeuble, SURFACE, DATE).build();
        garage.save();
        assertNotEquals(-1, garage.getIdBien());
        garage.delete();
    }

    @Test
    public void testSavingAndDeletingInstance() throws Bien.BienException {
        Garage garage = new Garage.GBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, immeuble, SURFACE, DATE).build();
        garage.save();
        assertFalse(Garage.findAll().isEmpty());
        assertEquals(1, Garage.findAll().stream().filter(g -> g.getNumeroFiscal().equals(NUMERO_FISCAL)).count());
        assertEquals(1, Garage.findAll().stream().filter(g -> g.getIdBien() == garage.getIdBien()).count());
        garage.delete();
        assertEquals(0, Garage.findAll().stream().filter(g -> g.getNumeroFiscal().equals(NUMERO_FISCAL)).count());
        assertEquals(0, Garage.findAll().stream().filter(g -> g.getIdBien() == garage.getIdBien()).count());
    }

    @Test
    public void testModifyingFields() throws Bien.BienException {
        Garage garage = new Garage.GBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, immeuble, SURFACE, DATE).build();
        garage.save();
        garage.setComplementAdresse("Batiment B, Appartement 87");
        garage.setNbPieces(5);
        garage.setNumeroFiscal("0987654321");
        garage.setSurface(62f);
        garage.setDateAjout(Date.valueOf(LocalDate.now()));
        garage.modify();

        Garage garage2 = Garage.findAll().stream().filter(g -> g.getIdBien() == garage.getIdBien()).findFirst().get();

        assertEquals("Batiment B, Appartement 87", garage2.getComplementAdresse());
        assertEquals(5, garage2.getNbPieces());
        assertEquals("0987654321", garage2.getNumeroFiscal());
        assertEquals(62f, garage2.getSurface());

        garage.delete();
    }
}