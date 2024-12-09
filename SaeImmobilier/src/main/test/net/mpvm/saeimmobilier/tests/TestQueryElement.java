package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.Connection.BD;
import net.mpvm.saeimmobilier.sql.Query.QueryElement;
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
    public void close() throws QueryElement.QueryException {
        if(selectQueryElement != null && !selectQueryElement.isClosed()) {
            selectQueryElement.close();
        }
        if(updateQueryElement != null && !updateQueryElement.isClosed()) {
            updateQueryElement.rollback();
            updateQueryElement.close();
        }
    }

    @Test
    public void testSelectQueryBehaviour() throws QueryElement.QueryException, SQLException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        ResultSet rsQuery = selectQueryElement.execute();
        Connection connection = BD.getConnection(false);
        ResultSet rs1 = connection.prepareStatement(Locataire.SELECT_QUERY).executeQuery();
        while(rs1.next() && rsQuery.next())
            for(int i = 0; i < Math.min(rs1.getMetaData().getColumnCount(),rsQuery.getMetaData().getColumnCount()); i++)
                assertEquals(rs1.getObject(i + 1), rsQuery.getObject(i + 1));
    }

    @Test
    public void     testUpdateQueryBehaviour() throws QueryElement.QueryException, SQLException {
        updateQueryElement = new UpdateQueryElement(Locataire.INSERT_QUERY, false);
        updateQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, 'M', 4, "telephone", 5, "email"));
        updateQueryElement.execute();
        updateQueryElement.commit();
        updateQueryElement.close();

        selectQueryElement = new SelectQueryElement("SELECT * FROM Locataire WHERE email = ?");
        selectQueryElement.setArgs(Map.of(1, "email"));
        ResultSet rsQuery = selectQueryElement.execute();
        rsQuery.next();
        assertEquals("nom", rsQuery.getString("nom"));

        Integer id = rsQuery.getInt("IdLocataire");
        selectQueryElement.close();

        updateQueryElement = new UpdateQueryElement(Locataire.DELETE_QUERY, false);
        updateQueryElement.setArgs(Map.of(1, id));
        updateQueryElement.execute();
        updateQueryElement.commit();

    }

    @Test
    public void testExecuteUpdateWithSelectQuery() throws QueryElement.QueryException {
        selectQueryElement = new SelectQueryElement(Locataire.INSERT_QUERY);
        selectQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, 'M', 4, "telephone", 5, "email"));
        assertThrows(QueryElement.QueryException.class, () -> {
            selectQueryElement.execute();
        });
    }

    @Test
    public void testExecuteSelectWithUpdateQuery() throws QueryElement.QueryException {
        updateQueryElement = new UpdateQueryElement(Locataire.SELECT_QUERY, false);
        assertThrows(QueryElement.QueryException.class, () -> {
            updateQueryElement.execute();
        });
    }

    @Test
    public void testUpdateQueryWithTooManyArgs() throws QueryElement.QueryException {
        updateQueryElement = new UpdateQueryElement(Locataire.SELECT_QUERY, false);
        assertThrows(QueryElement.QueryException.class, () -> {
            updateQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, 'M', 4, "telephone", 5, "email"));
            updateQueryElement.execute();
        });
    }

    @Test
    public void testSelectQueryWithTooManyArgs() throws QueryElement.QueryException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        assertThrows(QueryElement.QueryException.class, () -> {
            selectQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, 'M', 4, "telephone", 5, "email"));
            selectQueryElement.execute();
        });
    }

    @Test
    public void testSelectResultSetWithGet() throws QueryElement.QueryException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        assertEquals(selectQueryElement.execute(),selectQueryElement.getResultSet());
    }

    @Test
    public void testGettingResultSetBeforeExecute() throws QueryElement.QueryException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        assertThrows(QueryElement.QueryException.class, () -> {
            selectQueryElement.getResultSet();
        });
    }

    @Test
    public void testCloseQuery() throws QueryElement.QueryException, SQLException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        updateQueryElement = new UpdateQueryElement(Locataire.INSERT_QUERY,false);

        selectQueryElement.execute();
        selectQueryElement.close();
        updateQueryElement.close();

        assertTrue(selectQueryElement.isClosed());
        assertTrue(updateQueryElement.isClosed());

    }

    @Test
    public void testGetNbArgs() throws QueryElement.QueryException {
        selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
        updateQueryElement = new UpdateQueryElement(Locataire.INSERT_QUERY, false);

        assertEquals(selectQueryElement.getNArgs(),0);
        assertEquals(updateQueryElement.getNArgs(),5);
    }

}
