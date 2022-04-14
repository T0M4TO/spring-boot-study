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
import java.util.Optional;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private BoardRepository boardRepository;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public CommentInfoDto getComment(Long id) {
        CommentInfo comment = commentRepository.findById(id).get();

        CommentInfoDto commentDto = CommentInfoDto.builder()
                .id(comment.getId())
                .author(comment.getAuthor())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .build();
        return commentDto;
    }

    @Transactional
    public Long saveComment(Long boardId, CommentInfoDto commentDto) {
        BoardInfo boardInfo = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 boardId가 없습니다. id=" + boardId));
        commentDto.setBoardInfo(boardInfo);
        return commentRepository.save(commentDto.toEntity()).getId();
    }

    @Transactional
    public Long editComment(Long boardId, Long id, CommentInfoDto commentDto) {
        BoardInfo boardInfo = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 boardId가 없습니다. id=" + boardId));
        commentDto.setBoardInfo(boardInfo);
        commentDto.setId(id);
        return commentRepository.save(commentDto.toEntity()).getId();
    }

    @Transactional
    public List<CommentInfoDto> getCommentList(Long id) {
        List<CommentInfo> commentList = commentRepository.findAll();
        List<CommentInfoDto> commentDtoList = new ArrayList<>();
        for (CommentInfo comment : commentList) {
            System.out.println("bbbbb : "+comment.getBoardInfo().getId());
            System.out.println("bbbbb : "+id);
            if (comment.getBoardInfo().getId() == id) {
                CommentInfoDto commentDto = CommentInfoDto.builder()
                        .id(comment.getId())
                        .author(comment.getAuthor())
                        .content(comment.getContent())
                        .createdDate(comment.getCreatedDate())
                        .build();
                commentDtoList.add(commentDto);
            }
        }
        return commentDtoList;
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
