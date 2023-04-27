package ru.practicum.controller.publicc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Compilation;
import ru.practicum.service.compilation.CompilationService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public Collection<CategoryDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(required = false, defaultValue = "0") int from,
                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        Collection<Compilation> compilationCollectionForDto = compilationService.getCategories(from, size);
        return CompilationMapper.toCategoryDtoList(compilationCollectionForDto);
    }
}
