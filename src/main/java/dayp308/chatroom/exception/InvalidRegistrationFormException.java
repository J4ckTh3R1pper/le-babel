package dayp308.chatroom.exception;

import lombok.Getter;

@Getter
public class InvalidRegistrationFormException extends Exception {

    private final int errorCode;

    public InvalidRegistrationFormException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
