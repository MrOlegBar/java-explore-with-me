package ru.practicum.dto.comment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.model.Comment;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final ModelMapper modelMapper;

    public CommentDto toCommentDto(Comment comment) {
        return modelMapper.map(Objects.requireNonNull(comment), CommentDto.class);
    }

    public Collection<CommentDto> toCommentDtoList(Collection<Comment> commentCollection) {
        return commentCollection
                .stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }
}