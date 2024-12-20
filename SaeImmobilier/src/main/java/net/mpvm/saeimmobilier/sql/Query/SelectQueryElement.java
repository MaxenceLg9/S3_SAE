package net.mpvm.saeimmobilier.sql.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SelectQueryElement extends QueryElement<ResultSet> {

    private final PreparedStatement fakeStatement;
    private ResultSet rs;

    public SelectQueryElement(String query) throws QEltException {
        super(query, false);
        try {
            fakeStatement = this.prepareStatement();
        } catch (SQLException e) {
            throw new QEltException("Error creating fake statement", e);
        }
        //methods to get the number of lines obtained by the query

    }

    @Override
    public QueryElement<ResultSet> setArgs(Map<Integer,Object> args) throws QEltException {
        //adding the args for the fake query
        if(args.size() != getNArgs())
            throw new QEltException("Error, wrong number of args");
        for(Map.Entry<Integer,Object> entry : args.entrySet())
            try {
                fakeStatement.setObject(entry.getKey(), entry.getValue());
            } catch (SQLException sqlException) {
                throw new QEltException("Error setting args", sqlException);
            }
        //executing the overrided method
        return super.setArgs(args);
    }

    @Override
    public ResultSet execute() throws QEltException {
        //TODO : migration to getResult and map instead of ResultSet with full Exception type SQL
        //execute the preparedStatement with the query
        try {
            rs = this.getPreparedStatement().executeQuery();
        } catch (SQLException e) {
            throw new QEltException("Error, select query can't be used to modify the database", e);
        }
        //print the number of rows obtained by the query
        System.out.println(this.getClass().getSimpleName() + " : " + setRowCount() + " rows selected");
        return rs;
    }

    private int setRowCount() throws QEltException {
        //get the false resultset
        try(ResultSet rs2 = this.executeFakeStatement()) {
            int rows = 0;
            while (rs2.next()) {
                rows++;
            }
            //closing the set
            rs2.close();
            //count the number of lines
            return rows;
        } catch (SQLException e) {
            throw new QEltException("Error setting row count", e);
        }
    }

    public ResultSet getResultSet() throws QEltException {
        if(rs == null)
            throw new QEltException("Result is null, maybe you should try executing the query first");
        try{
            if(rs.isClosed())
                throw new QEltException("ResultSet is closed");
        }
        catch(SQLException e){
            throw new QEltException("Error : cannot get the resultSet");
        }
        return this.rs;
    }

    public List<Map<String,Object>> getResult() throws QEltException {

        List<Map<String, Object>> rows = new ArrayList<>();
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
        }
        catch(SQLException sqlException){
            throw new QEltException("Error getting the result", sqlException);
        }
        return rows;
    }

    public void close() throws QEltException {
        try {
            if(rs != null)
                rs.close();
            super.close();
        }catch (SQLException e){
            throw new QEltException("Error closing resultSet", e);
        }
    }

    /**
     * @return boolean : true if the resultSet is closed && the superclass {@code QueryElement} too, false otherwise
     * @parameters none
     * @description check if the SelectQueryElement is closed
     * @throws QEltException
     */
    public boolean isClosed() throws QEltException {
        try {
            return super.isClosed() && rs.isClosed();
        }catch(SQLException s){
            throw new QEltException("Error checking if the ResultSet is closed", s);
        }
    }

    private ResultSet executeFakeStatement() throws SQLException {
        return fakeStatement.executeQuery();
    }
}
