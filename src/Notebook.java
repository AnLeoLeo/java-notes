import Entity.Note;
import Repository.NoteRepository;
import Repository.UserRepository;
import Service.NoteService;
import Service.UserService;
import Entity.User;

import java.util.Date;
import java.util.List;

public class Notebook {
    final NoteRepository noteRepository;
    final UserRepository userRepository;
    final NoteService noteService;
    final UserService userService;
    private User currentUser = new User();

    public Notebook() {
        this.noteRepository = new NoteRepository();
        this.userRepository = new UserRepository();
        this.noteService = new NoteService(this.noteRepository);
        this.userService = new UserService(this.userRepository);
    }

    public void openForUserId(int userId) {
        this.currentUser = this.userService.login(this.userRepository.getLogin(userId));
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

        return this.noteService.addNote(
                "А такой заметки ещё не было",
                "Как-то открыл я эту программу и написал её.",
                this.currentUser.getUserId()
        );
    }

    public void shareNote(int noteId, int userId) {
        User user = this.userRepository.findUserById(userId);
        if (user.getUserId() <= 0) {
            throw new RuntimeException("Пользователь не найден.");
        }
        if (user.isAdmin()) {
            System.out.println("С админом не надо делиться, он и так всё видит.");
        }
        Note note = this.noteRepository.getNoteById(noteId);
        if (note.getNoteId() <= 0) {
            throw new RuntimeException("Заметка не найдена.");
        }
        if (note.isPublic() || note.isDeleted()) {
            System.out.println("Такой заметкой поделиться нельзя.");
        }
        this.noteService.shareNote(note, user);

        System.out.printf("Теперь заметку № %d видит пользователь %d.\n", noteId, userId);
    }
}
