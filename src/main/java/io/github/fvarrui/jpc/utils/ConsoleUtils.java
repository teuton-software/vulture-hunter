package io.github.fvarrui.jpc.utils;

import org.apache.commons.lang.StringUtils;

public class ConsoleUtils {

	public static String frame(String content) {
		StringBuffer buffer = new StringBuffer();
		int length = content.length();
		String line = StringUtils.repeat("-", length + 2);
		buffer.append("+" + line + "+" + "\n");
		buffer.append("| " + content + " |" + "\n");
		buffer.append("+" + line + "+" + "\n");
		return buffer.toString();
	}

}
