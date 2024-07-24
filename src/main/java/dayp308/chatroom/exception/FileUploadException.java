package dayp308.chatroom.exception;


public class FileUploadException extends Exception {
	public FileUploadException(String msg) {
		super(msg);
	}

	public FileUploadException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
