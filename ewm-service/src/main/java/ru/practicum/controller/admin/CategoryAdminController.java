package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.constraintGroup.Post;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.service.category.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@Validated({Post.class}) @RequestBody NewCategoryDto newCategoryDto) {
        Category categoryFromDto = CategoryMapper.toCategory(newCategoryDto);

        Category categoryForDto = categoryService.save(categoryFromDto);
        return CategoryMapper.toCategoryDto(categoryForDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteCategories(@PathVariable Long catId) {
        categoryService.getCategoryByIdOrElseThrow(catId);

        return categoryService.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto patchCategory(@PathVariable Long catId,
                                     @Validated({Patch.class}) @RequestBody NewCategoryDto newCategoryDto) throws NotFoundException {
        Category category = categoryService.getCategoryByIdOrElseThrow(catId);
        category.setName(newCategoryDto.getName());

        Category categoryForDto = categoryService.save(category);
        return CategoryMapper.toCategoryDto(categoryForDto);
    }
}
