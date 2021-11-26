package io.github.fvarrui.vulturehunter;

import java.io.IOException;
import java.util.List;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

public class TextMatch extends Match<TextFile> {

	private List<AbstractDelta<String>> deltas;

	public TextMatch(TextFile file1, TextFile file2) throws Exception {
		super(file1, file2);
	}
	
	private int diffFiles(TextFile testFile1, TextFile testFile2) throws IOException {
		Patch<String> patch = DiffUtils.diff(testFile1.getLines(), testFile2.getLines());
		deltas = patch.getDeltas();
		return countDifferences(deltas);
	}

	private int countDifferences(List<AbstractDelta<String>> deltas) {
		int count = 0;
		for (AbstractDelta<String> delta : deltas) {
			count += delta.getSource().size() + delta.getTarget().size();
		}
		return count;
	}
	
	protected double compare() throws Exception {
		int differences = diffFiles(getFile1(), getFile2());
		int file1Lines = getFile1().getLines().size();
		int file2Lines = getFile2().getLines().size();
		return 100.0 - ((double)differences / (double)(file1Lines + file2Lines)) * 100.0;
	}
	
	public List<AbstractDelta<String>> getDeltas() {
		return deltas;
	}
	
	@Override
	public String toString() {
		return getFile1() + " compared with " + getFile2() + ": files match in a " + String.format("%.2f", getSimilarity()) + "%. ";
	}
	
}
