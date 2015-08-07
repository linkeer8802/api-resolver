package org.tangerine.apiresolver.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.tangerine.apiresolver.doc.ConmandArgs;
import org.tangerine.apiresolver.doc.DocConmand;

/**
 * 抓取api源文件注释文档的Goal
 */
@Mojo( name = "apiDocFetch", defaultPhase=LifecyclePhase.COMPILE, requiresDependencyResolution=ResolutionScope.COMPILE_PLUS_RUNTIME)
public class ApiDocFetchMojo extends AbstractMojo {

	@Parameter(defaultValue="true")
	private Boolean debug;
	
	@Parameter(required=true)
	private List<String> subpackages;
	
	@Parameter
	private List<String> excludes;
	
//	@Parameter(defaultValue="${project.build.outputDirectory}")
//	private String outputRootDir;
	
	@Parameter
	private List<String> sourcepaths;
	
	@Component
	private MavenProject project;
	@Component
	private PluginDescriptor descriptor;
	
	
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {
			init();
			initProjectClassLoader();
			
			ConmandArgs cmdArgs = new ConmandArgs();
			cmdArgs.setDebug(debug);
			cmdArgs.setExcludes(excludes);
			cmdArgs.setSourcepaths(sourcepaths);
			cmdArgs.setSubpackages(subpackages);
			cmdArgs.setDocletpaths(getDocletpaths());
			cmdArgs.addCmdRuntimeOption("-DoutputRootDir=" + project.getBuild().getOutputDirectory());
			
			new DocConmand(getLog(), cmdArgs).exec();
		} catch (Exception e) {
			throw new MojoExecutionException("execute docConmand failure.", e);
		}
	}
	
	private void init() {
		if (sourcepaths == null || sourcepaths.isEmpty()) {
			sourcepaths = new ArrayList<String>();
			sourcepaths.add(project.getBuild().getSourceDirectory());
		}
	}

	private List<String> getDocletpaths() {
		
		List<String> docletpaths = new ArrayList<String>();
		Set<Artifact> artifacts = project.getArtifacts();
		
		docletpaths.add(project.getBuild().getOutputDirectory());
		
		for (Artifact artifact : artifacts) {
//			getLog().info("docletpath:" + artifact.getFile().getAbsolutePath());
			docletpaths.add(artifact.getFile().getAbsolutePath());
		}
		
		return docletpaths;
	}
	
	/**
	 * @author weird
	 * @version 1.0
	 * @throws Exception
	 */
	private void initProjectClassLoader() throws Exception {
		URL projectUrl = new File(project.getBuild().getOutputDirectory()).toURI().toURL();
		descriptor.getClassRealm().addURL(projectUrl);
	}
 	
//	private List<String> getSuourcesPath() {
//		
//		final List<String> sourcesPath = new ArrayList<String>();
//		
//		getLog().info( "will create api doc for below files:");
//		getLog().info( "------------------------------------------------------------------------");
//		
//		FileUtil.traverse(sourceDirectory, null, new FileHandler() {
//			AntPathFileMatcher antPathFileMatcher = new AntPathFileMatcher(includes, excludes);
//			public void handle(File file) {
//				if (antPathFileMatcher.match(file)) {
//					sourcesPath.add(file.getAbsolutePath());
//					getLog().info("file:" + file.getAbsolutePath());
//				}
//			}
//		});
//		
//		getLog().info( "------------------------------------------------------------------------");
//		
//		return sourcesPath;
//	}
}