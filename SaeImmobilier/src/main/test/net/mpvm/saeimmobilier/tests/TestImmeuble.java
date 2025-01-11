package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.Immeuble;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestImmeuble {

    public static final String COMPLEMENT_ADRESSE = "Batiment A, Appartement 86";
    public static final String VILLE = "Toulouse";
    public static final String CODE_POSTAL= "31000";
    public static final String ADRESSE = "1 rue de la paix";
    public static final String NUMERO_FISCAL = "1234567890";
    public static final String NUMERO_FISCAL_IMMEUBLE = "6789012345";
    public static final String IDPROPRIO = "IMMEUBLE COMME JAIME";
    public static final int NBPIECES = 2;
    public static final float SURFACE = 2;
    public static final Date DATE = Date.valueOf(LocalDate.now());


    @Test
    public void testFactoryPatternInstance() throws Bien.BienException {
        Immeuble immeuble = new Immeuble.IBuilder(VILLE, CODE_POSTAL, ADRESSE, NUMERO_FISCAL_IMMEUBLE,DATE,IDPROPRIO).build();
        immeuble.save();
        Immeuble immeuble1 = new Immeuble.IBuilder(immeuble.getIdBien()).build();
        assertEquals(immeuble, immeuble1);
        immeuble.delete();
    }

    @Test
    public void testModifyImmeuble() throws Bien.BienException {
        Immeuble immeuble = new Immeuble.IBuilder(VILLE, CODE_POSTAL, ADRESSE, NUMERO_FISCAL_IMMEUBLE,DATE,IDPROPRIO).build();
        immeuble.save();

        immeuble.setIdProprio("AAAAAAAAAAAA");
        immeuble.setVille("LAVILLE");
        immeuble.setCodePostal("41025");
        immeuble.setNumeroFiscal("521425315");
        immeuble.modify();

        int id = immeuble.getIdBien();

        immeuble = null;

        Immeuble immeuble1 = new Immeuble.IBuilder(id).build();
        assertEquals(id, immeuble1.getIdBien());
        assertEquals("LAVILLE",immeuble1.getVille());
        assertEquals("41025",immeuble1.getCodePostal());
        assertEquals("521425315",immeuble1.getNumeroFiscal());
        immeuble1.delete();
    }

    @Test
    public void testGetImmeuble(){

    }
}