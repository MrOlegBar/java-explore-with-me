package ru.practicum.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.constraintGroup.Post;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserShortDto {
    private Long id;
    @NotBlank(message = "Имя пользователя отсутствует или представлено пустым символом.",
            groups = {Post.class})
    private String name;
}