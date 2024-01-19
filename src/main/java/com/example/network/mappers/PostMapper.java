package com.example.network.mappers;

import com.example.network.dtos.post.PostCreateRequestDto;
import com.example.network.dtos.post.PostResponseDto;
import com.example.network.dtos.post.PostUpdateRequestDto;
import com.example.network.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "comments", ignore = true),
            @Mapping(target = "ratings", ignore = true)
    })
    Post toEntityCreate(PostCreateRequestDto postCreateRequestDto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "comments", ignore = true),
            @Mapping(target = "ratings", ignore = true)
    })
    Post toEntityUpdate(PostUpdateRequestDto postUpdateRequestDto);


    PostResponseDto toResponseDto(Post post);

    List<PostResponseDto>toResponseListDto(List<Post> postList);
}
