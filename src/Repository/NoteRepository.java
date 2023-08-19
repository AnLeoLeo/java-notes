package Repository;

import Entity.Note;
import Entity.Share;
import Entity.User;

import java.util.ArrayList;
import java.util.Date;

public class NoteRepository {

    protected ArrayList<Note> noteRegistry = new ArrayList<>();
    protected ArrayList<Share> sharedNotes = new ArrayList<>();

    public NoteRepository() {
        this.initFakeData();
    }

    public ArrayList<Note> getAllNotes() {
        return this.noteRegistry;
    }

    public Note getNoteById(int noteId) {
        // TODO find note for sharing
        Note foundNote = new Note();
        foundNote.setNoteId(noteId);
        return foundNote;
    }

    public int saveNote(Note note) {
        Note newNote = (Note)note.clone();
        newNote.setNoteId(this.noteRegistry.size() + 1);
        this.noteRegistry.add(newNote);
        return newNote.getNoteId();
    }

    public boolean saveShare(Share share) {
        this.sharedNotes.add(share);
        return true;
    }

    protected void initFakeData() {
        for (int i = 1; i <= 30; i++) {
            Note extraNote = new Note();
            extraNote.setOwnerId((int)Math.round(Math.random() * 10));
            extraNote.setDeleted(Math.random() < 0.5);
            extraNote.setPublic(Math.random() < 0.1);
            extraNote.setCreated(new Date(this.getRandomTimestamp()));
            extraNote.setModified(new Date(this.getRandomTimestamp()));
            extraNote.setTitle(String.format("Заметка от %s", extraNote.getCreated()));
            if (extraNote.isDeleted()) {
                extraNote.setText("А текст удалил");
            } else {
                extraNote.setText(String.format("Как-то раз, %s, написал я эту заметку для %s.", extraNote.getCreated(), extraNote.isPublic() ? "всех" : "себя"));
            }
            this.saveNote(extraNote);
        }
    }

    protected long getRandomTimestamp() {
        return (long)(1600000000 + Math.floor(Math.random() * 100000000)) * 1000;
    }

    public boolean hasShare(Note currentNote, User user) {
        for (Share share : this.sharedNotes) {
            if (share.getNoteId() == currentNote.getNoteId() && share.getUserId() == user.getUserId()) {
                return true;
            }
        }

        return false;
    }
}
