package org.tangerine.apiresolver.doc.entity;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * api返回结果(必须)
 * @author weird
 */
public class ResultDoc {
	
	private String name;
	
	private String type;
	
	private Boolean isSimpleType;
	
	private Set<RefTypeDoc> refTypes;
	
	private String exampleValue;
	
	private String desc;
	
	public ResultDoc() {
		refTypes = new LinkedHashSet<RefTypeDoc>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Boolean getIsSimpleType() {
		return isSimpleType;
	}
	
	public void setIsSimpleType(Boolean isSimpleType) {
		this.isSimpleType = isSimpleType;
	}

	public Set<RefTypeDoc> getRefTypes() {
		return refTypes;
	}

	public Set<RefTypeDoc> addRefType(RefTypeDoc refType) {
		refTypes.add(refType);
		return refTypes;
	}
	
	public Set<RefTypeDoc> addRefTypes(List<RefTypeDoc> refTypes) {
		this.refTypes.addAll(refTypes);
		return this.refTypes;
	}

	public String getExampleValue() {
		return exampleValue;
	}

	public void setExampleValue(String exampleValue) {
		this.exampleValue = exampleValue;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
