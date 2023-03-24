package com.stc.assessment.service;

import com.stc.assessment.model.User;

import java.util.List;

public interface UserService {

    public abstract User createUsers(User user);

    public abstract List<User> findAll();

}
