package net.mpvm.saeimmobilier.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public abstract class QueryElement<T> {

    private final String query;
    final PreparedStatement preparedStatement;

    public QueryElement(String query) throws SQLException {
        this.query = query;
        this.preparedStatement = BD.prepareStatement(query);
    }

    public String getQuery(){
        return this.query;
    }

    public QueryElement<T> addArgs(Map<Integer,Object> args){
        for(Map.Entry<Integer,Object> entry : args.entrySet()){
            try {
                preparedStatement.setObject(entry.getKey(), entry.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public abstract T execute() throws SQLException;
}
