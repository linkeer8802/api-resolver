package org.tangerine.apiresolver.doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;

public class DocConmand {

	private static final String pkgSeparator = ":";
	
	private Log mavenLogger;
	
	private ConmandArgs cmdArgs;
	
	public DocConmand(Log mavenLogger, ConmandArgs cmdArgs) {
		this.mavenLogger = mavenLogger;
		this.cmdArgs = cmdArgs;
	}
	/**
	 * @see http://docs.oracle.com/javase/7/docs/technotes/tools/windows/javadoc.html
	 * @throws Exception
	 */
	public void exec() throws Exception {
		String javaHome = System.getProperty("java.home");
		String pathSeparator = System.getProperty("path.separator");
		String classPath = System.getProperty("java.class.path");
		String encoding = "UTF-8";
		
		String doclet = ApiDoclet.class.getName();
		
		if (classPath.indexOf("tools.jar") == -1) {
			classPath = classPath + pathSeparator + javaHome + File.separator + "lib" + File.separator + "tools.jar";
		}
		
		List<String> conmmand = new ArrayList<String>();
		conmmand.add("javadoc");
		//显示所有类和成员
		conmmand.add("-private");
		
		if (cmdArgs.getDebug()) {
			conmmand.add("-verbose");
		} else {
			conmmand.add("-quiet");
		}
		
		if (!cmdArgs.getCmdRuntimeOptions().isEmpty()) {
			for (String option : cmdArgs.getCmdRuntimeOptions()) {
				conmmand.add("-J" + option);
			}
		}
		
		conmmand.add("-sourcepath");
		conmmand.add(StringUtils.join(cmdArgs.getSourcepaths(), ";"));
		
		conmmand.add("-subpackages");
		conmmand.add(StringUtils.join(cmdArgs.getSubpackages(), pkgSeparator));
		
		if (cmdArgs.getExcludes() != null && !cmdArgs.getExcludes().isEmpty()) {
			conmmand.add("-exclude");
			conmmand.add(StringUtils.join(cmdArgs.getExcludes(), pkgSeparator));
		}
		
		conmmand.add("-classpath");
		conmmand.add(classPath + File.pathSeparator + StringUtils.join(cmdArgs.getDocletpaths(), File.pathSeparator));
		
		conmmand.add("-docletpath");
		conmmand.add(StringUtils.join(cmdArgs.getDocletpaths(), File.pathSeparator) + File.pathSeparator + classPath);
		
		conmmand.add("-doclet");
		conmmand.add(doclet);
		
		conmmand.add("-encoding");
		conmmand.add(encoding);
		
//		System.out.println(JsonUtil.toJson(conmmand));
		
		ProcessBuilder pb = new ProcessBuilder(conmmand);
		pb.redirectErrorStream(true);
		Map<String, String> env = pb.environment();
		env.clear();
		
		Process process = pb.start();
		InputStream fis = process.getInputStream();
		//命令行字符编码
		String jnuEncoding = System.getProperty("sun.jnu.encoding");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, jnuEncoding));
		
		//输出调试信息
		if (cmdArgs.getDebug() != null && cmdArgs.getDebug()) {
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		}
 
		int exitValue = process.waitFor();
		
		if (exitValue != 0) {
	    	mavenLogger.error("exec command abnormal termination, exitValue:" + exitValue);
	    	throw new IllegalStateException("exec command abnormal termination");
	    }
	}
}
