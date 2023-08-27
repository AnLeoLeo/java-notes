package db;

public interface DatasourceConfigInterface {
    String getServer();
    int getPort();
    String getUser();
    String getPassword();
    String getDatabase();
}
