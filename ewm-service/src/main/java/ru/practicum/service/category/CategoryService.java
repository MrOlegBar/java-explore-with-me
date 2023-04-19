package ru.practicum.service.category;

import ru.practicum.error.NotFoundException;
import ru.practicum.model.Category;

import java.util.Collection;

public interface CategoryService {
    Category save(Category category);

    Category getCategoryByIdOrElseThrow(long catId) throws NotFoundException;

    Collection<Category> getCategories(int from, int size);

    Boolean deleteCategory(long catId) throws NotFoundException;
}
