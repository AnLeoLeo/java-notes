package db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatasourceProvider {
    private final static int MAX_CONNECTIONS = 3;
    private final DatasourceConfigInterface config;
    private final MysqlDataSource mysql = new MysqlDataSource();
    private final List<Connection> availableConnections = new ArrayList<>(MAX_CONNECTIONS);
    private final List<Connection> activeConnections = new ArrayList<>(MAX_CONNECTIONS);

    public DatasourceProvider(DatasourceConfigInterface config) {
        this.config = config;
        initDatasource();
        initPool();
    }

    public Connection getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            throw new RuntimeException("Нет доступных соединений к базе");
        }
        Connection connection = availableConnections.get(availableConnections.size() -1);
        availableConnections.remove(connection);
        if (isInvalidConnection(connection)) {
            connection = mysql.getConnection();
        }
        activeConnections.add(connection);

        status();
        return connection;
    }

    public void releaseConnection(Connection connection) throws SQLException {
        if (activeConnections.remove(connection)) {
            if (availableConnections.size() < MAX_CONNECTIONS && !isInvalidConnection(connection)) {
                availableConnections.add(connection);
            } else {
                connection.close();
            }
        }
        status();
    }

    private void initPool() {
        int i;
        for (i = 0; i < MAX_CONNECTIONS; i++) {
            try {
                availableConnections.add(mysql.getConnection());
            } catch (SQLException exception) {
                i--;
            }
        }
    }

    private void initDatasource() {
        mysql.setServerName(config.getServer());
        mysql.setPort(config.getPort());
        mysql.setUser(config.getUser());
        mysql.setPassword(config.getPassword());
        mysql.setDatabaseName(config.getDatabase());
    }

    private boolean isInvalidConnection(Connection connection) throws SQLException {
        return connection == null || connection.isClosed() || !connection.isValid(1);
    }

    private void status() {
        System.out.println("Использовано соединений: " + activeConnections.size());
        System.out.println("Осталось соединений: " + availableConnections.size());
    }
}
