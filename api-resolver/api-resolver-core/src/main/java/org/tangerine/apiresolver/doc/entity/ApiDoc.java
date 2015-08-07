package org.tangerine.apiresolver.doc.entity;

import java.util.ArrayList;
import java.util.List;

public class ApiDoc {

	private String id;
	
	private String mapping; //映射路径
	
	private String name; //API名称
	
	private String desc; //API说明
	
	private String author; //作者
	
	private String category; //分类
	
	private String version; //API版本
	
	private String reqMethod; //请求方式
	
	private String useLimit; //使用限制
	
	private List<ParamDoc> params; //参数列表
	
	private ResultDoc resultDoc; //返回结果

	private String resultExample; //返回示例
	
	private ApiErrorDoc apiErrorDoc; //错误码文档

	public ApiDoc() {
		params = new ArrayList<ParamDoc>();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public String getUseLimit() {
		return useLimit;
	}

	public void setUseLimit(String useLimit) {
		this.useLimit = useLimit;
	}

	public List<ParamDoc> getParams() {
		return params;
	}

	public void addParam(ParamDoc param) {
		this.params.add(param);
	}
	
	public void addParams(List<ParamDoc> params) {
		this.params.addAll(params);
	}

	public ResultDoc getResultDoc() {
		return resultDoc;
	}
	
	public void setResultDoc(ResultDoc resultDoc) {
		this.resultDoc = resultDoc;
	}
	
	public String getResultExample() {
		return resultExample;
	}
	
	public void setResultExample(String resultExample) {
		this.resultExample = resultExample;
	}
	
	public ApiErrorDoc getApiErrorDoc() {
		return apiErrorDoc;
	}

	public void setApiErrorDoc(ApiErrorDoc apiErrorDoc) {
		this.apiErrorDoc = apiErrorDoc;
	}
	
	@Override
	public String toString() {
		return "ApiDoc [name=" + name + ", desc=" + desc + ", author=" + author + ", version=" + version
				+ ", reqMethod=" + reqMethod + ", useLimit=" + useLimit + ", params=" + params + ", returnDoc="
				+ resultDoc + ", returnExampleDoc=" + resultExample + ", apiErrorDoc=" + apiErrorDoc + "]";
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
