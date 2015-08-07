package org.tangerine.apiassistant.sample.vo;

public class ParamBean {
	/**
	 * 字符串类型参数 e.g:hello
	 */
	private String paramStr;
	/**
	 * 整形类型参数 e.g:100
	 */
	private Integer paramInt;
	/**
	 * 布尔类型参数 e.g:true
	 */
	private Boolean paramBool;
	
	public ParamBean() {
	}
	
	public String getParamStr() {
		return paramStr;
	}
	
	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}
	
	public Integer getParamInt() {
		return paramInt;
	}
	
	public void setParamInt(Integer paramInt) {
		this.paramInt = paramInt;
	}
	
	public Boolean getParamBool() {
		return paramBool;
	}
	
	public void setParamBool(Boolean paramBool) {
		this.paramBool = paramBool;
	}

	@Override
	public String toString() {
		return "ParamBean [paramStr=" + paramStr + ", paramInt=" + paramInt + ", paramBool=" + paramBool + "]";
	}
}
