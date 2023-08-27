package service;

import entity.Note;
import entity.Share;
import entity.User;
import repository.NoteRepository;
import repository.ShareRepository;

import java.util.ArrayList;

public class NoteService {

    protected NoteRepository noteRepository;
    protected ShareRepository shareRepository;

    public NoteService(NoteRepository noteRepository, ShareRepository shareRepository) {
        this.noteRepository = noteRepository;
        this.shareRepository = shareRepository;
    }

    public ArrayList<Note> getPersonalNotes(User user) {
        ArrayList<Note> personalNotes = new ArrayList<>();

        for (Note currentNote : this.noteRepository.getAll()) {
            if (currentNote.isDeleted()) {
                continue;
            }
            if (currentNote.isPublic() || currentNote.getOwnerId().equals(user.getUserId())) {
                personalNotes.add(currentNote);
            }
            if (user.isAdmin()) {
                personalNotes.add(currentNote);
            }
            if (this.shareRepository.hasShare(currentNote, user)) {
                personalNotes.add(currentNote);
            }
        }

        return personalNotes;
    }

    public int add(String title, String text, int userId) {
        Note userNote = new Note(userId, title);
        userNote.setText(text);

        int noteId = this.noteRepository.save(userNote);
        if (noteId <= 0) {
            throw new RuntimeException("Не удалось сохранить заметку.");
        }

        return noteId;
    }

    public void share(Note note, User user) {
        Share share = new Share(note.getNoteId(), user.getUserId());
        if (!this.shareRepository.save(share)) {
            throw new RuntimeException("Не удалось поделиться заметкой.");
        }
    }

    public Note get(int noteId) {
        return this.noteRepository.getById(noteId);
    }
}