package ru.practicum.dto.category;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public CategoryMapper() {
        modelMapper = new ModelMapper();
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return modelMapper.map(Objects.requireNonNull(newCategoryDto), Category.class);
    }

    public static CategoryDto toCategoryDto(Category category) {
        return modelMapper.map(Objects.requireNonNull(category), CategoryDto.class);
    }

    public static List<CategoryDto> toCategoryDtoList(Collection<Category> categoryCollection) {
        return categoryCollection
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}