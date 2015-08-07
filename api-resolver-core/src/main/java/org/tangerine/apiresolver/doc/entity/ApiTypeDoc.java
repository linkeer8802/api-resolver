package org.tangerine.apiresolver.doc.entity;

import java.util.List;

/**
 * api返回结果类型信息(可选)
 * @author weird
 */
public class ApiTypeDoc {

	private String name; //类型名称
	
	private String qualifiedTypeName; //限定类型名
	
	private String desc; //类型描述
	
	private List<ResultDoc> attrs; //类型属性列表信息

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getQualifiedTypeName() {
		return qualifiedTypeName;
	}
	
	public void setQualifiedTypeName(String qualifiedTypeName) {
		this.qualifiedTypeName = qualifiedTypeName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ResultDoc> getAttrs() {
		return attrs;
	}
	
	public void setAttrs(List<ResultDoc> attrs) {
		this.attrs = attrs;
	}
}
