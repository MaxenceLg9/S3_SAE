package net.mpvm.saeimmobilier.sql.Query;

public interface Queryable {

    void save() throws QueryableException;
    void modify() throws QueryableException;
    void delete() throws QueryableException;

    class QueryableException extends Exception {
        public QueryableException(String message) {
            super(message);
        }
        public QueryableException(String message, Throwable cause) {
            super(message,cause);
        }

    }
}
