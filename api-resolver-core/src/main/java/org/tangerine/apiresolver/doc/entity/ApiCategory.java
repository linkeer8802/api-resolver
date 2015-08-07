package org.tangerine.apiresolver.doc.entity;

/**
 * API类目
 * @author weird
 */
public class ApiCategory {

	private String cid; //id
	
	private String name; //API类目名称
	
	private String desc; //API类目说明

	public String getCid() {
		return cid;
	}
	
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
