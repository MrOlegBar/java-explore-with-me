package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Category;

import java.util.Collection;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Collection<Category> findAllBy(Pageable pageable);
}
