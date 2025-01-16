package net.mpvm.saeimmobilier.sql.Query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public final class SelectQueryElement extends QueryElement<Result> {

    private Result rows;

    public SelectQueryElement(String query) throws QEltException {
        super(query, false);
        //methods to get the number of lines obtained by the query
    }

    @Override
    public Result execute() throws QEltException {
        //execute the preparedStatement with the query
        return getResult(executeRs());
    }

    private ResultSet executeRs() throws QEltException {
        try {
            return this.preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new QEltException("Error, select query can't be used to modify the database", e);
        }
        //print the number of rows obtained by the query
    }

    public Result getResult() throws QEltException {
        if(rows == null)
            throw new QEltException("Error, le resultat n'existe pas, essayez d'exécuter la requête");
        return rows;
    }

    private Result getResult(ResultSet rs) throws QEltException {
        if(rows != null)
            return rows;
        rows = new Result();
        resultSetIntoResult(rs, rows);
        System.out.println(rows.size() + " rows selected");
        return rows;
    }
}
