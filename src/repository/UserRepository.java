package repository;

import db.Db;
import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends AbstractRepository {

    public UserRepository(Db db) {
        super(db);
    }

    public User getByLogin(String login) {
        try (ResultSet results = getResult("`login` = '" + login + "'")) {
            if (!results.next()) {
                throw new RuntimeException("Пользователь не найден.");
            }

            return makeEntity(results);
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка получения данных: " + exception.getMessage());
        }
    }

    public User getById(int userId) {
        try (ResultSet results = getResult("`userId` = " + userId)) {
            if (!results.next()) {
                throw new RuntimeException("Пользователь не найден.");
            }

            return makeEntity(results);
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка получения данных: " + exception.getMessage());
        }
    }

    public String getLogin(int sequence) {
        return String.format("account-no-%d", sequence);
    }

    protected void initFakeData() {
        for (int i = 1; i <= 10; i++) {
            User extraUser = new User(i, getLogin(i));
            extraUser.setAdmin(Math.random() < 0.5);
            save(extraUser);
        }
    }

    protected void save(User user) {
        db.save("INSERT INTO `my_notes`.`user` (`userId`, `login`, `isAdmin`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `userId` = VALUES(`userId`), `login` = VALUES(`login`), `isAdmin` = VALUES(`isAdmin`)",
            user.getUserId(),
            user.getLogin(),
            user.isAdmin()
        );
    }

    protected User makeEntity(ResultSet result) throws SQLException {
        User user = new User(result.getInt(1), result.getString(2));
        user.setAdmin(result.getBoolean(3));
        return user;
    }

    protected ResultSet getResult(String where) {
        return db.get("SELECT `userId`, `login`, `isAdmin` FROM `my_notes`.`user` WHERE " + where);
    }
}
