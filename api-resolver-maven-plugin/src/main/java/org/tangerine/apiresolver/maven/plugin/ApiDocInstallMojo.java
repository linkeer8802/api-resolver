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

import org.apache.commons.lang3.StringUtils;
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
import org.tangerine.apiresolver.doc.ApiDocInstallHandler;
import org.tangerine.apiresolver.doc.DocInstallArgs;
import org.tangerine.apiresolver.support.ProjectClassLoader;

/**
 * 生成api文档
 */
@Mojo( name = "apiDocInstall", defaultPhase=LifecyclePhase.INSTALL, requiresDependencyCollection=ResolutionScope.COMPILE_PLUS_RUNTIME)
public class ApiDocInstallMojo extends AbstractMojo {

	@Parameter(required = false)
	private String docName;
	
	@Parameter(required = false)
	private String version;
	
	@Parameter(defaultValue="true")
	private Boolean showResultExample;
	
	@Parameter(defaultValue="${user.home}/apiresolver/")
	private String installDir;
	
	@Component
	private MavenProject project;
	@Component
	private PluginDescriptor descriptor;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			initProjectClassLoader();
			
			DocInstallArgs docInstallArgs = new DocInstallArgs();
			docInstallArgs.setDocName(docName);
			docInstallArgs.setVersion(version);
			StringBuffer childPath = new StringBuffer(project.getName());
			if (!StringUtils.isEmpty(version)) {
				childPath.append(File.separator).append(version);
			}
			childPath.append(File.separator).append("APIDOC");
			File instDir = new File(installDir, childPath.toString());
			docInstallArgs.setInstallDir(instDir);
			docInstallArgs.setShowResultExample(showResultExample);
			new ApiDocInstallHandler(getLog(), docInstallArgs).install();
		} catch (Exception e) {
			throw new MojoExecutionException("DocInstall Mojo execute failure.", e);
		}
	}
	
	/**
	 * @author weird
	 * @version 1.0
	 * @throws Exception
	 */
	private void initProjectClassLoader() throws Exception {
		int i = 0;
		for (URL url : descriptor.getClassRealm().getURLs()) {
			getLog().debug("["+(++i)+"]before url:" + url);
		}
		i = 0;
		for (Artifact artifact : project.getArtifacts()) {
			getLog().debug("["+(++i)+"]artifact:" + artifact.getFile());
			descriptor.getClassRealm().addURL(artifact.getFile().toURI().toURL());
		}
		
		URL projectUrl = new File(project.getBuild().getOutputDirectory()).toURI().toURL();
		ProjectClassLoader.addURLs(projectUrl);
		ProjectClassLoader.addURLs(descriptor.getClassRealm().getURLs());
		
		i = 0;
		for (URL url : ProjectClassLoader.get().getURLs()) {
			getLog().debug("["+(++i)+"]after url:" + url);
		}
	}
}