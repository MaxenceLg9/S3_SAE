package net.mpvm.saeimmobilier.sql.QueryElement;

import java.sql.SQLException;

public final class UpdateQueryElement extends QueryElement<Integer> {

    public UpdateQueryElement(String query, boolean commit) throws QueryException {
        super(query,commit);
    }

    @Override
    public Integer execute() throws QueryException {
        int row;
        try {
            row = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new QueryException("Error executing query", e);
        }
        System.out.println(this.getClass().getSimpleName() + " : " + row + " rows updated");
        return row;
    }

}
