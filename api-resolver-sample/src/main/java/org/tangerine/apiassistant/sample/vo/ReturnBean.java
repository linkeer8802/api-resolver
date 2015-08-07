/**ReturnBean.java**/
package org.tangerine.apiassistant.sample.vo;

/**
 * 返回结果Bean, 仅供测试使用
 * @author weird
 */
public class ReturnBean {
	/**int类型属性 e.g:1**/
	private Integer retInt;
	
	/**字符串类型属性 **/
	private String retStr;
	
	/**Bool类型属性 e.g:true**/
	private Boolean retBool;
	
	/**客户端api请求响应 vo**/
	private ClientApiRspVO vo;

	public Integer getRetInt() {
		return retInt;
	}

	public void setRetInt(Integer retInt) {
		this.retInt = retInt;
	}

	public String getRetStr() {
		return retStr;
	}

	public void setRetStr(String retStr) {
		this.retStr = retStr;
	}

	public Boolean getRetBool() {
		return retBool;
	}

	public void setRetBool(Boolean retBool) {
		this.retBool = retBool;
	}
	
	public ClientApiRspVO getVo() {
		return vo;
	}
	
	public void setVo(ClientApiRspVO vo) {
		this.vo = vo;
	}
}
