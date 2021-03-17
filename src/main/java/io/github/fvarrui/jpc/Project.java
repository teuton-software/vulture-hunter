package io.github.fvarrui.jpc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import io.github.fvarrui.jpc.utils.FilenameUtils;

public class Project {

    public static final List<String> TEXT_FILES = Arrays.asList("java", "fxml", "xml", "gradle", "txt", "json", "meta", "html");
    public static final List<String> BINARY_FILES = Arrays.asList("pdf", "png", "jpg", "jpeg");
    public static final String[] ALL_FILES = Stream.concat(TEXT_FILES.stream(), BINARY_FILES.stream()).toArray(String[]::new);

    public static final String[] DEFAULT_EXCLUDED_FILES = { ".*/build/.*", ".*/target/.*", ".*/bin/.*", ".*/.idea/.*", ".*/.git/.*" };

	private File rootDir;
	private List<ComparedFile> files;
	private List<String> excludes = new ArrayList<>();

	public Project(File rootDir, List<String> excludes) {
		this.rootDir = rootDir;
		this.excludes.addAll(Arrays.asList(DEFAULT_EXCLUDED_FILES));
		this.excludes.addAll(excludes);
		this.listFiles();
	}
	
	public Project(File rootDir) {
		this(rootDir, new ArrayList<>());
	}
	
	public boolean isExcluded(File f) {
		final String relativePath = relativize(f);
		return getExcludes()
				.stream()
				.anyMatch(e -> relativePath.matches(e));
	}
	
	public boolean isBinary(File f) {
		String extension = FilenameUtils.getExtension(f.getName());
		return BINARY_FILES.stream().anyMatch(b -> b.equalsIgnoreCase(extension));
	}
	
	public void listFiles() {
	    files = 
	    		FileUtils
	    			.listFiles(rootDir, ALL_FILES, true)
	    			.stream()
	    			.filter(f -> !isExcluded(f))
			    	.map(f -> {
						try {
							if (isBinary(f))
								return new BinaryFile(f, this);
							else 
								return new TextFile(f, this);
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
	
	public final List<String> getExcludes() {
		return Collections.unmodifiableList(excludes);
	}
	
	public String relativize(File file) {
		return getRootDir().toURI().relativize(file.toURI()).getPath();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Project: " + getName() + "\n");
		getFiles().stream().forEach(f -> buffer.append("* " + f + "\n"));
		return buffer.toString();
	}
	
	public static void main(String[] args) throws IOException {

		File rootDir = new File(".");
		
		Project project = new Project(rootDir, Arrays.asList(".*utils.*"));
		System.out.println(project);

		File f = new File("perico/de/los/palotes.xxx");		
		System.out.println(project.isBinary(f));

	}
	
	
	
}
