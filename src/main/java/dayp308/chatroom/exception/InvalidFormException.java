package dayp308.chatroom.exception;

import lombok.Getter;

@Getter
public class InvalidFormException extends Exception {

    private final int errorCode;

    public InvalidFormException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode.code;
    }

    public enum ErrorCode {
        INVALID_LOGIN_NAME(101),
        INVALID_NICKNAME(102),
        INVALID_PASSWORD(103),
        INVALID_TOKEN(104);

        private final int code;
        ErrorCode(int i) {
            this.code = i;
        }
    }
}
