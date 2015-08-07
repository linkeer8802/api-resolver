package org.tangerine.apiresolver.support;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

public class ProjectClassLoader extends URLClassLoader{

	private static ProjectClassLoader instance = new ProjectClassLoader(new URL[]{});
	
	private ProjectClassLoader(URL[] urls) {
		super(urls);
	}
	
	private ProjectClassLoader(ClassLoader parent) {
		super(new URL[]{}, parent);
	}
	
	private ProjectClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	
	public static ProjectClassLoader get(){
		return instance; 
	}
	
	public static Class<?> C(String name) throws ClassNotFoundException {
		Class<?> clz = null;
		try {
			clz = Class.forName(name);
		} catch (ClassNotFoundException e) {
			clz = instance.loadClass(name);
		}
		return clz;
	}
	
	public static URL R(String name) {
		URL resource = instance.getResource(name);
		if (resource == null) {
			resource = ProjectClassLoader.class.getClassLoader().getResource(name);
		}
		return resource;
	}
	
	public static Enumeration<URL> RS(String name) throws IOException, URISyntaxException {
		Enumeration<URL> resources = instance.getResources(name);
		if (!resources.hasMoreElements()) {
			resources = ProjectClassLoader.class.getClassLoader().getResources(name);
		}
		return resources;
	}
	
	/**
	 * @see URLClassLoader#addURL java.net.URLClassLoader.addURL(URL url)
	 * @author weird
	 * @version 1.0
	 * @param parent
	 * @param urls
	 */
	public static void addURLs(URL... urls) {
		for (URL url : urls) {
			instance.addURL(url);
		}
	}
	
	public static CompositeClassLoader wraperParents(ClassLoader... parents) {
		CompositeClassLoader wraperClassLoader = new CompositeClassLoader(instance);
		for (ClassLoader classLoader : parents) {
			wraperClassLoader = new CompositeClassLoader(classLoader, wraperClassLoader);
		}
		return wraperClassLoader;
	}
}
