package com.example.network.services;

import com.example.network.dtos.post.PostCreateRequestDto;
import com.example.network.dtos.post.PostResponseDto;
import com.example.network.dtos.post.PostUpdateRequestDto;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;

import java.util.List;

public interface PostService {

    PostResponseDto createPost(PostCreateRequestDto postCreateRequestDto) throws AuthorizationCustomException;

    PostResponseDto updatePost(Long id,PostUpdateRequestDto postUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException;

    PostResponseDto deletePost(Long id) throws NotFoundCustomException, AuthorizationCustomException;

    PostResponseDto getById(Long id) throws NotFoundCustomException, AuthorizationCustomException;

    List<PostResponseDto> getAll() throws AuthorizationCustomException;
}
