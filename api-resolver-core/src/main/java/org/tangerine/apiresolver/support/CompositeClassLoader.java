package org.tangerine.apiresolver.support;

import java.io.IOException;
import java.net.URL;
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
}
