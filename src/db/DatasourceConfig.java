package db;

public class DatasourceConfig implements DatasourceConfigInterface {
    private final String server = "localhost";
    private final int port = 3307;
    private final String user = "root";
    private final String password = "root";
    private final String database = "my_notes";

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }
}
