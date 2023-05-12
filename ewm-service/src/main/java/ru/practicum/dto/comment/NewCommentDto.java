package ru.practicum.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.constraintGroup.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank(message = "Текст комментария отсутствует или представлен пустым символом.", groups = Post.class)
    @Size(min = 3, max = 1000)
    private String text;
}