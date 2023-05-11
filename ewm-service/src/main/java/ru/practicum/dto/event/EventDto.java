package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.ShortUserDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private ShortUserDto initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit = 0L;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration = true;
    private Event.EventStatus state;
    private String title;
    private Long views;
}