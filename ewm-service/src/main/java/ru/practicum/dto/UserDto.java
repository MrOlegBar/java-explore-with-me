package ru.practicum.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.constraintGroup.Post;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя пользователя отсутствует или представлено пустым символом.",
            groups = {Post.class})
    private String name;
    @Email(message = "Email не соответствует формату электронной почты.",
            groups = {Post.class})
    @NotNull(message = "Электронная почта отсутствует.",
            groups = {Post.class})
    private String email;
}
