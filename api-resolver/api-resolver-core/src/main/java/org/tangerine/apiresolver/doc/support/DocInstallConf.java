package org.tangerine.apiresolver.doc.support;

import java.util.List;

public class DocInstallConf {
	
	private String docName;
	
	private String version;
	
	private String installDir;
	
	private String apiAccessBaseUrl;

	private Boolean showResultExample;
	
	private List<DocInstaller> installers;

	
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

	public String getInstallDir() {
		return installDir;
	}

	public void setInstallDir(String installDir) {
		this.installDir = installDir;
	}

	public String getApiAccessBaseUrl() {
		return apiAccessBaseUrl;
	}

	public void setApiAccessBaseUrl(String apiAccessBaseUrl) {
		this.apiAccessBaseUrl = apiAccessBaseUrl;
	}

	public Boolean getShowResultExample() {
		return showResultExample;
	}

	public void setShowResultExample(Boolean showResultExample) {
		this.showResultExample = showResultExample;
	}

	public List<DocInstaller> getInstallers() {
		return installers;
	}

	public void setInstallers(List<DocInstaller> installers) {
		this.installers = installers;
	}
	
	public static class DocInstaller {
		
		private String format;
		
		private String installer;

		public DocInstaller(String format, String installer) {
			this.format = format;
			this.installer = installer;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getInstaller() {
			return installer;
		}
		
		public void setInstaller(String installer) {
			this.installer = installer;
		}
	}
}

