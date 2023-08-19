package Service;

import Entity.Note;
import Entity.Share;
import Entity.User;
import Repository.NoteRepository;

import java.util.ArrayList;
import java.util.Date;

public class NoteService {

    protected NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public ArrayList<Note> getPersonalNotes(User user) {
        ArrayList<Note> personalNotes = new ArrayList<>();

        for (Note currentNote : this.noteRepository.getAllNotes()) {
            if (currentNote.isDeleted()) {
                continue;
            }
            if (currentNote.isPublic() || currentNote.getOwnerId() == user.getUserId()) {
                personalNotes.add(currentNote);
            }
            if (user.isAdmin()) {
                personalNotes.add(currentNote);
            }
            if (this.noteRepository.hasShare(currentNote, user)) {
                personalNotes.add(currentNote);
            }
        }

        return personalNotes;
    }

    public int addNote(String title, String text, int userId) {
        Note userNote = new Note();
        userNote.setOwnerId(userId);
        userNote.setCreated(new Date());
        userNote.setModified(new Date());
        userNote.setTitle(title);
        userNote.setText(text);

        int noteId = this.noteRepository.saveNote(userNote);
        if (noteId <= 0) {
            throw new RuntimeException("Не удалось сохранить заметку.");
        }

        return noteId;
    }

    public void shareNote(Note note, User user) {
        Share share = new Share();
        share.setNoteId(note.getNoteId());
        share.setUserId(user.getUserId());
        if (!this.noteRepository.saveShare(share)) {
            throw new RuntimeException("Не удалось поделиться заметкой.");
        }
    }
}
