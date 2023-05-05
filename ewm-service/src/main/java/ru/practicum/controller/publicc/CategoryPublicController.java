package ru.practicum.controller.publicc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.service.category.CategoryService;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable @NotNull Long catId) throws NotFoundException {
        Category categoryForDto = categoryService.getCategoryByIdOrElseThrow(catId);

        return CategoryMapper.toCategoryDto(categoryForDto);
    }

    @GetMapping("/categories")
    public Collection<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        Collection<Category> categoryCollectionForDto = categoryService.getCategories(from, size);
        return CategoryMapper.toCategoryDtoList(categoryCollectionForDto);
    }
}