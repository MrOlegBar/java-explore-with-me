package ru.practicum.dto.request;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RequestMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public RequestMapper() {
        modelMapper = new ModelMapper();
    }

    Converter<User, Long> userLongConverter = mappingContext -> mappingContext.getSource().getId();

    Converter<Event, Long> eventLongConverter = mappingContext -> mappingContext.getSource().getId();

    Converter<Collection<Request>, Collection<RequestDto>> confirmedRequestsConverter = mappingContext ->
            toRequestDtoList(mappingContext.getSource().stream()
                    .filter(request -> request.getStatus().equals(Request.RequestStatus.CONFIRMED))
                    .collect(Collectors.toList()));

    Converter<Collection<Request>, Collection<RequestDto>> rejectedRequestsConverter = mappingContext ->
            toRequestDtoList(mappingContext.getSource().stream()
                    .filter(request -> request.getStatus().equals(Request.RequestStatus.REJECTED))
                    .collect(Collectors.toList()));

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Request.class, RequestDto.class)
                .addMappings(modelMapper -> modelMapper.skip(RequestDto::setRequester))
                .setPropertyConverter(userLongConverter)
                .addMappings(modelMapper -> modelMapper.skip(RequestDto::setEvent))
                .setPropertyConverter(eventLongConverter);

        modelMapper.createTypeMap(Collection.class, RequestStatusDto.class)
                .addMappings(modelMapper -> modelMapper.skip(RequestStatusDto::setConfirmedRequests))
                .setPropertyConverter(confirmedRequestsConverter)
                .addMappings(modelMapper -> modelMapper.skip(RequestStatusDto::setRejectedRequests))
                .setPropertyConverter(rejectedRequestsConverter);
    }

    public static RequestDto toRequestDto(Request request) {
        return modelMapper.map(Objects.requireNonNull(request), RequestDto.class);
    }

    public static Collection<RequestDto> toRequestDtoList(Collection<Request> requestCollection) {
        return requestCollection
                .stream()
                .map(requestForDto -> modelMapper.map(requestForDto, RequestDto.class))
                .collect(Collectors.toList());
    }

    public static RequestStatusDto toRequestStatusDto(Collection<Request> requestCollection) {
        return modelMapper.map(Objects.requireNonNull(requestCollection), RequestStatusDto.class);
    }
}
