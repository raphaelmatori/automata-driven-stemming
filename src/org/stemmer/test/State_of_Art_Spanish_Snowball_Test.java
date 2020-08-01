package org.stemmer.test;

import java.io.IOException;

import org.stemmer.state.of.art.snowball.spanish.PorterStemmer;

public class State_of_Art_Spanish_Snowball_Test {

	public static void main(String[] args) throws IOException {
		PorterStemmer porter = new PorterStemmer();
		porter.test();
	}

}
