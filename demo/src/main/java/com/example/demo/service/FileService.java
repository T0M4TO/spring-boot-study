package com.example.demo.service;

import com.example.demo.domain.FileInfo;
import com.example.demo.dto.FileInfoDto;
import com.example.demo.repository.FileRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long saveFile(FileInfoDto fileDto) {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileInfoDto getFile(Long id) {
        FileInfo file = fileRepository.findById(id).get();

        FileInfoDto fileDto = FileInfoDto.builder()
                .id(id)
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDto;
    }
}