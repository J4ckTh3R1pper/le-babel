package dayp308.chatroom.exception;

public class UserExistsException extends AbstractCodedException {
    public UserExistsException(String message) {
        super(message);
    }

  @Override
  int getErrCode() {
    return 1001;
  }
}
