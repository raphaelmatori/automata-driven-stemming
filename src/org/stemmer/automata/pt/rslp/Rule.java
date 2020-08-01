package org.stemmer.automata.pt.rslp;

public class Rule{

	public String suffix, replacement;
	public int stemSize;
	public String[] exceptionList;

	public Rule(String suffix, int stemSize, String replacement, String[] exceptionList){
		this.suffix = suffix;
		this.stemSize = stemSize;
		this.replacement = replacement;
		this.exceptionList = exceptionList;
	}

}