package org.tangerine.apiresolver.support;

import java.net.URL;

import freemarker.cache.URLTemplateLoader;

public class ProjectClassTemplateLoader extends URLTemplateLoader {

	private String path;
	
	public ProjectClassTemplateLoader(String pathPrefix) {
		this.path = canonicalizePrefix(pathPrefix);
	}
	
	@Override
	protected URL getURL(String name) {
		return ProjectClassLoader.R(path + name);
	}
}
