package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.service.event.EventService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Override
    public Compilation save(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation getCompilationByIdOrElseThrow(long compId) throws NotFoundException {
        return compilationRepository.findById(compId).orElseThrow(() -> {
            log.debug("Подборка событий с compId = {} не найдена.", compId);
            throw new NotFoundException(String.format("Подборка событий с compId = %s не найдена.",
                    compId));
        });
    }

    @Override
    public Boolean deleteCompilation(long compId) throws NotFoundException {
        compilationRepository.deleteById(compId);
        return !compilationRepository.existsById(compId);
    }

    @Override
    public void patchCompilation(NewCompilationDto newCompilationDto, Compilation compilation) {
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }

        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }

        if (newCompilationDto.getEvents() != null) {
            Set<Event> events = new HashSet<>();

            for (Long eventId : newCompilationDto.getEvents()) {
                events.add(eventService.getEventByIdOrElseThrow(eventId));
            }

            compilation.setEvents(events);
        }
    }

    @Override
    public Collection<Compilation> getCompilations(Boolean pinned, int from, int size) {
        if (pinned == null) {
            return compilationRepository.findAllBy(PageRequest.of(from, size));
        }
        if (pinned) {
            return compilationRepository.findAllByPinnedIsTrue(PageRequest.of(from, size));
        } else {
            return compilationRepository.findAllByPinnedIsFalse(PageRequest.of(from, size));
        }
    }
}