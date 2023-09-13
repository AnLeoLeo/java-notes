package notebook;

import java.io.PrintWriter;

public class OutBufferMessenger implements Notebook.Messenger {
    private final PrintWriter printWriter;

    public OutBufferMessenger(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }
    public void println(String text) {
        printWriter.println(text);
    }
}
