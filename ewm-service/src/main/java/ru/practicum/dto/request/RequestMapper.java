package ru.practicum.dto.request;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.model.Request;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestMapper {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Request.class, RequestDto.class)
                .addMapping(request -> request.getRequester().getId(), RequestDto::setRequester)
                .addMapping(request -> request.getEvent().getId(), RequestDto::setEvent);
    }

    public RequestDto toRequestDto(Request request) {
        return modelMapper.map(Objects.requireNonNull(request), RequestDto.class);
    }

    public Collection<RequestDto> toRequestDtoList(Collection<Request> requestCollection) {
        return requestCollection
                .stream()
                .map(this::toRequestDto)
                .collect(Collectors.toList());
    }

    public RequestStatusDto toRequestStatusDto(Collection<Request> requestCollection) {
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