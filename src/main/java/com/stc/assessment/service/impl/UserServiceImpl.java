package com.stc.assessment.service.impl;

import com.stc.assessment.model.User;
import com.stc.assessment.repository.UserRepository;
import com.stc.assessment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUsers(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUserName(User updatedUser) {

        User existingUser = userRepository.findById(updatedUser.getId()).orElseThrow();

        existingUser.setName(updatedUser.getName());

        return userRepository.save(existingUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
