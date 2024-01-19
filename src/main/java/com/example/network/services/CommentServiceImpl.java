package com.example.network.services;

import com.example.network.dtos.comment.CommentCreateRequestDto;
import com.example.network.dtos.comment.CommentResponseDto;
import com.example.network.dtos.comment.CommentUpdateRequestDto;
import com.example.network.entities.Comment;
import com.example.network.entities.Post;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import com.example.network.mappers.CommentMapper;
import com.example.network.repositories.CommentRepository;
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
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    WebSocketNotificationService notificationService;

    @Autowired
    UserRoleValidator roleValidator;

    @Autowired
    UserResolver userResolver;

    @Autowired
    UserRepository userRepository;

    @Override
    public CommentResponseDto createCommentForPost(Long id, CommentCreateRequestDto commentCreateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isEmpty()){
            throw new NotFoundCustomException("Post not found");
        }

        roleValidator.validateUserWithUsername(userResolver.getUsername());

        Comment comment=commentMapper.toEntityCreate(commentCreateRequestDto);
        comment.setTimestamp(LocalDateTime.now());
        comment.setPost(postOptional.get());
        comment.setUser(userRepository.findByUsername(userResolver.getUsername()));

        notificationService.sendNotification("/comment",comment);

        return commentMapper.toResponseDto(commentRepository.save(comment));

    }

    @Override
    public CommentResponseDto updateComment(Long id_comment, CommentUpdateRequestDto commentUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {

        Optional<Comment> optionalComment = commentRepository.findById(id_comment);
        if (optionalComment.isEmpty()) {
            throw new NotFoundCustomException("Comment not found");
        }

        roleValidator.validateUserWithUsernameAndAdmin(userResolver.getUsername());

        Comment existingComment = optionalComment.get();
        existingComment.setText(commentUpdateRequestDto.text());
        existingComment.setTimestamp(LocalDateTime.now());
        existingComment.setUser(userRepository.findByUsername(userResolver.getUsername()));

        return commentMapper.toResponseDto(commentRepository.save(existingComment));
    }


    @Override
    public CommentResponseDto delete(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            throw new NotFoundCustomException("Comment not found");
        }

        roleValidator.validateUserWithUsernameAndAdmin(userResolver.getUsername());

        commentRepository.delete(optionalComment.get());

        return commentMapper.toResponseDto(optionalComment.get());
    }

    @Override
    public CommentResponseDto getById(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            throw new NotFoundCustomException("Comment not found");
        }

        roleValidator.validateUserAndAdmin();

        return commentMapper.toResponseDto(optionalComment.get());
    }

    @Override
    public List<CommentResponseDto> getAll() throws AuthorizationCustomException {

        roleValidator.validateAdmin();

        return commentMapper.toResponseDtoList(commentRepository.findAll());
    }
}
