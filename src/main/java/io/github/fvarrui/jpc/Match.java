package io.github.fvarrui.jpc;

public abstract class Match<T extends ComparedFile> {

	private T file1;
	private T file2;
	private double similarity = 0.0;

	public Match(T file1, T file2) throws Exception {
		this.file1 = file1;
		this.file2 = file2;
		this.similarity = compare();
	}
	
	protected abstract double compare() throws Exception;
	
	public T getFile1() {
		return file1;
	}
	
	public T getFile2() {
		return file2;
	}
	
	public double getSimilarity() {
		return similarity;
	}
	
	@Override
	public String toString() {
		return getFile1() + " compared with " + getFile2() + ": files match in a " + String.format("%.2f", getSimilarity()) + "%. ";
	}
	
}
