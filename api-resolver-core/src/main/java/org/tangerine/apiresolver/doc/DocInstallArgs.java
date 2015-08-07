package org.tangerine.apiresolver.doc;

import java.io.File;

public class DocInstallArgs {
	
	private String docName;
	
	private String version;
	
	private File installDir;
	
	private Boolean showResultExample;

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public File getInstallDir() {
		return installDir;
	}

	public void setInstallDir(File installDir) {
		this.installDir = installDir;
	}

	public Boolean getShowResultExample() {
		return showResultExample;
	}

	public void setShowResultExample(Boolean showResultExample) {
		this.showResultExample = showResultExample;
	}
}
