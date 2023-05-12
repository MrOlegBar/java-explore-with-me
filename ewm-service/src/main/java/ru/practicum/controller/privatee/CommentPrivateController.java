package ru.practicum.controller.privatee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.constraintGroup.Post;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.error.ConflictException;
import ru.practicum.model.Comment;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentPrivateController {

    private final CommentService commentService;
    private final UserService userService;
    private final EventService eventService;
    private final CommentMapper commentMapper;

    @PostMapping("/users/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable @NotNull Long userId,
                                      @RequestParam @NotNull Long eventId,
                                      @Validated({Post.class}) @RequestBody NewCommentDto newCommentDto) {
        User author = userService.getUserByIdOrElseThrow(userId);
        Event event = eventService.getEventByIdOrElseThrow(eventId);

        if (!event.getState().equals(Event.EventStatus.PUBLISHED)) {
            log.debug("Оставлять комментарий можно только у мероприятий, находящихся в " +
                    "состоянии {}.", Event.EventStatus.PENDING);
            throw new ConflictException(String.format("Оставлять комментарий можно только у мероприятий, находящихся в " +
                    "состоянии %s.", Event.EventStatus.PENDING));
        }

        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setAuthor(author);
        comment.setEvent(event);

        Comment commentForDto = commentService.save(comment);
        return commentMapper.toCommentDto(commentForDto);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    public CommentDto patchComment(@PathVariable Long userId,
                                   @PathVariable Long commentId,
                                   @Validated({Patch.class}) @RequestBody NewCommentDto newCommentDto) {
        Comment comment = commentService.getCommentByIdOrElseThrow(commentId);
        userService.getUserByIdOrElseThrow(userId);

        if (!userId.equals(comment.getAuthor().getId())) {
            log.debug("Доступ к комментарию с commentId = {} не возможен для пользователя с userId = {}.", commentId,
                    userId);
            throw new ConflictException(String.format("Доступ к комментарию с commentId = %s не возможен для " +
                            "пользователя с userId = %s.", commentId,
                    userId));
        }

        comment.setText(newCommentDto.getText());
        comment.setEdited(LocalDateTime.now());

        Comment commentForDto = commentService.save(comment);
        return commentMapper.toCommentDto(commentForDto);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteComment(@PathVariable @NotNull Long userId,
                                 @PathVariable Long commentId) {
        userService.getUserByIdOrElseThrow(userId);
        Comment comment = commentService.getCommentByIdOrElseThrow(commentId);

        if (!userId.equals(comment.getAuthor().getId())) {
            log.debug("Доступ к комментарию с commentId = {} не возможен для пользователя с userId = {}.", commentId,
                    userId);
            throw new ConflictException(String.format("Доступ к комментарию с commentId = %s не возможен для пользователя с userId = %s.", commentId,
                    userId));
        }

        return commentService.deleteComment(commentId);
    }

    @GetMapping("/users/{userId}/comments")
    public Collection<CommentDto> getComments(@PathVariable Long userId,
                                              @RequestParam(required = false) Long eventId,
                                              @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        userService.getUserByIdOrElseThrow(userId);
        if (eventId != null) {
            eventService.getEventByIdOrElseThrow(eventId);
        }

        Collection<Comment> commentCollectionForDto = commentService.getComments(userId, eventId,
                PageRequest.of(from, size));
        return commentMapper.toCommentDtoList(commentCollectionForDto);
    }
}