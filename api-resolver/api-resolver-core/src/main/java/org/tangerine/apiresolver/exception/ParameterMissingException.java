package org.tangerine.apiresolver.exception;
/**
 * 缺少请求参数异常
 * @author weird
 *
 */
public class ParameterMissingException extends RuntimeException {

	private static final long serialVersionUID = 3864547389872050486L;

	public ParameterMissingException() {
        super();
    }

    public ParameterMissingException(String s) {
        super(s);
    }

    public ParameterMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterMissingException(Throwable cause) {
        super(cause);
    }
}
