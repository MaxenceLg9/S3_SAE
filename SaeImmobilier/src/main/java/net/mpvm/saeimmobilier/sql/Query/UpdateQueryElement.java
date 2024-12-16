package net.mpvm.saeimmobilier.sql.Query;

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
        } catch (SQLException sqlException) {
            throw new QueryException("Error executing query", sqlException);
        }
        System.out.println(this.getClass().getSimpleName() + " : " + row + " rows updated");
        return row;
    }

}
