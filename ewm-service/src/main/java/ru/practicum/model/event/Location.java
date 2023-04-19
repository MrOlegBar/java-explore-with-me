package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {
    @NotNull(message = "Широта места проведения события отсутствует.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Широта не соответсвует формату 'хх.хххххх'.")
    private Float lat;
    @NotNull(message = "Долгота места проведения события отсутствует.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Долгота не соответсвует формату 'хх.хххххх'.")
    private Float lon;
}