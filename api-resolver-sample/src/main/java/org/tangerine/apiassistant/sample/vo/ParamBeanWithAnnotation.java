package org.tangerine.apiassistant.sample.vo;

import org.tangerine.apiresolver.annotation.Param;

public class ParamBeanWithAnnotation {
	
	/**字符串类型参数 e.g:hello**/
	@Param("paramStr")
	private String paramStr;
	
	/**整形类型参数 e.g:100**/
	@Param("paramInt")
	private Integer paramInt;
	
	/**布尔类型参数 e.g:true**/
	@Param(value="paramBool", defaultValue="true")
	private Boolean paramBool;
	
	public ParamBeanWithAnnotation() {
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
		return "ParamBeanWithAnnotation [paramStr=" + paramStr + ", paramInt=" + paramInt + ", paramBool="
				+ paramBool + "]";
	}
}
