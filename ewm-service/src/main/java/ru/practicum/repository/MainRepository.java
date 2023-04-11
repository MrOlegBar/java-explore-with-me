package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Main;

public interface MainRepository extends JpaRepository<Main, Long> {
}
