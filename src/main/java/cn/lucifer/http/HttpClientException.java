package cn.lucifer.http;

public class HttpClientException extends RuntimeException {

    private static final long serialVersionUID = -9095702616213992961L;
    
    protected int httpStatus;

    public HttpClientException() {
        super();
    }

    public HttpClientException(String message) {
        super(message);
    }
    
    public HttpClientException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
    
    public HttpClientException(Throwable cause) {
        super(cause);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

	/**
	 * @return the {@link #httpStatus}
	 */
	public int getHttpStatus() {
		return httpStatus;
	}

}