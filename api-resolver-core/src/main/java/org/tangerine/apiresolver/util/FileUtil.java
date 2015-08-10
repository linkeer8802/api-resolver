package org.tangerine.apiresolver.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.List;

import org.springframework.util.AntPathMatcher;

public class FileUtil {

	public static void traverse(File root, FileFilter filter, FileHandler handler) {
		for (File file : root.listFiles(filter)) {
			if (file.isDirectory()) {
				traverse(file, filter, handler);
			}
			handler.handle(file);
		}
	}
	
	public static boolean clearDir(File dir) {
		if (!dir.exists()) {
			return false;
		}
		
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				clearDir(file);
			} 
			if (!file.delete()) return false;
		}
		return true;
	}
	
	
	public static void mkdir(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	public static interface FileHandler {
		public void handle(File file);
	}
	
	public  static class AntPathFileMatcher {
		
		private List<String> includes;
		private List<String> excludes;
		
		private static final String separator = "/";
		
		private AntPathMatcher matcher = new AntPathMatcher();
		
		@SuppressWarnings("unchecked")
		public AntPathFileMatcher(List<String> includes, List<String> excludes) {
			matcher.setPathSeparator(separator);
			this.includes = (includes == null) ? Collections.EMPTY_LIST : includes;
			this.excludes = (excludes == null) ? Collections.EMPTY_LIST : excludes;
		}
		
		public boolean match(File file) {
			
			boolean include = false;
			boolean exclude = false;
			
			String path = file.getAbsolutePath().replace("\\", separator);
			
			if (includes.isEmpty()) {
				include = true;
			}
			for (String includePatternPath : includes) {
				if (matcher.match(includePatternPath, path)) {
					include = true;
				}
			}
			
			if (excludes.isEmpty()) {
				exclude = false;
			}
			for (String excludePatternPath : excludes) {
				if (matcher.match(excludePatternPath, path)) {
					exclude = true;
				}
			}
			
			return include && !exclude;
		}
	}
}
