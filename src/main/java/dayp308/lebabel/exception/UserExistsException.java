package dayp308.lebabel.exception;

public class UserExistsException extends AbstractCodedException {
    public UserExistsException(String message) {
        super(message);
    }

  @Override
  int getErrCode() {
    return 1001;
  }
}
