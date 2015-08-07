package org.tangerine.apiresolver.core;

import java.io.Serializable;

import org.tangerine.apiresolver.annotation.Param;

public class ApiParamMeta implements Serializable {

	private static final long serialVersionUID = -8921373376388070571L;

	private String name;
	
	private Boolean required;
	
	private String defaultValue;
	
	public ApiParamMeta() {}
	
	public ApiParamMeta(String name) {
		this.name = name;
		this.required = true;
	}
	
	public ApiParamMeta(String name, Boolean required, String defaultValue) {
		this.name = name;
		this.required = required;
		this.defaultValue = defaultValue;
	}
	
	public static ApiParamMeta getFromParam(Param annotationParam) {
		
		if (annotationParam == null) {
			return null;
		}
		
		ApiParamMeta apiParamMeta = new ApiParamMeta();
		apiParamMeta.setName(annotationParam.value());
		apiParamMeta.setRequired(annotationParam.required());
		apiParamMeta.setDefaultValue(annotationParam.defaultValue());
		
		return apiParamMeta;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiParamMeta other = (ApiParamMeta) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
