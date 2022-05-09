package zju.se.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zju.se.management.repository.UserRepository;
import zju.se.management.entity.User;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByName(String name) {
        List<User> list = userRepository.findByUserName(name);
        if(list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }

    public String getPasswordByName(String name) {
        User user = getUserByName(name);
        return user.getPassword();
    }

}
