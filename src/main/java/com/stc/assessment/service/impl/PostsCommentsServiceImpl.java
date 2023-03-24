package com.stc.assessment.service.impl;

import com.stc.assessment.model.PostsComments;
import com.stc.assessment.repository.PostsCommentsRepository;
import com.stc.assessment.service.PostsCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostsCommentsServiceImpl implements PostsCommentsService {

    @Autowired
    private PostsCommentsRepository postsCommentsRepository;

    @Override
    public PostsComments createPostsComment(PostsComments postsComments) {
        return postsCommentsRepository.save(postsComments);
    }
}
