package net.mpvm.saeimmobilier.sql.Query;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

public abstract class Queryable {

    public abstract void save() throws QbleException;
    public abstract void modify() throws QbleException;
    public abstract void delete() throws QueryElement.QEltException;
    public abstract void archiver() throws QbleException;

    //fonction pour renvoyer la dernière clée générée (PRIMAREY KEY : AUTO_INCREMENT)
    public int lastID(UpdateQueryElement updateQueryElement) throws QbleException{
        try {
            return ((BigInteger) updateQueryElement.getGeneratedKeys().getFirst().get("GENERATED_KEY")).intValue();
        } catch (QueryElement.QEltException e) {
            throw new Queryable.QbleException("Impossible de sauvegarder le document",null);
        }
    }


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
