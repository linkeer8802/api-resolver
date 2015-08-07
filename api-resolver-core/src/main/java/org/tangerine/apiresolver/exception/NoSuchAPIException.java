package org.tangerine.apiresolver.exception;
/**
 * 没有API接口实现异常
 * @author weird
 *
 */
public class NoSuchAPIException extends Exception {
	
	private static final long serialVersionUID = 5803052883689830591L;

	public NoSuchAPIException() {
        super();
    }

    public NoSuchAPIException(String s) {
        super(s);
    }

    public NoSuchAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchAPIException(Throwable cause) {
        super(cause);
    }
}
