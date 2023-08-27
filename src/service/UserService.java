package service;

import entity.User;
import repository.UserRepository;

public class UserService {

    protected UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String login) {
        if (login.isEmpty()) {
            throw new RuntimeException("Авторизация с пустым логином невозможна.");
        }

        User loggedUser = this.userRepository.getByLogin(login);
        if (loggedUser.getUserId() <= 0) {
            throw new RuntimeException("Пользователь не найден.");
        }

        return loggedUser;
    }

    public User get(int userId) {
        return this.userRepository.getById(userId);
    }

    public String generateFakeLoginByUserId(int userId) {
        return this.userRepository.getLogin(userId);
    }
}
