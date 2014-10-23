package com.github.arkainnihx.SC.Challenge04;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileInput {
	String filePath;
	BufferedReader myFileParser;
	FileReader myFileReader;
	
	public FileInput(String filePath) {
		this.filePath = filePath;
		try {
			myFileReader = new FileReader(this.filePath);
			myFileParser = new BufferedReader(myFileReader);
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			e.printStackTrace();
		}
	}
	
	public String[] getFileContents() {
		ArrayList<String> fileContents = new ArrayList<String>();
		try {
			while (myFileParser.ready()) {
			fileContents.add(myFileParser.readLine());
			}
			return fileContents.toArray(new String[fileContents.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
