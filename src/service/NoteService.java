package service;

import entity.Note;
import entity.Share;
import entity.User;
import repository.NoteRepository;

import java.util.ArrayList;

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

    public int add(String title, String text, int userId) {
        Note userNote = new Note(userId, title);
        userNote.setText(text);

        int noteId = this.noteRepository.saveNote(userNote);
        if (noteId <= 0) {
            throw new RuntimeException("Не удалось сохранить заметку.");
        }

        return noteId;
    }

    public void share(Note note, User user) {
        Share share = new Share(note.getNoteId(), user.getUserId());
        if (!this.noteRepository.saveShare(share)) {
            throw new RuntimeException("Не удалось поделиться заметкой.");
        }
    }

    public Note get(int noteId) {
        return this.noteRepository.getNoteById(noteId);
    }
}