package com.example.network.dtos.rating;

import com.example.network.entities.Post;
import com.example.network.entities.User;

public record RatingResponseDto(

        User user,

        Post post,

        int rating
) {
}
