package com.github.arkainnihx.SC.Challenge04;

public class Variable {
	private final String identifier;
	private int value = 0;
	
	public Variable(String id) {
		identifier = id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
