package com.parse.starter;
import java.util.*;

// taken from website: http://technojeeves.com/index.php/9-freebies/58-scramble-a-string-in-java
public class Scramble {
	// return a scrambled s string
	public static String scramble(String s) {
		String[] scram = s.split("");
		List<String> letters = Arrays.asList(scram);
		Collections.shuffle(letters);
		StringBuilder sb = new StringBuilder(s.length());
		for (String c: letters) {
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		// nothing here
	}
}
