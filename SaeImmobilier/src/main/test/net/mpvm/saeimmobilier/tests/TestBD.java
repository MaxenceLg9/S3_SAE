package net.mpvm.saeimmobilier.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import net.mpvm.saeimmobilier.sql.Connection.BD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestBD {

    private Connection connection;

    @BeforeEach
    public void init() throws SQLException{
        connection = BD.getConnection(false);
    }

    @AfterEach
    public void close() throws SQLException{
        connection.close();
    }

    @Test
    public void testDeuxInstancesNotEquals() throws SQLException {
        Connection connection2 = BD.getConnection(false);
        assertNotEquals(connection, connection2);
    }
}
