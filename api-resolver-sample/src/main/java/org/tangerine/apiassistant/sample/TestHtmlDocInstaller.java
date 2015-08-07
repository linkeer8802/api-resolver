package org.tangerine.apiassistant.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tangerine.apiresolver.doc.DocInstallArgs;
import org.tangerine.apiresolver.doc.entity.ApiTypeDoc;
import org.tangerine.apiresolver.doc.support.SimpleHtmlDocInstaller;

public class TestHtmlDocInstaller extends SimpleHtmlDocInstaller {

	public TestHtmlDocInstaller(DocInstallArgs docInstallArgs) throws Exception {
		super(docInstallArgs);
	}

	@Override
	public void installApiTypeDocs(List<ApiTypeDoc> apiTypeDocs) throws Exception {
		for (ApiTypeDoc type : apiTypeDocs) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("apiTypeDoc", type);
			
			createStaticFile(data, "api-type.ftl", htmlInstallDir, "api-type-" + type.getName() + ".html");
		}
	}
}
