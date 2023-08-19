package Entity;

import java.util.Date;

public class Note implements Cloneable {
    private int noteId = 0;
    private int ownerId = 0;
    private String title = "";
    private String text = "";
    private boolean isPublic = false;
    private boolean isDeleted = false;
    private Date created = new Date();
    private Date modified = new Date();

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "Заметка № " + this.getNoteId() + " с названием " +
                "'" + this.getTitle() + "':\n" +
                ">> " + this.getText() + "\n" +
                "(последний раз менялась " + this.getModified() + ").\n\n";
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException("Ошибка клонирования Note");
        }
    }
}
