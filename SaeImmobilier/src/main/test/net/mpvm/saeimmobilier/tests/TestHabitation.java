package net.mpvm.saeimmobilier.tests;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.modele.Habitation;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.modele.TypeBien;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import static net.mpvm.saeimmobilier.tests.TestImmeuble.IMMEUBLE;

public class TestHabitation {

    public static final String COMPLEMENT_ADRESSE = "Batiment A, Appartement 86";
    public static final String VILLE = "Toulouse";
    public static final String CODE_POSTAL= "31000";
    public static final String ADRESSE = "1 rue de la paix";
    public static final String NUMERO_FISCAL = "1234567890";
    public static final String NUMERO_FISCAL_IMMEUBLE = "6789012345";
    public static final int NBPIECES = 2;
    public static final float SURFACE = 2;
    public static final String IDPROPRIO = "IMMEUBLE COMME JAIME";
    public static final Date DATE = Date.valueOf(LocalDate.now());

    public static final Habitation HABITATION;

    static {
        try {
            HABITATION = new Habitation.HBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, TestImmeuble.IMMEUBLE, SURFACE,IDPROPRIO, DATE).build();
        } catch (Bien.BienException e) {
            throw new RuntimeException(e);
        }
    }


    @BeforeAll
    public static void setUp() throws Bien.BienException {
        QueryElement.newStaticConnection();
        TestImmeuble.IMMEUBLE.save();
    }

    @AfterAll
    public static void setDown() throws Immeuble.ImmeubleException, BienLouable.BienLouableException {
        IMMEUBLE.delete();
        QueryElement.rollBackStaticConnection();
        QueryElement.removeStaticConnection();
    }

    @Test
    public void testCreatingInstance() throws Bien.BienException {
        Habitation habitation = new Habitation.HBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, TestImmeuble.IMMEUBLE, SURFACE,IDPROPRIO, DATE).build();
        assertEquals(-1,habitation.getIdBien());
    }

    @Test
    public void testFindAll() throws Bien.BienException{
        Habitation habitation = new Habitation.HBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, TestImmeuble.IMMEUBLE, SURFACE,IDPROPRIO, DATE).build();
        habitation.save();
        assertEquals(1, Habitation.findAll().stream().filter(g -> g.getIdBien() == habitation.getIdBien()).count());
        assertEquals(habitation.getIdBien(), Habitation.findAll().getFirst().getIdBien());
        assertEquals(habitation.getNumeroFiscal(), Habitation.findAll().getFirst().getNumeroFiscal());
        habitation.delete();
    }

    @Test
    public void testSaveModifyId() throws Bien.BienException {
        Habitation habitation = new Habitation.HBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, TestImmeuble.IMMEUBLE, SURFACE,IDPROPRIO, DATE).build();
        habitation.save();
        assertNotEquals(-1, habitation.getIdBien());
        habitation.delete();
    }

    @Test
    public void testSavingAndDeletingInstance() throws Bien.BienException {
        Habitation habitation = new Habitation.HBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, TestImmeuble.IMMEUBLE, SURFACE,IDPROPRIO, DATE).build();
        habitation.save();
        assertNotEquals(-1, habitation.getIdBien());
        assertEquals(1, Habitation.findAll().stream().filter(g -> g.getNumeroFiscal().equals(NUMERO_FISCAL)).count());
        habitation.delete();
        assertEquals(0, Habitation.findAll().stream().filter(g -> g.getNumeroFiscal().equals(NUMERO_FISCAL)).count());
    }

    @Test
    public void testFactoryPatternInstance() throws Bien.BienException {
        Habitation habitation = new Habitation.HBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, TestImmeuble.IMMEUBLE, SURFACE,IDPROPRIO, DATE).build();
        habitation.save();
        Habitation habitation2 = Habitation.findAll().stream().filter(g -> g.getNumeroFiscal().equals(NUMERO_FISCAL)).findAny().get();
        assertEquals(habitation, habitation2);
        habitation.delete();
    }

    @Test
    public void testModifyingFields() throws Bien.BienException {
        Habitation habitation = new Habitation.HBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, TestImmeuble.IMMEUBLE, SURFACE,IDPROPRIO, DATE).build();
        habitation.save();
        habitation.setComplementAdresse("Batiment B, Appartement 87");
        habitation.setNbPieces(5);
        habitation.setNumeroFiscal("0987654321");
        habitation.setSurface(62f);
        habitation.modify();

        Habitation habitation2 = Habitation.findAll().stream().filter(g -> g.getIdBien() == habitation.getIdBien()).findFirst().get();

        assertEquals("Batiment B, Appartement 87", habitation2.getComplementAdresse());
        assertEquals(5, habitation2.getNbPieces());
        assertEquals("0987654321", habitation2.getNumeroFiscal());
        assertEquals(62f, habitation2.getSurface());

        habitation.delete();
    }

    @Test
    public void testGetters() throws Bien.BienException {
        Habitation habitation = new Habitation.HBuilder(COMPLEMENT_ADRESSE, NBPIECES, NUMERO_FISCAL, TestImmeuble.IMMEUBLE, SURFACE,IDPROPRIO, DATE).build();
        assertEquals(COMPLEMENT_ADRESSE, habitation.getComplementAdresse());
        assertEquals(NBPIECES, habitation.getNbPieces());
        assertEquals(NUMERO_FISCAL, habitation.getNumeroFiscal());
        assertEquals(TestImmeuble.IMMEUBLE, habitation.getImmeuble());
        assertEquals(SURFACE, habitation.getSurface());
        assertEquals(DATE, habitation.getDateAjout());
        assertEquals(-1, habitation.getIdBien());
        assertEquals(TypeBien.HABITATION, habitation.getTypeBien());
    }
}