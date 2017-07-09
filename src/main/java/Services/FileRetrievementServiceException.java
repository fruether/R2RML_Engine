package Services;

/**
 * Created by freddy on 08.07.17.
 */
public class FileRetrievementServiceException extends Throwable{
	private Exception exception;
	private String method;
	private String message;
	private String uri;
	
	public FileRetrievementServiceException(Exception exception, String method, String message, String uri) {
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public String getMethod() {
		return method;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void printError() {
		System.out.println("Error inside XSDCheckPlugin.bodyCall because of a call to FileRetrievementService " + getMethod());
		System.out.println("The error message is: " +getMessage());
	}
	
}
