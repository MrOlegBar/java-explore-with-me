package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllBy(Pageable pageable);

    List<Compilation> findAllByPinnedIsTrue(Pageable pageable);

    List<Compilation> findAllByPinnedIsFalse(Pageable pageable);
}