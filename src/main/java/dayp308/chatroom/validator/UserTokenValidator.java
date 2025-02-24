package dayp308.chatroom.validator;

import dayp308.chatroom.annotation.UserTokenExists;
import dayp308.chatroom.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTokenValidator implements ConstraintValidator<UserTokenExists, String> {
    private final UserRepository userRepository;

    @Autowired
    public UserTokenValidator(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }
    @Override
    public void initialize(UserTokenExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return userRepository.findByToken(value).isPresent();
    }
}
