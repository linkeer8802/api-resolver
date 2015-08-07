package org.tangerine.apiresolver.doc.entity;

/**
 * API错误码 (可选)
 * @author weird
 */
public class ApiErrorDoc {

	private String errorCode; //错误码
	
	private String errorDesc; // 错误描述
	
	private String solution; //解决方案

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}
}
