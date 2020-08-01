package org.stemmer.state.of.art.RSLP;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.stemmer.automata.pt.rslp.Exception;
import org.stemmer.automata.pt.rslp.ReductionRules;
import org.stemmer.automata.pt.rslp.Rule;

public class RSLPList {

	public static final boolean WITH_ACCENTS = true;

	private boolean suffixRemoved = false;

	private static ReductionRules rules = new ReductionRules();

	public String apply(String in) {
		if (in.length() > 3) {
			if (in.charAt(in.length() - 1) == 's') {
				in = pluralReduction(in);
			}
			if (in.charAt(in.length() - 1) == 'a'
					|| in.charAt(in.length() - 1) == 'ã') {
				in = feminineReduction(in);
			}
			in = degreeReduction(in);
			in = adverbReduction(in);
			in = nounReduction(in);
			if (suffixRemoved == false) {
				in = verbReduction(in);
				if (suffixRemoved == false) {
					in = removeVowel(in);
				}
			}
			suffixRemoved = false;
		}
		return removeAccents(in);

	}

	public String apply(String in, boolean withAccents) {

		if (in.length() > 3) {
			if (in.charAt(in.length() - 1) == 's') {
				in = pluralReduction(in);
			}
			if (in.charAt(in.length() - 1) == 'a'
					|| in.charAt(in.length() - 1) == 'ã') {
				in = feminineReduction(in);
			}
			in = degreeReduction(in);
			in = adverbReduction(in);
			in = nounReduction(in);
			if (suffixRemoved == false) {
				in = verbReduction(in);
				if (suffixRemoved == false) {
					in = removeVowel(in);
				}
			}
			suffixRemoved = false;
		}
		return in;

	}

	private String searchRule(String word, Rule[] list) {
		for (int i = 0; i < list.length; i++) {
			if (word.endsWith(list[i].suffix) == true) {
				if (list[i].exceptionList != null) {
					if (Exception.isException(word, list[i].exceptionList) == false) {
						if (word.length() >= list[i].suffix.length()
								+ list[i].stemSize) {
							word = word.substring(0, word.length()
									- list[i].suffix.length())
									+ list[i].replacement;
						}
					}
				} else {
					if (word.length() >= list[i].suffix.length()
							+ list[i].stemSize) {
						word = word.substring(0,
								word.length() - list[i].suffix.length())
								+ list[i].replacement;
					}
				}
				i = list.length;
			}
		}
		return word;
	}

	public String pluralReduction(String in) {
		return searchRule(in, rules.PLURAL);
	}

	public String feminineReduction(String in) {
		return searchRule(in, rules.FEMININE);
	}

	public String degreeReduction(String in) {
		return searchRule(in, rules.DEGREE);
	}

	public String adverbReduction(String in) {
		if (in.endsWith("mente") == true) {
			if (Exception.isException(in, Exception.ADVERB_MENTE) == false) {
				return in.replace("mente", "");
			}
		}
		return in;
	}

	public String nounReduction(String in) {
		return searchRule(in, rules.NOUN);
	}

	public String verbReduction(String in) {
		return searchRule(in, rules.VERB);
	}

	public static String removeVowel(String in) {
		if (in.length() > 2
				&& Exception.isException(in, Exception.VOWEL) == false) {
			if (in.endsWith("a") || in.endsWith("e") || in.endsWith("o")) {
				return in.substring(0, in.length() - 1);
			}
		}
		return in;
	}

	public static String removeAccents(String in) {
		return in.replaceAll("à|ã|á|â", "a").replaceAll("é|ê", "e")
				.replaceAll("í", "i").replaceAll("ó|õ|ô", "o")
				.replaceAll("ú", "");
	}

	public void test() {

		String file = "wiki_01_pt.txt";
		String encoding = "UTF-8";
		BufferedReader input;
		long initialTime = System.nanoTime();

		try {
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), encoding));
			String linea;
			while (input.ready()) {
				String array[] = new String[2];
				linea = input.readLine();
				array = linea.split(" ");
				if (array.length >= 1) {
					this.apply(array[0]);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long finalTime = System.nanoTime() - initialTime;
		double seconds = finalTime / 1000000000.0;
		System.out.println("Running time was " + seconds + " seconds");
		// System.out.println(finalTime - initialTime);

	}
}