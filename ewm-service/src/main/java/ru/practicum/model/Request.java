package ru.practicum.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "request_event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "request_requester_id")
    private User requester;
    @Column(name = "request_created")
    @CreationTimestamp
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus status;

    public enum RequestStatus {
        PENDING,
        CANCELED,
        CONFIRMED,
        REJECTED
    }
}