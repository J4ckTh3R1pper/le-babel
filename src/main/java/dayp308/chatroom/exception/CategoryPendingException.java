package dayp308.chatroom.exception;

public class CategoryPendingException extends AbstractCodedException {
    public final String message = "category is currently pending.";
    public final int errCode = 1100;
    public CategoryPendingException(String message) {
        super(message);
    }

  @Override
  int getErrCode() {
    return errCode;
  }

}
