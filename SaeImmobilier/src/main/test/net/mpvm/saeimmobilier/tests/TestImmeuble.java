package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.Immeuble;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestImmeuble {
    
    public static final String VILLE = "Toulouse";
    public static final String CODE_POSTAL= "31000";
    public static final String ADRESSE = "1 rue de la paix";
    public static final String NUMERO_FISCAL_IMMEUBLE = "6789012345";
    public static final String IDPROPRIO = "IMMEUBLE COMME JAIME";
    public static final Date DATE = Date.valueOf(LocalDate.now());

    public static final Immeuble IMMEUBLE;

    static {
        try {
            IMMEUBLE = new Immeuble.IBuilder(VILLE, CODE_POSTAL, ADRESSE, NUMERO_FISCAL_IMMEUBLE,DATE,IDPROPRIO).build();
        } catch (Bien.BienException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void setUp(){
        QueryElement.newStaticConnection();
    }

    @AfterAll
    public static void setDown(){
        QueryElement.rollBackStaticConnection();
        QueryElement.removeStaticConnection();
    }


    @Test
    public void testFactoryPatternInstance() throws Bien.BienException {
        Immeuble immeuble = new Immeuble.IBuilder(VILLE, CODE_POSTAL, ADRESSE, NUMERO_FISCAL_IMMEUBLE,DATE,IDPROPRIO).build();

        assertEquals(-1, immeuble.getIdBien());
        assertNotEquals(IMMEUBLE,immeuble);

        immeuble.save();
        assertNotEquals(-1,immeuble.getIdBien());
        Immeuble immeuble1 = Immeuble.IBuilder.getImmeuble(immeuble.getIdBien());
        assertEquals(immeuble, immeuble1);
        immeuble.delete();
        assertEquals(-1, immeuble.getIdBien());
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

        Immeuble immeuble1 = Immeuble.IBuilder.getImmeuble(id);
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