package ru.practicum.service.comment;

import org.springframework.data.domain.Pageable;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Comment;

import java.util.Collection;

public interface CommentService {
    Comment save(Comment comment);

    Comment getCommentByIdOrElseThrow(long commentId) throws NotFoundException;

    Boolean deleteComment(long commentId);

    Collection<Comment> getComments(Long userId, Long eventId, Pageable pageable);
}