package net.mpvm.saeimmobilier.tests;

import net.mpvm.saeimmobilier.sql.Query.QueryElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBiens {

    @BeforeEach
    public void setUp(){
        QueryElement.newStaticConnection();
    }

    @AfterEach
    public void setDown(){
        QueryElement.rollBackStaticConnection();
        QueryElement.removeStaticConnection();
    }

}
