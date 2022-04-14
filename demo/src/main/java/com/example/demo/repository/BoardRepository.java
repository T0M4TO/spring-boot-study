package com.example.demo.repository;

import com.example.demo.domain.BoardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardInfo, Long> {
}
