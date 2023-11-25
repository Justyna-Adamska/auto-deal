package com.example.autodeal.service;

import com.example.autodeal.model.UserModel;
import com.example.autodeal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public UserModel saveUser(UserModel user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

}
