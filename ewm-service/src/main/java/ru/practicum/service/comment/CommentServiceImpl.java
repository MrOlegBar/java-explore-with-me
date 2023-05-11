package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentByIdOrElseThrow(long commentId) throws NotFoundException {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.debug("Комментарий с commentId = {} не найден.", commentId);
            throw new NotFoundException(String.format("Комментарий с commentId = %s не найден.",
                    commentId));
        });
    }

    @Override
    public Boolean deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
        return !commentRepository.existsById(commentId);
    }

    @Override
    public Collection<Comment> getComments(Long userId, Long eventId, Pageable pageable) {
        Collection<Comment> comments = new ArrayList<>();

        if (userId != null && eventId != null) {
            comments = commentRepository.findAllByAuthorIdAndEventId(userId, eventId, pageable);
        } else if (userId != null) {
            comments = commentRepository.findAllByAuthorId(userId, pageable);
        } else if  (eventId != null) {
            comments = commentRepository.findAllByEventId(eventId, pageable);
        }

        return comments;
    }
}
