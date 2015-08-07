package org.tangerine.apiresolver.doc;

import java.util.ArrayList;
import java.util.List;

public class ConmandArgs {

	private Boolean debug;
	
	private Boolean chuckDocTag;
	
	private List<String> subpackages;
	
	private List<String> excludes;
	
	private List<String> sourcepaths;
	
	private List<String> docletpaths;
	
	private List<String> cmdRuntimeOptions;
	
	public ConmandArgs() {
		cmdRuntimeOptions = new ArrayList<String>();
	}
	
	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public List<String> getSubpackages() {
		return subpackages;
	}

	public void setSubpackages(List<String> subpackages) {
		this.subpackages = subpackages;
	}

	public List<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	public List<String> getSourcepaths() {
		return sourcepaths;
	}

	public void setSourcepaths(List<String> sourcepaths) {
		this.sourcepaths = sourcepaths;
	}

	public List<String> getCmdRuntimeOptions() {
		return cmdRuntimeOptions;
	}
	
	public ConmandArgs addCmdRuntimeOption(String cmdRuntimeOption) {
		this.cmdRuntimeOptions.add(cmdRuntimeOption);
		return this;
	}

	public ConmandArgs addCmdRuntimeOptions(List<String> cmdRuntimeOptions) {
		this.cmdRuntimeOptions.addAll(cmdRuntimeOptions);
		return this;
	}

	public List<String> getDocletpaths() {
		return docletpaths;
	}

	public void setDocletpaths(List<String> docletpaths) {
		this.docletpaths = docletpaths;
	}
}
