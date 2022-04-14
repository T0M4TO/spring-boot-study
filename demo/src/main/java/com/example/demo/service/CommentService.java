package com.example.demo.service;

import com.example.demo.domain.BoardInfo;
import com.example.demo.dto.BoardInfoDto;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    @Transactional
    public Long savePost(BoardInfoDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }
    @Transactional
    public List<BoardInfoDto> getBoardList() {
        List<BoardInfo> boardList = boardRepository.findAll();
        List<BoardInfoDto> boardDtoList = new ArrayList<>();

        for(BoardInfo board : boardList) {
            BoardInfoDto boardDto = BoardInfoDto.builder()
                    .id(board.getId())
                    .author(board.getAuthor())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    @Transactional
    public BoardInfoDto getPost(Long id) {
        BoardInfo board = boardRepository.findById(id).get();

        BoardInfoDto boardDto = BoardInfoDto.builder()
                .id(board.getId())
                .author(board.getAuthor())
                .title(board.getTitle())
                .content(board.getContent())
                .fileId(board.getFileId())
                .createdDate(board.getCreatedDate())
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
}
