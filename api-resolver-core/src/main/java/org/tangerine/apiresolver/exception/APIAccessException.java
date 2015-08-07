package org.tangerine.apiresolver.exception;
/**
 * API访问异常
 * @author weird
 *
 */
public class APIAccessException extends Exception {
	
	private static final long serialVersionUID = 5803052883689830591L;

	public APIAccessException() {
        super();
    }

    public APIAccessException(String s) {
        super(s);
    }

    public APIAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIAccessException(Throwable cause) {
        super(cause);
    }
}
