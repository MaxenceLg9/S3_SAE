package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.QueryElement.QueryElement;
import net.mpvm.saeimmobilier.sql.QueryElement.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.QueryElement.UpdateQueryElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestQueryElement {

    private SelectQueryElement selectQueryElement;
    private UpdateQueryElement updateQueryElement;

    @BeforeEach
    public void init() {
        selectQueryElement = null;
        updateQueryElement = null;
    }

    @AfterEach
    public void close() throws QueryElement.QueryException {
        if(selectQueryElement != null)
            selectQueryElement.close();
        if(updateQueryElement != null)
            updateQueryElement.close();
    }

    @Test
    public void testUpdateWithselectQuery() {
        assertThrows(QueryElement.QueryException.class, () -> {
            selectQueryElement = new SelectQueryElement(Locataire.INSERT_QUERY);
            selectQueryElement.addArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, 'M', 5, "telephone"));
            selectQueryElement.execute();
        });
    }
}
