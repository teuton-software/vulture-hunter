package io.github.fvarrui.vulturehunter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import io.github.fvarrui.vulturehunter.utils.FilenameUtils;

public class Project {

	private File rootDir;
	private List<ComparedFile> files;
	private List<String> binaryFiles = new ArrayList<>();
	private List<String> textFiles = new ArrayList<>();
	private List<String> excludes = new ArrayList<>();

	public Project(File rootDir, List<String> textFiles, List<String> binaryFiles, List<String> excludes) {
		this.rootDir = rootDir;
		this.binaryFiles.addAll(binaryFiles);
		this.textFiles.addAll(textFiles);
		this.excludes.addAll(excludes);
		this.listFiles();
	}
	
	public Project(File rootDir, List<String> textFiles, List<String> binaryFiles) {
		this(rootDir, textFiles, binaryFiles, new ArrayList<>());
	}

	public Project(File rootDir) {
		this(rootDir, new ArrayList<>(), new ArrayList<>());
	}

	public boolean isExcluded(File f) {
		final String relativePath = relativize(f);
		return getExcludes()
				.stream()
				.anyMatch(e -> relativePath.matches(e));
	}
	
	public boolean isIncluded(File f) {
		return !isExcluded(f);
	}

	public boolean isText(File f) {
		if (textFiles.isEmpty()) return false;
		String extension = FilenameUtils.getExtension(f.getName());
		return textFiles.stream().anyMatch(b -> b.equalsIgnoreCase(extension));
	}

	public void listFiles() {
		List<String> allFiles = getAllFiles();
		if (allFiles.isEmpty()) allFiles = null;
	    files = 
	    		FileUtils
	    			.listFiles(rootDir, allFiles == null ? null : allFiles.toArray(String[]::new), true)
	    			.stream()
	    			.filter(this::isIncluded)
			    	.map(f -> {
						try {
							return isText(f) ? new TextFile(f, this) : new BinaryFile(f, this);
						} catch (IOException e) {
							return null;
						}
			    	})
			    	.filter(f -> f != null)
			    	.collect(Collectors.toList());
	}
	
	public File getRootDir() {
		try {
			return rootDir.getCanonicalFile();
		} catch (IOException e) {
			return rootDir;
		}
	}
	
	public String getName() {
		return getRootDir().getName();
	}
	
	public List<ComparedFile> getFiles() {
		return files;
	}
	
	public List<String> getExcludes() {
		return excludes;
	}
	
	public String relativize(File file) {
		return getRootDir().toURI().relativize(file.toURI()).getPath();
	}
	
	public List<String> getBinaryFiles() {
		return binaryFiles;
	}
	
	public List<String> getTextFiles() {
		return textFiles;
	}

	public List<String> getAllFiles() {
		return Stream.concat(textFiles.stream(), binaryFiles.stream()).collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Project: " + getName() + "\n");
		getFiles().stream().forEach(f -> buffer.append("* " + f + "\n"));
		return buffer.toString();
	}
	
}
