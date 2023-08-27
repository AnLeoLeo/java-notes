package repository;

import db.Db;
import entity.Note;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class NoteRepository extends AbstractRepository {

    public NoteRepository(Db db) {
        super(db);
    }

    public List<Note> getAll() {
        try (ResultSet results = getResult()) {
            List<Note> entityList = new ArrayList<>();
            while (results.next()) {
                entityList.add(makeEntity(results));
            }

            return entityList;
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка получения данных: " + exception.getMessage());
        }
    }

    public Note getById(int noteId) {
        try (ResultSet results = getResult("`noteId` = " + noteId)) {
            if (!results.next()) {
                throw new RuntimeException("Заметка не найдена.");
            }

            return makeEntity(results);
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка получения данных: " + exception.getMessage());
        }
    }

    public int save(Note note) {
        return db.save("INSERT INTO `my_notes`.`note` (`ownerId`, `title`, `text`, `isPublic`, `isDeleted`, `created`, `modified`) VALUES (?, ?, ?, ?, ?, ?, ?)",
            note.getOwnerId(),
            note.getTitle(),
            note.getText(),
            note.isPublic(),
            note.isDeleted(),
            note.getCreated(),
            note.getModified()
        );
    }

    protected void initFakeData() {
        for (int i = 1; i <= 30; i++) {
            Date randomCreated = new Date(this.getRandomTimestamp());
            Note extraNote = new Note((int)Math.round(Math.random() * 10), String.format("Заметка от %s", randomCreated));
            extraNote.setIsDeleted(Math.random() < 0.5);
            extraNote.setIsPublic(Math.random() < 0.1);
            extraNote.setCreated(randomCreated);
            extraNote.setModified(new Date(this.getRandomTimestamp()));
            if (extraNote.isDeleted()) {
                extraNote.setText("А текст удалил");
            } else {
                extraNote.setText(String.format("Как-то раз, %s, написал я эту заметку для %s.", extraNote.getCreated(), extraNote.isPublic() ? "всех" : "себя"));
            }
            save(extraNote);
        }
    }

    protected Note makeEntity(ResultSet result) throws SQLException {
        Note note = new Note(result.getInt(2), result.getString(3));
        note.setNoteId(result.getInt(1));
        note.setText(result.getString(4));
        note.setIsPublic(result.getBoolean(5));
        note.setIsDeleted(result.getBoolean(6));
        note.setCreated(new Date((long)result.getInt(7) * 1000));
        note.setModified(new Date((long)result.getInt(8) * 1000));
        return note;
    }

    protected ResultSet getResult() {
        return getResult("1");
    }

    protected ResultSet getResult(String where) {
        return db.get("SELECT `noteId`, `ownerId`, `title`, `text`, `isPublic`, `isDeleted`, UNIX_TIMESTAMP(`created`), UNIX_TIMESTAMP(`modified`) FROM `my_notes`.`note` WHERE " + where);
    }

    protected long getRandomTimestamp() {
        return (long)(1600000000 + Math.floor(Math.random() * 100000000)) * 1000;
    }
}
