package com.example.network.controllers;

import com.example.network.dtos.rating.RatingCreateRequestDto;
import com.example.network.dtos.rating.RatingResponseDto;
import com.example.network.dtos.rating.RatingUpdateRequestDto;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import com.example.network.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @RequestMapping(method = RequestMethod.POST,value = "/createRating/{id}")
    public RatingResponseDto createRating(@PathVariable Long id, @RequestBody @Valid RatingCreateRequestDto ratingCreateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        return ratingService.createRatingForPost(id,ratingCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/updateRating/{id_rating}")
    public RatingResponseDto updateRating(@PathVariable Long id_rating,@RequestBody @Valid RatingUpdateRequestDto ratingUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        return ratingService.updateRating(id_rating,ratingUpdateRequestDto);
    }

    @RequestMapping(method = RequestMethod.DELETE,value = "/deleteRating/{id}")
    public RatingResponseDto deleteRating(@PathVariable Long id) throws NotFoundCustomException, AuthorizationCustomException {
        return ratingService.deleteRating(id);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public RatingResponseDto getById(@PathVariable Long id) throws NotFoundCustomException, AuthorizationCustomException {
        return ratingService.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RatingResponseDto> getAll() throws AuthorizationCustomException {
        return ratingService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET,value = "/avgRating/{id}")
    public double avgRating(@PathVariable Long id) throws AuthorizationCustomException, NotFoundCustomException {
        return ratingService.getAvgForPost(id);
    }
}
