package net.mpvm.saeimmobilier.sql.Query;

import java.sql.SQLException;
import java.util.List;

public abstract class Queryable {

    public abstract void save() throws QbleException;
    public abstract void modify() throws QbleException;
    public abstract void delete() throws QbleException;


    public static List<? extends Queryable> findAll() throws QbleException {
        throw new QbleException("findAll() not implemented");
    }

    public abstract static class Builder {

        public abstract Queryable build() throws Queryable.QbleException;
    }

    public static class QbleException extends QueryElement.QEltException {

        public QbleException(String message) {
            this(message,null);
        }

        public QbleException(String message, SQLException sqlException) {
            super(message,sqlException);
        }
    }
}
