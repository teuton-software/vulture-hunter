package io.github.fvarrui.jpc.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class HashingUtils {
	
	public static String md5(File f) {	
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update(Files.readAllBytes(f.toPath()));
		    byte[] digest = md.digest();
		    return Hex.encodeHexString(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}

}
