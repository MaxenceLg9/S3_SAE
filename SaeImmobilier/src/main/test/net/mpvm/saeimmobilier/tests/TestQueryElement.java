package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.modele.Locataire;
import net.mpvm.saeimmobilier.sql.Connection.BD;
import net.mpvm.saeimmobilier.sql.QueryElement.QueryElement;
import net.mpvm.saeimmobilier.sql.QueryElement.SelectQueryElement;
import net.mpvm.saeimmobilier.sql.QueryElement.UpdateQueryElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
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
            selectQueryElement.close();
        }
        if(updateQueryElement != null) {
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
    public void testUpdateQueryBehaviour() throws QueryElement.QueryException, SQLException {
        updateQueryElement = new UpdateQueryElement(Locataire.INSERT_QUERY, false);
        updateQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, 'M', 5, "telephone"));
        updateQueryElement.execute();
        updateQueryElement.commit();
        updateQueryElement.close();

        selectQueryElement = new SelectQueryElement("SELECT * FROM Locataire WHERE email = ?");
        selectQueryElement.setArgs(Map.of(1, "email"));
        ResultSet rsQuery = selectQueryElement.execute();
        rsQuery.next();
        assertEquals("nom", rsQuery.getString("nom"));


        updateQueryElement = new UpdateQueryElement(Locataire.DELETE_QUERY, false);
        updateQueryElement.setArgs(Map.of(1, rsQuery.getObject("IdLocataire")));

        selectQueryElement.close();

        updateQueryElement.execute();
        updateQueryElement.commit();

    }

    @Test
    public void testUpdateWithSelectQuery() {
        assertThrows(QueryElement.QueryException.class, () -> {
            selectQueryElement = new SelectQueryElement(Locataire.INSERT_QUERY);
            selectQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, 'M', 5, "telephone"));
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
            updateQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, 'M', 5, "telephone"));
            updateQueryElement.execute();
        });
    }

    @Test
    public void testSelectQueryWithTooManyArgs() {
        assertThrows(QueryElement.QueryException.class, () -> {
            selectQueryElement = new SelectQueryElement(Locataire.SELECT_QUERY);
            selectQueryElement.setArgs(Map.of(1, "nom", 2, "prenom", 3, "email", 4, 'M', 5, "telephone"));
            selectQueryElement.execute();
        });
    }
}
