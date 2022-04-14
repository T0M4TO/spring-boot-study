package com.example.demo.repository;

import com.example.demo.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
}