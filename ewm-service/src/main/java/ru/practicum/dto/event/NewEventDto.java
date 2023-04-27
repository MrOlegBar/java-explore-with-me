package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.constraintGroup.Post;
import ru.practicum.model.event.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NewEventDto {
    @NotNull(message = "Краткое описание события отсутствует.",
            groups = {Post.class})
    @Size(min = 20, max = 2000, message = "Количество символов в кратком описании должно быть в пределах 20-2000.",
            groups = {Post.class, Patch.class})
    private String annotation;
    @NotNull(message = "ID категории мероприятия отсутствует.",
            groups = {Post.class})
    private Long category;
    @NotNull(message = "Полное описание события отсутствует.",
            groups = {Post.class})
    @Size(min = 20, max = 7000, message = "Количество символов в полном описании должно быть в пределах 20-7000.",
            groups = {Post.class, Patch.class})
    private String description;
    @NotNull(message = "Дата и время на которые намечено событие отсутствует.",
            groups = {Post.class})
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(message = "Широта и долгота места проведения события отсутствуют.",
            groups = {Post.class})
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @NotNull(message = "Заголовок события отсутствует.",
            groups = {Post.class})
    @Size(min = 20, max = 7000, message = "Количество символов в заголовке события должно быть в пределах 3-120.",
            groups = {Post.class, Patch.class})
    private String title;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW,
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}
