package com.example.demo.service;

import com.example.demo.domain.BoardInfo;
import com.example.demo.domain.CommentInfo;
import com.example.demo.dto.BoardInfoDto;
import com.example.demo.dto.CommentInfoDto;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    @Transactional
    public Long saveComment(CommentInfoDto commentDto) {
        return commentRepository.save(commentDto.toEntity()).getId();
    }
    @Transactional
    public List<CommentInfoDto> getCommentList() {
        List<CommentInfo> commentList = commentRepository.findAll();
        List<CommentInfoDto> commentDtoList = new ArrayList<>();

        for(CommentInfo comment : commentList) {
            CommentInfoDto commentDto = CommentInfoDto.builder()
                    .id(comment.getId())
                    .author(comment.getAuthor())
                    .content(comment.getContent())
                    .createdDate(comment.getCreatedDate())
                    .build();
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
