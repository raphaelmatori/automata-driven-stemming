package org.stemmer.test;

import java.io.IOException;

import org.stemmer.state.of.art.Porter.PorterStemmer;

public class State_of_Art_Porter_Test {

	public static void main(String[] args) throws IOException {
		PorterStemmer porter = new PorterStemmer();
		porter.test();
	}

}
