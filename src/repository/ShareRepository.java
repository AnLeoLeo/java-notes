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
         return (Boolean)processOne("`userId` = " + user.getUserId() + " AND `noteId` = " + currentNote.getNoteId(), "");
    }

    public boolean save(Share share) {
        return db.save("INSERT INTO `my_notes`.`share` (`userId`, `noteId`, `modified`) VALUES (?, ?, ?)",
            share.getUserId(),
            share.getNoteId(),
            share.getModified()
        ) > 0;
    }

    protected ResultSet getResult(String where) {
        return db.get("SELECT `userId`, `noteId`, UNIX_TIMESTAMP(`modified`) FROM `my_notes`.`share` WHERE " + where);
    }

    protected Boolean makeEntity(ResultSet result) throws SQLException {
        return result.next();
    }

    protected void initFakeData() {}
}
