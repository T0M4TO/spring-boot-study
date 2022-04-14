package com.example.demo.repository;

import com.example.demo.domain.CommentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentInfo, Long> {
}
