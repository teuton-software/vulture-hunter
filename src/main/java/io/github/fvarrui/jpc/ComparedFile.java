package io.github.fvarrui.jpc;

import java.io.File;
import java.io.IOException;

public abstract class ComparedFile {

	private Project project;
	private File file;
	
	public ComparedFile(File file, Project project) throws IOException {
		this.project = project;		
		this.file = file;
	}
	
	public String getName() {
		return file.getName();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public String getRelativePath() {
		return getProject().relativize(file);
	}
	
	public abstract Match<?> compare(ComparedFile file) throws Exception;

	@Override
	public String toString() {
		return "[" + getProject().getName() + ":" +  getRelativePath() + "]";
	}

}
