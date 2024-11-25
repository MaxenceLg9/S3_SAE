package net.mpvm.saeimmobilier.tests;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import net.mpvm.saeimmobilier.sql.BD;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestBD {

    @Test
    public void testDeuxInstancesMemeAdresse(){
        try {
            Connection connection1 = BD.getConnection(false);
            Connection connection2 = BD.getConnection(false);
            assertNotEquals(connection1, connection2);
            connection2.close();
            connection1.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
