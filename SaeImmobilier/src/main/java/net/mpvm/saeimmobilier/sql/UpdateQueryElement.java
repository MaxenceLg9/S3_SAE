package net.mpvm.saeimmobilier.sql;

import java.sql.SQLException;

public class UpdateQueryElement extends QueryElement<Integer> {

    public UpdateQueryElement(String query) throws SQLException {
        super(query);
    }

    @Override
    public Integer execute() throws SQLException {
        int row = preparedStatement.executeUpdate();
        System.out.println(row + "rows updated");
        return row;
    }

}
