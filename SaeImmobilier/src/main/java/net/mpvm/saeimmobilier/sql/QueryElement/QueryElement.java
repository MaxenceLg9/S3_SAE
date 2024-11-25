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

    public QueryElement(String query, boolean commit) throws QueryException {
        this.query = query;
        try {
            this.connection = BD.getConnection(commit);
            this.preparedStatement = this.prepareStatement();
        } catch (SQLException e) {
            throw new QueryException("Error creating connection or Statement", e);
        }
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
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new QueryException("Error closing connection or Statement", e);
        }
    }

    public abstract T execute() throws QueryException;

    public static class QueryException extends IOException {
        public QueryException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
