package net.mpvm.saeimmobilier.sql.QueryElement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public final class SelectQueryElement extends QueryElement<ResultSet> {

    private final PreparedStatement fakeStatement;
    private ResultSet rs;

    public SelectQueryElement(String query) throws QueryException {
        super(query, false);
        //TODO : reviewed
        try {
            fakeStatement = this.prepareStatement();
        } catch (SQLException e) {
            throw new QueryException("Error creating fake statement", e);
        }
        //methods to get the number of lines obtained by the query

    }

    @Override
    public QueryElement<ResultSet> addArgs(Map<Integer,Object> args) throws QueryException {
        //adding the args for the fake query
        if(args.size() != getArgs())
            throw new QueryException("Error, wrong number of args");
        for(Map.Entry<Integer,Object> entry : args.entrySet()) {
            try {
                fakeStatement.setObject(entry.getKey(), entry.getValue());
            } catch (SQLException e) {
                throw new QueryException("Error setting args", e);
            }
        }
        //executing the overrided method
        return super.addArgs(args);
    }

    @Override
    public ResultSet execute() throws QueryException {
        //execute the preparedStatement with the query
        try {
            rs = this.getPreparedStatement().executeQuery();
        } catch (SQLException e) {
            throw new QueryException("Error, select query can't be used to modify the database", e);
        }
        //print the number of rows obtained by the query
        System.out.println(this.getClass().getSimpleName() + " : " + setRowCount() + " rows updated");
        return rs;
    }

    public int setRowCount() throws QueryException {
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
            throw new RuntimeException(e);
        }
    }

    public void close() throws QueryException {
        try {
            if(rs != null)
                rs.close();
            super.close();
        }catch (SQLException e){
            throw new QueryException("Error closing resultSet", e);
        }
    }

    private ResultSet executeFakeStatement() throws SQLException {
        return fakeStatement.executeQuery();
    }
}
