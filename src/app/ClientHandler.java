package app;

import notebook.OutBufferMessenger;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket client;
    private Controller controller;

    public ClientHandler(Socket client, Controller controller) {
        this.client = client;
        this.controller = controller;
    }

    public void run() {
        try (InputStream inputStream = client.getInputStream();
             OutputStream outputStream = client.getOutputStream()) {
            controller.setWriter(new PrintWriter(outputStream, true));
            controller.setReader(new BufferedReader(new InputStreamReader(inputStream)));
            while (controller.processRequest()) {}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
