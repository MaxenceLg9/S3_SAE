package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.modele.Garage;
import net.mpvm.saeimmobilier.modele.Immeuble;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    }

    @AfterEach
    public void setDown(){

    }

    @Test
    public void testCreatingInstance() throws Bien.BienException {
        BienLouable garage = new Garage.GBuilder(COMPLEMENT_ADRESSE,NBPIECES,NUMERO_FISCAL,new Immeuble.IBuilder(VILLE,CODE_POSTAL,ADRESSE,NUMERO_FISCAL_IMMEUBLE,DATE).build(),SURFACE,DATE).build();
        assertEquals(-1,garage.getIdBien());
        try{
            garage.save();
        }catch (Bien.BienException bienException) {
            bienException.getSqlException().printStackTrace();
            bienException.printStackTrace();
        }
    }
}