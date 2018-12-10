package com.oasys.repository;

import com.oasys.entities.Comment;
import com.oasys.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
