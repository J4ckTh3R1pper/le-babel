package dayp308.chatroom.exception;

public class MemberNotExistException extends AbstractCodedException {

    public MemberNotExistException(String message) {
        super(message);
    }

    @Override
    int getErrCode() {
        return 1103;
    }
}
