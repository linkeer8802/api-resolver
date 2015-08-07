package org.tangerine.apiresolver.doc.entity;
/**
 * api请求参数(必须)
 * @author weird
 */
public class ParamDoc {

	private String name;
	
	private String type;
	
	private String exampleValue;
	
	private String defaultValue;
	
	private Boolean required;
	
	private String desc;

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

	public String getExampleValue() {
		return exampleValue;
	}

	public void setExampleValue(String exampleValue) {
		this.exampleValue = exampleValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
