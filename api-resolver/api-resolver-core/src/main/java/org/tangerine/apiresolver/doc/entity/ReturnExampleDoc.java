package org.tangerine.apiresolver.doc.entity;
/**
 * 	返回示例(可选)
 * @author weird
 */
public class ReturnExampleDoc {

	private String exampleUrl; //api请求URL示例
	
	private String exampleDesc; //返回示例结果说明

	public String getExampleUrl() {
		return exampleUrl;
	}

	public void setExampleUrl(String exampleUrl) {
		this.exampleUrl = exampleUrl;
	}

	public String getExampleDesc() {
		return exampleDesc;
	}

	public void setExampleDesc(String exampleDesc) {
		this.exampleDesc = exampleDesc;
	}
}
