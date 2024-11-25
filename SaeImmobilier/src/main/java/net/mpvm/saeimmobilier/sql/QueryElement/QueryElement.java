package net.mpvm.saeimmobilier.sql.QueryElement;

import net.mpvm.saeimmobilier.sql.Connection.BD;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public abstract class QueryElement<T> implements Closeable {

    private final String query;
    protected final PreparedStatement preparedStatement;
    private final Connection connection;
    private final long nArgs;

    public QueryElement(String query, boolean commit) throws QueryException {
        this.query = query;
        nArgs = query.chars().filter(ch -> ch == '?').count();
        try {
            this.connection = BD.getConnection(commit);
            this.preparedStatement = this.prepareStatement();
        } catch (SQLException e) {
            throw new QueryException("Error creating connection or Statement", e);
        }
    }

    public long getArgs(){
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

    public QueryElement<T> addArgs(Map<Integer,Object> args) throws QueryException {
        if(args.size() != nArgs)
            throw new QueryException("Error, wrong number of args");
        for(Map.Entry<Integer,Object> entry : args.entrySet()) {
            try {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
            } catch (SQLException e) {
                throw new QueryException("Error setting args", e);
            }
        }
        return this;
    }

    public void close() throws QueryException {
        try {
            if(preparedStatement != null)
                preparedStatement.close();
            if(connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new QueryException("Error closing connection or Statement", e);
        }
    }

    public void rollback() throws QueryException {
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new QueryException("Error rolling back", e);
        }
    }

    public abstract T execute() throws QueryException;

    public static class QueryException extends IOException {
        public QueryException(String message, Throwable cause) {
            super(message, cause);
        }
        public QueryException(String message) {
            super(message);
        }
    }
}
