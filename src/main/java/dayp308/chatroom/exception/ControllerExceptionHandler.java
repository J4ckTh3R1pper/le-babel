package dayp308.chatroom.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import dayp308.chatroom.entity.view.ErrorResponseBody;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(InvalidFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String invalidRegistrationForm(final InvalidFormException e) {
        return new ErrorResponseBody(e.getErrCode(), e.getMessage()).toString();
    }

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String userExists(final UserExistsException e) {
        return new ErrorResponseBody(e.getErrCode(), e.getMessage()).toString();
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String entityExists(final EntityExistsException e) {
        return new ErrorResponseBody(409, e.getMessage()).toString();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFound( final EntityNotFoundException e) {
        return new ErrorResponseBody(404, e.getMessage()).toString();
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFound( final NoResultException e) {
        return new ErrorResponseBody(404, e.getMessage()).toString();
    }
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFound( final NoSuchElementException e) {
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

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String authorizationDeniedException(final AuthorizationDeniedException e) {
        return new ErrorResponseBody(401, e.getMessage()).toString();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String accessDeniedException(final AccessDeniedException e) {
        return new ErrorResponseBody(403, e.getMessage()).toString();
    }
}
