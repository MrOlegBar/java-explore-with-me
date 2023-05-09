package ru.practicum.controller.publicc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @GetMapping("/compilations")
    public Collection<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                      @RequestParam(required = false, defaultValue = "0") int from,
                                                      @RequestParam(required = false, defaultValue = "10") int size) {
        Collection<Compilation> compilationCollectionForDto = compilationService.getCompilations(pinned, from, size);
        return compilationMapper.toCompilationDtoList(compilationCollectionForDto);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable @NotNull Long compId) {

        Compilation compilationForDto = compilationService.getCompilationByIdOrElseThrow(compId);
        return compilationMapper.toCompilationDto(compilationForDto);
    }
}