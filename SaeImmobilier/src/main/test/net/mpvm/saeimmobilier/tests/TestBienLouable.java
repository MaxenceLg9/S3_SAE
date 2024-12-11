package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.modele.Garage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

public class TestBienLouable {

    public static final String COMPLEMENT_ADRESSE = "Batiment A, Appartement 86";
    public static final String VILLE = "Toulouse";
    public static final int CODE_POSTAL= 31000;
    public static final String ADRESSE = "1 rue de la paix";
    public static final String NUMERO_FISCAL = "1234567890";
    public static final int NBPIECES = 2;
    public static final float SURFACE = 2;
    public static final Date DATE = Date.valueOf(LocalDate.now());



    @BeforeEach
    public void setUp(){

    }

    @AfterEach
    public void setDown(){

    }

    @Test
    public void testCreatingInstance(){
        BienLouable garage = new Garage(COMPLEMENT_ADRESSE,VILLE,CODE_POSTAL,ADRESSE,NBPIECES,NUMERO_FISCAL,null,SURFACE,DATE);
    }
}