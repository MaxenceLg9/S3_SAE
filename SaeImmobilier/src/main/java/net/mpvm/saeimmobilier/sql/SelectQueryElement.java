package net.mpvm.saeimmobilier.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectQueryElement extends QueryElement<ResultSet> {

    public SelectQueryElement(String query) throws SQLException {
        super(query);
    }

    @Override
    public ResultSet execute() throws SQLException {
        ResultSet rs = preparedStatement.executeQuery();
        System.out.println(rs.getFetchSize());
        return rs;
    }
}
