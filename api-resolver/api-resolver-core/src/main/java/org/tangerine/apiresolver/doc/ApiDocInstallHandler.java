package org.tangerine.apiresolver.doc;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.tangerine.apiresolver.doc.entity.ApiDoc;
import org.tangerine.apiresolver.doc.support.DocInstallConf;
import org.tangerine.apiresolver.doc.support.DocInstallConf.DocInstaller;
import org.tangerine.apiresolver.support.ProjectClassLoader;
import org.tangerine.apiresolver.util.FileUtil;
import org.tangerine.apiresolver.util.JsonUtil;

import com.google.gson.reflect.TypeToken;

public class ApiDocInstallHandler {

	private Log logger;
	
	DocInstallArgs docInstallArgs;
	
	public ApiDocInstallHandler(Log logger, DocInstallArgs docInstallArgs) throws Exception {
		
		this.logger = logger;
		this.docInstallArgs = docInstallArgs;
	}

	private void init() throws Exception {
		File outputRootDir = docInstallArgs.getInstallDir();
		
		FileUtil.clearDir(outputRootDir);
		docInstallArgs.setInstallDir(outputRootDir);
		
		RefTypeResolver refTypeResolver = new RefTypeResolver();
		refTypeResolver.instantiationAllApiTypes();
		if (docInstallArgs.getShowResultExample()) {
			for (ApiDoc apiDoc : DocContainer.get().getApiDocs()) {
				apiDoc.setResultExample(refTypeResolver.resolveResultExample(apiDoc.getResultDoc()));
			}
			
		}
	}
	
	public void install() throws Exception {
		init();
		DocInstallConf conf = gtInstallConfFromJson();
		mergeConf(conf);
		
		for (DocInstaller docInstaller : conf.getInstallers()) {
			AbstractApiDocInstaller installer = (AbstractApiDocInstaller) 
					Class.forName(docInstaller.getInstaller())
					.getConstructor(DocInstallArgs.class).newInstance(docInstallArgs);
			
			logger.info("install " + docInstaller.getFormat() 
					+ " doc to " + docInstallArgs.getInstallDir().getAbsolutePath());
			installer.install();
		}
	}
	
	private void mergeConf(DocInstallConf conf) {
		if (StringUtils.isEmpty(docInstallArgs.getDocName())) {
			docInstallArgs.setDocName(conf.getDocName());
		}
		if (StringUtils.isEmpty(docInstallArgs.getVersion())) {
			docInstallArgs.setVersion(conf.getVersion());
		}
		if (docInstallArgs.getInstallDir() == null) {
			docInstallArgs.setInstallDir(new File(conf.getInstallDir()));
		}
		if (docInstallArgs.getShowResultExample() == null) {
			docInstallArgs.setShowResultExample(conf.getShowResultExample());
		}
	}

	private DocInstallConf gtInstallConfFromJson() throws Exception {
		InputStreamReader reader = null;
		try {
			URL url = ProjectClassLoader.R("apiresolver/docInstallConf.json");
			reader = new InputStreamReader(url.openStream(), "UTF-8");
			return JsonUtil.fromJson(reader, new TypeToken<DocInstallConf>(){}.getType());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}
}
