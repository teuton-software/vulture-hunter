package io.github.fvarrui.vulturehunter;

import java.io.File;
import java.io.FileNotFoundException;
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

import io.github.fvarrui.vulturehunter.utils.ConsoleUtils;
import io.github.fvarrui.vulturehunter.utils.ZipUtils;

public class Hunter {

	private static final String COMMAND_NAME = "hunt";
	private static final String COMMAND_DESCRIPTION = "Comparison tool to determine the degree of similarity between two projects (aka \"Vulture Hunter\")";

	private static final String OPTION_ALL = "all";
	private static final String OPTION_HELP = "help";
	private static final String OPTION_COMPARE = "compare";
	private static final String OPTION_SIMILARITY = "similarity";
	private static final String OPTION_THRESHOLD = "threshold";
	private static final String OPTION_TEXT_FILES = "text";
	private static final String OPTION_BINARY_FILES = "binary";
	private static final String OPTION_EXCLUDED_FILES = "excluded";

	private static final double THRESHOLD = 80.0;
	private static final double SIMILARITY = 75.0;

	public static void compareAll(String submission, double similarity, double threshold, List<String> textFiles, List<String> binaryFiles, List<String> excludedFiles) throws IOException {

		List<File> comparedProjects = new ArrayList<>();
		
		File submissionsDir = new File(submission);
		
		// checks if specified file/folder exists 
		if (!submissionsDir.exists()) {
			throw new FileNotFoundException(submissionsDir + " doesn't exist");
		}
		
		// checks if it's a file
		if (submissionsDir.isFile()) {

			File submissionsFile = submissionsDir;
			
			// checks if it's a zip file
			if (ZipUtils.isCompressed(submissionsFile)) {
				
				System.out.print("Unzipping file " + submissionsFile + " ... ");
				submissionsDir = ZipUtils.uncompress(submissionsFile, true);
				System.out.println("[OK]");
				
			} else {
				
				System.out.println("[ERROR]");
				throw new IllegalArgumentException(submissionsFile + " isn't a zipped file");
				
			}
			
		}

		System.out.println(ConsoleUtils.frame(submissionsDir.getName()));
		
		List<File> submissions = Arrays.asList(submissionsDir.listFiles());
		
		// process submissions
		submissions
			.stream()
			.filter(s -> s.isDirectory())
			.forEach(s -> processSubmission(s));

		System.out.println();
		
		// compares all found projects
		submissions
			.stream()
			.filter(s1 -> s1.isDirectory())
			.forEach(s1 -> {

				comparedProjects.add(s1);
				
				submissions
					.stream()
					.filter(s2 -> s2.isDirectory() && !comparedProjects.contains(s2))
					.map(s2 -> compare(threshold, s1, s2, textFiles, binaryFiles, excludedFiles))
					.filter(c -> c != null)
					.filter(c -> c.getSimilarity() > similarity)
					.forEach(System.out::println);
				
			});

	}
	
	public static void processSubmission(File submissionDir) {
		System.out.println("Processing submission: " + submissionDir);
		Arrays.asList(submissionDir.listFiles())
			.stream()
			.filter(f -> ZipUtils.isCompressed(f))
			.forEach(f -> {
				System.out.print("Unzipping " + f.getName() + " ... ");
				try {
					ZipUtils.uncompress(f);
					f.delete();
					System.out.println("[OK]");
				} catch (IOException e) {
					System.out.println("[ERROR] " + e.getMessage());
				}				
			});
	}

	public static Comparison compare(double threshold, File submission1, File submission2, List<String> textFiles, List<String> binaryFiles, List<String> excludedFiles) {
		try {
			Project project1 = new Project(submission1, textFiles, binaryFiles, excludedFiles);
			Project project2 = new Project(submission2, textFiles, binaryFiles, excludedFiles);
			Comparison comparison = new Comparison(project1, project2);
			comparison.calculateSimilarity(threshold);
			return comparison;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void help(Options options) {
		
		System.out.println(COMMAND_DESCRIPTION);
		
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(COMMAND_NAME, options);
		
	}

	public static void main(String[] args) throws IOException, URISyntaxException {

		// options
		Options options = new Options();
		options.addOption("h", "help", false, "print this message");
		options.addOption(Option.builder("s")
				.longOpt("similarity")
                .desc("degree of similarity between projects (default value: " + SIMILARITY + ")")
                .hasArg()
                .build());
		options.addOption(Option.builder("th")
				.longOpt("threshold")
                .desc("degree of similarity between files to be considered equal (default value: " + THRESHOLD + ")")
                .hasArg()
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
		options.addOption(Option.builder("b")
				.longOpt("binary")
                .desc("extensions of the included binary files (default value: all files)")
                .hasArgs()
                .valueSeparator(',')
                .argName("ext1,ext2,...")
                .build());
		options.addOption(Option.builder("t")
				.longOpt("text")
                .desc("extensions of the included text files (default value: empty)")
                .hasArgs()
                .valueSeparator(',')
                .argName("ext1,ext2,...")
                .build());
		options.addOption(Option.builder("e")
				.longOpt("excluded")
                .desc("excluded relative paths (default value: empty)")
                .hasArgs()
                .valueSeparator(',')
                .argName("path1,path2,...")
                .build());
		
		// create the parser
		CommandLineParser parser = new DefaultParser();
		try {
			
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			
			// prints help
			if (line.hasOption(OPTION_HELP) || args.length == 0) {
				help(options);
			}
			
			double similarity = SIMILARITY;
			if (line.hasOption(OPTION_SIMILARITY)) {
				similarity = Double.parseDouble(line.getOptionValue(OPTION_SIMILARITY));
			}

			double threshold = THRESHOLD;
			if (line.hasOption(OPTION_THRESHOLD)) {
				threshold = Double.parseDouble(line.getOptionValue(OPTION_THRESHOLD));
			}
			
			List<String> binaryFiles = new ArrayList<>();
			if (line.hasOption(OPTION_BINARY_FILES)) {
				binaryFiles = Arrays.asList(line.getOptionValues(OPTION_BINARY_FILES));
			}

			List<String> textFiles = new ArrayList<>();
			if (line.hasOption(OPTION_TEXT_FILES)) {
				textFiles = Arrays.asList(line.getOptionValues(OPTION_TEXT_FILES));
			}

			List<String> excludedFiles = new ArrayList<>();
			if (line.hasOption(OPTION_EXCLUDED_FILES)) {
				excludedFiles = Arrays.asList(line.getOptionValues(OPTION_EXCLUDED_FILES));
			}

			if (line.hasOption(OPTION_ALL)) {
				compareAll(line.getOptionValue(OPTION_ALL), similarity, threshold, textFiles, binaryFiles, excludedFiles);
			} else if (line.hasOption(OPTION_COMPARE)) {
				String [] values = line.getOptionValues(OPTION_COMPARE);
				File submission1 = new File(values[0]);
				File submission2 = new File(values[1]);
				System.out.println(compare(threshold, submission1, submission2, textFiles, binaryFiles, excludedFiles));				
			}
			
		} catch (ParseException exp) {
			
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			
		}

	}

}
