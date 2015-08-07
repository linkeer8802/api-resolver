package org.tangerine.apiresolver.doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.tangerine.apiresolver.util.JsonUtil;

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
	public void exec1() throws Exception {
		String javaHome = System.getProperty("java.home");
		String pathSeparator = System.getProperty("path.separator");
		String classPath = System.getProperty("java.class.path");
		String encoding = "UTF-8";
		
		String doclet = ApiDoclet.class.getName();
		
		if (classPath.indexOf("tools.jar") == -1) {
			classPath = classPath + pathSeparator + javaHome + File.separator + "lib" + File.separator + "tools.jar";
		}
		
		StringBuffer cmd = new StringBuffer("javadoc");
		
		//显示所有类和成员
		cmd.append(" -private");
		
		if (cmdArgs.getDebug()) {
			cmd.append(" -verbose");
		} else {
			cmd.append(" -quiet");
		}
		
		if (!cmdArgs.getCmdRuntimeOptions().isEmpty()) {
			for (String option : cmdArgs.getCmdRuntimeOptions()) {
				cmd.append(" -J").append(option);
			}
		}
		
		cmd.append(" -sourcepath ").append(StringUtils.join(cmdArgs.getSourcepaths(), ";"));
		
		cmd.append(" -subpackages ").append(StringUtils.join(cmdArgs.getSubpackages(), pkgSeparator));
		
		if (cmdArgs.getExcludes() != null && !cmdArgs.getExcludes().isEmpty()) {
			cmd.append(" -exclude ").append(StringUtils.join(cmdArgs.getExcludes(), pkgSeparator));
		}
		
		cmd.append(" -classpath ").append("\"" + classPath + File.pathSeparator + StringUtils.join(cmdArgs.getDocletpaths(), File.pathSeparator) + "\"");
		
		cmd.append(" -docletpath ").append("\"" + StringUtils.join(cmdArgs.getDocletpaths(), File.pathSeparator))
		.append(File.pathSeparator).append(classPath + "\"");
		
		cmd.append(" -doclet ").append(doclet);
		
		cmd.append(" -encoding ").append(encoding);
		
//		cmd.append(" -charset ").append(encoding);
//		cmd.append(" " + sourcePath);
		
		mavenLogger.info("exec cmd:" + cmd);
		
		Process process = Runtime.getRuntime().exec(cmd.toString());
		
		InputStream fis = process.getInputStream();
		//命令行字符编码
		String jnuEncoding = System.getProperty("sun.jnu.encoding");
		mavenLogger.debug("JNU-Encoding:" + jnuEncoding);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, jnuEncoding));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			if (cmdArgs.getDebug()) {
//				mavenLogger.info(line);
			}
		}

	    int exitValue = process.exitValue();
		if (exitValue != 0) {
	    	mavenLogger.error("exec command abnormal termination, exitValue:" + exitValue);
	    	throw new IllegalStateException("exec command abnormal termination, try execute the command again whith shell or command line then view detail error info\r\n. cmd=" + cmd);
	    }
	}
	
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
		
		System.out.println(JsonUtil.toJson(conmmand));
		
		ProcessBuilder pb = new ProcessBuilder(conmmand);
		pb.redirectErrorStream(true);
		Map<String, String> env = pb.environment();
		env.clear();
//		 env.put("VAR1", "myValue");
//		 env.remove("OTHERVAR");
//		 env.put("VAR2", env.get("VAR1") + "suffix");
		Process process = pb.start();
		InputStream fis = process.getInputStream();
		//命令行字符编码
		String jnuEncoding = System.getProperty("sun.jnu.encoding");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, jnuEncoding));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
 
		int exitValue = process.waitFor();
		
		if (exitValue != 0) {
	    	mavenLogger.error("exec command abnormal termination, exitValue:" + exitValue);
	    	throw new IllegalStateException("exec command abnormal termination");
	    }
	}
	
	public static void main(String[] args) throws Exception {
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
		
		conmmand.add("-verbose");
		
//		conmmand.add("");
		conmmand.add("-J-DoutputRootDir=F:\\兴邦\\workspace\\api-resolver\\api-resolver-sample\\target\\classes");
		
		conmmand.add("-sourcepath");
		conmmand.add("F:\\兴邦\\workspace\\api-resolver\\api-resolver-sample\\src\\main\\java");
		
		conmmand.add("-subpackages");
		conmmand.add("org.tangerine.apiassistant.sample.api");
		
		conmmand.add("-exclude");
		conmmand.add("org.tangerine.apiassistant.sample.api.child");
		
		conmmand.add("-classpath");
		conmmand.add("F:\\soft\\apache-maven-3.2.3\\boot\\plexus-classworlds-2.5.1.jar;C:\\Program Files\\Java\\jdk1.8.0_25\\jre\\lib\\tools.jar;F:\\兴邦\\workspace\\api-resolver\\api-resolver-sample\\target\\classes;F:\\兴邦\\workspace\\api-resolver\\api-resolver-core\\target\\api-resolver-core-1.0-SNAPSHOT.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-context\\3.2.3.RELEASE\\spring-context-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\com\\google\\code\\gson\\gson\\2.3\\gson-2.3.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\apache\\commons\\commons-lang3\\3.3.2\\commons-lang3-3.3.2.jar;C:\\Users\\Administrator\\.m2\\repository\\com\\lowagie\\itext\\4.2.1\\itext-4.2.1.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\bouncycastle\\bctsp-jdk14\\1.38\\bctsp-jdk14-1.38.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\bouncycastle\\bcprov-jdk14\\1.38\\bcprov-jdk14-1.38.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\bouncycastle\\bcmail-jdk14\\1.38\\bcmail-jdk14-1.38.jar;C:\\Users\\Administrator\\.m2\\repository\\dom4j\\dom4j\\1.6.1\\dom4j-1.6.1.jar;C:\\Users\\Administrator\\.m2\\repository\\xml-apis\\xml-apis\\1.0.b2\\xml-apis-1.0.b2.jar;C:\\Users\\Administrator\\.m2\\repository\\jfree\\jfreechart\\1.0.12\\jfreechart-1.0.12.jar;C:\\Users\\Administrator\\.m2\\repository\\jfree\\jcommon\\1.0.15\\jcommon-1.0.15.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\swinglabs\\pdf-renderer\\1.0.5\\pdf-renderer-1.0.5.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-web\\3.2.3.RELEASE\\spring-web-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\aopalliance\\aopalliance\\1.0\\aopalliance-1.0.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-aop\\3.2.3.RELEASE\\spring-aop-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-beans\\3.2.3.RELEASE\\spring-beans-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-core\\3.2.3.RELEASE\\spring-core-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\commons-logging\\commons-logging\\1.1.1\\commons-logging-1.1.1.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-webmvc\\3.2.3.RELEASE\\spring-webmvc-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-expression\\3.2.3.RELEASE\\spring-expression-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-context-support\\3.2.3.RELEASE\\spring-context-support-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\freemarker\\freemarker\\2.3.18\\freemarker-2.3.18.jar;C:\\Users\\Administrator\\.m2\\repository\\javax\\servlet\\javax.servlet-api\\3.0.1\\javax.servlet-api-3.0.1.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\codehaus\\jackson\\jackson-mapper-asl\\1.9.4\\jackson-mapper-asl-1.9.4.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\codehaus\\jackson\\jackson-core-asl\\1.9.4\\jackson-core-asl-1.9.4.jar");
		
		conmmand.add("-docletpath");
		conmmand.add("F:\\兴邦\\workspace\\api-resolver\\api-resolver-sample\\target\\classes;F:\\兴邦\\workspace\\api-resolver\\api-resolver-core\\target\\api-resolver-core-1.0-SNAPSHOT.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-context\\3.2.3.RELEASE\\spring-context-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\com\\google\\code\\gson\\gson\\2.3\\gson-2.3.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\apache\\commons\\commons-lang3\\3.3.2\\commons-lang3-3.3.2.jar;C:\\Users\\Administrator\\.m2\\repository\\com\\lowagie\\itext\\4.2.1\\itext-4.2.1.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\bouncycastle\\bctsp-jdk14\\1.38\\bctsp-jdk14-1.38.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\bouncycastle\\bcprov-jdk14\\1.38\\bcprov-jdk14-1.38.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\bouncycastle\\bcmail-jdk14\\1.38\\bcmail-jdk14-1.38.jar;C:\\Users\\Administrator\\.m2\\repository\\dom4j\\dom4j\\1.6.1\\dom4j-1.6.1.jar;C:\\Users\\Administrator\\.m2\\repository\\xml-apis\\xml-apis\\1.0.b2\\xml-apis-1.0.b2.jar;C:\\Users\\Administrator\\.m2\\repository\\jfree\\jfreechart\\1.0.12\\jfreechart-1.0.12.jar;C:\\Users\\Administrator\\.m2\\repository\\jfree\\jcommon\\1.0.15\\jcommon-1.0.15.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\swinglabs\\pdf-renderer\\1.0.5\\pdf-renderer-1.0.5.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-web\\3.2.3.RELEASE\\spring-web-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\aopalliance\\aopalliance\\1.0\\aopalliance-1.0.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-aop\\3.2.3.RELEASE\\spring-aop-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-beans\\3.2.3.RELEASE\\spring-beans-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-core\\3.2.3.RELEASE\\spring-core-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\commons-logging\\commons-logging\\1.1.1\\commons-logging-1.1.1.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-webmvc\\3.2.3.RELEASE\\spring-webmvc-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-expression\\3.2.3.RELEASE\\spring-expression-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\springframework\\spring-context-support\\3.2.3.RELEASE\\spring-context-support-3.2.3.RELEASE.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\freemarker\\freemarker\\2.3.18\\freemarker-2.3.18.jar;C:\\Users\\Administrator\\.m2\\repository\\javax\\servlet\\javax.servlet-api\\3.0.1\\javax.servlet-api-3.0.1.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\codehaus\\jackson\\jackson-mapper-asl\\1.9.4\\jackson-mapper-asl-1.9.4.jar;C:\\Users\\Administrator\\.m2\\repository\\org\\codehaus\\jackson\\jackson-core-asl\\1.9.4\\jackson-core-asl-1.9.4.jar;F:\\soft\\apache-maven-3.2.3\\boot\\plexus-classworlds-2.5.1.jar;C:\\Program Files\\Java\\jdk1.8.0_25\\jre\\lib\\tools.jar");
		
		conmmand.add("-doclet");
		conmmand.add(doclet);
		
		conmmand.add("-encoding");
		conmmand.add(encoding);
		
		ProcessBuilder pb = new ProcessBuilder(conmmand);
		pb.redirectErrorStream(true);
		Map<String, String> env = pb.environment();
		env.clear();
//		 env.put("VAR1", "myValue");
//		 env.remove("OTHERVAR");
//		 env.put("VAR2", env.get("VAR1") + "suffix");
		Process process = pb.start();
		InputStream fis = process.getInputStream();
		//命令行字符编码
		String jnuEncoding = System.getProperty("sun.jnu.encoding");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, jnuEncoding));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
 
		int exitValue = process.waitFor();
		
		if (exitValue != 0) {
	    	throw new IllegalStateException("exec command abnormal termination");
	    }
	}
}
