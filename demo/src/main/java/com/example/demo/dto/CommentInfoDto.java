package com.example.demo.dto;

import com.example.demo.domain.BoardInfo;
import com.example.demo.domain.CommentInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentInfoDto {
    private Long id;
    private String author;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private BoardInfo boardInfo;

    public CommentInfo toEntity() {
        CommentInfo build = CommentInfo.builder()
                .id(id)
                .author(author)
                .content(content)
                .boardInfo(boardInfo)
                .build();
        return build;
    }

    @Builder
    public CommentInfoDto(Long id, String author, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, BoardInfo boardInfo) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.boardInfo = boardInfo;

    }
}
