package io.github.fvarrui.jpc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class TextFile extends ComparedFile {

	private List<String> lines;
	
	public TextFile(File file, Project project) throws IOException {
		super(file, project);
		this.lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		this.lines = this.lines.stream().map(s -> StringUtils.trimToNull(s)).filter(s -> s != null).collect(Collectors.toList()); // removes empty/blank lines
	}
	
	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	
	@Override
	public Match<?> compare(ComparedFile file) throws Exception {
		return new TextMatch(this, (TextFile)file);
	}
	
}
