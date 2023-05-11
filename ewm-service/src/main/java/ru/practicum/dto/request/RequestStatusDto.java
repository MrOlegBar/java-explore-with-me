package ru.practicum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class RequestStatusDto {
    Collection<RequestDto> confirmedRequests;
    Collection<RequestDto> rejectedRequests;
}