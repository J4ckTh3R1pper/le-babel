package dayp308.chatroom.validator;

import dayp308.chatroom.annotation.CategoryIdExists;
import dayp308.chatroom.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryIdValidator implements ConstraintValidator<CategoryIdExists, Integer> {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryIdValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void initialize(CategoryIdExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return categoryRepository.findById(value).isPresent();
    }
}
