package org.tangerine.apiresolver.doc;



public abstract class AbstractApiDocInstaller implements ApiDocInstaller {

	protected DocInstallArgs docInstallArgs;
	
	public AbstractApiDocInstaller(DocInstallArgs docInstallArgs) {
		this.docInstallArgs = docInstallArgs;
	}

	public abstract void init() throws Exception;
	
	public void completed() throws Exception {}
	
	public abstract void destroy() throws Exception;
	
	protected void install() throws Exception {
		
		init();
		
		installCategorys(DocContainer.get().getApiCategorys());
		installApiDocs(DocContainer.get().getApiDocs());
		installApiTypeDocs(DocContainer.get().getApiTypeDocs());
		
		completed();
		destroy();
	}
}
