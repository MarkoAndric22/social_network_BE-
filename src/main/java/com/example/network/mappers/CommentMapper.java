package com.example.network.mappers;

import com.example.network.dtos.comment.CommentCreateRequestDto;
import com.example.network.dtos.comment.CommentResponseDto;
import com.example.network.dtos.comment.CommentUpdateRequestDto;
import com.example.network.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "post", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    Comment toEntityCreate(CommentCreateRequestDto commentCreateRequestDto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "post", ignore = true),
            @Mapping(target = "user", ignore = true),
    })
    Comment toEntityUpdate(CommentUpdateRequestDto commentUpdateRequestDto);


            @Mapping(target = "post", ignore = true)
    CommentResponseDto toResponseDto(Comment comment);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "post", ignore = true),
    })
    List<CommentResponseDto> toResponseDtoList(List<Comment> commentList);
}
