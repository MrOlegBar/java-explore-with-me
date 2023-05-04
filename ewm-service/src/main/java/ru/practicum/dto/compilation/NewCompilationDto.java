package ru.practicum.dto.compilation;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.constraintGroup.Post;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Getter
@Setter
public class NewCompilationDto {
    private Boolean pinned;
    @NotBlank(message = "Заголовок подборки отсутствует или представлен пустым символом.",
            groups = {Post.class})
    private String title;
    private Collection<Long> events;
}