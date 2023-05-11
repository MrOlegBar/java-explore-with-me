package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {
    private final CommentService commentService;
    private final UserService userService;
    private final EventService eventService;
    private final CommentMapper commentMapper;

    @GetMapping("/admin/comments")
    public Collection<CommentDto> getComments(@RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) Long eventId,
                                              @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        if (userId != null) {
            userService.getUserByIdOrElseThrow(userId);
        }
        if (eventId != null) {
            eventService.getEventByIdOrElseThrow(eventId);
        }

        Collection<Comment> commentCollectionForDto = commentService.getComments(userId, eventId,
                PageRequest.of(from, size));
        return commentMapper.toCommentDtoList(commentCollectionForDto);
    }

    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteById(@PathVariable Long commentId) {
        commentService.getCommentByIdOrElseThrow(commentId);

        return commentService.deleteComment(commentId);
    }
}