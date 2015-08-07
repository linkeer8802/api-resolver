package org.tangerine.apiresolver.doc;

import java.util.List;

import org.tangerine.apiresolver.doc.entity.ApiCategory;
import org.tangerine.apiresolver.doc.entity.ApiDoc;
import org.tangerine.apiresolver.doc.entity.ApiTypeDoc;

public interface ApiDocInstaller {

	public abstract void installCategorys(List<ApiCategory> apiCategorys) throws Exception;

	public abstract void installApiDocs(List<ApiDoc> apiDocs) throws Exception;

	public abstract void installApiTypeDocs(List<ApiTypeDoc> apiTypeDocs) throws Exception;

}