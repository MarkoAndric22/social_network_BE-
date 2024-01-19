package com.example.network.services;

import com.example.network.dtos.post.PostCreateRequestDto;
import com.example.network.dtos.post.PostResponseDto;
import com.example.network.dtos.post.PostUpdateRequestDto;
import com.example.network.entities.Post;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import com.example.network.mappers.PostMapper;
import com.example.network.repositories.PostRepository;
import com.example.network.repositories.UserRepository;
import com.example.network.services.authorization.UserResolver;
import com.example.network.services.authorization.UserRoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    PostRepository postRepository;
    @Autowired
    PostMapper postMapper;
    @Autowired
    WebSocketNotificationService notificationService;
    @Autowired
    UserRoleValidator roleValidator;
    @Autowired
    UserResolver userResolver;
    @Autowired
    UserRepository userRepository;

    @Override
    public PostResponseDto createPost(PostCreateRequestDto postCreateRequestDto) throws AuthorizationCustomException {
        roleValidator.validateUserWithUsername(userResolver.getUsername());

        Post post= postMapper.toEntityCreate(postCreateRequestDto);
        post.setTimestamp(LocalDateTime.now());
        post.setUser(userRepository.findByUsername(userResolver.getUsername()));

        notificationService.sendNotification("/post",post);

        return postMapper.toResponseDto(postRepository.save(post));

    }

    @Override
    public PostResponseDto updatePost(Long id, PostUpdateRequestDto postUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isEmpty()){
            throw new NotFoundCustomException("Post not found");
        }
        roleValidator.validateUserWithUsername(userResolver.getUsername());

        Post post= postMapper.toEntityUpdate(postUpdateRequestDto);
        post.setTimestamp(LocalDateTime.now());
        post.setId(id);
        post.setUser(userRepository.findByUsername(userResolver.getUsername()));
        return postMapper.toResponseDto(postRepository.save(post));
    }

    @Override
    public PostResponseDto deletePost(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Post> postOptional= postRepository.findById(id);
        if (postOptional.isEmpty()){
            throw new NotFoundCustomException("Post not found");
        }
        roleValidator.validateUserWithUsernameAndAdmin(userResolver.getUsername());
        postRepository.delete(postOptional.get());

        return postMapper.toResponseDto(postOptional.get());
    }

    @Override
    public PostResponseDto getById(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Post> postOptional= postRepository.findById(id);
        if (postOptional.isEmpty()){
            throw new NotFoundCustomException("Post not found");
        }
        roleValidator.validateUserAndAdmin();

        return postMapper.toResponseDto(postOptional.get());
    }

    @Override
    public List<PostResponseDto> getAll() throws AuthorizationCustomException {
        roleValidator.validateAdmin();

        List<Post> posts=postRepository.findAll();

        return postMapper.toResponseListDto(posts);
    }
}
