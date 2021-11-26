package io.github.fvarrui.vulturehunter;

import java.io.File;
import java.io.IOException;

import io.github.fvarrui.vulturehunter.utils.HashingUtils;

public class BinaryFile extends ComparedFile {

	private String md5;

	public BinaryFile(File file, Project project) throws IOException {
		super(file, project);
		this.md5 = HashingUtils.md5(file);
	}

	public String getMd5() {
		return md5;
	}

	@Override
	public Match<?> compare(ComparedFile file) throws Exception {
		return new BinaryMatch(this, (BinaryFile) file);
	}

}
