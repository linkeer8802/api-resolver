package org.tangerine.apiresolver.doc.entity;

/**
 * @author weird
 *
 */
public class RefTypeDoc {

	private String name;
	
	private Boolean isSimpleType;

	public RefTypeDoc(String name, Boolean isSimpleType) {
		this.name = name;
		this.isSimpleType = isSimpleType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsSimpleType() {
		return isSimpleType;
	}

	public void setIsSimpleType(Boolean isSimpleType) {
		this.isSimpleType = isSimpleType;
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
		RefTypeDoc other = (RefTypeDoc) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
