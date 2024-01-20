package com.example.network.services;

import com.example.network.dtos.rating.RatingCreateRequestDto;
import com.example.network.dtos.rating.RatingResponseDto;
import com.example.network.dtos.rating.RatingUpdateRequestDto;
import com.example.network.entities.Post;
import com.example.network.entities.Rating;
import com.example.network.entities.User;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import com.example.network.mappers.RatingMapper;
import com.example.network.repositories.PostRepository;
import com.example.network.repositories.RatingRepository;
import com.example.network.repositories.UserRepository;
import com.example.network.services.authorization.UserResolver;
import com.example.network.services.authorization.UserRoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RatingMapper ratingMapper;

    @Autowired
    WebSocketNotificationService notificationService;

    @Autowired
    UserRoleValidator roleValidator;

    @Autowired
    UserResolver userResolver;

    @Autowired
    UserRepository userRepository;

    @Override
    public RatingResponseDto createRatingForPost(Long id,RatingCreateRequestDto ratingCreateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Post> optionalPost=postRepository.findById(id);
        if (optionalPost.isEmpty()){
            throw new NotFoundCustomException("Post not found");
        }

        roleValidator.validateUserWithUsername(userResolver.getUsername());

        Rating rating=ratingMapper.toEntityCreate(ratingCreateRequestDto);
        rating.setPost(optionalPost.get());
        rating.setUser(userRepository.findByUsername(userResolver.getUsername()));

        notificationService.sendNotification("/rating",rating);

        return ratingMapper.toResponseDto(ratingRepository.save(rating));
    }

    @Override
    public RatingResponseDto updateRating(Long id_rating,RatingUpdateRequestDto ratingUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {

        Optional<Rating> optionalRating = ratingRepository.findById(id_rating);
        if (optionalRating.isEmpty()){
            throw new NotFoundCustomException("Rating not found");
        }

        roleValidator.validateUserWithUsername(userResolver.getUsername());

        Rating existingRating= optionalRating.get();
        if (existingRating.getUser().getUsername().equals((userResolver.getUsername()))) {
            existingRating.setRating(ratingUpdateRequestDto.rating());
        }
        else throw new NotFoundCustomException("Rating is not your");

        return ratingMapper.toResponseDto(ratingRepository.save(existingRating));
    }

    @Override
    public RatingResponseDto deleteRating(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        if (optionalRating.isEmpty()){
            throw new NotFoundCustomException("Rating not found");
        }

        roleValidator.validateUserWithUsername(userResolver.getUsername());

        Rating existingRating= optionalRating.get();
        if (existingRating.getUser().getUsername().equals((userResolver.getUsername()))) {
            ratingRepository.delete(optionalRating.get());
        }
        else throw new NotFoundCustomException("Rating is not your");

        return ratingMapper.toResponseDto(optionalRating.get());
    }

    @Override
    public RatingResponseDto getById(Long id) throws NotFoundCustomException, AuthorizationCustomException {
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        if (optionalRating.isEmpty()){
            throw new NotFoundCustomException("Rating not found");
        }
        roleValidator.validateUserAndAdmin();

        return ratingMapper.toResponseDto(optionalRating.get());
    }

    @Override
    public List<RatingResponseDto> getAll() throws AuthorizationCustomException {
        roleValidator.validateAdmin();

        return ratingMapper.toResponseListDto(ratingRepository.findAll());
    }

    @Override
    public double getAvgForPost(Long id) throws AuthorizationCustomException {
        roleValidator.validateUserWithUsername(userResolver.getUsername());

        User user =userRepository.findByUsername(userResolver.getUsername());

        Post post=postRepository.findById(id).get();

        int sum = 0;

        for (Rating rating:post.getRatings()){
            sum+=rating.getRating();
        }

        double avg= 1.0*sum/post.getRatings().size();

        return avg;
    }
}
