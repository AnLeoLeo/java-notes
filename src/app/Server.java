package app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final Controller controller;

    public Server(Controller controller) {
        this.controller = controller;
    }

    public void start() throws IOException {
        try (ServerSocket socket = new ServerSocket(8080)) {
            while (true) {
                Socket client = socket.accept();
                Thread thread = new Thread(new ClientHandler(client, controller));
                thread.start();
            }
        }
    }
}
