package com.example.demo.dto;

import com.example.demo.domain.FileInfo;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileInfoDto {
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;

    public FileInfo toEntity() {
        FileInfo build = FileInfo.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public FileInfoDto(Long id, String origFilename, String filename, String filePath) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}
