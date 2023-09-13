package notebook;

public class SoutMessenger implements Notebook.Messenger {

    public void println(String text) {
        System.out.println(text);
    }
}
