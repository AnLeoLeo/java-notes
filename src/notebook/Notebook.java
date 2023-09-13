package notebook;

import entity.Note;
import service.NoteService;
import service.UserService;
import entity.User;

import java.util.List;

public class Notebook {
    private final NoteService noteService;
    private final UserService userService;
    private User currentUser;
    private Messenger messenger;

    public Notebook(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    public interface Messenger {
        void println(String text);
    }

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }

    public void openForUserId(int userId) {
        this.currentUser = this.userService.login(this.userService.generateFakeLoginByUserId(userId));
        if (this.currentUser.isAdmin()) {
            messenger.println("Сегодня я админ и вижу всё.\n");
        }
    }

    public void showContents() {
        List<Note> personalNotes = this.noteService.getPersonalNotes(this.currentUser);

        messenger.println(String.format("И оказалось тут вот сколько заметок: %d\n\n", personalNotes.size()));
        messenger.println(personalNotes.toString());
    }

    public void addNote(String title, String text) {
        messenger.println("Добавим ещё одну");

        this.noteService.add(title, text, this.currentUser.getUserId());
    }

    public void shareNote(int noteId, int userId) {
        User user = this.userService.get(userId);
        if (user.getUserId() <= 0) {
            throw new RuntimeException("Пользователь не найден.");
        }
        if (user.isAdmin()) {
            messenger.println("С админом не надо делиться, он и так всё видит.");
            return;
        }
        Note note = this.noteService.get(noteId);
        if (note.isPublic() || note.isDeleted()) {
            messenger.println("Такой заметкой поделиться нельзя.");
            return;
        }
        this.noteService.share(note, user);

        messenger.println(String.format("Теперь заметку № %d видит пользователь %d.\n", noteId, userId));
    }
}
