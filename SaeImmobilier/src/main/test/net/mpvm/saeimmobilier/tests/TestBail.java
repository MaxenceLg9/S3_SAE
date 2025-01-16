package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Bail;
import net.mpvm.saeimmobilier.modele.Bien;
import net.mpvm.saeimmobilier.modele.BienLouable;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Queryable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static net.mpvm.saeimmobilier.modele.Garage.GARAGE;
import static net.mpvm.saeimmobilier.tests.TestImmeuble.IMMEUBLE;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBail {

    public static final Date DATE_DEBUT = Date.valueOf("2021-01-01");
    public static final Date DATE_FIN = Date.valueOf("2021-12-31");
    public static final Date DATE_SIGNATURE = Date.valueOf("2021-01-01");
    public static final float LOYER = 1000;
    public static final boolean RENOUVELABLE = true;
    public static final float DEPOT_GARANTIE = 1000;
    public static final float TOTAL_CHARGES = 100;



    @BeforeAll
    public static void setUp() throws Bien.BienException {
        QueryElement.newStaticConnection();
        IMMEUBLE.save();
        GARAGE.save();
    }

    @AfterAll
    public static void setDown() throws Bien.BienException {
        IMMEUBLE.delete();
        GARAGE.delete();
        QueryElement.rollBackStaticConnection();
        QueryElement.removeStaticConnection();
    }

    @Test
    public void testInstance() throws Queryable.QbleException {
        Bail bail = new Bail(DATE_DEBUT, LOYER, RENOUVELABLE, DEPOT_GARANTIE, TOTAL_CHARGES, DATE_SIGNATURE, DATE_FIN, BienLouable.BLBuilder.getBienLouable(GARAGE.getIdBien()));
        bail.save();
    }

    @Test
    public void testAssertThrowsInstance() {
        assertThrows(Bien.BienException.class, () -> new Bail(DATE_DEBUT, LOYER, RENOUVELABLE, DEPOT_GARANTIE, TOTAL_CHARGES, DATE_SIGNATURE, DATE_FIN, BienLouable.BLBuilder.getBienLouable(IMMEUBLE.getIdBien())));
    }
}
