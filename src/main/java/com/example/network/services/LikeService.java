package com.example.network.services;

import com.example.network.entities.EmailObject;
import com.example.network.entities.Post;
import com.example.network.entities.User;
import com.example.network.repositories.PostRepository;
import com.example.network.repositories.UserRepository;
import com.example.network.services.authorization.UserResolver;
import com.example.network.services.authorization.UserRoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class LikeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRoleValidator roleValidator;

    @Autowired
    private EmailService emailService;

    @Autowired
    UserResolver userResolver;

    @Transactional
    public Set<User> likePost(Long postId) throws Exception {
        User user = userRepository.findByUsername(userResolver.getUsername());
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        roleValidator.validateUser();


        if (post.getLikedByUsers() == null) {
            post.setLikedByUsers(new HashSet<>());
        }

        if(user.getLikedPosts() == null) {
            user.setLikedPosts(new HashSet<>());
        }

        post.getLikedByUsers().add(user);
        user.getLikedPosts().add(post);

        userRepository.save(user);
        postRepository.save(post);

        User postUser=post.getUser();

        EmailObject email = new EmailObject();
        email.setTo(postUser.getEmail());
        email.setSubject("New like");
        email.setText("Hello mr/mrs "+postUser.getName()+" got like from "+user.getUsername());
        emailService.sendSimpleMessage(email);

        return post.getLikedByUsers();
    }

}
