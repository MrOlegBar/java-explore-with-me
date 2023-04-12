package ru.practicum.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "main_service")
@Getter
public class Main {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
