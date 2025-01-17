package net.mpvm.saeimmobilier.sql.Query;

import net.mpvm.saeimmobilier.sql.Connection.BD;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class QueryElement<T> implements Closeable {

    private final String query;
    protected final PreparedStatement preparedStatement;
    private final Connection connection;
    private final long nArgs;

    private static Connection StaticConnection;

    public QueryElement(String query, boolean commit) throws QEltException {
        this.query = query;
        nArgs = query.chars().filter(ch -> ch == '?').count();
        try {
            if(StaticConnection != null)
                this.connection = StaticConnection;
            else
                this.connection = BD.getConnection(commit);
            this.preparedStatement = this.prepareStatement();
        } catch (SQLException sqlException) {
            throw new QEltException("Cannot create the query : Statement or Connection problem", sqlException);
        }
    }



    public static void newStaticConnection(){
        try {
            StaticConnection = BD.getConnection(false);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void rollBackStaticConnection() {
        try {
            StaticConnection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeStaticConnection(){
        try {
            StaticConnection.close();
            StaticConnection = null;
            System.out.println("Closed static connection");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    public long getNArgs(){
        return this.nArgs;
    }

    protected PreparedStatement getPreparedStatement(){
        return this.preparedStatement;
    }

    protected PreparedStatement prepareStatement() throws SQLException{
        return connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
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
            if(StaticConnection != null)
                return this.preparedStatement.isClosed();
            else
                return this.preparedStatement.isClosed() && this.connection.isClosed();
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
            if(connection != null && StaticConnection == null)
                connection.close();
        } catch (SQLException sqlException) {
            throw new QEltException("Error closing connection or Statement", sqlException);
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

    void resultSetIntoResult(ResultSet rs, Result rows) throws QEltException {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                rows.add(row);
            }
            rs.close();
        }catch(SQLException sqlException){
            throw new QEltException("Error getting the result", sqlException);
        }
    }

    public Result getGeneratedKeys() throws QEltException {
        Result result = new Result();
        try {
            resultSetIntoResult(this.preparedStatement.getGeneratedKeys(),result);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return result;
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