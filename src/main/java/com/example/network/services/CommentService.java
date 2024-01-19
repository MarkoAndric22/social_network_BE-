package com.example.network.services;

import com.example.network.dtos.comment.CommentCreateRequestDto;
import com.example.network.dtos.comment.CommentResponseDto;
import com.example.network.dtos.comment.CommentUpdateRequestDto;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;

import java.util.List;

public interface CommentService {

    CommentResponseDto createCommentForPost(Long id, CommentCreateRequestDto commentCreateRequestDto) throws NotFoundCustomException, AuthorizationCustomException;

    CommentResponseDto updateComment(Long id_comment,CommentUpdateRequestDto commentUpdateRequestDto ) throws NotFoundCustomException, AuthorizationCustomException;

    CommentResponseDto delete(Long id) throws NotFoundCustomException, AuthorizationCustomException;

    CommentResponseDto getById(Long id) throws NotFoundCustomException, AuthorizationCustomException;

    List<CommentResponseDto> getAll() throws AuthorizationCustomException;
}
