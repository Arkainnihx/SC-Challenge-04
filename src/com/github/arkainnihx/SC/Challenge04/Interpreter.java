package com.github.arkainnihx.SC.Challenge04;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.*;

public class Interpreter {
	static int programCounter = 0;
	static String fileName;
	static String[] program;
	static HashMap<String, Integer> variables = new HashMap<String, Integer>();
	static Stack<Integer> loopStack = new Stack<Integer>();
	static Stack<Integer> endStack = new Stack<Integer>();
	static Keyword currentCommand = null;
	static String currentIdentifier = "";
	static boolean quit = false;
	static boolean validFile = false;
	
	public static void main(String[] args) throws IOException {
		File directory = new File("BB-Programs/");
		String[] fileList = directory.list(
			new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.endsWith(".bones")) {
						return true;
					} else {
						return false;
					}
				}
			}
		);
		do {
			System.out.println("Available programs to run:");
			System.out.println();
			for (String element : fileList) {
				System.out.println(element);
			}
			do {
				try {
					System.out.println();
					System.out.println("What program would you like to run?");
					fileName = directory.toString() + "/" + getUserInput();
					if (!fileName.contains(".bones"))
						fileName += ".bones";
					program = new FileInput(fileName).getFileContents();
					validFile = true;
				} catch (IOException e) {
					System.out.println("Invalid input. No file of that name.");
				}
			} while (!validFile);
			runProgram();
			System.out.println();
			System.out.println("Would you like to run another program? (Y/N)");
			if (getUserInput().equalsIgnoreCase("n")) quit = true;
		} while (!quit);
	}
	
	public static String getUserInput() throws IOException {
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
				return userInput.readLine();
	}
	
	public static int runProgram() {
		programCounter = 0;
		variables = new HashMap<String, Integer>();
		loopStack = new Stack<Integer>();
		endStack = new Stack<Integer>();
		currentCommand = null;
		currentIdentifier = "";
		do {
			try {
				System.out.print(programCounter + ": ");
				System.out.print(program[programCounter] + "      ");
				currentCommand = interpretCommand(program[programCounter]);
				currentIdentifier = interpretIdentifier(program[programCounter]);
				execute(currentCommand, currentIdentifier);
			} catch (BareBonesException e) {
				System.out.println("Exiting program.");
				return 0;
			}
			printVariables();
		} while (programCounter < program.length);
		System.out.println();
		System.out.println("Program complete:");
			printVariables();
		return 0;
	}
	
	public static void printVariables() {
		for (int varCount = 0; varCount < variables.size() - 1; varCount++) {
			System.out.print(variables.keySet().toArray()[varCount] + " = " + variables.get(variables.keySet().toArray()[varCount]) + ", ");
		}
		System.out.println(variables.keySet().toArray()[variables.size() - 1] + " = " + variables.get(variables.keySet().toArray()[variables.size() - 1]));
	}
	
	public static HashMap<Keyword, String> interpret(String programLine) throws BareBonesException {
		Keyword command = null;
		String identifier = "";
		HashMap<Keyword, String> parsedLine = new HashMap<Keyword, String>();
		Pattern validLine = Pattern.compile("\\s*(?:(clear|incr|decr)\\s+(\\w+)|(while)\\s+(\\w+)\\s+not\\s+0\\s+do|(end));");
		Matcher line = validLine.matcher(programLine);
		boolean valid = line.matches();
		if (valid) {
			//System.out.println("Valid line.");
			if (line.group(1) != null) {
				for (Keyword keyword : Keyword.values()) {
					if (keyword.toString().equalsIgnoreCase(line.group(1))) {
						command = keyword;
						identifier = line.group(2);
					}
				}
			} else if (line.group(3) != null) {
				command = Keyword.WHILE;
				identifier = line.group(4);
			} else {
				command = Keyword.END;
			}
			parsedLine.put(command, identifier);
 		} else {
			System.out.println("Invalid line.");
			throw new BareBonesException();
		}
		return parsedLine;
	}
	
	public static Keyword interpretCommand(String programLine) throws BareBonesException {
		return (Keyword) interpret(programLine).keySet().toArray()[0];
	}
	
	public static String interpretIdentifier(String programLine) throws BareBonesException {
		return  (String) interpret(programLine).values().toArray()[0];
	}
	
	public static void execute(Keyword command, String identifier) throws BareBonesException {
		switch (command) {
		case CLEAR :
				variables.put(identifier, 0);
				programCounter++;
			break;
		case INCR :
			if (variables.containsKey(identifier)) {
				variables.put(identifier, variables.get(identifier) + 1);
			} else {
				variables.put(identifier, 1);
			}
			programCounter++;
			break;
		case DECR :
			if (variables.containsKey(identifier)) {
				variables.put(identifier, variables.get(identifier) - 1);
				if (variables.get(identifier) < 0) {
					System.out.println("Invalid operation. Negative values aren't supported.");
					throw new BareBonesException();
				}
			} else {
				System.out.println("Invalid operation. Negative values aren't supported.");
				throw new BareBonesException();
			}
				programCounter++;
			break;
		case WHILE :
			try {
				if (loopStack.peek() != programCounter) {
					loopStack.push(programCounter);
					endStack.push(endSearch(loopStack.peek()));
				}
			} catch (EmptyStackException e) {
				loopStack.push(programCounter);
				endStack.push(endSearch(loopStack.peek()));
			}
			if (variables.get(identifier) == 0) {
				loopStack.pop();
				programCounter = endStack.pop() + 1;
			} else {
				programCounter++;
			}
			break;
		case END :
			programCounter = loopStack.peek();
			break;
		}
	}
	
	public static int endSearch(int whilePointer) throws BareBonesException {
		int lineCounter = whilePointer + 1;
		int loopDepth = 0;
		do {
			if (loopDepth == 0 && interpretCommand(program[lineCounter]) == Keyword.END) {
				return lineCounter;
			} else if (interpretCommand(program[lineCounter]) == Keyword.WHILE) {
				loopDepth++;
			} else if (loopDepth > 0 && interpretCommand(program[lineCounter]) == Keyword.END) {
				loopDepth--;
			}
			lineCounter++;
		} while (lineCounter < program.length);
		System.out.println("Missing end statement.");
		throw new BareBonesException();
	} 
}