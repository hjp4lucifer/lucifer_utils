package cn.lucifer.util.http;

public class HttpException extends Exception {

    private static final long serialVersionUID = -9095702616213992961L;
    
    protected int httpStatus;

    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }
    
    public HttpException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
    
    public HttpException(Throwable cause) {
        super(cause);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

	/**
	 * @return the {@link #httpStatus}
	 */
	public int getHttpStatus() {
		return httpStatus;
	}

}