package io.github.fvarrui.jpc;

public class BinaryMatch extends Match<BinaryFile> {

	public BinaryMatch(BinaryFile file1, BinaryFile file2) throws Exception {
		super(file1, file2);
	}

	@Override
	protected double compare() throws Exception {
		return getFile1().getMd5().equals(getFile2().getMd5()) ? 100.0 : 0.0;
	}

}
