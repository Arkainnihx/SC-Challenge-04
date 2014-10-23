package com.github.arkainnihx.SC.Challenge04;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.*;

public class Interpreter {
	static String fileName;
	static String[] program;
	static HashMap<String, Integer> variables = new HashMap<String, Integer>();
	
	public static void main(String[] args) throws IOException {
		File directory = new File(".");
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		String[] fileList = directory.list(
				new FilenameFilter() {
					public boolean accept(File dir, String name) {
						String lowercaseName = name.toLowerCase();
						if (lowercaseName.endsWith(".bb")) {
							return true;
						} else {
							return false;
						}
					}
				} );
		
		System.out.println("Available programs to run:");
		System.out.println();
		for (String element : fileList) {
			System.out.println(element);
		}
		System.out.println();
		System.out.println("What program would you like to run?");
		fileName = userInput.readLine();
		program = new FileInput(fileName).getFileContents();
		for (String programLine : program) {
			interpret(programLine);
			System.out.println(programLine);
		}
	}
	
	public static void interpret(String programLine) {
		Pattern validLine = Pattern.compile("\\s*((clear|incr|decr)\\s+(\\w+)|(while\\s+(\\w+)\\s+not\\s+0\\s+do)|(end));");
		Matcher line = validLine.matcher(programLine);
		boolean valid = line.matches();
		if (valid) {
			//System.out.println("Valid line.");
			
		} else {
			System.err.println("Invalid line.");
		}
	}
	
}