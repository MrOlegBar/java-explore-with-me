package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.constraintGroup.Post;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.event.EventService;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminController {
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CategoryMapper categoryMapper;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@Validated({Post.class}) @RequestBody NewCategoryDto newCategoryDto) {
        Category categoryFromDto = categoryMapper.toCategory(newCategoryDto);

        Category categoryForDto = categoryService.save(categoryFromDto);
        return categoryMapper.toCategoryDto(categoryForDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteCategories(@PathVariable @NotNull Long catId) {
        categoryService.getCategoryByIdOrElseThrow(catId);

        if (eventService.getEventsByCategoryId(catId).isEmpty()) {
            return categoryService.deleteCategory(catId);
        } else {
            log.debug("Категория события с catId = {} используется событыями.", catId);
            throw new ConflictException(String.format("Категория события с catId = %s используется событыями.",
                    catId));
        }
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto patchCategory(@PathVariable @NotNull Long catId,
                                     @Validated({Patch.class}) @RequestBody NewCategoryDto newCategoryDto) throws NotFoundException {
        Category category = categoryService.getCategoryByIdOrElseThrow(catId);
        category.setName(newCategoryDto.getName());

        Category categoryForDto = categoryService.save(category);
        return categoryMapper.toCategoryDto(categoryForDto);
    }
}