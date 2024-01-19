package com.example.network.controllers;

import com.example.network.dtos.post.PostCreateRequestDto;
import com.example.network.dtos.post.PostResponseDto;
import com.example.network.dtos.post.PostUpdateRequestDto;
import com.example.network.entities.User;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import com.example.network.services.LikeService;
import com.example.network.services.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    private LikeService likeService;

    @RequestMapping(method = RequestMethod.POST,value = "/createPost")
    public PostResponseDto create(@RequestBody @Valid PostCreateRequestDto postCreateRequestDto) throws AuthorizationCustomException {
        return postService.createPost(postCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/updatePost/{id}")
    public PostResponseDto update(@PathVariable Long id, @RequestBody @Valid PostUpdateRequestDto postUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        return postService.updatePost(id,postUpdateRequestDto);
    }

    @RequestMapping(method = RequestMethod.DELETE,value = "/deletePost/{id}")
    public PostResponseDto delete(@PathVariable Long id) throws NotFoundCustomException, AuthorizationCustomException {
        return postService.deletePost(id);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public PostResponseDto getById(@PathVariable Long id) throws NotFoundCustomException, AuthorizationCustomException {
        return postService.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/getAll")
    public List<PostResponseDto> getAll() throws AuthorizationCustomException {
        return postService.getAll();
    }

    @PostMapping("/like/{postId}")
    public Set<User> likePost(@PathVariable Long postId) throws Exception {
       return likeService.likePost(postId);
    }
}
