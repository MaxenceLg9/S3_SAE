package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class TestBienLouable {

    public static final String COMPLEMENT_ADRESSE = "Batiment A, Appartement 86";
    public static final String VILLE = "Toulouse";
    public static final int CODE_POSTAL= 31000;
    public static final String ADRESSE = "1 rue de la paix";
    public static final String NUMERO_FISCAL = "1234567890";
    public static final String NUMERO_FISCAL_IMMEUBLE = "6789012345";
    public static final int NBPIECES = 2;
    public static final float SURFACE = 2;
    public static final Date DATE = Date.valueOf(LocalDate.now());



    @BeforeEach
    public void setUp(){
        QueryElement.newStaticConnection();
    }

    @AfterEach
    public void setDown(){
        QueryElement.rollBackStaticConnection();
        QueryElement.removeStaticConnection();
    }

    @Test
    public void testFindAll() throws Bien.BienException {
        List<? extends BienLouable> biens = BienLouable.findAll();
    }
}