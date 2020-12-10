package io.github.fvarrui.jpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.difflib.algorithm.DiffException;

import io.github.fvarrui.jpc.utils.FilenameUtils;

public class Comparator {
	
	public List<Match> compareProjects(Project project1, Project project2) throws IOException, DiffException {
		List<Match> matches = new ArrayList<>();
		for (ComparedFile file1 : project1.getFiles()) {
			
			for (ComparedFile file2 : project2.getFiles()) {
				if (FilenameUtils.equalExtensions(file1.getFile(), file2.getFile())) {
					matches.add(new Match(file1, file2));
				}				
			}
		}
		return matches;
	}
	
}
