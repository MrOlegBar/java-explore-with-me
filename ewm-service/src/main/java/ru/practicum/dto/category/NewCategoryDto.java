package ru.practicum.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.constraintGroup.Post;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Имя категории отсутствует или представлено пустым символом.",
            groups = {Post.class, Patch.class})
    private String name;
}