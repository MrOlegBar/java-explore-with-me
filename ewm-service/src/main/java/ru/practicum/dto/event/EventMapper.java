package ru.practicum.dto.event;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.User;
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

            NewEventDto.StateAction stateAction = mappingContext.getSource().getStateAction();

            if (stateAction.equals(NewEventDto.StateAction.PUBLISH_EVENT)) {
                event.setState(Event.EventStatus.PUBLISHED);
            }
            if (stateAction.equals(NewEventDto.StateAction.REJECT_EVENT)) {
                event.setState(Event.EventStatus.CANCELED);
            }

            event.setState(Event.EventStatus.PENDING);
            return event;
        };
    }

    Converter<Category, CategoryDto> categoryCategoryDtoConverter = MappingContext::getDestination;

    Converter<User, UserShortDto> userUserShortDtoConverter = MappingContext::getDestination;

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(NewEventDto.class, Event.class)
                .addMappings(modelMapper -> modelMapper.skip(Event::setCategory))
                .addMappings(modelMapper -> modelMapper.skip(Event::setState))
                .setPostConverter(toNewEventDtoEventConverter());

        modelMapper.createTypeMap(Event.class, EventDto.class)
                .addMappings(modelMapper -> modelMapper.skip(EventDto::setCategory))
                .setPropertyConverter(categoryCategoryDtoConverter)
                .addMappings(modelMapper -> modelMapper.skip(EventDto::setInitiator))
                .setPropertyConverter(userUserShortDtoConverter);

        modelMapper.createTypeMap(Event.class, ShortEventDto.class)
                .addMappings(modelMapper -> modelMapper.skip(ShortEventDto::setCategory))
                .setPropertyConverter(categoryCategoryDtoConverter)
                .addMappings(modelMapper -> modelMapper.skip(ShortEventDto::setInitiator))
                .setPropertyConverter(userUserShortDtoConverter);
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return modelMapper.map(Objects.requireNonNull(newEventDto), Event.class);
    }

    public static EventDto toEventDto(Event event) {
        return modelMapper.map(Objects.requireNonNull(event), EventDto.class);
    }

    public static Collection<ShortEventDto> toShortEventDtoList(Collection<Event> eventCollection) {
        return eventCollection
                .stream()
                .map(eventForDto -> modelMapper.map(eventForDto, ShortEventDto.class))
                .collect(Collectors.toList());
    }

    public static Collection<EventDto> toEventDtoList(Collection<Event> eventCollection) {
        return eventCollection
                .stream()
                .map(eventForDto -> modelMapper.map(eventForDto, EventDto.class))
                .collect(Collectors.toList());
    }
}