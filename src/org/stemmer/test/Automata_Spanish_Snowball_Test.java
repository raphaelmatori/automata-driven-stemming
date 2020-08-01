package org.stemmer.test;

import java.io.IOException;

import org.stemmer.automata.snowball.spanish.PorterStemmer;

public class Automata_Spanish_Snowball_Test {

	public static void main(String[] args) throws IOException {
		PorterStemmer automata = new PorterStemmer();
		automata.test();
	}

}
