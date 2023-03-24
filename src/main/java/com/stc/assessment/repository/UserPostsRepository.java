package com.stc.assessment.repository;

import com.stc.assessment.model.UsersPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostsRepository extends JpaRepository<UsersPosts, Long> {
}
