package com.example.network.controllers;

import com.example.network.dtos.comment.CommentCreateRequestDto;
import com.example.network.dtos.comment.CommentResponseDto;
import com.example.network.dtos.comment.CommentUpdateRequestDto;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import com.example.network.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @RequestMapping(method = RequestMethod.POST,value = "/createComment/{id}")
    public CommentResponseDto createCommentForPost(@PathVariable Long id, @RequestBody @Valid CommentCreateRequestDto commentCreateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        return commentService.createCommentForPost(id,commentCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/updateComment/{id_comment}")
    public CommentResponseDto updateComment(@PathVariable Long id_comment,@RequestBody @Valid CommentUpdateRequestDto commentUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        return commentService.updateComment(id_comment,commentUpdateRequestDto);
    }

    @RequestMapping(method = RequestMethod.DELETE,value = "/deleteComment/{id}")
    public CommentResponseDto deleteComment(@PathVariable Long id) throws NotFoundCustomException, AuthorizationCustomException {
        return commentService.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public CommentResponseDto getById(@PathVariable Long id) throws NotFoundCustomException, AuthorizationCustomException {
        return commentService.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CommentResponseDto> getAll() throws AuthorizationCustomException {
        return commentService.getAll();
    }
}
