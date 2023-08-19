package Service;

import Entity.User;
import Repository.UserRepository;

public class UserService {

    protected UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String login) {
        if (login.isEmpty()) {
            throw new RuntimeException("Авторизация с пустым логином невозможна.");
        }

        User loggedUser = this.userRepository.getUserByLogin(login);
        if (loggedUser.getUserId() <= 0) {
            throw new RuntimeException("Пользователь не найден.");
        }

        return loggedUser;
    }
}
