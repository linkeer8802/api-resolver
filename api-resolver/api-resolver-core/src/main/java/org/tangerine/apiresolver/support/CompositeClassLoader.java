package org.tangerine.apiresolver.support;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;


public final class CompositeClassLoader extends ClassLoader {

	private ClassLoader instalce;
	
	public CompositeClassLoader() {
		super();
		this.instalce = ClassLoader.getSystemClassLoader();
	}
	
	public CompositeClassLoader(ClassLoader parent) {
		this(ClassLoader.getSystemClassLoader(), parent);
	}
	
	public CompositeClassLoader(ClassLoader instalce, ClassLoader parent) {
		super(parent);
		this.instalce = instalce;
	}
	
	@Override
	protected final Class<?> findClass(String name) throws ClassNotFoundException {
		return instalce.loadClass(name);
	}
	
	@Override
	protected final URL findResource(String name) {
		return instalce.getResource(name);
	}
	
	@Override
	protected final Enumeration<URL> findResources(String name) throws IOException {
		return instalce.getResources(name);
	}
	
	public static void main(String[] args) throws Exception {
		URL url2 = new File("F:\\兴邦\\workspace\\api-helper-web-sample\\target\\classes").toURI().toURL();
		URL url = new File("F:\\兴邦\\workspace\\api-helper-sample\\target\\api-helper-sample-0.0.1-SNAPSHOT.jar").toURI().toURL();
		
		CompositeClassLoader parent = new CompositeClassLoader(new URLClassLoader(new URL[]{url}));
		CompositeClassLoader compositeClassLoader = new CompositeClassLoader(
				new URLClassLoader(new URL[]{url2}), parent);
		System.out.println(compositeClassLoader.loadClass(String.class.getName()));
		
		System.out.println(compositeClassLoader.getResource("META-INF/APIDOC/api_type.json"));
	}
}
