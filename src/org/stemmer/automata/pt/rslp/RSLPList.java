package org.stemmer.automata.pt.rslp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class RSLPList {
	public static final boolean WITH_ACCENTS = true;

	private static final int STOP = -1;

	private boolean suffixRemoved = false;

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

	public String pluralReduction(String in) {

		int position = in.length() - 2;
		int state = 2;
		boolean accept = false;

		while (position >= 0) {
			switch (state) {
			case 2:
				if (accept) {
					// rule s
					if (Exception.isException(in, Exception.PLURAL_S) == false) {
						if (position >= 1) {
							in = in.substring(0, in.length() - 1);
						}
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'e':
						state = 4;
						position--;
						break;
					case 'i':
						state = 5;
						position--;
						break;
					case 'n':
						// rule ns
						if (position >= 0) {
							in = in.substring(0, position) + "m";
						}
						position = STOP;
						break;
					default:
						// rule s
						if (Exception.isException(in, Exception.PLURAL_S) == false) {
							if (position >= 1) {
								in = in.substring(0, in.length() - 1);
							}
						}
						position = STOP;
						break;
					}
				}
				break;
			case 4:
				switch (in.charAt(position)) {
				case 'õ':
					// rule ões
					if (position >= 2) {
						in = in.substring(0, position) + "ão";
					}
					position = STOP;
					break;
				case 'ã':
					// rule ães
					if (Exception.isException(in, Exception.PLURAL_AES) == false) {
						if (position >= 0) {
							in = in.substring(0, position) + "ão";
						}
					}
					position = STOP;
					break;
				case 'l':
					// rule les
					if (position >= 2) {
						in = in.substring(0, position) + "l";
					}
					position = STOP;
					break;
				case 'r':
					// rule res
					if (Exception.isException(in, Exception.PLURAL_RES) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "r";
						}
					}
					position = STOP;
					break;
				default:
					state = 2;
					accept = true;
					break;
				}
				break;
			case 5:
				switch (in.charAt(position)) {
				case 'a':
					// rule ais
					if (Exception.isException(in, Exception.PLURAL_AIS) == false) {
						if (position >= 0) {
							in = in.substring(0, position) + "al";
						}
					}
					position = STOP;
					break;
				case 'e':
					// rule eis
					if (Exception.isException(in, Exception.PLURAL_EIS) == false) {
						if (position >= 1) {
							in = in.substring(0, position) + "el";
						}
					}
					position = STOP;
					break;
				case 'é':
					// rule éis
					if (position >= 1) {
						in = in.substring(0, position) + "el";
					}
					position = STOP;
					break;
				case 'ó':
					// rule óis
					if (position >= 1) {
						in = in.substring(0, position) + "ol";
					}
					position = STOP;
					break;
				default:
					// rule is
					if (Exception.isException(in, Exception.PLURAL_IS) == false) {
						if (position >= 1) {
							in = in.substring(0, position + 1) + "il";
						}
					}
					position = STOP;
					break;
				}
				break;
			}
		}
		return in;
	}

	public String feminineReduction(String in) {

		int position = in.length() - 2;
		int state = 1;

		while (position >= 0) {
			switch (state) {
			case 1:
				switch (in.charAt(position)) {
				case 'n':
					state = 3;
					position--;
					break;
				case 'r':
					state = 4;
					position--;
					break;
				case 'h':
					state = 5;
					position--;
					break;
				case 's':
					state = 6;
					position--;
					break;
				case 'c':
					state = 7;
					position--;
					break;
				case 'd':
					state = 8;
					position--;
					break;
				case 'm':
					state = 9;
					position--;
					break;
				case 'v':
					state = 10;
					position--;
					break;
				default:
					if (in.charAt(in.length() - 1) == 'ã') {
						// rule ã
						if (Exception.isException(in, Exception.FEMININE_A) == false) {
							if (position >= 1) {
								in = in.substring(0, position + 1) + "ão";
							}
						}
					}
					position = STOP;
					break;
				}
				break;
			case 3:
				if (in.charAt(position) == 'o') {
					// rule ona
					if (Exception.isException(in, Exception.FEMININE_ONA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "ão";
						}
					}
				} else {
					// rule na
					if (Exception.isException(in, Exception.FEMININE_NA) == false) {
						if (position >= 3) {
							in = in.substring(0, position + 1) + "no";
						}
					}
				}
				position = STOP;
				break;
			case 4:
				switch (in.charAt(position)) {
				case 'i':
					state = 23;
					position--;
					break;
				case 'o':
					// rule ora
					if (position >= 2) {
						in = in.substring(0, position) + "or";
					}
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 5:
				if (in.charAt(position) == 'n') {
					state = 13;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 6:
				switch (in.charAt(position)) {
				case 'e':
					// rule esa
					if (Exception.isException(in, Exception.FEMININE_ESA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "ês";
						}
					}
					break;
				case 'o':
					// rule osa
					if (Exception.isException(in, Exception.FEMININE_OSA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "oso";
						}
					}
					break;
				}
				position = STOP;
				break;
			case 7:
				switch (in.charAt(position)) {
				case 'i':
					// rule ica
					if (Exception.isException(in, Exception.FEMININE_ICA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "ico";
						}
					}
					position = STOP;
					break;
				case 'a':
					state = 16;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 8:
				switch (in.charAt(position)) {
				case 'a':
					// rule ada
					if (Exception.isException(in, Exception.FEMININE_ADA) == false) {
						if (position >= 1) {
							in = in.substring(0, position) + "ado";
						}
					}
					break;
				case 'i':
					// rule ida
					if (Exception.isException(in, Exception.FEMININE_IDA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "ido";
						}
					}
					break;
				case 'í':
					// rule ída
					if (Exception.isException(in, Exception.FEMININE_IIDA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "ído";
						}
					}
					break;
				}
				position = STOP;
				break;
			case 9:
				if (in.charAt(position) == 'i') {
					// rule ima
					if (Exception.isException(in, Exception.FEMININE_IMA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "imo";
						}
					}
				}
				position = STOP;
				break;
			case 10:
				if (in.charAt(position) == 'i') {
					// rule iva
					if (Exception.isException(in, Exception.FEMININE_IVA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "ivo";
						}
					}
				}
				position = STOP;
				break;
			case 13:
				if (in.charAt(position) == 'i') {
					// rule inha
					if (Exception.isException(in, Exception.FEMININE_INHA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "inho";
						}
					}
				}
				position = STOP;
				break;
			case 16:
				if (in.charAt(position) == 'í') {
					// rule íaca
					if (position >= 2) {
						in = in.substring(0, position) + "íaco";
					}
				}
				position = STOP;
				break;
			case 23:
				if (in.charAt(position) == 'e') {
					// rule eira
					if (Exception.isException(in, Exception.FEMININE_EIRA) == false) {
						if (position >= 2) {
							in = in.substring(0, position) + "eiro";
						}
					}
				}
				position = STOP;
				break;
			}
		}
		return in;
	}

	public String degreeReduction(String in) {

		int position = in.length() - 1;
		int state = 0;
		boolean accept = false;

		while (position >= 0) {
			switch (state) {
			case 0:
				switch (in.charAt(position)) {
				case 'a':
					state = 1;
					position--;
					break;
				case 'o':
					state = 4;
					position--;
					break;
				case 'z':
					state = 36;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 1:
				switch (in.charAt(position)) {
				case 'ç':
					state = 2;
					position--;
					break;
				case 'r':
					state = 33;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 2:
				if (in.charAt(position) == 'u') {
					// rule uça
					if (position >= 3) {
						in = in.substring(0, position);
					}
				}
				position = STOP;
				break;
			case 4:
				switch (in.charAt(position)) {
				case 'm':
					state = 5;
					position--;
					break;
				case 'h':
					state = 6;
					position--;
					break;
				case 'ã':
					state = 7;
					position--;
					break;
				case 'ç':
					state = 8;
					position--;
					break;
				case 'i':
					state = 41;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 5:
				if (in.charAt(position) == 'i') {
					state = 9;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 6:
				if (in.charAt(position) == 'n') {
					state = 10;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 7:
				if (accept) {
					// rule ão
					if (Exception.isException(in, Exception.DEGREE_AO) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
						}
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'd':
						state = 44;
						position--;
						break;
					case 'h':
						state = 11;
						position--;
						break;
					case 'z':
						// rule zão
						if (Exception.isException(in, Exception.DEGREE_ZAO) == false) {
							if (position >= 1) {
								in = in.substring(0, position);
							}
						}
						position = STOP;
						break;
					default:
						// rule ão
						if (Exception.isException(in, Exception.DEGREE_AO) == false) {
							if (position >= 2) {
								in = in.substring(0, position + 1);
							}
						}
						position = STOP;
						break;
					}
				}
				break;
			case 8:
				if (in.charAt(position) == 'a') {
					// rule aço
					if (Exception.isException(in, Exception.DEGREE_ACO) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
						}
					}
				}
				position = STOP;
				break;
			case 9:
				switch (in.charAt(position)) {
				case 's':
					state = 13;
					position--;
					break;
				case 'r':
					state = 14;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 10:
				if (in.charAt(position) == 'i') {
					state = 15;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 11:
				if (in.charAt(position) == 'l') {
					state = 16;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 13:
				if (in.charAt(position) == 's') {
					state = 17;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 14:
				if (in.charAt(position) == 'r') {
					state = 18;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 15:
				if (accept) {
					// rule inho
					if (Exception.isException(in, Exception.DEGREE_INHO) == false) {
						if (position >= 2) {
							in = in.substring(0, position + 1);
						}
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'z':
						// rule zinho
						if (position >= 1) {
							in = in.substring(0, position);
						}
						position = STOP;
						break;
					case 'u':
						state = 20;
						position--;
						break;
					case 'd':
						state = 21;
						position--;
						break;
					default:
						// rule inho
						if (Exception.isException(in, Exception.DEGREE_INHO) == false) {
							if (position >= 2) {
								in = in.substring(0, position + 1);
							}
						}
						position = STOP;
						break;
					}
				}
				break;
			case 16:
				// rule alhão
				if (in.charAt(position) == 'a') {
					if (position >= 3) {
						in = in.substring(0, position);
					}
				}
				position = STOP;
				break;
			case 17:
				if (in.charAt(position) == 'í') {
					state = 23;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 18:
				if (in.charAt(position) == 'é') {
					// rule érrimo
					if (position >= 3) {
						in = in.substring(0, position);
					}
				}
				position = STOP;
				break;
			case 20:
				if (in.charAt(position) == 'q') {
					// rule quinho
					if (position >= 3) {
						in = in.substring(0, position) + "c";
					}
				} else {
					// rule uinho
					if (position >= 3) {
						in = in.substring(0, position + 1);
					}
				}
				position = STOP;
				break;
			case 21:
				if (in.charAt(position) == 'a') {
					// rule adinho
					if (position >= 2) {
						in = in.substring(0, position);
					}
					position = STOP;
				} else {
					state = 15;
					position += 1;
					accept = true;
				}
				break;
			case 23:
				if (accept) {
					// rule íssimo
					if (position >= 2) {
						in = in.substring(0, position + 1);
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'd':
						// rule díssimo
						if (position >= 4) {
							in = in.substring(0, position);
						}
						position = STOP;
						break;
					case 'l':
						state = 28;
						position--;
						break;
					default:
						// rule íssimo
						if (position >= 2) {
							in = in.substring(0, position + 1);
						}
						position = STOP;
						break;
					}
				}
				break;
			case 28:
				if (in.charAt(position) == 'i') {
					state = 29;
					position--;
				} else {
					state = 23;
					position += 1;
					accept = true;
				}
				break;
			case 29:
				if (in.charAt(position) == 'b') {
					state = 30;
					position--;
				} else {
					state = 23;
					position += 2;
					accept = true;
				}
				break;
			case 30:
				if (in.charAt(position) == 'a') {
					// rule abilíssimo
					if (position >= 4) {
						in = in.substring(0, position);
					}
					position = STOP;
				} else {
					state = 23;
					position += 3;
					accept = true;
				}
				break;
			case 33:
				if (in.charAt(position) == 'r') {
					state = 34;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 34:
				if (in.charAt(position) == 'a') {
					// rule arra
					if (position >= 2) {
						in = in.substring(0, position);
					}
				}
				position = STOP;
				break;
			case 36:
				if (in.charAt(position) == 'a') {
					state = 37;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 37:
				if (in.charAt(position) == 'r') {
					state = 38;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 38:
				if (in.charAt(position) == 'r') {
					state = 39;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 39:
				if (in.charAt(position) == 'a') {
					// rule arraz
					if (position >= 3) {
						in = in.substring(0, position);
					}
				}
				position = STOP;
				break;
			case 41:
				if (in.charAt(position) == 'z') {
					state = 42;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 42:
				if (in.charAt(position) == 'á') {
					// rule ázio
					if (Exception.isException(in, Exception.DEGREE_AZIO) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
						}
					}
				}
				position = STOP;
				break;
			case 44:
				if (in.charAt(position) == 'a') {
					// rule adão
					if (position >= 3) {
						in = in.substring(0, position);
					}
				}
				position = STOP;
				break;
			}
		}
		return in;
	}

	public String adverbReduction(String in) {
		// rule mente
		if (in.endsWith("mente") == true) {
			if (Exception.isException(in, Exception.ADVERB_MENTE) == false) {
				return in.replace("mente", "");
			}
		}
		return in;
	}

	public String nounReduction(String in) {

		int position = in.length() - 1;
		int state = 1;
		boolean accept = false;

		while (position >= 0) {
			switch (state) {
			case 1:
				switch (in.charAt(position)) {
				case 'a':
					state = 2;
					position--;
					break;
				case 'e':
					state = 34;
					position--;
					break;
				case 'o':
					state = 71;
					position--;
					break;
				case 'ç':
					state = 27;
					position--;
					break;
				case 'm':
					state = 67;
					position--;
					break;
				case 'r':
					state = 121;
					position--;
					break;
				case 's':
					state = 128;
					position--;
					break;
				case 'z':
					state = 130;
					position--;
					break;
				case 'l':
					state = 50;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 2:
				switch (in.charAt(position)) {
				case 't':
					state = 3;
					position--;
					break;
				case 'i':
					state = 4;
					position--;
					break;
				case 'r':
					state = 126;
					position--;
					break;
				case 'z':
					state = 5;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 3:
				if (in.charAt(position) == 's') {
					state = 6;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 4:
				switch (in.charAt(position)) {
				case 'r':
					state = 7;
					position--;
					break;
				case 'c':
					state = 8;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 5:
				if (in.charAt(position) == 'e') {
					// rule eza
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 6:
				if (in.charAt(position) == 'i') {
					state = 10;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 7:
				switch (in.charAt(position)) {
				case 'o':
					// rule oria
					if (Exception.isException(in, Exception.NOUN_ORIA) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				case 'ó':
					state = 11;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 8:
				if (in.charAt(position) == 'n') {
					state = 12;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 10:
				if (accept) {
					// rule ista
					if (position >= 3) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'l':
						state = 14;
						position--;
						break;
					case 'n':
						state = 15;
						position--;
						break;
					default:
						// rule ista
						if (position >= 3) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
						break;
					}
				}
				break;
			case 11:
				if (in.charAt(position) == 't') {
					state = 21;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 12:
				switch (in.charAt(position)) {
				case 'ê':
					// rule ência
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'â':
					// rule ância
					if (Exception.isException(in, Exception.NOUN_ANCIA) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				}
				position = STOP;
				break;
			case 14:
				if (in.charAt(position) == 'a') {
					state = 16;
					position--;
				} else {
					state = 10;
					position += 1;
					accept = true;
				}
				break;
			case 15:
				if (in.charAt(position) == 'o') {
					state = 134;
					position--;
				} else {
					state = 10;
					position += 1;
					accept = true;
				}
				break;
			case 16:
				if (accept) {
					// rule alista
					if (position >= 4) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'i') {
						state = 17;
						position--;
					} else {
						// rule alista
						if (position >= 4) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
					}
				}
				break;
			case 17:
				if (in.charAt(position) == 'c') {
					state = 18;
					position--;
				} else {
					state = 16;
					position += 1;
					accept = true;
				}
				break;
			case 18:
				if (in.charAt(position) == 'n') {
					state = 19;
					position--;
				} else {
					state = 16;
					position += 2;
					accept = true;
				}
				break;
			case 19:
				if (in.charAt(position) == 'e') {
					// rule encialista
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 16;
					position += 3;
					accept = true;
				}
				break;
			case 21:
				if (in.charAt(position) == 'a') {
					// rule atória
					if (position >= 4) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 24:
				if (in.charAt(position) == 'c') {
					state = 25;
					position--;
				} else {
					state = 10;
					position += 3;
					accept = true;
				}
				break;
			case 25:
				if (in.charAt(position) == 'i') {
					// rule icionista
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				} else {
					// rule cionista
					if (position >= 4) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 27:
				switch (in.charAt(position)) {
				case 'a':
					state = 28;
					position--;
					break;
				case 'i':
					// rule iç
					if (Exception.isException(in, Exception.NOUN_IC) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 28:
				if (accept) {
					// rule aç
					if (Exception.isException(in, Exception.NOUN_AC) == false) {
						if (position >= 2) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'z') {
						state = 30;
						position--;
					} else {
						// rule aç
						if (Exception.isException(in, Exception.NOUN_AC) == false) {
							if (position >= 2) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 30:
				if (in.charAt(position) == 'i') {
					state = 31;
					position--;
				} else {
					state = 28;
					position += 1;
					accept = true;
				}
				break;
			case 31:
				if (accept) {
					// rule izaç
					if (Exception.isException(in, Exception.NOUN_IZAC) == false) {
						if (position >= 4) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'l') {
						state = 32;
						position--;
					} else {
						// rule izaç
						if (Exception.isException(in, Exception.NOUN_IZAC) == false) {
							if (position >= 4) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 32:
				if (in.charAt(position) == 'a') {
					// rule alizaç
					if (position >= 4) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 31;
					position += 1;
					accept = true;
				}
				break;
			case 34:
				switch (in.charAt(position)) {
				case 'd':
					state = 35;
					position--;
					break;
				case 't':
					state = 36;
					position--;
					break;
				case 'c':
					state = 37;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 35:
				if (in.charAt(position) == 'a') {
					state = 39;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 36:
				if (in.charAt(position) == 'n') {
					state = 38;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 37:
				if (in.charAt(position) == 'i') {
					state = 64;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 38:
				switch (in.charAt(position)) {
				case 'a':
					// rule ante
					if (Exception.isException(in, Exception.NOUN_ANTE) == false) {
						if (position >= 1) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'e':
					// rule ente
					if (Exception.isException(in, Exception.NOUN_ENTE) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				}
				position = STOP;
				break;
			case 39:
				if (in.charAt(position) == 'd') {
					state = 42;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 42:
				if (in.charAt(position) == 'i') {
					state = 43;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 43:
				if (accept) {
					// rule idade
					if (Exception.isException(in, Exception.NOUN_IDADE) == false) {
						if (position >= 4) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'v':
						state = 44;
						position--;
						break;
					case 'l':
						state = 46;
						position--;
						break;
					default:
						// rule idade
						if (Exception.isException(in, Exception.NOUN_IDADE) == false) {
							if (position >= 4) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
						break;
					}
				}
				break;
			case 44:
				if (in.charAt(position) == 'i') {
					// rule ividade
					if (position >= 4) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 43;
					position += 1;
					accept = true;
				}
				break;
			case 46:
				if (in.charAt(position) == 'i') {
					state = 47;
					position--;
				} else {
					state = 43;
					position += 1;
					accept = true;
				}
				break;
			case 47:
				if (in.charAt(position) == 'b') {
					state = 48;
					position--;
				} else {
					state = 43;
					position += 2;
					accept = true;
				}
				break;
			case 48:
				if (in.charAt(position) == 'a') {
					// rule abilidade
					if (position >= 4) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 43;
					position += 3;
					accept = true;
				}
				break;
			case 50:
				switch (in.charAt(position)) {
				case 'a':
					state = 51;
					position--;
					break;
				case 'e':
					state = 54;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 51:
				if (accept) {
					// rule al
					if (Exception.isException(in, Exception.NOUN_AL) == false) {
						if (position >= 3) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'u':
						// rule ual
						if (Exception.isException(in, Exception.NOUN_UAL) == false) {
							if (position >= 2) {
								in = in.substring(0, position);
								suffixRemoved = true;
							}
						}
						position = STOP;
						break;
					case 'i':
						state = 52;
						position--;
						break;
					case 'n':
						state = 57;
						position--;
						break;
					default:
						// rule al
						if (Exception.isException(in, Exception.NOUN_AL) == false) {
							if (position >= 3) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
						break;
					}
				}
				break;
			case 52:
				if (accept) {
					// rule ial
					if (position >= 2) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'c') {
						state = 59;
						position--;
					} else {
						// rule ial
						if (position >= 2) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
					}
				}
				break;
			case 54:
				if (in.charAt(position) == 'v') {
					state = 55;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 55:
				switch (in.charAt(position)) {
				case 'á':
					// rule ável
					if (Exception.isException(in, Exception.NOUN_AVEL) == false) {
						if (position >= 1) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'í':
					// rule ível
					if (Exception.isException(in, Exception.NOUN_IVEL) == false) {
						if (position >= 4) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				}
				position = STOP;
				break;
			case 57:
				switch (in.charAt(position)) {
				case 'i':
					// rule inal
					if (Exception.isException(in, Exception.NOUN_INAL) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				case 'o':
					state = 62;
					position--;
					break;
				default:
					state = 51;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 59:
				if (in.charAt(position) == 'n') {
					state = 60;
					position--;
				} else {
					state = 52;
					position += 1;
					accept = true;
				}
				break;
			case 60:
				if (in.charAt(position) == 'e') {
					// rule encial
					if (position >= 4) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 52;
					position += 2;
					accept = true;
				}
				break;
			case 62:
				if (in.charAt(position) == 'i') {
					// rule ional
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 51;
					position += 2;
					accept = true;
				}
				break;
			case 64:
				if (accept) {
					// rule ice
					if (Exception.isException(in, Exception.NOUN_ICE) == false) {
						if (position >= 3) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'u') {
						state = 65;
						position--;
					} else {
						// rule ice
						if (Exception.isException(in, Exception.NOUN_ICE) == false) {
							if (position >= 3) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 65:
				if (in.charAt(position) == 'q') {
					// rule quice
					if (position >= 3) {
						in = in.substring(0, position) + "c";
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 64;
					position += 1;
					accept = true;
				}
				break;
			case 67:
				if (in.charAt(position) == 'e') {
					state = 68;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 68:
				if (in.charAt(position) == 'g') {
					state = 69;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 69:
				if (in.charAt(position) == 'a') {
					// rule agem
					if (Exception.isException(in, Exception.NOUN_AGEM) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
				}
				position = STOP;
				break;
			case 71:
				switch (in.charAt(position)) {
				case 'n':
					state = 119;
					position--;
					break;
				case 't':
					state = 72;
					position--;
					break;
				case 'd':
					state = 73;
					position--;
					break;
				case 'v':
					state = 74;
					position--;
					break;
				case 'r':
					state = 75;
					position--;
					break;
				case 's':
					state = 76;
					position--;
					break;
				case 'm':
					state = 77;
					position--;
					break;
				case 'i':
					state = 78;
					position--;
					break;
				case 'c':
					state = 79;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 72:
				if (in.charAt(position) == 'n') {
					state = 80;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 73:
				switch (in.charAt(position)) {
				case 'a':
					state = 81;
					position--;
					break;
				case 'i':
					// rule ido
					if (Exception.isException(in, Exception.NOUN_IDO) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 74:
				if (in.charAt(position) == 'i') {
					state = 82;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 75:
				switch (in.charAt(position)) {
				case 'u':
					state = 84;
					position--;
					break;
				case 'i':
					state = 85;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 76:
				if (in.charAt(position) == 'o') {
					// rule oso
					if (Exception.isException(in, Exception.NOUN_OSO) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
				}
				position = STOP;
				break;
			case 77:
				if (in.charAt(position) == 's') {
					state = 105;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 78:
				if (in.charAt(position) == 'r') {
					state = 107;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 79:
				switch (in.charAt(position)) {
				case 'i':
					state = 110;
					position--;
					break;
				case 's':
					state = 115;
					position--;
					break;
				case 'a':
					state = 117;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 80:
				if (in.charAt(position) == 'e') {
					state = 88;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 81:
				if (accept) {
					// rule ado
					if (Exception.isException(in, Exception.NOUN_ADO) == false) {
						if (position >= 1) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'z') {
						state = 93;
						position--;
					} else {
						// rule ado
						if (Exception.isException(in, Exception.NOUN_ADO) == false) {
							if (position >= 1) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 82:
				if (in.charAt(position) == 't') {
					state = 98;
					position--;
				} else {
					// rule ivo
					if (Exception.isException(in, Exception.NOUN_IVO) == false) {
						if (position >= 3) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				}
				break;
			case 84:
				if (in.charAt(position) == 'o') {
					state = 100;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 85:
				if (in.charAt(position) == 'e') {
					state = 86;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 86:
				if (accept) {
					// rule eiro
					if (Exception.isException(in, Exception.NOUN_EIRO) == false) {
						if (position >= 2) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'u') {
						state = 103;
						position--;
					} else {
						// rule eiro
						if (Exception.isException(in, Exception.NOUN_EIRO) == false) {
							if (position >= 2) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 88:
				if (in.charAt(position) == 'm') {
					state = 89;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 89:
				switch (in.charAt(position)) {
				case 'a':
					state = 90;
					position--;
					break;
				case 'i':
					// rule imento
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 90:
				if (in.charAt(position) == 'i') {
					// rule iamento
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				} else {
					// rule amento
					if (Exception.isException(in, Exception.NOUN_AMENTO) == false) {
						if (position >= 2) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
				}
				position = STOP;
				break;
			case 93:
				if (in.charAt(position) == 'i') {
					state = 94;
					position--;
				} else {
					state = 81;
					position += 1;
					accept = true;
				}
				break;
			case 94:
				if (accept) {
					// rule izado
					if (Exception.isException(in, Exception.NOUN_IZADO) == false) {
						if (position >= 4) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'l':
						state = 95;
						position--;
						break;
					case 't':
						state = 96;
						position--;
						break;
					default:
						// rule izado
						if (Exception.isException(in, Exception.NOUN_IZADO) == false) {
							if (position >= 4) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
						break;
					}
				}
				break;
			case 95:
				if (in.charAt(position) == 'a') {
					// rule alizado
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 94;
					position += 1;
					accept = true;
				}
				break;
			case 96:
				if (in.charAt(position) == 'a') {
					// rule atizado
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 94;
					position += 1;
					accept = true;
				}
				break;
			case 98:
				if (in.charAt(position) == 'a') {
					// rule ativo
					if (Exception.isException(in, Exception.NOUN_ATIVO) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
				} else {
					// rule tivo
					if (Exception.isException(in, Exception.NOUN_TIVO) == false) {
						if (position >= 3) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
				}
				position = STOP;
				break;
			case 100:
				if (in.charAt(position) == 'd') {
					state = 101;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 101:
				if (in.charAt(position) == 'e') {
					// rule edouro
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 103:
				if (in.charAt(position) == 'q') {
					// rule queiro
					if (position >= 2) {
						in = in.substring(0, position) + "c";
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 86;
					position += 1;
					accept = true;
				}
				break;
			case 105:
				if (in.charAt(position) == 'i') {
					// rule ismo
					if (Exception.isException(in, Exception.NOUN_ISMO) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
				}
				position = STOP;
				break;
			case 107:
				switch (in.charAt(position)) {
				case 'á':
					// rule ário
					if (Exception.isException(in, Exception.NOUN_ARIO) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'é':
					// rule ério
					if (position >= 5) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				}
				position = STOP;
				break;
			case 110:
				if (accept) {
					// rule ico
					if (Exception.isException(in, Exception.NOUN_ICO) == false) {
						if (position >= 3) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 't') {
						state = 111;
						position--;
					} else {
						// rule ico
						if (Exception.isException(in, Exception.NOUN_ICO) == false) {
							if (position >= 3) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 111:
				switch (in.charAt(position)) {
				case 'á':
					// rule ático
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 's':
					state = 112;
					position--;
					break;
				default:
					state = 110;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 112:
				if (in.charAt(position) == 'á') {
					// rule ástico
					if (Exception.isException(in, Exception.NOUN_ASTICO) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					state = 110;
					position += 2;
					accept = true;
				}
				break;
			case 115:
				if (in.charAt(position) == 'e') {
					// rule esco
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 117:
				if (in.charAt(position) == 'í') {
					// rule íaco
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 119:
				if (in.charAt(position) == 'a') {
					// rule ano
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 121:
				if (in.charAt(position) == 'o') {
					state = 122;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 122:
				if (accept) {
					// rule or
					if (Exception.isException(in, Exception.NOUN_OR) == false) {
						if (position >= 1) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'd') {
						state = 123;
						position--;
					} else {
						// rule or
						if (Exception.isException(in, Exception.NOUN_OR) == false) {
							if (position >= 1) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 123:
				switch (in.charAt(position)) {
				case 'a':
					// rule ador
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					// rule edor
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'i':
					// rule idor
					if (Exception.isException(in, Exception.NOUN_IDOR) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				default:
					state = 122;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 126:
				if (in.charAt(position) == 'u') {
					// rule ura
					if (Exception.isException(in, Exception.NOUN_URA) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
				}
				position = STOP;
				break;
			case 128:
				if (in.charAt(position) == 'ê') {
					// rule ês
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 130:
				if (in.charAt(position) == 'e') {
					// rule ez
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
				}
				position = STOP;
				break;
			case 134:
				if (in.charAt(position) == 'i') {
					state = 24;
					position--;
				} else {
					state = 10;
					position += 2;
					accept = true;
				}
				break;
			}
		}
		return in;
	}

	public String verbReduction(String in) {

		int position = in.length() - 1;
		int state = 0;
		boolean accept = false;

		while (position >= 0) {
			switch (state) {
			case 0:
				switch (in.charAt(position)) {
				case 'a':
					state = 4;
					position--;
					break;
				case 'e':
					state = 5;
					position--;
					break;
				case 'i':
					state = 2;
					position--;
					break;
				case 'o':
					state = 1;
					position--;
					break;
				case 'u':
					state = 8;
					position--;
					break;
				case 'r':
					state = 7;
					position--;
					break;
				case 'á':
					state = 6;
					position--;
					break;
				case 'm':
					state = 3;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 1:
				switch (in.charAt(position)) {
				case 'm':
					state = 9;
					position--;
					break;
				case 'd':
					state = 32;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 2:
				switch (in.charAt(position)) {
				case 'a':
					// rule ai
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					state = 44;
					position--;
					break;
				default:
					// rule i
					if (position >= 2) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				}
				break;
			case 3:
				switch (in.charAt(position)) {
				case 'a':
					state = 66;
					position--;
					break;
				case 'e':
					state = 70;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 4:
				switch (in.charAt(position)) {
				case 'i':
					state = 89;
					position--;
					break;
				case 'v':
					state = 96;
					position--;
					break;
				case 'r':
					state = 94;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 5:
				switch (in.charAt(position)) {
				case 'r':
					state = 116;
					position--;
					break;
				case 'd':
					state = 100;
					position--;
					break;
				case 's':
					state = 103;
					position--;
					break;
				case 't':
					state = 106;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 6:
				if (in.charAt(position) == 'r') {
					state = 120;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 7:
				switch (in.charAt(position)) {
				case 'a':
					state = 124;
					position--;
					break;
				case 'e':
					// rule er
					if (Exception.isException(in, Exception.VERB_ER) == false) {
						if (position >= 1) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				case 'i':
					// rule ir
					if (Exception.isException(in, Exception.VERB_IR) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 8:
				switch (in.charAt(position)) {
				case 'o':
					// rule ou
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'e':
					// rule eu
					if (Exception.isException(in, Exception.VERB_EU) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'i':
					// rule iu
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				}
				position = STOP;
				break;
			case 9:
				switch (in.charAt(position)) {
				case 'a':
					state = 10;
					position--;
					break;
				case 'e':
					state = 14;
					position--;
					break;
				case 'r':
					state = 38;
					position--;
					break;
				case 'o':
					// rule omo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'i':
					// rule imo
					if (Exception.isException(in, Exception.VERB_IMO) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 10:
				if (accept) {
					// rule amo
					if (position >= 1) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'í':
						state = 11;
						position--;
						break;
					case 'v':
						state = 26;
						position--;
						break;
					case 'r':
						state = 22;
						position--;
						break;
					default:
						// rule amo
						if (position >= 1) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
						break;
					}
				}
				break;
			case 11:
				if (accept) {
					// rule íamo
					if (position >= 2) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'r') {
						state = 12;
						position--;
					} else {
						// rule íamo
						if (position >= 2) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
					}
				}
				break;
			case 12:
				switch (in.charAt(position)) {
				case 'i':
					// rule iríamo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					// rule eríamo
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'a':
					// rule aríamo
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 11;
					position += 1;
					accept = true;
				}
				break;
			case 14:
				if (accept) {
					// rule emo
					if (position >= 1) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 's':
						state = 15;
						position--;
						break;
					case 'r':
						state = 24;
						position--;
						break;
					default:
						// rule emo
						if (position >= 1) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
						break;
					}
				}
				break;
			case 15:
				if (in.charAt(position) == 's') {
					state = 16;
					position--;
				} else {
					state = 14;
					position += 1;
					accept = true;
				}
				break;
			case 16:
				switch (in.charAt(position)) {
				case 'ê':
					// rule êssemo
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'í':
					// rule íssemo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'á':
					// rule ássemo
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 14;
					position += 2;
					accept = true;
					break;
				}
				break;
			case 22:
				switch (in.charAt(position)) {
				case 'í':
					// rule íramo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'ê':
					// rule êramo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'á':
					// rule áramo
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 10;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 24:
				switch (in.charAt(position)) {
				case 'a':
					// rule aremo
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					// rule eremo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'i':
					// rule iremo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 14;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 26:
				if (in.charAt(position) == 'á') {
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 10;
					position += 1;
					accept = true;
				}
				break;
			case 32:
				if (in.charAt(position) == 'n') {
					state = 33;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 33:
				switch (in.charAt(position)) {
				case 'a':
					// rule ando
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'e':
					// rule endo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'i':
					// rule indo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'o':
					// rule ondo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				}
				position = STOP;
				break;
			case 38:
				switch (in.charAt(position)) {
				case 'a':
					// rule armo
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'e':
					// rule ermo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'i':
					// rule irmo
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				}
				position = STOP;
				break;
			case 44:
				if (accept) {
					// rule ei
					if (position >= 2) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'r':
						state = 45;
						position--;
						break;
					case 's':
						state = 51;
						position--;
						break;
					case 'v':
						state = 59;
						position--;
						break;
					case 'u':
						// rule uei
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
						position = STOP;
						break;
					case 'í':
						state = 47;
						position--;
						break;
					default:
						// rule ei
						if (position >= 2) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
						break;
					}
				}
				break;
			case 45:
				switch (in.charAt(position)) {
				case 'ê':
					// rule êrei
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'a':
					// rule arei
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					// rule erei
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'á':
					// rule árei
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'i':
					// rule irei
					if (Exception.isException(in, Exception.VERB_IREI) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				default:
					state = 44;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 47:
				if (accept) {
					if (position >= 2) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'r') {
						state = 49;
						position--;
					} else {
						if (position >= 2) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
					}
				}
				break;
			case 49:
				switch (in.charAt(position)) {
				case 'i':
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'a':
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 47;
					position += 1;
					accept = true;
					break;
				}

				break;
			case 51:
				if (in.charAt(position) == 's') {
					state = 52;
					position--;
				} else {
					state = 44;
					position += 1;
					accept = true;
				}
				break;
			case 52:
				switch (in.charAt(position)) {
				case 'á':
					// rule ássei
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'ê':
					// rule êssei
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'í':
					// rule íssei
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 44;
					position += 2;
					accept = true;
					break;
				}
				break;
			case 59:
				if (in.charAt(position) == 'á') {
					// rule ávei
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					state = 44;
					position += 1;
					accept = true;
				}
				break;
			case 66:
				if (accept) {
					// rule am
					if (position >= 1) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 'i':
						state = 67;
						position--;
						break;
					case 'r':
						state = 78;
						position--;
						break;
					case 'v':
						state = 82;
						position--;
						break;
					default:
						// rule am
						if (position >= 1) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
						position = STOP;
						break;
					}
				}
				break;
			case 67:
				// rule iam
				if (accept) {
					if (position >= 2) {
						in = in.substring(0, position + 1);
						suffixRemoved = true;
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'r') {
						state = 68;
						position--;
					} else {
						// rule iam
						if (Exception.isException(in, Exception.VERB_IAM) == false) {
							if (position >= 2) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 68:
				switch (in.charAt(position)) {
				case 'i':
					// rule iriam
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					// rule eriam
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'a':
					// rule ariam
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 67;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 70:
				if (accept) {
					// rule em
					if (Exception.isException(in, Exception.VERB_EM) == false) {
						if (position >= 1) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					switch (in.charAt(position)) {
					case 's':
						state = 71;
						position--;
						break;
					case 'r':
						state = 80;
						position--;
						break;
					default:
						// rule em
						if (Exception.isException(in, Exception.VERB_EM) == false) {
							if (position >= 1) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
						break;
					}
				}
				break;
			case 71:
				if (in.charAt(position) == 's') {
					state = 72;
					position--;
				} else {
					state = 70;
					position += 1;
					accept = true;
				}
				break;
			case 72:
				switch (in.charAt(position)) {
				case 'e':
					// rule essem
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'a':
					// rule assem
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'i':
					// rule issem
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 70;
					position += 2;
					accept = true;
					break;
				}
				break;
			case 78:
				switch (in.charAt(position)) {
				case 'i':
					// rule iram
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'a':
					// rule aram
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					// rule eram
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'í':
					// rule íram
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 66;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 80:
				switch (in.charAt(position)) {
				case 'a':
					// rule arem
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'e':
					// rule erem
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'i':
					// rule irem
					if (Exception.isException(in, Exception.VERB_IREM) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
					break;
				default:
					state = 70;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 82:
				if (in.charAt(position) == 'a') {
					// rule avam
					if (Exception.isException(in, Exception.VERB_AVAM) == false) {
						if (position >= 1) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					state = 66;
					position += 1;
					accept = true;
				}
				break;
			case 89:
				if (accept) {
					// rule ia
					if (Exception.isException(in, Exception.VERB_IA) == false) {
						if (position >= 2) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
					position = STOP;
				} else {
					if (in.charAt(position) == 'r') {
						state = 90;
						position--;
					} else {
						// rule ia
						if (Exception.isException(in, Exception.VERB_IA) == false) {
							if (position >= 2) {
								in = in.substring(0, position + 1);
								suffixRemoved = true;
							}
						}
						position = STOP;
					}
				}
				break;
			case 90:
				switch (in.charAt(position)) {
				case 'e':
					// rule eria
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'a':
					// rule aria
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				case 'i':
					// rule iria
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					position = STOP;
					break;
				default:
					state = 89;
					position += 1;
					accept = true;
					break;
				}
				break;
			case 94:
				switch (in.charAt(position)) {
				case 'i':
					// rule ira
					if (Exception.isException(in, Exception.VERB_IRA) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'a':
					// rule ara
					if (Exception.isException(in, Exception.VERB_ARA) == false) {
						if (position >= 1) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'e':
					// rule era
					if (Exception.isException(in, Exception.VERB_ERA) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				}
				position = STOP;
				break;
			case 96:
				if (in.charAt(position) == 'a') {
					// rule ava
					if (Exception.isException(in, Exception.VERB_AVA) == false) {
						if (position >= 1) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
				}
				position = STOP;
				break;
			case 100:
				if (in.charAt(position) == 'r') {
					state = 101;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 101:
				switch (in.charAt(position)) {
				case 'i':
					// rule irde
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'e':
					// rule erde
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'a':
					// rule arde
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				}
				position = STOP;
				break;
			case 103:
				if (in.charAt(position) == 's') {
					state = 104;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 104:
				switch (in.charAt(position)) {
				case 'e':
					// rule esse
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'i':
					// rule isse
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'a':
					// rule asse
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				}
				position = STOP;
				break;
			case 106:
				if (in.charAt(position) == 's') {
					state = 107;
					position--;
				} else {
					position = STOP;
				}
				break;
			case 107:
				switch (in.charAt(position)) {
				case 'e':
					// rule este
					if (Exception.isException(in, Exception.VERB_ESTE) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'a':
					// rule aste
					if (position >= 1) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'i':
					// rule iste
					if (position >= 3) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				}
				position = STOP;
				break;
			case 116:
				switch (in.charAt(position)) {
				case 'i':
					// rule ire
					if (Exception.isException(in, Exception.VERB_IRE) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'a':
					// rule are
					if (Exception.isException(in, Exception.VERB_ARE) == false) {
						if (position >= 1) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'e':
					// rule ere
					if (Exception.isException(in, Exception.VERB_ERE) == false) {
						if (position >= 2) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				}
				position = STOP;
				break;
			case 120:
				switch (in.charAt(position)) {
				case 'i':
					// rule irá
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				case 'a':
					// rule ará
					if (Exception.isException(in, Exception.VERB_ARAA) == false) {
						if (position >= 1) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
					break;
				case 'e':
					// rule erá
					if (position >= 2) {
						in = in.substring(0, position);
						suffixRemoved = true;
					}
					break;
				}
				position = STOP;
				break;
			case 124:
				if (in.charAt(position) == 'e') {
					// rule ear
					if (Exception.isException(in, Exception.VERB_EAR) == false) {
						if (position >= 3) {
							in = in.substring(0, position);
							suffixRemoved = true;
						}
					}
				} else {
					// rule ar
					if (Exception.isException(in, Exception.VERB_AR) == false) {
						if (position >= 1) {
							in = in.substring(0, position + 1);
							suffixRemoved = true;
						}
					}
				}
				position = STOP;
				break;
			}
		}
		return in;
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

	public void test() throws IOException {

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