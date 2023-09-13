import app.DI;
import app.Server;

public class Main {
    public static void main(String[] args) {
        try {
            DI registry = new DI();

            Server server = (Server)registry.getInstance(Server.class.getName());
            server.start();

        } catch (Exception exception) {
            System.out.printf("Записная книжка не открывается: %s\n", exception.getMessage());
        }
    }
}