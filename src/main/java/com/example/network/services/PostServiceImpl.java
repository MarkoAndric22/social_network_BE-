package com.example.network.services;

import com.example.network.dtos.post.PostCreateRequestDto;
import com.example.network.dtos.post.PostResponseDto;
import com.example.network.dtos.post.PostUpdateRequestDto;
import com.example.network.entities.Post;
import com.example.network.entities.User;
import com.example.network.entities.enums.Role;
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

        Post post= postOptional.get();
        if (post.getUser().getUsername().equals(userResolver.getUsername())) {
            post.setText(postUpdateRequestDto.text());
            post.setTimestamp(LocalDateTime.now());
            post.setId(id);
            post.setUser(postOptional.get().getUser());
        }
       else throw new NotFoundCustomException("Post is not your");

        return postMapper.toResponseDto(postRepository.save(post));
    }

    @Override
    public PostResponseDto deletePost(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Post> postOptional= postRepository.findById(id);
        if (postOptional.isEmpty()){
            throw new NotFoundCustomException("Post not found");
        }

        Post post= postOptional.get();
        if ((post.getUser().getUsername().equals(userResolver.getUsername())) || userResolver.getRole().equals(Role.ROLE_ADMIN)) {
            for (User likedUser : post.getLikedByUsers()) {
                likedUser.getLikedPosts().remove(post);
            }

            post.getLikedByUsers().clear();

            postRepository.saveAndFlush(post);
            postRepository.delete(post);
        }

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
