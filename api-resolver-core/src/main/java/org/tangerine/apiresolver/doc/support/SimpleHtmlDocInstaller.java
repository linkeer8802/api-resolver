package org.tangerine.apiresolver.doc.support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tangerine.apiresolver.doc.AbstractApiDocInstaller;
import org.tangerine.apiresolver.doc.DocContainer;
import org.tangerine.apiresolver.doc.DocInstallArgs;
import org.tangerine.apiresolver.doc.entity.ApiCategory;
import org.tangerine.apiresolver.doc.entity.ApiDoc;
import org.tangerine.apiresolver.doc.entity.ApiTypeDoc;
import org.tangerine.apiresolver.support.ProjectClassTemplateLoader;
import org.tangerine.apiresolver.util.FileUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SimpleHtmlDocInstaller extends AbstractApiDocInstaller {
	
	private Configuration cfg;
	
	protected File htmlInstallDir;
	
	public SimpleHtmlDocInstaller(DocInstallArgs docInstallArgs) throws Exception {
		super(docInstallArgs);
		cfg = new Configuration();
		htmlInstallDir = new File(docInstallArgs.getInstallDir(), "html");
	}
	
	@Override
	public void init() throws Exception {
		FileUtil.mkdir(htmlInstallDir);
		
		cfg.setSharedVariable("docInstallArgs", docInstallArgs);
		cfg.setTemplateLoader(new ProjectClassTemplateLoader("apiresolver/tpl"));
	}
	
	@Override
	public void destroy() throws Exception {
	}
	
	/* (non-Javadoc)
	 * @see org.tangerine.apiresolver.doc.support.ApiDocInstall#installCategorys(java.util.List)
	 */
	@Override
	public void installCategorys(List<ApiCategory> apiCategorys) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("apiCategorys", apiCategorys);
		
		createStaticFile(data, "api-list.ftl", htmlInstallDir, "api-list.html");
		
		installSummary(apiCategorys);
	}
	
	public void installSummary(List<ApiCategory> apiCategorys) throws Exception {
		for (ApiCategory category : apiCategorys) {
			List<ApiDoc> apiSummarys = DocContainer.get().getApiDocQuery().queryByCategory(category.getCid()); 
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("apiCategory", category);
			data.put("apiSummarys", apiSummarys);
			
			createStaticFile(data, "api-summary.ftl", htmlInstallDir, "api-summary-" + category.getCid() + ".html");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tangerine.apiresolver.doc.support.ApiDocInstall#installApiDocs(java.util.List)
	 */
	@Override
	public void installApiDocs(List<ApiDoc> apiDocs) throws Exception {
		for (ApiDoc apiDoc : apiDocs) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("apiDoc", apiDoc);
			System.out.println(apiDoc.getName());
			createStaticFile(data, "api-doc.ftl", htmlInstallDir, "api-doc-" + apiDoc.getId() + ".html");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tangerine.apiresolver.doc.support.ApiDocInstall#installApiTypeDocs(java.util.List)
	 */
	@Override
	public void installApiTypeDocs(List<ApiTypeDoc> apiTypeDocs) throws Exception {
		for (ApiTypeDoc type : apiTypeDocs) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("apiTypeDoc", type);
			
			createStaticFile(data, "api-type.ftl", htmlInstallDir, "api-type-" + type.getName() + ".html");
		}
	}
	
	protected void createStaticFile(Map<String, Object> data, String name, File baseDir, String targetFile) throws Exception {
		
		Template template = cfg.getTemplate(name);
		template.setEncoding("UTF-8");
		File target = new File(baseDir.getAbsoluteFile() + File.separator + targetFile);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
		
		template.process(data, out);
		out.flush();
		out.close();
	}
}
