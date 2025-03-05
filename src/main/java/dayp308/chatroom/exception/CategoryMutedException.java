package dayp308.chatroom.exception;

public class CategoryMutedException extends AbstractCodedException {
    public final String message = "category is currently muted.";
    public final int errCode = 1101;
    public CategoryMutedException(String message) {
        super(message);
    }

    @Override
    int getErrCode() {
        return errCode;
    }
}
