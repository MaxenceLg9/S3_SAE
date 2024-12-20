package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        private BienLouable bienLouable;


        @BeforeEach
        public void setUp() throws Bien.BienException {
                Immeuble immeuble = new Immeuble.IBuilder(VILLE,CODE_POSTAL,ADRESSE, NUMERO_FISCAL_IMMEUBLE, DATE).build();
                bienLouable = new Garage.GBuilder(COMPLEMENT_ADRESSE,NBPIECES,NUMERO_FISCAL,immeuble,SURFACE,DATE).build();
        }

        @AfterEach
        public void setDown() throws BienLouable.BienLouableException {

        }

        @Test
        public void testBienLouable() throws Bien.BienException {
        }

        @Test
        public void testCreatingInstanceImmeubleNull() {
                assertThrows(BienLouable.BienLouableException.class, () -> new Habitation.HBuilder(COMPLEMENT_ADRESSE,NBPIECES,NUMERO_FISCAL_IMMEUBLE,null,SURFACE,DATE).build());
        }
}
