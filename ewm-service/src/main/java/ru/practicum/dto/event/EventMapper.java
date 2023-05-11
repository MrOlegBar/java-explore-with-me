package ru.practicum.dto.event;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    Converter<NewEventDto, Event> toNewEventDtoEventConverter() {
        return mappingContext -> {
            NewEventDto newEventDto = mappingContext.getSource();
            Event event = mappingContext.getDestination();

            event.setCategory(categoryRepository.findById(newEventDto.getCategory()).orElse(null));

            NewEventDto.StateAction stateAction = newEventDto.getStateAction();

            if (stateAction != null && stateAction.equals(NewEventDto.StateAction.PUBLISH_EVENT)) {
                event.setState(Event.EventStatus.PUBLISHED);
            }
            if (stateAction != null && stateAction.equals(NewEventDto.StateAction.REJECT_EVENT)) {
                event.setState(Event.EventStatus.CANCELED);
            }

            event.setState(Event.EventStatus.PENDING);
            return event;
        };
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(NewEventDto.class, Event.class)
                .addMappings(modelMapper -> modelMapper.skip(Event::setState))
                .addMappings(modelMapper -> modelMapper.skip(Event::setCategory))
                .setPostConverter(toNewEventDtoEventConverter());
    }

    public Event toEvent(NewEventDto newEventDto) {
        return modelMapper.map(Objects.requireNonNull(newEventDto), Event.class);
    }

    public EventDto toEventDto(Event event) {
        return modelMapper.map(Objects.requireNonNull(event), EventDto.class);
    }

    public ShortEventDto toShortEventDto(Event event) {
        return modelMapper.map(Objects.requireNonNull(event), ShortEventDto.class);
    }

    public Collection<ShortEventDto> toShortEventDtoList(Collection<Event> eventCollection) {
        return eventCollection
                .stream()
                .map(this::toShortEventDto)
                .collect(Collectors.toList());
    }

    public Collection<EventDto> toEventDtoList(Collection<Event> eventCollection) {
        return eventCollection
                .stream()
                .map(this::toEventDto)
                .collect(Collectors.toList());
    }
}