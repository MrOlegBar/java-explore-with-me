package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.model.Category;
import ru.practicum.model.Compilation;
import ru.practicum.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(name = "event_confirmed_requests", columnDefinition = "integer default 0")
    private Long confirmedRequests = 0L;
    @Column(name = "event_created_on")
    @CreationTimestamp
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
            @AttributeOverride(name = "lat", column = @Column(name = "event_location_lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "event_location_lon")),
    })
    private Location location;
    @Column(name = "event_paid")
    private Boolean paid;
    @Column(name = "event_participant_limit", columnDefinition = "integer default 0")
    private Long participantLimit = 0L;
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
    private Long views = 0L;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;

    public enum EventStatus {
        PENDING, PUBLISHED, CANCELED
    }

    public enum EventSort {
        EVENT_DATE, VIEWS
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        return id != null && id.equals(((Event) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}