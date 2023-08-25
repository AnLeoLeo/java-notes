import entity.Note;
import service.NoteService;
import service.UserService;
import entity.User;

import java.util.List;

public class Notebook {
    private final NoteService noteService;
    private final UserService userService;
    private User currentUser = new User();

    public Notebook(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    public void openForUserId(int userId) {
        this.currentUser = this.userService.login(this.userService.generateFakeLoginByUserId(userId));
        if (this.currentUser.isAdmin()) {
            System.out.println("Сегодня я админ и вижу всё.\n");
        }
    }

    public void showContents() {
        List<Note> personalNotes = this.noteService.getPersonalNotes(this.currentUser);

        System.out.printf("И оказалось тут вот сколько заметок: %d\n\n", personalNotes.size());
        System.out.println(personalNotes);
    }

    public int addNote() {
        System.out.println("Добавим ещё одну");

        return this.noteService.add(
                "А такой заметки ещё не было",
                "Как-то открыл я эту программу и написал её.",
                this.currentUser.getUserId()
        );
    }

    public void shareNote(int noteId, int userId) {
        User user = this.userService.get(userId);
        if (user.getUserId() <= 0) {
            throw new RuntimeException("Пользователь не найден.");
        }
        if (user.isAdmin()) {
            System.out.println("С админом не надо делиться, он и так всё видит.");
            return;
        }
        Note note = this.noteService.get(noteId);
        if (note.getNoteId() <= 0) {
            throw new RuntimeException("Заметка не найдена.");
        }
        if (note.isPublic() || note.isDeleted()) {
            System.out.println("Такой заметкой поделиться нельзя.");
            return;
        }
        this.noteService.share(note, user);

        System.out.printf("Теперь заметку № %d видит пользователь %d.\n", noteId, userId);
    }
}
