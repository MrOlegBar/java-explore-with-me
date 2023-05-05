package ru.practicum.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.net.URI;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@Getter
@Setter
@NoArgsConstructor
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Long id;
    @Column(name = "stat_app")
    private String app;
    @Column(name = "stat_uri")
    private URI uri;
    @Column(name = "stat_ip")
    private String ip;
    @CreationTimestamp
    @Column(name = "stat_timestamp")
    private LocalDateTime timestamp;
    @Transient
    private Long hits;

    public Stat(String app, URI uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stat stat = (Stat) o;
        return (this.id != null && id.equals(stat.id));
    }

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}