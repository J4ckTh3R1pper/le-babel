package dayp308.lebabel.exception;

import java.time.Instant;

public class UserMutedException extends AbstractCodedException {
    public final int errCode = 1103;
    public final Instant expirationDate;
    public UserMutedException(String message, Instant expirationDate) {
        super(message);
        this.expirationDate = expirationDate;
    }
  @Override
  int getErrCode() {
    return errCode;
  }
}
