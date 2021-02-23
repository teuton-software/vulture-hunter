package io.github.fvarrui.jpc;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.github.difflib.algorithm.DiffException;

public class Main {

	private static final double THRESHOLD = 80.0;
	private static final double SIMILARITY = 75.0;

	public static void compareAll(String submission, double similarity, double threshold) throws IOException, DiffException {

		List<File> comparedProjects = new ArrayList<>();
		
		File submissionsDir = new File(submission);
		List<File> submissions = Arrays.asList(submissionsDir.listFiles());

		System.out.println("==========================================================");
		System.out.println("===> " + submissionsDir.getName());
		System.out.println("==========================================================");
		System.out.println();

		submissions
			.stream()
			.filter(s1 -> s1.isDirectory())
			.forEach(s1 -> {

				comparedProjects.add(s1);
				
				submissions
					.stream()
					.filter(s2 -> s2.isDirectory() && !comparedProjects.contains(s2))
					.forEach(s2 -> {
						try {
							compare(similarity, threshold, s1, s2);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (DiffException e) {
							e.printStackTrace();
						}
					});
				
			});

	}

	public static Comparison compare(double similarity, double threshold, File submission1, File submission2) throws IOException, DiffException {
		Project project1 = new Project(submission1);
		Project project2 = new Project(submission2);
		Comparison comparison = new Comparison(project1, project2);

		double estimatedSimilarity = comparison.getSimilarity(threshold);


		if (estimatedSimilarity > similarity) {

			System.out.println("-----------------------------------------------------------------------");
			System.out.println("--->" + submission1.getName() + " compared with " + submission2.getName());
			System.out.println("-----------------------------------------------------------------------");
			System.out.println("Estimated similarity between projects: " + String.format("%.2f", estimatedSimilarity));
			System.out.println("Showing matches:");
			comparison.getMatches(threshold).forEach(System.out::println);
			System.out.println();

		}
		
		return comparison;
	}
	
	public static void help(Options options) {
		
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("jpc", options);
	}

	public static void main(String[] args) throws IOException, DiffException, URISyntaxException {

		// options
		Options options = new Options();
		options.addOption("h", "help", false, "print this message");
		options.addOption(Option.builder("s")
				.longOpt("similarity")
                .desc("degree of similarity between projects (default value: " + SIMILARITY + ")")
                .build());
		options.addOption(Option.builder("t")
				.longOpt("threshold")
                .desc("degree of similarity between files to be considered equal (default value: " + THRESHOLD + ")")
                .build());
		options.addOption(Option.builder("a")
				.longOpt("all")
                .desc("compare all projects in specified folder")
                .hasArg()
                .argName("folder")
                .build());
		options.addOption(Option.builder("c")
				.longOpt("compare")
                .desc("compare two projects")
                .hasArgs()
                .valueSeparator(',')
                .argName("folder1,folder2")
                .numberOfArgs(2)
                .build());
		
		// create the parser
		CommandLineParser parser = new DefaultParser();
		try {
			
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			
			// prints help
			if (line.hasOption("help") || args.length == 0) {
				help(options);
			}
			
			double similarity = SIMILARITY;
			if (line.hasOption("similarity")) {
				similarity = Double.parseDouble(line.getOptionValue("similarity"));
			}

			double threshold = SIMILARITY;
			if (line.hasOption("threshold")) {
				threshold = Double.parseDouble(line.getOptionValue("threshold"));
			}
			
			if (line.hasOption("all")) {
				
				compareAll(line.getOptionValue("all"), similarity, threshold);
			}

			if (line.hasOption("compare")) {
				
				String [] values = line.getOptionValues("compare");
				File submission1 = new File(values[0]);
				File submission2 = new File(values[1]);
				compare(similarity, threshold, submission1, submission2);
				
			}
			
		} catch (ParseException exp) {
			
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			
		}

	}

}
