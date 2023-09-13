package app;

import notebook.Notebook;
import notebook.OutBufferMessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final Notebook notebook;
    private BufferedReader reader;
    private PrintWriter writer;

    public Controller(Notebook notebook) {
        this.notebook = notebook;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
        notebook.setMessenger(new OutBufferMessenger(this.writer));
    }

    public boolean processRequest() throws IOException {
        writer.println("\n\nВведите команду: [login|show|add|share|exit] ");
        List<String> parameters;
        switch (reader.readLine()) {
            case "login" -> {
                parameters = collectParameters("Введите ID пользователя");
                notebook.openForUserId(Integer.parseInt(parameters.get(0)));
            }
            case "show" -> notebook.showContents();
            case "add" -> {
                parameters = collectParameters("Введите заголовок", "Введите текст");
                notebook.addNote(parameters.get(0), parameters.get(1));
            }
            case "share" -> {
                parameters = collectParameters("Введите ID заметки", "Введите ID пользователя");
                notebook.shareNote(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)));
            }
            case "exit" -> {
                return false;
            }
            default -> {
                writer.println("\n\nНеизвестная команда");
            }
        }
        return true;
    }

    private List<String> collectParameters(String ... labels) throws IOException {
        List<String> parameters = new ArrayList<>();
        for (String label : labels) {
            writer.println("\n" + label);
            parameters.add(reader.readLine());
        }
        return parameters;
    }
}
