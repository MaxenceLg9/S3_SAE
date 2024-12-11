package net.mpvm.saeimmobilier.sql.Query;

import java.sql.SQLException;

public interface Queryable {

    void save() throws QueryableException;
    void modify() throws QueryableException;
    void delete() throws QueryableException;

    class QueryableException extends QueryElement.QueryException {

        public QueryableException(String message) {
            super(message);
        }
        public QueryableException(String message, SQLException sqlException) {
            super(message,sqlException);
        }

    }
}
