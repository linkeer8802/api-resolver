package org.tangerine.apiassistant.sample.vo;

import java.io.Serializable;

/**
 * 客户端api请求响应 vo
 * @author weird
 *
 */
public class ClientApiRspVO implements Serializable {
	
	private static final long serialVersionUID = 8929816211415153436L;
	
	/**结果状态,默认是成功 e.g:true**/
    private Boolean result = true;
    /**错误代码,成功返回200 e.g:200**/
    private String errorcode = "";
    /**错误原因**/
    private String reason = "";
    /**响应业务数据**/
    private Object data = new EmptyData();
    
    public ClientApiRspVO() {}

    public ClientApiRspVO(Object result) {
    	this.errorcode = "200";
    	this.data = (result == null) ?  new EmptyData() : result;
    }
    
    public void respFailure(String reason) {
    	this.data = new EmptyData();
    	this.errorcode = "500";
    	this.reason = reason;
    	this.result = false;
    }
    
    public void respFailure(String errorcode, String reason) {
    	this.data = new EmptyData();
    	this.result = false;
    	this.errorcode = errorcode;
    	this.reason = reason;
    }

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	/**
	 * 空数据(json会格式化为data:{}形式)
	 * @author weird
	 *
	 */
	public static class EmptyData implements Serializable {
		private static final long serialVersionUID = -4843090473246922606L;
	}
}
