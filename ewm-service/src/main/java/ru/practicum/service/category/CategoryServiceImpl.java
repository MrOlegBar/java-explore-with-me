package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryByIdOrElseThrow(long catId) throws NotFoundException {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            log.debug("Категория события с catId = {} не найдена.", catId);
            throw new NotFoundException(String.format("Категория события с catId = %s не найдена.",
                    catId));
        });
    }

    @Override
    public Collection<Category> getCategories(int from, int size) {
        return categoryRepository.findAllBy(PageRequest.of(from, size));
    }

    @Override
    public Boolean deleteCategory(long catId) throws NotFoundException {
        categoryRepository.deleteById(catId);

        return !categoryRepository.existsById(catId);
    }
}
