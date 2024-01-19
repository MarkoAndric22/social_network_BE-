package com.example.network.mappers;

import com.example.network.dtos.rating.RatingCreateRequestDto;
import com.example.network.dtos.rating.RatingResponseDto;
import com.example.network.dtos.rating.RatingUpdateRequestDto;
import com.example.network.entities.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "post", ignore = true),
    })
    Rating toEntityCreate(RatingCreateRequestDto ratingCreateRequestDto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "post", ignore = true),
    })
    Rating toEntityUpdate(RatingUpdateRequestDto ratingUpdateRequestDto);


    RatingResponseDto toResponseDto(Rating rating);

    @Mapping(target = "id", ignore = true)
    List<RatingResponseDto> toResponseListDto(List<Rating> ratingList);
}
