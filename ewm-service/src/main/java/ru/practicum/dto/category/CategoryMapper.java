package ru.practicum.dto.category;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public Category toCategory(NewCategoryDto newCategoryDto) {
        return modelMapper.map(Objects.requireNonNull(newCategoryDto), Category.class);
    }

    public CategoryDto toCategoryDto(Category category) {
        return modelMapper.map(Objects.requireNonNull(category), CategoryDto.class);
    }

    public List<CategoryDto> toCategoryDtoList(Collection<Category> categoryCollection) {
        return categoryCollection
                .stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }
}