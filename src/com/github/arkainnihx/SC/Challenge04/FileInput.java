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
	
	public FileInput(String filePath) throws FileNotFoundException {
		this.filePath = filePath;
			myFileReader = new FileReader(this.filePath);
			myFileParser = new BufferedReader(myFileReader);
	}
	
	public String[] getFileContents() throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();
			while (myFileParser.ready()) {
			fileContents.add(myFileParser.readLine());
			}
			return fileContents.toArray(new String[fileContents.size()]);
	}
}