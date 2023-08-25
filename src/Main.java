public class Main {
    public static void main(String[] args) {
        int firstUserId = (int)Math.floor(Math.random() * 10);
        int secondUserId = (int)Math.floor(Math.random() * 10);

        try {
            DI registry = new DI();

            Notebook notebook = (Notebook)registry.getInstance(Notebook.class.getName());

            // Открыли для пользователя 1.
            notebook.openForUserId(firstUserId);
            notebook.showContents();
            // Добавили заметку.
            int noteId = notebook.addNote();
            notebook.showContents();
            // Поделились заметкой с пользователем 2.
            notebook.shareNote(noteId, secondUserId);
            // Открыли для пользователя 2.
            notebook.openForUserId(secondUserId);
            notebook.showContents();

        } catch (RuntimeException exception) {
            System.out.printf("Записная книжка не открывается: %s\n", exception.getMessage());
        }
    }
}