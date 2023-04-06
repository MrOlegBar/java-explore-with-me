package ru.practicum.explore.with.me.stat.server;

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
    private Long id;
    private String app;
    private URI uri;
    private String ip;
    @CreationTimestamp
    private LocalDateTime timestamp;
    private Long hits;

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