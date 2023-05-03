package ru.practicum.dto.request;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.model.Request;

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

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Request.class, RequestDto.class)
                .addMapping(request -> request.getRequester().getId(), RequestDto::setRequester)
                .addMapping(request -> request.getEvent().getId(), RequestDto::setEvent);
    }

    public static RequestDto toRequestDto(Request request) {
        return modelMapper.map(Objects.requireNonNull(request), RequestDto.class);
    }

    public static Collection<RequestDto> toRequestDtoList(Collection<Request> requestCollection) {
        return requestCollection
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    public static RequestStatusDto toRequestStatusDto(Collection<Request> requestCollection) {
        RequestStatusDto requestStatusDto = new RequestStatusDto();

        Collection<Request> confirmedRequests = requestCollection.stream()
                .filter(request -> request.getStatus().equals(Request.RequestStatus.CONFIRMED))
                .collect(Collectors.toList());
        Collection<Request> rejectedRequests = requestCollection.stream()
                .filter(request -> request.getStatus().equals(Request.RequestStatus.REJECTED))
                .collect(Collectors.toList());

        Collection<RequestDto> confirmedRequestDto = toRequestDtoList(confirmedRequests);
        Collection<RequestDto> rejectedRequestDto = toRequestDtoList(rejectedRequests);

        requestStatusDto.setConfirmedRequests(confirmedRequestDto);
        requestStatusDto.setRejectedRequests(rejectedRequestDto);

        return requestStatusDto;
    }
}
