package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.constraintGroup.Post;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Validated({Post.class}) @RequestBody NewCompilationDto newCompilationDto) {
        Compilation compilationFromDto = CompilationMapper.toCompilation(newCompilationDto);

        Compilation compilationForDto = compilationService.save(compilationFromDto);
        return CompilationMapper.toCompilationDto(compilationForDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteCompilation(@PathVariable @NotNull Long compId) {
        compilationService.getCompilationByIdOrElseThrow(compId);

        return compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto patchCompilation(@PathVariable @NotNull Long compId,
                               @Validated({Patch.class}) @RequestBody NewCompilationDto newCompilationDto) throws NotFoundException {

        Compilation compilation = compilationService.getCompilationByIdOrElseThrow(compId);

        compilationService.patchCompilation(newCompilationDto, compilation);

        Compilation compilationForDto = compilationService.save(compilation);
        return CompilationMapper.toCompilationDto(compilationForDto);
    }
}
