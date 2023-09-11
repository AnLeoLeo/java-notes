package repository;

import db.Db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract public class AbstractRepository {

    protected Db db;

    public AbstractRepository(Db db) {
        this.db = db;

        this.initFakeData();
    }

    abstract protected void initFakeData();

    abstract protected ResultSet getResult(String where);

    abstract protected Object makeEntity(ResultSet result) throws SQLException;

    protected Object processOne(String sqlWhere, String errorText) {
        ResultSet result = null;
        try {
            result = getResult(sqlWhere);
            if (!errorText.isEmpty() && !result.next()) {
                throw new RuntimeException(errorText);
            }
            return makeEntity(result);
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка получения данных: " + exception.getMessage());
        } finally {
            db.release(result);
        }
    }

    protected void processResults(String where, List<Object> entityList) {
        ResultSet results = null;
        try {
            results = getResult(where);
            while (results.next()) {
                entityList.add(makeEntity(results));
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка получения данных: " + exception.getMessage());
        } finally {
            db.release(results);
        }
    }
}
