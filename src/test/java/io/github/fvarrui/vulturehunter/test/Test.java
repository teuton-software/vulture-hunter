package io.github.fvarrui.vulturehunter.test;

import java.io.IOException;
import java.net.URISyntaxException;

import io.github.fvarrui.vulturehunter.Hunter;

public class Test {

	public static void main(String[] args) throws IOException, URISyntaxException {

		Hunter.main(new String [] { "-c", ".,.", "-t", "java,fxml", "-e", "target/.*", "\\..*" });

	}

}
