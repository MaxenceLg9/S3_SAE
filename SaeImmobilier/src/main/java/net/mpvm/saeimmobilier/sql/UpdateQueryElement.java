package net.mpvm.saeimmobilier.sql;

import java.sql.SQLException;

public class UpdateQueryElement extends QueryElement<Integer> {

    public UpdateQueryElement(String query, boolean commit) throws QueryException {
        super(query,commit);
    }

    @Override
    public Integer execute() throws QueryException {
        int row = 0;
        try {
            row = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new QueryException("Error executing query", e);
        }
        System.out.println(this.getClass().getSimpleName() + " : " + row + " rows updated");
        return row;
    }

}
