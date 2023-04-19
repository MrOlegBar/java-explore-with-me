package ru.practicum.model.event;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.Category;
import ru.practicum.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Column(name = "event_annotation")
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "event_category_id")
    private Category category;
    @Column(name = "event_confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "event_created_on")
    private LocalDateTime createdOn;
    @Column(name = "event_description")
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "event_initiator_id")
    private User initiator;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "lat", column = @Column(name = "event_location_lat")),
            @AttributeOverride( name = "lon", column = @Column(name = "event_location_lon")),
    })
    private Location location;
    @Column(name = "event_paid")
    private Boolean paid;
    @Column(name = "event_participant_limit")
    private Long participantLimit;
    @Column(name = "event_published_on")
    private LocalDateTime publishedOn;
    @Column(name = "event_request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "event_state")
    private EventStatus state;
    @Column(name = "event_title")
    private String title;
    @Column(name = "event_views")
    private Long views;
}
