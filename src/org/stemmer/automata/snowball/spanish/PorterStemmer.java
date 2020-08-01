package org.stemmer.automata.snowball.spanish;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PorterStemmer extends Stemmer {

	private final int STOP = -1;
	private static final long serialVersionUID = 1L;
	String r1_txt;
	String r2_txt;
	String rv_txt;
	int r1, r2, rv;
	
	public String stem(String str) {

		int len = str.length();
		
		this.r1 = this.r2 = this.rv = len;

		// check for zero length
		if (len > 3) {
			// all characters must be letters
			char[] c = str.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (!Character.isLetter(c[i])) {
					return str.toLowerCase();
				}
			}
		} else {
			return str.toLowerCase();
		}

		str = str.toLowerCase();

		// R1 is the region after the first non-vowel following a vowel, or is
				// the null region at the end of the word if there is no such non-vowel.
				for (int i = 0; i < (len - 1) && r1 == len; i++) {
					if (is_vowel(str.charAt(i)) && !is_vowel(str.charAt(i + 1))) {
						this.r1 = i + 2;
					}
				}

				// R2 is the region after the first non-vowel following a vowel in R1,
				// or is the null region at the end of the word if there is no such
				// non-vowel.
				for (int i = this.r1; i < (len - 1) && this.r2 == len; i++) {
					if (is_vowel(str.charAt(i)) && !is_vowel(str.charAt(i + 1))) {
						this.r2 = i + 2;
					}
				}

				if (len > 3) {
					if (!is_vowel(str.charAt(1))) {
						// If the second letter is a consonant, RV is the region after
						// the next following vowel
						this.rv = getNextVowelPos(str, 2) + 1;
					} else if (is_vowel(str.charAt(0)) && is_vowel(str.charAt(1))) {
						// or if the first two letters are vowels, RV is the region
						// after the next consonant
						this.rv = getNextConsonantPos(str, 2) + 1;
					} else {
						// otherwise (consonant-vowel case) RV is the region after the
						// third letter. But RV is the end of the word if these
						// positions cannot be found.
						this.rv = 3;
					}
				}
				
		this.r1_txt = substr(str, this.r1);
		this.r2_txt = substr(str, this.r2);
		this.rv_txt = substr(str, this.rv);

		str = step0(str);

		String aux = str;
		this.r1_txt = substr(str, this.r1);
		this.r2_txt = substr(str, this.r2);
		this.rv_txt = substr(str, this.rv);
		
		str = step1(str);
		if (aux.equals(str)) {
			str = step2a(str);
			aux = str;
			if (aux.equals(str)) {
				str = step2b(str);
			}

		}
		
		this.r1_txt = substr(str, this.r1);
		this.r2_txt = substr(str, this.r2);
		this.rv_txt = substr(str, this.rv);
		return step3(str);

	} // end stem

	protected String step0 (String str) {
		int saved_position = 0;
        int position = str.length() - 1;
        boolean accept = false;
        int state = 0;

        while (position >= 0) {
            switch (state) {
            case 0:
                switch (str.charAt(position)) {
                case 'e':
                    state = 5;
                    position--;
                    break;
                case 's':
                    state = 7;
                    position--;
                    break;
                case 'a':
                case 'o':
                    state = 1;
                    position--;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            case 1:
                switch (str.charAt(position)) {
                case 'l':
                    state = 2;
                    position--;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            case 2:
                switch (str.charAt(position)) {
                case 'e':
                    state = 3;
                    position--;
                    break;

                default:
                    saved_position = position;
                    position = STOP;
                    accept = true;
                    break;
                }
                break;

            case 3:
                switch (str.charAt(position)) {
                case 's':
                    state = 4;
                    position--;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            case 4:
                saved_position = position;
                accept = true;
                position = STOP;
                break;

            case 5:
                switch (str.charAt(position)) {
                case 's':
                    state = 4;
                    position--;
                    break;

                case 'l':
                    state = 6;
                    position--;
                    break;

                case 'm':
                    state = 9;
                    position--;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            case 6:
                saved_position = position;
                accept = true;
                position = STOP;
                break;

            case 7:
                switch (str.charAt(position)) {
                case 'a':
                    state = 1;
                    position--;
                    break;

                case 'e':
                    state = 11;
                    position--;
                    break;

                case 'o':
                    state = 8;
                    position--;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            case 8:
                switch (str.charAt(position)) {
                case 'l':
                    state = 2;
                    position--;
                    break;

                case 'n':
                    state = 10;
                    position--;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            case 9:
                saved_position = position;
                accept = true;
                position = STOP;
                break;

            case 10:
                saved_position = position;
                accept = true;
                position = STOP;
                break;

            case 11:
                switch (str.charAt(position)) {
                case 'l':
                    state = 2;
                    position--;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            default:
                position = STOP;
                break;
            }
        }

        // CASE 0.1
        // 1- Delete suffix in case 0.0 if in rv;
        // 2- Delete suffix in 0.0 and remove accent if in rv;
        // 3- Delete suffix in 0.0 if yendo follows u if in rv
        int action = 0;
        state = 0;
        position = this.rv_txt.length() - (str.length() - saved_position);
        while (position >= 0 && accept) {
            switch (state) {
            case 0:
                switch (this.rv_txt.charAt(position)) {
                case 'r':
                    state = 1;
                    position--;
                    break;

                case 'o':
                    state = 6;
                    position--;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            case 1:
                switch (this.rv_txt.charAt(position)) {
                case 'a':
                case 'e':
                case 'i':
                    state = 2;
                    position = STOP;
                    accept = true;
                    action = 1;
                    break;

                case '�':
                    state = 3;
                    position = STOP;
                    accept = true;
                    action = 2;
                    break;

                case '�':
                    state = 4;
                    position = STOP;
                    accept = true;
                    action = 2;
                    break;

                case '�':
                    state = 5;
                    position = STOP;
                    accept = true;
                    action = 2;
                    break;

                default:
                    position = STOP;
                    break;
                }
                break;

            // case 2 removed
            // case 3 removed
            // case 4 removed
            // case 5 removed

            case 6:
                switch (this.rv_txt.charAt(position)) {
                case 'd':
                    state = 7;
                    position--;
                    break;
                default:
                    position = STOP;
                    break;
                }
                break;

            case 7:
                switch (this.rv_txt.charAt(position)) {
                case 'n':
                    state = 8;
                    position--;
                    break;
                default:
                    position = STOP;
                    break;
                }
                break;

            case 8:
                switch (this.rv_txt.charAt(position)) {
                case 'a':
                    position = STOP;
                    accept = true;
                    action = 1;
                    break;
                case '�':
                    position = STOP;
                    accept = true;
                    action = 2;
                    break;
                case 'e':
                    state = 11;
                    position--;
                    break;
                case '�':
                    state = 12;
                    position--;
                    break;
                default:
                    position = STOP;
                    break;
                }
                break;

            // case 9 removed
            // case 10 removed
            case 11:
                switch (this.rv_txt.charAt(position)) {
                case 'i':
                case 'y':
                    state = 9;
                    position = STOP;
                    accept = true;
                    action = 1;
                    break;
                default:
                    position = STOP;
                    break;
                }
                break;

            case 12:
                switch (this.rv_txt.charAt(position)) {
                case 'i':
                    state = 14;
                    position = STOP;
                    accept = true;
                    action = 2;
                    break;
                default:
                    position = STOP;
                    break;
                }
                break;
            // case 13 removed
            // case 14 removed
            default:
                accept = false;
                break;
            }
        }

        if (accept)
            switch (action) {
            case 1:
                return substr(str, 0, (saved_position + 1));
            case 2:
                return removeAccent(substr(str, 0, saved_position + 1));
            case 3:
                if (substr(str, saved_position - 6, 1).equals("u"))
                    return substr(str, 0, saved_position);
                break;
            default:
                break;
            }

        return str;
    } // end step0

	protected String step1(String str) {

		int position = r2_txt.length() - 1;
		boolean accept = false;
		int state = 0;
		int saved_position = position;
		int special_case = -1;

		while (position >= 0 && !accept) {
			switch (state) {
			case 0:
				switch (r2_txt.charAt(position)) {
				case 'a':
					state = 1;
					position--;
					break;
				case 'e':
					state = 2;
					position--;
					break;
				case 'o':
					state = 3;
					position--;
					break;
				case 's':
					state = 4;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 1:
				switch (r2_txt.charAt(position)) {
				case 'c':
					state = 5;
					position--;
					break;

				case 'z':
					state = 7;
					position--;
					break;

				case 's':
					state = 12;
					position--;
					break;
				case 't':
					state = 20;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 2:
				switch (r2_txt.charAt(position)) {
				case 'l':
					state = 8;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 3:
				switch (r2_txt.charAt(position)) {
				case 'c':
					state = 5;
					position--;
					break;
				case 'm':
					state = 6;
					position--;
					break;
				case 's':
					state = 12;
					position--;
					break;
				case 't':
					state = 14;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 4:
				switch (r2_txt.charAt(position)) {
				case 'a':
					state = 1;
					position--;
					break;
				case 'e':
					state = 2;
					position--;
					break;
				case 'o':
					state = 3;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 5:
				switch (r2_txt.charAt(position)) {
				case 'i':
					state = 10;
					accept = true;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 6:
				switch (r2_txt.charAt(position)) {
				case 's':
					state = 5;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 7:
				switch (r2_txt.charAt(position)) {
				case 'n':
					state = 9;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 8:
				switch (r2_txt.charAt(position)) {
				case 'b':
					state = 18;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 9:
				switch (r2_txt.charAt(position)) {
				case 'a':
					state = 11;
					accept = true;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			// case 10:

			// case 11:

			case 12:
				switch (r2_txt.charAt(position)) {
				case 'o':
					state = 13;
					accept = true;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			// case 13:

			case 14:
				switch (r2_txt.charAt(position)) {
				case 'n':
					state = 15;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 15:
				switch (r2_txt.charAt(position)) {
				case 'e':
					state = 16;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 16:
				switch (r2_txt.charAt(position)) {
				case 'i':
					state = 17;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 17:
				switch (r2_txt.charAt(position)) {
				case 'm':
					state = 18;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 18:
				switch (r2_txt.charAt(position)) {
				case 'a':
				case 'i':
					state = 19;
					accept = true;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			// case 19:

			case 20:
				switch (r2_txt.charAt(position)) {
				case 's':
					state = 5;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			default:
				position = STOP;

				break;
			}
		}

		saved_position = saved_position - position;
		if (accept)
			str = substr(str, 0, str.length() - saved_position);

		// case 1.1
		else {

			position = r2_txt.length() - 1;
			saved_position = position;
			state = 0;
			while (position >= 0) {
				switch (state) {
				case 0:
					switch (r2_txt.charAt(position)) {
					case 'n':
						state = 1;
						position--;
						break;
					case 'a':
						state = 4;
						position--;
						break;
					case 's':
						state = 8;
						position--;
						break;
					case 'r':
						state = 11;
						position--;
						break;
					case 'e':
						state = 12;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 1:
					switch (r2_txt.charAt(position)) {
					case '�':
						state = 2;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 2:
					switch (r2_txt.charAt(position)) {
					case 'i':
						state = 3;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 3:
					switch (r2_txt.charAt(position)) {
					case 'c':
						state = 7;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 4:
					switch (r2_txt.charAt(position)) {
					case 'r':
						state = 5;
						position--;
						break;
					case 'i':
						state = 14;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;
				case 5:
					switch (r2_txt.charAt(position)) {
					case 'o':
						state = 6;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 6:
					switch (r2_txt.charAt(position)) {
					case 'd':
						state = 7;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 7:
					switch (r2_txt.charAt(position)) {
					case 'a':
						state = 16;
						position--;
						accept = true;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 8:
					switch (r2_txt.charAt(position)) {
					case 'a':
						state = 4;
						position--;
						break;
					case 'e':
						state = 9;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 9:
					switch (r2_txt.charAt(position)) {
					case 'n':
						state = 10;
						position--;
						break;
					case 't':
						state = 13;
						position--;
						break;
					case 'r':
						state = 5;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 10:
					switch (r2_txt.charAt(position)) {
					case 'o':
						state = 2;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 11:
					switch (r2_txt.charAt(position)) {
					case 'o':
						state = 6;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 12:
					switch (r2_txt.charAt(position)) {
					case 't':
						state = 13;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 13:
					switch (r2_txt.charAt(position)) {
					case 'n':
						state = 7;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 14:
					switch (r2_txt.charAt(position)) {
					case 'c':
						state = 15;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 15:
					switch (r2_txt.charAt(position)) {
					case 'n':
						state = 7;
						position--;
						break;
					default:
						position = STOP;
						break;
					}
					break;

				case 16:
					switch (r2_txt.charAt(position)) {
					case 'c':
						state = 17;
						position--;
						break;
					default:
						saved_position = saved_position - position;
						position = STOP;
						accept = true;
						break;
					}
					break;

				case 17:
					switch (r2_txt.charAt(position)) {
					case 'i':
						state = 18;
						accept = true;
						saved_position = saved_position - position + 1;
						position = STOP;
						break;
					default:
						saved_position--;
						accept = true;
						position = STOP;
						break;
					}
					break;

				default:
					position = STOP;
					break;
				}
			}

			if (accept)
				str = substr(str, 0, str.length() - saved_position);
			// case 1.2
			else {
				position = r2_txt.length() - 1;
				saved_position = position;
				state = 0;
				while (position >= 0) {
					special_case = 0;
					switch (state) {
					case 0:
						switch (r2_txt.charAt(position)) {
						case 'a':
							state = 1;
							position--;
							break;

						case 'd':
							state = 26;
							position--;
							break;

						case 's':
							state = 17;
							position--;
							break;

						case 'o':
							state = 30;
							position--;
							break;

						case 'n':
							state = 12;
							position--;
							break;

						case 'e':
							state = 20;
							position--;
							break;

						default:
							position = STOP;
							break;
						}
						break;

					case 1:

						switch (r2_txt.charAt(position)) {
						case '�':
							state = 8;
							position--;
							break;
						case 'i':
							state = 2;
							position--;
							break;
						case 'v':
							state = 6;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 2:
						switch (r2_txt.charAt(position)) {
						case 'c':
							state = 3;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 3:
						switch (r2_txt.charAt(position)) {
						case 'n':
							state = 4;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 4:
						switch (r2_txt.charAt(position)) {
						case 'e':
							state = 5;
							accept = true;
							saved_position = saved_position - position + 1;
							special_case = 2;
							position = STOP;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 6:
						switch (r2_txt.charAt(position)) {
						case 'i':
							state = 7;
							accept = true;
							special_case = 3;
							saved_position = saved_position - position + 1;
							position = STOP;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 8:
						switch (r2_txt.charAt(position)) {
						case 'g':
							state = 9;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 9:
						switch (r2_txt.charAt(position)) {
						case 'o':
							state = 10;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 10:
						switch (r2_txt.charAt(position)) {
						case 'l':
							state = 11;
							accept = true;
							saved_position = saved_position - position + 1;
							special_case = 8;
							position = STOP;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 12:
						switch (r2_txt.charAt(position)) {
						case '�':
							state = 13;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 13:
						switch (r2_txt.charAt(position)) {
						case 'i':
							state = 14;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 14:
						switch (r2_txt.charAt(position)) {
						case 'c':
							state = 15;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 15:
						switch (r2_txt.charAt(position)) {
						case 'u':
							state = 16;
							accept = true;
							saved_position = saved_position - position + 1;
							special_case = 7;
							position = STOP;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 17:
						switch (r2_txt.charAt(position)) {
						case 'a':
							state = 1;
							position--;
							break;
						case 'e':
							state = 18;
							position--;
							break;
						case 'o':
							state = 30;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 18:
						switch (r2_txt.charAt(position)) {
						case 'd':
							state = 26;
							position--;
							break;
						case 'n':
							state = 19;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 19:
						switch (r2_txt.charAt(position)) {
						case 'o':
							state = 13;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 20:
						switch (r2_txt.charAt(position)) {
						case 't':
							state = 21;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 21:
						switch (r2_txt.charAt(position)) {
						case 'n':
							state = 22;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 22:
						switch (r2_txt.charAt(position)) {
						case 'e':
							state = 23;
							position--;
							break;
						default:
							position = STOP;
							accept = false;
							break;
						}
						break;

					case 23:
						switch (r2_txt.charAt(position)) {
						case 'm':
							state = 24;
							position--;
							saved_position -= position;
							accept = true;
							break;
						default:
							saved_position -= position;
							special_case = 4;
							position = STOP;
							accept = true;
							break;
						}
						break;

					case 24:
						switch (r2_txt.charAt(position)) {
						case 'a':
							state = 25;
							accept = true;
							special_case = 5;
							saved_position += 1;
							position = STOP;
							break;
						default:
							special_case = 4;
							accept = true;
							position = STOP;
							break;
						}
						break;

					case 26:
						switch (r2_txt.charAt(position)) {
						case 'a':
							state = 27;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 27:
						switch (r2_txt.charAt(position)) {
						case 'd':
							state = 28;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 28:
						switch (r2_txt.charAt(position)) {
						case 'i':
							state = 29;
							accept = true;
							saved_position = saved_position - position + 1;
							special_case = 1;
							position = STOP;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 30:
						switch (r2_txt.charAt(position)) {
						case 'v':
							state = 31;
							position--;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					case 31:
						switch (r2_txt.charAt(position)) {
						case 'i':
							state = 32;
							accept = true;
							saved_position = saved_position - position + 1;
							special_case = 6;
							position = STOP;
							break;
						default:
							position = STOP;
							break;
						}
						break;

					default:
						position = STOP;
						break;
					}
				}

				if (accept) {
					String str_aux;
					int aux = r2_txt.length() - saved_position;

					switch (special_case) {
					case 1:
						// if preceded by abil, ic or iv, delete if in R2
						if (aux >= 2)
							if (r2_txt.charAt(aux - 1) == 'c'
									|| r2_txt.charAt(aux - 1) == 'v') {
								if (r2_txt.charAt(aux - 2) == 'i')
									saved_position += 2;
							} else if (aux >= 4)
								if (r2_txt.charAt(aux - 1) == 'l')
									if (r2_txt.charAt(aux - 2) == 'i')
										if (r2_txt.charAt(aux - 3) == 'b')
											if (r2_txt.charAt(aux - 4) == 'a')
												saved_position += 4;

						return substr(str, 0, str.length() - saved_position);
						

					case 2:
						// replace with ente if in R2
						return substr(str, 0, str.length() - saved_position)
								+ "ente";

					case 3:
						// if preceded by at, delete if in R2
						if (aux >= 2)
							if (r2_txt.charAt(aux - 1) == 't') {
								if (r2_txt.charAt(aux - 2) == 'a')
									saved_position += 2;
							}
						return substr(str, 0, str.length() - saved_position);

					case 4:
						// delete if in R2
						str_aux = substr(r2_txt, 0, r2_txt.length()
								- saved_position);
						if (r2_txt.endsWith("mente")) {
							str = substr(str, 0, str.length() - saved_position);
							// if preceded by ante, able or ible, delete if in
							// R2
							if (str_aux.endsWith("ante")
									|| str_aux.endsWith("able")
									|| str_aux.endsWith("ible"))
								str = substr(str, 0, str.length() - 4);
						}

						break;

					case 5:
						str_aux = substr(r2_txt, 0, r2_txt.length()
								- saved_position);
						// delete if in R1
						if (r1_txt.endsWith("amente"))
							str = substr(str, 0, str.length() - saved_position);

						if (str_aux.endsWith("iv")) {
							str = substr(str, 0, str.length() - 2);
							str_aux = substr(str_aux, 0, str_aux.length() - 2);
							if (str_aux.endsWith("at"))
								str = substr(str, 0, str.length() - 2);
						}

						if (str_aux.endsWith("os") || str_aux.endsWith("ic")
								|| str_aux.endsWith("ad"))
							str = substr(str, 0, str.length() - 2);

						break;

					case 6:
						// delete if in R2
						// if preceded by at, delete if in R2
						if (aux >= 2)
							if (r2_txt.charAt(aux - 1) == 't')
								if (r2_txt.charAt(aux - 2) == 'a')
									saved_position += 2;

						return substr(str, 0, str.length() - saved_position);

					case 7:
						str = substr(str, 0, str.length() - saved_position);
						str += "u";
						break;

					case 8:
						str = substr(str, 0, str.length() - saved_position);
						str += "log";
						break;

					default:
						return substr(str, 0, str.length() - saved_position);
					}

				} else {
					if (r1_txt.endsWith("amente"))
						return substr(str, 0, str.length() - 6);
				}
			}
		}

		return str;

	} // end step1

	protected String step2a(String str) {
		
		int position = this.rv_txt.length() - 1;
		int saved_position = position;
		boolean accept = false;
		int state = 0;
		while (position >= 0) {
			switch (state) {
			case 0:
				switch (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case '�':
					state = 1;
					position--;
					break;

				case 's':
					state = 3;
					position--;
					break;

				case 'n':
					state = 4;
					position--;
					break;

				case 'o':
					state = 8;
					position--;
					break;
				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;

			case 1:
				switch (this.rv_txt.charAt(position)) {
				case 'y':
					state = 2;
					position--;
					saved_position = saved_position - position;
					position = STOP;
					accept = true;
					break;

				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;

			case 3:
				switch (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
					state = 1;
					position--;
					break;

				case 'i':
					state = 7;
					position--;
					break;

				case 'o':
					state = 6;
					position--;
					break;
				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;

			case 4:
				switch (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
					state = 1;
					position--;
					break;

				case 'o':
					state = 5;
					position--;
					break;

				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;

			case 5:
				switch (this.rv_txt.charAt(position)) {
				case 'r':
					state = 10;
					position--;
					break;

				default:
					saved_position -= position;
					position = STOP;
					break;
				}
				break;

			case 6:
				switch (this.rv_txt.charAt(position)) {
				case 'm':
					state = 7;
					position--;
					break;

				default:
					saved_position -= position;
					position = STOP;
					break;
				}
				break;

			case 7:
				switch (this.rv_txt.charAt(position)) {
				case 'a':
					state = 1;
					position--;
					break;

				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;

			case 8:
				switch (this.rv_txt.charAt(position)) {
				case 'd':
					state = 9;
					position--;
					break;

				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;

			case 9:
				switch (this.rv_txt.charAt(position)) {
				case 'n':
					state = 10;
					position--;
					break;

				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;

			case 10:
				switch (this.rv_txt.charAt(position)) {
				case 'e':
					state = 1;
					position--;
					break;

				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;
			case 11:
				switch (this.rv_txt.charAt(position)) {
				case 'l':
					state = 2;
					position--;
					break;

				default:
					saved_position = saved_position - position;
					position = STOP;
					break;
				}
				break;

			default:
				saved_position = saved_position - position;
				position = STOP;
				accept = false;
				break;
			}
		}

		if (accept)
			if (str.charAt(str.length() - (saved_position + 1)) == 'u')
				return substr(str, 0, str.length() - saved_position);
		
		return str;
	} // end step2a

	protected String step2b(String str) {
		int position = rv_txt.length() - 1;
		int saved_position = position;
		int original_position = position;
		boolean accept = false;
		int state = 0;
		String suf = "";
		while (position >= 0) {
			switch (state) {
			case 0:
				switch  (this.rv_txt.charAt(position)) {

				case 'n':
					state = 21;
					position--;
					break;

				case 's':
					state = 46;
					position--;
					break;

				case '�':
					state = 8;
					position--;
					break;

				case 'a':
					state = 1;
					position--;
					break;

				case 'd':
					state = 5;
					position--;
					break;

				case 'r':
					state = 44;
					position--;
					break;

				case 'e':
					state = 13;
					position--;
					break;

				case '�':
					state = 12;
					position--;
					break;

				case 'o':
					state = 35;
					position--;
					break;

				case '�':
					state = 42;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 1:
				switch  (this.rv_txt.charAt(position)) {
				case '�':
					state = 4;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				case 'r':
					state = 9;
					position--;
					break;
				case 'b':
					state = 2;
					position--;
					break;
				case 'd':
					state = 7;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 2:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 3;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			// case 3:
			case 4:
				switch  (this.rv_txt.charAt(position)) {
				case 'r':
					state = 5;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 5:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case 'i':
					state = 16;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 6:
				switch  (this.rv_txt.charAt(position)) {
				case 'e':
					state = 20;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;

				case 'a':
					state = 70;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 7:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
				case 'a':
					state = 3;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 8:
				switch  (this.rv_txt.charAt(position)) {
				case 'r':
					state = 5;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 9:
				switch  (this.rv_txt.charAt(position)) {
				case 'e':
					state = 10;
					position--;
					break;
				case 'a':
					state = 3;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 10:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
					state = 3;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 11:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
				case 'a':
					state = 70;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 12:
				switch  (this.rv_txt.charAt(position)) {
				case 'r':
					state = 5;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 13:
				switch  (this.rv_txt.charAt(position)) {
				case 's':
					state = 14;
					position--;
					break;

				case 't':
					state = 18;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 14:
				switch  (this.rv_txt.charAt(position)) {
				case 'e':
					state = 15;
					position--;
					break;
				case 'a':
					state = 16;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 15:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
					state = 16;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 16:

			// case 17:

			case 18:
				switch  (this.rv_txt.charAt(position)) {
				case 's':
					state = 19;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 19:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
				case 'a':
					state = 16;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			// case 20:

			case 21:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 22;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				case '�':
					state = 28;
					position--;
					break;
				case 'e':
					state = 32;
					position--;
					break;
				case 'o':
					state = 29;
					position--;
					break;

				default:
					position = STOP;
					break;
				}
				break;

			case 22:
				switch  (this.rv_txt.charAt(position)) {
				case '�':
					state = 25;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				case 'b':
					state = 23;
					position--;
					break;
				case 'r':
					state = 30;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 23:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 24;
					position--;
					saved_position = original_position - position;
					accept = true;
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 24:

			case 25:
				switch (this.rv_txt.charAt(position)) {
				case 'r':
					state = 26;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 26:
				switch (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case 'i':
					state = 24;
					position--;
					accept = true;
					saved_position = original_position - position;
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 27:
				switch (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case 'i':
					state = 17;// 71 & 34 too
					position--;
					accept = true;
					saved_position = original_position - position;
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 28:
				switch (this.rv_txt.charAt(position)) {
				case 'r':
					state = 26;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 29:
				switch (this.rv_txt.charAt(position)) {
				case 'r':
					state = 30;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 30:
				switch (this.rv_txt.charAt(position)) {
				case 'e':
					state = 33;
					position--;
					break;
				case 'a':
					state = 24;
					position--;
					saved_position = original_position - position;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 31:
				switch (this.rv_txt.charAt(position)) {
				case 'r':
					state = 27;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 32:
				switch (this.rv_txt.charAt(position)) {
				case 's':
					state = 30;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 33:
				switch (this.rv_txt.charAt(position)) {
				case 'i':
					state = 24;
					position--;
					saved_position = original_position - position;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 34:

			case 35:
				switch (this.rv_txt.charAt(position)) {
				case 'd':
					state = 36;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 36:
				switch (this.rv_txt.charAt(position)) {
				case 'n':
					state = 38;
					position--;
					break;
				case 'a':
				case 'i':
					state = 43;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 37:
				switch  (this.rv_txt.charAt(position)) {
				case 's':
					state = 11;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 38:
				switch  (this.rv_txt.charAt(position)) {
				case 'e':
					state = 40;
					position--;
					break;
				case 'a':
					state = 43;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 39:
				switch  (this.rv_txt.charAt(position)) {
				case 'e':
					state = 41;
					position--;
					break;
				case 'a':
					state = 70;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 40:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
					state = 43;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 41:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
					state = 70;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 42:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
					state = 43;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 43:

			case 44:
				switch  (this.rv_txt.charAt(position)) {
				case 'e':
				case 'a':
				case 'i':
					state = 43;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 45:
				switch  (this.rv_txt.charAt(position)) {
				case 'd':
					state = 11;
					position--;
					break;
				case 'm':
					state = 72;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 46:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 47;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				case '�':
					state = 61;
					position--;
					break;
				case '�':
					state = 49;
					position--;
					accept = true;
					saved_position = original_position - position;
					position = STOP;
					break;
				case 'o':
					state = 45;
					position--;
					break;
				case 'e':
					state = 57;
					position--;
					break;

				case 'i':
					state = 64;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 47:
				switch  (this.rv_txt.charAt(position)) {
				case 'b':
					state = 48;
					position--;
					break;
				case 'd':
					state = 53;
					position--;
					break;
				case '�':
					state = 50;
					position--;
					accept = true;
					saved_position = original_position - position;

					break;
				case 'r':
					state = 54;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 48:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 52;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 49:

			case 50:
				switch  (this.rv_txt.charAt(position)) {
				case 'r':
					state = 51;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 51:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case 'i':
					state = 52;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 52:

			case 53:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
				case 'i':
					state = 52;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 54:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 55;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				case 'e':
					state = 56;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 55:

			case 56:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
					state = 55;
					position--;
					saved_position = original_position - position;
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 57:
				switch  (this.rv_txt.charAt(position)) {
				case 's':
					state = 58;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 58:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 55;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				case 'e':
					state = 60;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 59:

			case 60:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
					state = 59;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 61:
				switch  (this.rv_txt.charAt(position)) {
				case 'r':
					state = 62;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 62:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case 'i':
					state = 63;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 63:

			case 64:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 65;
					position--;
					break;
				case 'e':
					state = 69;
					position--;
					break;
				case '�':
					state = 31;
					position--;
					break;
				case '�':
					state = 70;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 65:
				switch  (this.rv_txt.charAt(position)) {
				case 'b':
					state = 68;
					position--;
					break;
				case 'r':
					state = 6;
					position--;
					break;
				case '�':
					state = 66;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 66:
				switch  (this.rv_txt.charAt(position)) {
				case 'r':
					state = 67;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 67:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case 'i':
					state = 70;
					position--;
					accept = true;
					saved_position = original_position - position;
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 68:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 70;
					position--;
					accept = true;
					saved_position = original_position - position;
					position = STOP;
					break;
				default:

					position = STOP;
					break;
				}
				break;

			case 69:
				switch  (this.rv_txt.charAt(position)) {
				case 's':
					state = 39;
					position--;
					break;
				case 't':
					state = 37;
					position--;
					break;
				default:
					saved_position = original_position - position;
					position = STOP;
					break;
				}
				break;

			// case 70:
			// case 71:

			case 72:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
					state = 73;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				case 'i':
					state = 78;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				case 'e':
					state = 86;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 73:
				switch  (this.rv_txt.charAt(position)) {
				case 'b':
					state = 77;
					position--;
					break;
				case 'r':
					state = 79;
					position--;
					break;
				case '�':
					state = 74;
					position--;
					accept = true;
					saved_position = original_position - position;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 74:
				switch  (this.rv_txt.charAt(position)) {
				case 'r':
					state = 6;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 75:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case 'i':
					state = 76;
					position--;
					saved_position = original_position - position;
					position = STOP;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			// case 76:

			case 77:
				switch  (this.rv_txt.charAt(position)) {
				case '�':
					state = 78;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			// case 78:
			case 79:
				switch  (this.rv_txt.charAt(position)) {
				case '�':
					state = 82;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				case '�':
					state = 80;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			case 80:
				switch  (this.rv_txt.charAt(position)) {
				case 'i':
					state = 81;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;
			// case 81:
			// case 82:

			case 83:
				switch  (this.rv_txt.charAt(position)) {
				case 'e':
				case 'i':
					state = 78;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 84:
				switch  (this.rv_txt.charAt(position)) {
				case '�':
				case 'i':
					state = 78;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				case '�':
					state = 85;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
				default:
					position = STOP;
					break;
				}
				break;

			// case 85:
			case 86:
				switch  (this.rv_txt.charAt(position)) {
				case 'r':
					state = 87;
					position--;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			case 87:
				switch  (this.rv_txt.charAt(position)) {
				case 'a':
				case 'e':
				case 'i':
					state = 78;
					position--;
					saved_position = original_position - position;
					position = STOP;
					accept = true;
					break;
				default:
					position = STOP;
					break;
				}
				break;

			default:
				position = STOP;
				break;
			}

		}
		
		if (accept)
			str = substr(str, 0, str.length() - saved_position);
		else if (!(suf = endsinArr(this.rv_txt,array("en", "es", "�is", "emos"))).equals("")) {
			str = substr(str, 0, -suf.length());
			
			if (endsin(str, "gu")) {
				str = substr(str, 0, -1);
			}
		}
	
		
		return str;
	} // end step2b

	protected String step3(String str) {

		String suf ="";
		
		if (!(suf = endsinArr(rv_txt, array("os", "a", "o", "�", "�", "�")))
				.equals("")) {
			str = substr(str, 0, -suf.length());
		} else if (!(suf = endsinArr(rv_txt, array("e", "�"))).equals("")) {
			str = substr(str, 0, -1);
			rv_txt = substr(str, this.rv);
			if (endsin(rv_txt, "u") && endsin(str, "gu")) {
				str = substr(str, 0, -1);
			}
		}

		return removeAccent(str);
		
	} // end step1c

	/*
	 * ------------------------------------------------------- The following are
	 * functions to help compute steps 1 - 3
	 * -------------------------------------------------------
	 */


	/**
	 * Retorna un array de String dados muchos Strings
	 */
	private String[] array(String... strings) {
		return strings;
	}
	

    /**
     * Determina si el sufijo esta presente en una palabra
     */
    private boolean endsin(String word, String suffix) {
        if (word.length() < suffix.length()) {
            return false;
        }
        return (substr(word, -suffix.length()).equals(suffix));
    }
    
	

	 /**
     *Retorna una subcadena de un String
     */
    private String substr(String word, int beginIndex, int length) {
        if (beginIndex == length) {
            return "";

        } else {

            if ((beginIndex >= 0)) { // incio positivo
                int endIndex;
                if ((length >= 0)) { // longitud positiva
                    endIndex = beginIndex + length;
                    if (endIndex > word.length()) {
                        word = word.substring(beginIndex, word.length());
                        return word;
                    } else {
                        word = word.substring(beginIndex, endIndex);
                        return word;
                    }
                } else { // longitud negativa
                    endIndex = word.length() + length;
                    try {
                        word = word.substring(beginIndex, endIndex);
                    } catch (StringIndexOutOfBoundsException e) {
                        word = "";
                    }
                    return word;
                }

            } else {// incio negativo
                int endIndex;
                int newBeginIndex;
                if ((length >= 0)) { // longitud positiva
                    newBeginIndex = word.length() + beginIndex;
                    endIndex = newBeginIndex + length;
                    if (endIndex > word.length()) {
                        word = word.substring(newBeginIndex, word.length());
                        return word;
                    } else {
                        word = word.substring(newBeginIndex, endIndex);
                        return word;
                    }
                } else { // longitud negativa
                    newBeginIndex = word.length() + beginIndex;
                    endIndex = word.length() + length;

                    try {
                        word = word.substring(newBeginIndex, endIndex);
                    } catch (StringIndexOutOfBoundsException e) {
                        word = "";
                    }

                    return word;
                }
            }

        }

    }

    
    /**
     * Retorna una subcadena de un String
     */
    private String substr(String word, int beginIndex) {
        if (Math.abs(beginIndex) > word.length()) {
            return word;
        } else if (beginIndex >= 0) {
            return word.substring(beginIndex, word.length());
        } else {
            return word.substring(word.length() + beginIndex, word.length());
        }
    }
    
    /**
     * Retorna una palabra sin acentos
     */
    private String removeAccent(String word) {
        
        return word.replaceAll("�", "a").replaceAll("�", "e")
                .replaceAll("�", "i").replaceAll("�", "o")
                .replaceAll("�", "u");
        
    }
    
    /**
     * Determina si el caracter es una vocal.
     */
    private boolean is_vowel(char c) {
        return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
                || c == '�' || c == '�' || c == '�' || c == '�' || c == '�');
    }

    /**
     * Retorna la posicion de la primera vocal encontrada
     */
    private int getNextVowelPos(String word, int start) {
        int len = word.length();
        for (int i = start; i < len; i++) {
            if (is_vowel(word.charAt(i))) {
                return i;
            }
        }
        return len;
    }
    

    /**
     * Retorna la posicion de la primera consonante encontrada
     */
    private int getNextConsonantPos(String word, int start) {
        int len = word.length();
        for (int i = start; i < len; i++) {
            if (!is_vowel(word.charAt(i))) {
                return i;
            }
        }
        return len;
    }
    
    
    /**
     * Retorna el sufijo mas largo presente en una palabra
     */
    private String endsinArr(String word, String[] suffixes) {
        String tmp = "";
        for (String suff : suffixes) {
            if (endsin(word, suff)) {
                if (suff.length() >= tmp.length()) {
                    tmp = suff;
                }
            }
        }
        return tmp;
    }

	

	public void test() {

		String file = "wiki_02_es.txt";
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
				//	String test = stem(array[0]);
					//if(!test.equals(array[1]))
						stem(array[0]);
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

} // end class