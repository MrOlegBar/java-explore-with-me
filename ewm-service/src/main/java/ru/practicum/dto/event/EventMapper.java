package ru.practicum.dto.event;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    private static ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public EventMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        modelMapper = new ModelMapper();
    }

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

    public static Event toEvent(NewEventDto newEventDto) {
        return modelMapper.map(Objects.requireNonNull(newEventDto), Event.class);
    }

    public static EventDto toEventDto(Event event) {
        return modelMapper.map(Objects.requireNonNull(event), EventDto.class);
    }

    public static ShortEventDto toShortEventDto(Event event) {
        return modelMapper.map(Objects.requireNonNull(event), ShortEventDto.class);
    }

    public static Collection<ShortEventDto> toShortEventDtoList(Collection<Event> eventCollection) {
        return eventCollection
                .stream()
                .map(EventMapper::toShortEventDto)
                .collect(Collectors.toList());
    }

    public static Collection<EventDto> toEventDtoList(Collection<Event> eventCollection) {
        return eventCollection
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }
}