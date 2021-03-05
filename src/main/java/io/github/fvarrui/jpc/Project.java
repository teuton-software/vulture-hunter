package io.github.fvarrui.jpc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public class Project {

    private static final String[] SUFFIXES = {"java", "fxml", "xml", "gradle", "txt", "json", "meta", "html"};

	private File rootDir;
	private List<ComparedFile> files; 

	public Project(File rootDir) {
		this.rootDir = rootDir;
		this.listFiles();
	}
	
	public void listFiles() {
	    files = 
	    		FileUtils
	    			.listFiles(rootDir, SUFFIXES, true)
	    			.stream()
	    			.filter(f -> !f.getAbsolutePath().contains("build"))
	    			.filter(f -> !f.getAbsolutePath().contains("target"))
	    			.filter(f -> !f.getAbsolutePath().contains(".idea"))
			    	.map(f -> {
						try {
							ComparedFile cf = new ComparedFile(f);
							cf.setProject(this);
							return cf;
						} catch (IOException e) {
							return null;
						}
			    	})
			    	.filter(f -> f != null)
			    	.collect(Collectors.toList());
	}
	
	public File getRootDir() {
		return rootDir;
	}
	
	public String getName() {
		return rootDir.getName();
	}
	
	public List<ComparedFile> getFiles() {
		return files;
	}
	
}
