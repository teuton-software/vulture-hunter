package io.github.fvarrui.jpc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.fvarrui.jpc.utils.ConsoleUtils;
import io.github.fvarrui.jpc.utils.FilenameUtils;

public class Comparison {

	private Project project1;
	private Project project2;
	private List<Match<?>> matches;
	private double similarity = 0.0;
	private double threshold = 0.0;

	public Comparison(final Project project1, final Project project2) throws Exception {
		this.project1 = project1;
		this.project2 = project2;
		this.matches = compare(project1, project2);
	}

	private List<Match<?>> compare(Project project1, Project project2) throws Exception {
		List<Match<?>> matches = new ArrayList<>();
		for (ComparedFile file1 : project1.getFiles()) {
			for (ComparedFile file2 : project2.getFiles()) {
				if (FilenameUtils.equalExtensions(file1.getFile(), file2.getFile())) {
					matches.add(file1.compare(file2));
				}
			}
		}
		return matches;
	}

	public final Project getProject1() {
		return project1;
	}

	public final Project getProject2() {
		return project2;
	}

	public final List<Match<?>> getAllMatches() {
		return matches;
	}

	public final List<Match<?>> getMatches(double threshold) {
		return matches.stream().filter(m -> m.getSimilarity() > threshold).collect(Collectors.toList());
	}
	
	public double calculateSimilarity(double threshold) {
		this.threshold = threshold;
		int totalMatches = getMatches(threshold).size();
		double totalFiles = (project1.getFiles().size() + project2.getFiles().size()) / 2.0;
		return similarity = ((double)totalMatches / (double)totalFiles) * 100.0;
	}
	
	public double getSimilarity() {
		return similarity;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(ConsoleUtils.frame(project1.getName() + " compared with " + project2.getName()));
		buffer.append("Compared files threshold: " + threshold + "%\n");		
		buffer.append("Estimated similarity between projects: " + String.format("%.2f", getSimilarity()) + "%\n");
		buffer.append("Matches:\n");
		getMatches(threshold).forEach(m -> buffer.append("* " + m + "\n"));
		return buffer.toString();
	}

}
