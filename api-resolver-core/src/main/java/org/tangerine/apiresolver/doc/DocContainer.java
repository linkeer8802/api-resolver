package org.tangerine.apiresolver.doc;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.tangerine.apiresolver.doc.entity.ApiCategory;
import org.tangerine.apiresolver.doc.entity.ApiDoc;
import org.tangerine.apiresolver.doc.entity.ApiTypeDoc;
import org.tangerine.apiresolver.support.ProjectClassLoader;
import org.tangerine.apiresolver.util.JsonUtil;

import com.google.gson.reflect.TypeToken;

public class DocContainer {
	
	private static DocContainer instance;
	
	private List<ApiCategory> apiCategorys;
	public List<ApiDoc> apiDocs;
	public List<ApiTypeDoc> apiTypeDocs;
	
	private ApiDocQuery apiDocQuery = new ApiDocQuery();
	private ApiTypeDocQuery apiTypeDocQuery = new ApiTypeDocQuery();
	
	
	private DocContainer() throws Exception {
		apiCategorys = getDocData(ApiDoclet.CATEGORY_FILE_PREFIX, new TypeToken<List<ApiCategory>>(){}.getType());
		apiDocs = getDocData(ApiDoclet.APIDOC_FILE_PREFIX, new TypeToken<List<ApiDoc>>(){}.getType());
		apiTypeDocs = getDocData(ApiDoclet.APITYPE_FILE_PREFIX, new TypeToken<List<ApiTypeDoc>>(){}.getType());
	}
	
	public synchronized static DocContainer get() throws Exception {
		if (instance == null) {
			instance = new DocContainer();
		}
		return instance;
	}
	
	@SuppressWarnings({ "unchecked"})
	private <T> List<T> getDocData(String prefix, Type typeOfT) throws Exception {
		List<T> result = new ArrayList<T>();
		Resource[] rs = new PathMatchingResourcePatternResolver(ProjectClassLoader.get())
					.getResources(ApiDoclet.DOC_DIR.replace(File.separator, "/")  + "/" + prefix + "*.json");
		if (rs.length > 0) {
			Map<String, URL> resourceMap = new HashMap<String, URL>(rs.length);
			for (Resource resource : rs) {
				resourceMap.put(resource.getFilename(), resource.getURL());
			}
			for (URL url : resourceMap.values()) {
				System.out.println("load api doc meta data from:" + url);
				InputStreamReader reader = new InputStreamReader(url.openStream(), ApiDoclet.DOC_ENCODING);
				result.addAll((Collection<? extends T>) JsonUtil.fromJson(reader, typeOfT));
				reader.close();
			}
		}
		return result;
	}

	public class ApiTypeDocQuery {
		
		public ApiTypeDoc getApiTypeDoc(String name) {
			for (ApiTypeDoc type : apiTypeDocs) {
				if (name.equals(type.getName())) {
					return type;
				}
			}
			return null;
		}
	}
	
	public class ApiDocQuery {
		
		public List<ApiDoc> queryByCategory(String category) {
			List<ApiDoc> apiDocList = new ArrayList<ApiDoc>();
			for (ApiDoc apiDoc : apiDocs) {
				if (apiDoc != null && apiDoc.getCategory().equals(category)) {
					apiDocList.add(apiDoc);
				}
			}
			return apiDocList;
		}
		
		public ApiDoc queryById(Integer id) {
			for (ApiDoc apiDoc : apiDocs) {
				if (apiDoc != null && apiDoc.getId().equals(id)) {
					return apiDoc;
				}
			}
			return null;
		}
	}

	public List<ApiCategory> getApiCategorys() {
		return apiCategorys;
	}

	public List<ApiDoc> getApiDocs() {
		return apiDocs;
	}

	public List<ApiTypeDoc> getApiTypeDocs() {
		return apiTypeDocs;
	}

	public ApiDocQuery getApiDocQuery() {
		return apiDocQuery;
	}

	public ApiTypeDocQuery getApiTypeDocQuery() {
		return apiTypeDocQuery;
	}
	
	public static void main(String[] args) throws Exception {
		ProjectClassLoader.addURLs(new File("F:\\兴邦\\workspace\\xband-qiuying\\xband-qiuying-inf\\xband-qiuying-inf-service\\target\\xband-qiuying-inf-service-1.0-SNAPSHOT.jar").toURI().toURL());
		URL url = ProjectClassLoader.R("META-INF/APIDOC");
		InputStream is = url.openStream();
		System.out.println(url);
		Resource[] rs = new PathMatchingResourcePatternResolver(ProjectClassLoader.get()).getResources(ApiDoclet.DOC_DIR.replace(File.separator, "/")  + "/" + "categ*.json");
		for (Resource resource : rs) {
			System.out.println(resource.getFilename());
		}
	}
}
