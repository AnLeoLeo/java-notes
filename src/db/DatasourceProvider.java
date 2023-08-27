package db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatasourceProvider {
    private final MysqlDataSource mysql;
    private Connection connection;

    public DatasourceProvider(DatasourceConfigInterface config) {
        mysql = new MysqlDataSource();
        mysql.setServerName(config.getServer());
        mysql.setPort(config.getPort());
        mysql.setUser(config.getUser());
        mysql.setPassword(config.getPassword());
        mysql.setDatabaseName(config.getDatabase());
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(1)) {
            connection = mysql.getConnection();
        }

        return connection;
    }
}
