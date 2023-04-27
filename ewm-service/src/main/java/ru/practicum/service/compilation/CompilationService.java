package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Compilation;

public interface CompilationService {
    Compilation save(Compilation compilation);

    Compilation getCompilationByIdOrElseThrow(long compId) throws NotFoundException;

    Boolean deleteCompilation(long compId) throws NotFoundException;

    void patchCompilation(NewCompilationDto newCompilationDto, Compilation compilation);
}
