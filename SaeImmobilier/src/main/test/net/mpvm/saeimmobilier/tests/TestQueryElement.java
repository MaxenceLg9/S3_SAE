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
        if(selectQueryElement != null) {
            selectQueryElement.rollback();
            selectQueryElement.close();
        }
        if(updateQueryElement != null) {
            updateQueryElement.rollback();
            updateQueryElement.close();
        }
    }

    @Test
    public void testUpdateWithSelectQuery() {
        assertThrows(QueryElement.QueryException.class, () -> {
            selectQueryElement = new SelectQueryElement(Locataire.INSERT_QUERY);
            selectQueryElement.addArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, 'M', 5, "telephone"));
            selectQueryElement.execute();
        });
    }

    @Test
    public void testSelectWithUpdateQuery() {
        assertThrows(QueryElement.QueryException.class, () -> {
            updateQueryElement = new UpdateQueryElement(Locataire.SELECT_QUERY, false);
            updateQueryElement.execute();
        });
    }

    @Test
    public void testUpdateQueryWithTooManyArgs() {
        assertThrows(QueryElement.QueryException.class, () -> {
            updateQueryElement = new UpdateQueryElement(Locataire.SELECT_QUERY, false);
            updateQueryElement.addArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, 'M', 5, "telephone"));
            updateQueryElement.execute();
        });
    }

    @Test
    public void testSelectQueryWithTooManyArgs() {
        assertThrows(QueryElement.QueryException.class, () -> {
            selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
            selectQueryElement.addArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, 'M', 5, "telephone"));
            selectQueryElement.execute();
        });
    }
}
