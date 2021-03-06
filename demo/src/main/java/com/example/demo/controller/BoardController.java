package com.example.demo.controller;

import com.example.demo.dto.BoardInfoDto;
import com.example.demo.dto.CommentInfoDto;
import com.example.demo.dto.FileInfoDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;
import com.example.demo.service.FileService;
import com.example.demo.service.UserService;
import com.example.demo.util.MD5Generator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class BoardController {
    private UserService userService;
    private BoardService boardService;
    private FileService fileService;
    private CommentService commentService;


    public BoardController(UserService userService, BoardService boardService, FileService fileService, CommentService commentService) {
        this.userService = userService;
        this.boardService = boardService;
        this.fileService = fileService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String list(Model model) {
        try {
            List<BoardInfoDto> boardDtoList = boardService.getBoardList();
            model.addAttribute("postList", boardDtoList);
        } catch (NullPointerException e) {
            return "board/list";
        }
        return "board/list";
    }

    @GetMapping("/post")
    public String post(Model model) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails)principal).getUsername();
            UserInfoDto userDto = userService.getCode(username);
            System.out.println(userDto);
            model.addAttribute("user", userDto);
        } catch (NullPointerException e) {
            System.out.println("123123 : error");
            return "board/post";
        }
        return "board/post";
    }

    @PostMapping("/post")
    public String write(@RequestParam("file") MultipartFile files, BoardInfoDto boardDto) {
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();
            /* ???????????? ????????? 'files' ????????? ????????? ???????????????. */
            String savePath = System.getProperty("user.dir") + "/files";
            /* ????????? ???????????? ????????? ????????? ????????? ???????????????. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "/" + filename;
            files.transferTo(new File(filePath));

            FileInfoDto fileDto = new FileInfoDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Long fileId = fileService.saveFile(fileDto);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails)principal).getUsername();
                boardDto.setAuthor(username);
            } else {
                String username = principal.toString();
                boardDto.setAuthor(username);
            }

            boardDto.setFileId(fileId);
            boardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardInfoDto boardDto = boardService.getPost(id);
        FileInfoDto fileDto = fileService.getFile(boardDto.getFileId());
        List<CommentInfoDto> commentDtoList = commentService.getCommentList(id);

        model.addAttribute("post", boardDto);
        model.addAttribute("file",fileDto);
        model.addAttribute("commentList", commentDtoList);

        return "board/detail";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardInfoDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/edit";
    }

    @GetMapping("/comment/{boardId}/{id}")
    public String commentEdit(@PathVariable("boardId") Long boardId, @PathVariable("id") Long id, Model model) {
        CommentInfoDto commentDto = commentService.getComment(id);
        BoardInfoDto boardDto = boardService.getPost(boardId);
        model.addAttribute("comment", commentDto);
        model.addAttribute("post", boardDto);
        return "board/commentEdit";
    }

    @PutMapping("/post/edit/{id}")
    public String update(@PathVariable("id") Long id, @RequestParam("file") MultipartFile files, BoardInfoDto boardDto) {
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();
            /* ???????????? ????????? 'files' ????????? ????????? ???????????????. */
            String savePath = System.getProperty("user.dir") + "/files";
            /* ????????? ???????????? ????????? ????????? ????????? ???????????????. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "/" + filename;
            files.transferTo(new File(filePath));

            FileInfoDto fileDto = new FileInfoDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Long fileId = fileService.saveFile(fileDto);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails)principal).getUsername();
                boardDto.setAuthor(username);
            } else {
                String username = principal.toString();
                boardDto.setAuthor(username);
            }

            boardDto.setFileId(fileId);
            System.out.println("bbbbbbbbbb : "+boardDto);
            boardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/post/"+id;
    }

    @PutMapping("/comment/{boardId}/{id}")
    public String editcomment(@PathVariable("boardId") Long boardId, @PathVariable("id") Long id, CommentInfoDto commentDto) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails)principal).getUsername();
                commentDto.setAuthor(username);
            } else {
                String username = principal.toString();
                commentDto.setAuthor(username);
            }
            commentService.editComment(boardId, id, commentDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/post/"+boardId;
    }

    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return "redirect:/";
    }

    @DeleteMapping("/comment/{boardId}/{id}")
    public String deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return "redirect:/post/"+boardId;
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileInfoDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/comment")
    public String addcomment(@RequestParam("boardId") Long boardId, CommentInfoDto commentDto) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails)principal).getUsername();
                commentDto.setAuthor(username);
            } else {
                String username = principal.toString();
                commentDto.setAuthor(username);
            }
            commentService.saveComment(boardId, commentDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/post/"+boardId;
    }
}
