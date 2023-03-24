package com.stc.assessment.service.impl;

import com.stc.assessment.model.UsersPosts;
import com.stc.assessment.repository.UserPostsRepository;
import com.stc.assessment.service.UsersPostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersPostsImpl implements UsersPostsService {

    @Autowired
    private UserPostsRepository userPostsRepository;

    @Override
    public UsersPosts createUsersPosts(UsersPosts usersPosts) {
        return userPostsRepository.save(usersPosts);
    }
}
