package com.stc.assessment.repository;

import com.stc.assessment.model.PostsComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsCommentsRepository extends JpaRepository<PostsComments, Long> {
}
