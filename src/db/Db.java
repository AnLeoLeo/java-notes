package db;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Db {
    private final DatasourceProvider datasourceProvider;

    public Db(DatasourceProvider datasourceProvider) {
        this.datasourceProvider = datasourceProvider;
    }

    public ResultSet get(String sql) {
        try {
            return datasourceProvider.getConnection().prepareStatement(sql).executeQuery();
        } catch (SQLException exception) {
            throw new RuntimeException("Соединение с mysql недоступно: " + exception.getMessage());
        }
    }

    public void release(ResultSet results) {
        if (results != null) {
            try {
                releaseStatement(results.getStatement());
            } catch (SQLException exception) {
                throw new RuntimeException("Не удалось вернуть соединение: " + exception.getMessage());
            }
        }
    }

    public int save(String sql, Object... values) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = datasourceProvider.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            initStatementFields(preparedStatement, values);
            int result = preparedStatement.executeUpdate();
            if (result <= 0) {
                throw new RuntimeException("Ошибка сохранения данных.");
            }
            return result;
        } catch (SQLException exception) {
            throw new RuntimeException("Соединение с mysql недоступно: " + exception.getMessage());
        } finally {
            releaseStatement(preparedStatement);
        }
    }

    private void releaseStatement(Statement statement) {
        if (statement != null) {
            try {
                datasourceProvider.releaseConnection(statement.getConnection());
            } catch (SQLException exception) {
                throw new RuntimeException("Не удалось вернуть соединение: " + exception.getMessage());
            }
        }
    }

    private void initStatementFields(PreparedStatement preparedStatement, Object[] values) throws SQLException {
        int i = 1;
        for (Object field: values) {
            if (field instanceof Boolean) {
                preparedStatement.setInt(i, (boolean)field ? 1 : 0);
            } else if (field instanceof Integer) {
                preparedStatement.setInt(i, (int)field);
            } else if (field instanceof Date) {
                preparedStatement.setString(i, MysqlDate.format((Date)field));
            } else if (field instanceof String) {
                preparedStatement.setString(i, (String)field);
            } else {
                throw new RuntimeException("Неизвестный тип данных для БД.");
            }
            i++;
        }
    }
}
