package ru.practicum.controller.publicc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.service.comment.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentPublicController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/comments")
    public Collection<CommentDto> getComments(@RequestParam Long eventId,
                                              @RequestParam(required = false, defaultValue = "0") @PositiveOrZero
                                              Integer from,
                                              @RequestParam(required = false, defaultValue = "10") @Positive
                                                  Integer size) {

        Collection<Comment> commentCollectionForDto = commentService.getComments(null, eventId, PageRequest.of(from, size));
        return commentMapper.toCommentDtoList(commentCollectionForDto);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        Comment commentForDto = commentService.getCommentByIdOrElseThrow(commentId);
        return commentMapper.toCommentDto(commentForDto);
    }
}