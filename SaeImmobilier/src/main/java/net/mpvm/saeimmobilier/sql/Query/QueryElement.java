package net.mpvm.saeimmobilier.sql.Query;

import net.mpvm.saeimmobilier.sql.Connection.BD;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Map;

public abstract class QueryElement<T> implements Closeable {

    private final String query;
    protected final PreparedStatement preparedStatement;
    private final Connection connection;
    private final long nArgs;

    public QueryElement(String query, boolean commit) throws QEltException {
        this.query = query;
        nArgs = query.chars().filter(ch -> ch == '?').count();
        try {
            this.connection = BD.getConnection(commit);
            this.preparedStatement = this.prepareStatement();
        } catch (SQLException sqlException) {
            throw new QEltException("Cannot create the query : Statement or Connection problem", sqlException);
        }
    }

    public long getNArgs(){
        return this.nArgs;
    }

    protected PreparedStatement getPreparedStatement(){
        return this.preparedStatement;
    }

    protected PreparedStatement prepareStatement() throws SQLException{
        return connection.prepareStatement(query);
    }

    public String getQuery(){
        return this.query;
    }

    /**
     * @return boolean : true if the connection & statement is closed from {@code QueryElement} too, false otherwise
     * @parameters none
     * @description check if the QueryElement<T> is closed
     * @throws QEltException
     */
    public boolean isClosed() throws QEltException {
        try{
            return this.connection.isClosed() && this.preparedStatement.isClosed();
        }
        catch(SQLException sqlException){
            throw new QEltException("error when checking if the queryElement is closed", sqlException);
        }
    }

    public QueryElement<T> setArgs(Map<Integer,Object> args) throws QEltException {
        if(args.size() != nArgs)
            throw new QEltException("Error, wrong number of args");
        for(Map.Entry<Integer,Object> entry : args.entrySet()) {
            try {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
            } catch (SQLException sqlException) {
                throw new QEltException("Error setting args", sqlException);
            }
        }
        return this;
    }

    public void close() throws QEltException {
        try {
            if(preparedStatement != null)
                preparedStatement.close();
            if(connection != null)
                connection.close();
        } catch (SQLException sqlException) {
            throw new QEltException("Error closing connection or Statement", sqlException);
        }
    }

    public void rollback() throws QEltException {
        try {
            this.connection.rollback();
        } catch (SQLException sqlException) {
            throw new QEltException("Error rolling back", sqlException);
        }
    }

    public void rollback(Savepoint savepoint) throws QEltException {
        try {
            this.connection.rollback(savepoint);
        } catch (SQLException sqlException) {
            throw new QEltException("Error rolling back to the savepoint", sqlException);
        }
    }

    public Savepoint savePoint() throws QEltException {
        try {
            return this.connection.setSavepoint();
        } catch (SQLException sqlException) {
            throw new QEltException("Error creating savepoint", sqlException);
        }
    }

    public void commit() throws QEltException {
        try{
            connection.commit();
        }
        catch (SQLException e) {
            throw new QEltException("Error committing", e);
        }
    }

    public abstract T execute() throws QEltException;

    public static class QEltException extends IOException {

        private final SQLException sqlException;
        public QEltException(String message) {
            this(message, null);
        }

        public QEltException(String message, SQLException sqlException){
            super(message);
            this.sqlException = sqlException;
        }

        public SQLException getSqlException(){
            return this.sqlException == null ? new SQLException("No SQL Exception") : this.sqlException;
        }

    }

}