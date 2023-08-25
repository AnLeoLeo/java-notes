package entity;

import java.util.Date;

public class Share {
    private int userId;
    private int noteId;
    private Date modified = new Date();

    public Share(int noteId, int userId) {
        this.setUserId(userId);
        this.setNoteId(noteId);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
