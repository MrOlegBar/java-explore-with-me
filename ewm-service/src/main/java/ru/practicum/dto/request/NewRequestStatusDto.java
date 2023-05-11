package ru.practicum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.Request;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class NewRequestStatusDto {
    private Collection<Long> requestIds;
    private Request.RequestStatus status;
}