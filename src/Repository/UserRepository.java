package Repository;

import Entity.User;

import java.util.ArrayList;

public class UserRepository {

    protected ArrayList<User> userRegistry = new ArrayList<>();

    public UserRepository() {
        this.initFakeData();
    }

    public User getUserByLogin(String login) {
        if (this.userRegistry.isEmpty()) {
            throw new RuntimeException("База пользователей пуста.");
        }
        for (User registryUser : this.userRegistry) {
            if (registryUser.getLogin().equals(login)) {
                return registryUser;
            }
        }
        throw new RuntimeException("Пользователь не найден.");
    }

    public User findUserById(int userId) {
        // TODO find user for sharing
        User foundUser = new User();
        foundUser.setUserId(userId);
        return foundUser;
    }

    public String getLogin(int sequence) {
        return String.format("account-no-%d", sequence);
    }

    protected void initFakeData() {
        for (int i = 1; i <= 10; i++) {
            User extraUser = new User();
            extraUser.setUserId(i);
            extraUser.setLogin(this.getLogin(i));
            extraUser.setAdmin(Math.random() < 0.5);
            this.userRegistry.add(extraUser);
        }
    }
}
