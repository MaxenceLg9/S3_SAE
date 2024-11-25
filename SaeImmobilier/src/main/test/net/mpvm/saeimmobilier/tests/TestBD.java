package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.sql.QueryElement.QueryElement;
import net.mpvm.saeimmobilier.sql.QueryElement.SelectQueryElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.mpvm.saeimmobilier.sql.Connection.BD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestBD {

    private Connection connection;

    @BeforeEach
    public void init() throws SQLException {
        connection = BD.getConnection(false);
    }

    @AfterEach
    public void close() throws SQLException {
        connection.close();
    }

    @Test
    public void testDeuxInstancesNotEquals() throws SQLException {
        Connection connection2 = BD.getConnection(false);
        assertNotEquals(connection, connection2);
        connection2.close();
    }

    @Test
    public void testQueryElement() throws QueryElement.QueryException, SQLException {
        String sql = "SELECT * FROM Locataire";

        QueryElement<ResultSet> query = new SelectQueryElement(sql);
        ResultSet resultSet1 = query.execute();

        PreparedStatement pSt = connection.prepareStatement(sql);
        ResultSet resultSet2 = pSt.executeQuery();

        while (resultSet1.next() && resultSet2.next()) {
            for(int i = 1; i <= resultSet1.getMetaData().getColumnCount(); i++) {
                assertEquals(resultSet1.getObject(i), resultSet2.getObject(i));
            }
        }
    }
}
