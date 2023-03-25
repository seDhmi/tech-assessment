package com.stc.assessment.service;

import com.stc.assessment.model.UsersPosts;

import java.util.List;

public interface UsersPostsService {

    public abstract UsersPosts createUsersPosts(UsersPosts usersPosts);

    public abstract List<UsersPosts> findAll();


}
