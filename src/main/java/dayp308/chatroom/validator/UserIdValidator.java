package dayp308.chatroom.validator;

import dayp308.chatroom.annotation.UserIdExists;
import dayp308.chatroom.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIdValidator implements ConstraintValidator<UserIdExists, Long> {

    private final UserRepository userRepository;

    @Autowired
    public UserIdValidator(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UserIdExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if ( value == null ) return false;
        return userRepository.findById(value).isPresent();
    }
}
