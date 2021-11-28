package io.github.fvarrui.vulturehunter.projects;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.github.fvarrui.vulturehunter.Project;

public class MavenProject extends Project {
	
    private static final List<String> TEXT_FILES = Arrays.asList("java", "fxml", "xml", "gradle", "txt", "json", "meta", "html");
    private static final List<String> BINARY_FILES = Arrays.asList("pdf", "png", "jpg", "jpeg");
    private static final List<String> DEFAULT_EXCLUDED_FILES = Arrays.asList("target", "bin", "\\..*");

	public MavenProject(File rootDir) {
		super(rootDir, TEXT_FILES, BINARY_FILES, DEFAULT_EXCLUDED_FILES);
	}

}
