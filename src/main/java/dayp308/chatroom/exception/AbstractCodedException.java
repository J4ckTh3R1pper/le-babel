package dayp308.chatroom.exception;

public abstract class AbstractCodedException extends RuntimeException {

    public AbstractCodedException(String message) {
        super(message);
    }

    public AbstractCodedException(String message, Throwable cause) {
        super(message, cause);
    }

    abstract int getErrCode();
}
