package dayp308.lebabel.exception;

public class CategoryDeletedException extends AbstractCodedException {
  public final int errCode = 1102;
    public CategoryDeletedException(String message) {
        super(message);
    }

  @Override
  int getErrCode() {
    return errCode;
  }

}
