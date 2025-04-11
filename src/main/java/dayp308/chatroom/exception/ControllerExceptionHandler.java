package dayp308.chatroom.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import dayp308.chatroom.entity.view.ErrorResponseBody;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(InvalidFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseBody invalidRegistrationForm(final InvalidFormException e) {
        return new ErrorResponseBody(e.getErrCode(), e.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseBody badRequest(final Exception e) {
        return new ErrorResponseBody(400, e.getMessage());
    }

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponseBody userExists(final UserExistsException e) {
        return new ErrorResponseBody(e.getErrCode(), e.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponseBody entityExists(final EntityExistsException e) {
        return new ErrorResponseBody(409, e.getMessage());
    }

    @ExceptionHandler({
        EntityNotFoundException.class,
        NoResultException.class,
        NoSuchElementException.class,
        NoResourceFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponseBody notFound( final Exception e) {
        return new ErrorResponseBody(404, e.getMessage());
    }

    @ExceptionHandler({
        JsonProcessingException.class,
        FileUploadException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseBody jsonProcessingException(final JsonProcessingException e) {
        return new ErrorResponseBody(500, e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponseBody authorizationDeniedException(final AuthorizationDeniedException e) {
        return new ErrorResponseBody(401, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponseBody accessDeniedException(final AccessDeniedException e) {
        return new ErrorResponseBody(403, e.getMessage());
    }
}
