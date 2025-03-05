package dayp308.chatroom.exception;


public class FileUploadException extends AbstractCodedException {
	public FileUploadException(String message) {
		super(message);
	}

	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	int getErrCode() {
		return 1201;
	}
}
