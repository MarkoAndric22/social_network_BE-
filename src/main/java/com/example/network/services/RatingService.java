package com.example.network.services;


import com.example.network.dtos.rating.RatingCreateRequestDto;
import com.example.network.dtos.rating.RatingResponseDto;
import com.example.network.dtos.rating.RatingUpdateRequestDto;
import com.example.network.entities.Rating;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;

import java.util.List;

public interface RatingService {

     RatingResponseDto createRatingForPost(Long id, RatingCreateRequestDto ratingCreateRequestDto) throws NotFoundCustomException, AuthorizationCustomException;

     RatingResponseDto updateRating(Long id_rating,RatingUpdateRequestDto ratingUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException;

     RatingResponseDto deleteRating(Long id) throws NotFoundCustomException, AuthorizationCustomException;

     RatingResponseDto getById(Long id) throws NotFoundCustomException, AuthorizationCustomException;

     List<RatingResponseDto> getAll() throws AuthorizationCustomException;

     double getAvgForPost(Long id) throws AuthorizationCustomException;


}
