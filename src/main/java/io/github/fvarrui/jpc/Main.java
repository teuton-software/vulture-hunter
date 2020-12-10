package io.github.fvarrui.jpc;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.difflib.algorithm.DiffException;

public class Main {
	
	private static final double threshold = 90.0; 
	
	public static void main(String[] args) throws IOException, DiffException {
		Project project1 = new Project(new File("proyecto1"));
		Project project2 = new Project(new File("proyecto2"));
		Comparator comparator = new Comparator();
		List<Match> matches = comparator.compareProjects(project1, project2);
		matches.stream().filter(m -> m.getSimilarity() > threshold).forEach(m -> System.out.println(m));
	}

}
