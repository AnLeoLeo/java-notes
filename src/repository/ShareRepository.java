package repository;

import db.Db;
import entity.Note;
import entity.Share;
import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShareRepository extends AbstractRepository {

    public ShareRepository(Db db) {
        super(db);
    }

    public boolean hasShare(Note currentNote, User user) {
        try (ResultSet results = getResult(user.getUserId(), currentNote.getNoteId())) {
            return results.next();
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка получения данных: " + exception.getMessage());
        }
    }

    public boolean save(Share share) {
        return db.save("INSERT INTO `my_notes`.`share` (`userId`, `noteId`, `modified`) VALUES (?, ?, ?)",
            share.getUserId(),
            share.getNoteId(),
            share.getModified()
        ) > 0;
    }

    protected ResultSet getResult(int userId, int noteId) {
        return db.get("SELECT `userId`, `noteId`, UNIX_TIMESTAMP(`modified`) FROM `my_notes`.`share` WHERE `userId` = " + userId + " AND `noteId` = " + noteId);
    }

    protected void initFakeData() {}
}
