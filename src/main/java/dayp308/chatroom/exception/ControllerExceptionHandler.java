package dayp308.chatroom.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import dayp308.chatroom.entity.view.ErrorResponseBody;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(InvalidFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String invalidRegistrationForm(final InvalidFormException e) {
        return new ErrorResponseBody(e.getErrorCode(), e.getMessage()).toString();
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String userExists(final EntityExistsException e) {
        return new ErrorResponseBody(100, e.getMessage()).toString();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFound( final EntityNotFoundException e) {
        return new ErrorResponseBody(404, e.getMessage()).toString();
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String jsonProcessingException(final JsonProcessingException e) {
        return new ErrorResponseBody(500, e.getMessage()).toString();
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String fileUploadException(final FileUploadException e) {
        return new ErrorResponseBody(500, e.getMessage()).toString();
    }
}
