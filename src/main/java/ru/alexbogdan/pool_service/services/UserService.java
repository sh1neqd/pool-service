package ru.alexbogdan.pool_service.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.alexbogdan.pool_service.exceptions.AppAnswer;
import ru.alexbogdan.pool_service.models.User;
import ru.alexbogdan.pool_service.models.dtos.UserDTO;
import ru.alexbogdan.pool_service.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> saveUser(@Valid UserDTO userDTO) {
        if (isExists(userDTO.getEmail(), userDTO.getPhone())) {
            return new ResponseEntity<>(new AppAnswer(HttpStatus.BAD_REQUEST.value(),
                    "client with this email or phone already exist"), HttpStatus.BAD_REQUEST);
        } else {
            User user = new User();
            user.setName(userDTO.getName());
            user.setPhone(userDTO.getPhone());
            user.setEmail(userDTO.getEmail());
            userRepository.save(convertToUser(userDTO));
            return new ResponseEntity<>(new AppAnswer(HttpStatus.OK.value(), "client added"), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<?> getUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            return new ResponseEntity<>(new AppAnswer(HttpStatus.NOT_FOUND.value(),
                    "client with this id not found"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userRepository.findById(id));
    }

    @Transactional
    public ResponseEntity<?> updateUser(User user) {
        var optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(userRepository.updateUser(user));
        } else {
            return new ResponseEntity<>(new AppAnswer(HttpStatus.NOT_FOUND.value(),
                    "client with this id not found"), HttpStatus.NOT_FOUND);
        }
    }
    private User convertToUser(UserDTO userDTO) {
        var user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        return user;
    }

    private boolean isExists(String email, String phone) {
        var users = userRepository.findAll();
        for(User user : users) {
            if (user.getEmail().equals(email) || user.getPhone().equals(phone)) return true;
        }
        return false;
    }
}
