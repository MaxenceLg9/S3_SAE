package net.mpvm.saeimmobilier.sql.Query;

import java.sql.SQLException;

public final class UpdateQueryElement extends QueryElement<Integer> {

    public UpdateQueryElement(String query, boolean commit) throws QEltException {
        super(query,commit);
    }

    @Override
    public Integer execute() throws QEltException {
        int row;
        try {
            row = preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new QEltException("Error executing query", sqlException);
        }
        System.out.println(this.getClass().getSimpleName() + " : " + row + " rows updated");
        System.out.println("Query : " + this.getQuery());
        return row;
    }

}
