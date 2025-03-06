package dayp308.chatroom.validator;

import dayp308.chatroom.validator.annotation.PostIdExists;
import dayp308.chatroom.repository.PostRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PostIdValidator implements ConstraintValidator<PostIdExists, Long> {

    @Autowired
    private final PostRepository postRepository;

    public PostIdValidator(PostRepository postRepository) {
        super();
        this.postRepository = postRepository;
    }

    @Override
    public void initialize(PostIdExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return postRepository.findById(value).isPresent();
    }
}
