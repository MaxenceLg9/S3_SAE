package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.Connection.BD;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import net.mpvm.saeimmobilier.sql.Query.Result;
import net.mpvm.saeimmobilier.sql.Query.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.Query.UpdateQueryElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueryElement {

    private SelectQueryElement selectQueryElement;
    private UpdateQueryElement updateQueryElement;

    @BeforeEach
    public void init() {
        selectQueryElement = null;
        updateQueryElement = null;
    }

    @AfterEach
    public void close() throws QueryElement.QEltException {
        if(selectQueryElement != null && !selectQueryElement.isClosed()) {
            selectQueryElement.close();
        }
        if(updateQueryElement != null && !updateQueryElement.isClosed()) {
            updateQueryElement.rollback();
            updateQueryElement.close();
        }
    }

    @Test
    public void testSelectQueryBehaviour() throws QueryElement.QEltException, SQLException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        Result result = selectQueryElement.execute();

        Connection connection = BD.getConnection(false);
        ResultSet rs = connection.prepareStatement(Locataire.SELECT_QUERY).executeQuery();
        int i = 0;
        while(rs.next() && i < result.size())
            for(int x = 0; x < Math.min(rs.getMetaData().getColumnCount(),result.get(i).size()); x++) {
                Map<String,Object> row = result.get(i);
                String c1 = rs.getMetaData().getColumnName(x + 1);
                String c2 = row.keySet().toArray()[x].toString();

                assertEquals(c1,c2);
                assertEquals(rs.getObject(c1), row.get(c2));
            }
        assertTrue(i == result.size() && !rs.next());
    }

    @Test
    public void testUpdateQueryBehaviour() throws QueryElement.QEltException, SQLException {
        updateQueryElement = new UpdateQueryElement(Locataire.INSERT_QUERY, false);
        updateQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, "M", 5, "telephone"));
        updateQueryElement.execute();
        updateQueryElement.commit();
        updateQueryElement.close();

        selectQueryElement = new SelectQueryElement("SELECT * FROM Locataire WHERE email = ?");
        selectQueryElement.setArgs(Map.of(1, "email"));
        Map<String,Object> row = selectQueryElement.execute().getFirst();
        assertEquals("nom", row.get("Nom"));

        Integer id = (int) row.get("IdLocataire");
        selectQueryElement.close();

        updateQueryElement = new UpdateQueryElement(Locataire.DELETE_QUERY, false);
        updateQueryElement.setArgs(Map.of(1, id));
        updateQueryElement.execute();
        updateQueryElement.commit();

    }

    @Test
    public void testExecuteUpdateWithSelectQuery() throws QueryElement.QEltException {
        selectQueryElement = new SelectQueryElement(Locataire.INSERT_QUERY);
        selectQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, 'M', 4, "telephone", 5, "email"));
        assertThrows(QueryElement.QEltException.class, () -> {
            selectQueryElement.execute();
        });
    }

    @Test
    public void testExecuteSelectWithUpdateQuery() throws QueryElement.QEltException {
        updateQueryElement = new UpdateQueryElement(Locataire.SELECT_QUERY, false);
        assertThrows(QueryElement.QEltException.class, () -> {
            updateQueryElement.execute();
        });
    }

    @Test
    public void testUpdateQueryWithTooManyArgs() throws QueryElement.QEltException {
        updateQueryElement = new UpdateQueryElement(Locataire.SELECT_QUERY, false);
        assertThrows(QueryElement.QEltException.class, () -> {
            updateQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, 'M', 4, "telephone", 5, "email"));
            updateQueryElement.execute();
        });
    }

    @Test
    public void testSelectQueryWithTooManyArgs() throws QueryElement.QEltException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        assertThrows(QueryElement.QEltException.class, () -> {
            selectQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, 'M', 4, "telephone", 5, "email"));
            selectQueryElement.execute();
        });
    }

    @Test
    public void testSelectResultSetWithGet() throws QueryElement.QEltException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        assertEquals(selectQueryElement.execute(),selectQueryElement.getResult());
    }

    @Test
    public void testGettingResultSetBeforeExecute() throws QueryElement.QEltException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        assertThrows(QueryElement.QEltException.class, () -> {
            selectQueryElement.getResult();
        });
    }

    @Test
    public void testCloseQuery() throws QueryElement.QEltException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        updateQueryElement = new UpdateQueryElement(Locataire.INSERT_QUERY,false);

        selectQueryElement.execute();
        selectQueryElement.close();
        updateQueryElement.close();

        assertTrue(selectQueryElement.isClosed());
        assertTrue(updateQueryElement.isClosed());

    }

    @Test
    public void testGetNbArgs() throws QueryElement.QEltException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        updateQueryElement = new UpdateQueryElement(Locataire.INSERT_QUERY, false);

        assertEquals(selectQueryElement.getNArgs(),0);
        assertEquals(updateQueryElement.getNArgs(),5);
    }
}