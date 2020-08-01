package org.stemmer.automata.porter.english;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Porter{

	private static final int STOP = -1;

	private String in = "";

	public String apply(String word){
		this.in = word;
		if(this.in.length() > 3)
			step1a().step1b().step1c().step2().step3().step4().step5a().step5b();
		return this.in;
	}

	public void applyTest(String word){
		this.in = word;
	}

	public String getWord(){
		return this.in;
	}

    // returns a CVC measure for the string
    protected int stringMeasure(String str) {
        int count = 0;
        boolean vowelSeen = false;
        char[] strchars = str.toCharArray();

        for (int i = 0; i < strchars.length; i++) {
            if (isVowel(strchars[i])) {
                vowelSeen = true;
            } else if (vowelSeen) {
                count++;
                vowelSeen = false;
            }
        } // end for
        return count;
    } // end function

    // does stem end with CVC?
    protected boolean endsWithCVC (String str) {
        char c, v, c2 = ' ';
        if (str.length() >= 3) {
            c = str.charAt(str.length() - 1);
            v = str.charAt(str.length() - 2);
            c2 = str.charAt(str.length() - 3);
        } else {
            return false;
        }

        if ((c == 'w') || (c == 'x') || (c == 'y')) {
            return false;
        } else if (isVowel(c)) {
            return false;
        } else if (!isVowel(v)) {
            return false;
        } else if (isVowel(c2)) {
            return false;
        } else {
            return true;
        }
    } // end function

    // is char a vowel?
    public boolean isVowel(char c) {
        if ((c == 'a') ||
            (c == 'e') ||
            (c == 'i') ||
            (c == 'o') ||
            (c == 'u'))
            return true;
        else
            return false;
    } // end function

	private int m(String word, int position){

		int m = 0;
		int state = 1;
		char letter = ' ';

		while(position >= 0){
			switch(state){
			case 1:
				letter = word.charAt(position);
				if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u'){
					//stay in state 1
				}else if(letter == 'y'){
					state = 6;
				}else{
					state = 2;
				}
				position--;
			break;
			case 2:
				letter = word.charAt(position);
				if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u'){
					state = 3;
				}else if(letter == 'y'){
					state = 4;
				}else{
					state = 2;
				}
				position--;
			break;
			case 3:
				m++;
				letter = word.charAt(position);
				if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u'){
					state = 1;
				}else if(letter == 'y'){
					state = 5;
				}else{
					state = 2;
				}
				position--;
			break;
			case 4:
				letter = word.charAt(position);
				if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u'){
					state = 2;
				}else{
					state = 3;
				}
				position --;
			break;
			case 5:
				letter = word.charAt(position);
				if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u'){
					state = 2;
				}else{
					state = 1;
				}
				position--;
			break;
			case 6:
				letter = word.charAt(position);
				if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u'){
					state = 3;
				}else{
					state = 2;
				}
				position--;
			break;
			}

		}

		//garante que se terminar a palavra no estado 3, m vai ser incrementado
		if(state == 3){
			m++;
		}

		return m;
	}

	private boolean hasVowel(String word, int position){

		int state = 1;
		boolean vowel = false;
		char letter = ' ';

		while(position >= 0){
			switch(state){
			case 1:
				letter = word.charAt(position);
				if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u'){
					state = 2;
				}else if(letter == 'y'){
					state = 3;
				}
				position--;
			break;
			case 2:
				vowel = true;
				position = STOP;
			break;
			case 3:
				letter = word.charAt(position);
				if(letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u'){
					state = 1;
				}else{
					state = 2;
				}
				position--;
			break;
			}
		}

		return vowel;
	}

	private boolean o(String in){
		//do not end with w, x or y
		if((in.endsWith("w") || in.endsWith("x") || in.endsWith("y")) == false){
			char letter = in.charAt(in.length() - 1);
			//ends with consonant
			if((letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') == false){
				letter = in.charAt(in.length() - 2);
				//verify if is vowel of type Cy
				if(letter == 'y'){
					letter = in.charAt(in.length() - 3);
					//verify if y is preceded by consonant, make this a vowel
					if((letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') == false){
						letter = in.charAt(in.length() - 4);
						//preceded by consonant
						if((letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') == false){
							//CVC identify
							return true;
						}
					}
				//CV identify
				}else if((letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') == true){
					//not a vowel
					letter = in.charAt(in.length() - 3);
					if((letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') == false){
						//CVC identify
						return true;
					}
				}
			}
		}
		return false;
	}

	public Porter step1a(){

		int state = 1;
		int position = in.length() - 1;
		boolean accept = false;

		while(position >= 0){
			switch(state){
			case 1:
				if(in.charAt(position) == 's'){
					state = 2;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 2:
				if(accept){
					//rule S
					//IF with ACCEPT always has position + 1
					in = in.substring(0, position + 1);
					position = STOP;
				}else{
					switch(in.charAt(position)){
					case 's':
						//rule SS
						//remove SS and add SS
						position = STOP;
					break;
					case 'e':
						state = 4;
						position--;
					break;
					default:
						//rule S
						//DEFAULT always has position + 1
						in = in.substring(0, position + 1);
						position = STOP;
					break;
					}
				}
			break;
			case 4:
				switch(in.charAt(position)){
				case 'i':
					//rule IES
					in = in.substring(0, position + 1);
					position = STOP;
				break;
				case 's':
					state = 6;
					position--;
				break;
				default:
					state = 2;
					accept = true;
				break;
				}
			break;
			case 6:
				if(in.charAt(position) == 's'){
					//rule SSES
					in = in.substring(0, position + 2);
				}
				position = STOP;
			break;
			default:
				position = STOP;
			break;
			}
		}

		return this;
	}

	public Porter step1b(){

		int state = 1;
		int position = in.length() - 1;

		while(position >= 0){
			switch(state){
			case 1:
				switch(in.charAt(position)){
				case 'd':
					state = 2;
					position--;
				break;
				case 'g':
					state = 3;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 2:
				if(in.charAt(position) == 'e'){
					state = 4;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 3:
				if(in.charAt(position) == 'n'){
					state = 5;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 4:
				if(in.charAt(position) == 'e'){
					//rule EED
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2);
					}
				}else{
					//rule ED
					if(hasVowel(in, position)){
						in = in.substring(0, position + 1);
						step1b2();
					}
				}
				position = STOP;
			break;
			case 5:
				if(in.charAt(position) == 'i'){
					//rule ING
					if(hasVowel(in, position - 1)){
						in = in.substring(0, position);
						step1b2();
					}
				}
				position = STOP;
			break;
			}
		}

		return this;
	}

	public Porter step1b2(){
		if(in.endsWith("at")){
			in += "e";
		}else if(in.endsWith("iz")){
			in += "e";
		}else if(in.endsWith("bl")){
			in += "e";
		}else if(in.length() >= 2 && (in.charAt(in.length() - 1) == in.charAt(in.length() - 2))){
			char letter = in.charAt(in.length() - 2);
			if((letter == 'l' || letter == 's' || letter == 'z') == false){
				in = in.substring(0, in.length() - 1);
			}
		}else{
			if(o(in) && m(in, in.length() - 1) == 1){
				in += "e";
			}
		}
		return this;
	}

	public Porter step1c(){
		if(in.endsWith("y")){
			if(hasVowel(in, in.length() - 2)){
				in = in.substring(0, in.length() - 1) + "i";
			}
		}
		return this;
	}

	public Porter step2(){

		int state = 1;
		int position = in.length() - 1;
		boolean accept = false;

		while(position >= 0){
			switch(state){
			case 1:
				switch(in.charAt(position)){
				case 'n':
					state = 7;
					position--;
				break;
				case 'i':
					state = 3;
					position--;
				break;

				case 'l':
					state = 2;
					position--;
				break;

				case 'r':
					state = 4;
					position--;
				break;
				case 'm':
					state = 5;
					position--;
				break;
				case 's':
					state = 6;
					position--;
				break;

				default:
					position = STOP;
				break;
				}
			break;
			case 2:
				if(in.charAt(position) == 'a'){
					state = 8;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 3:
				switch(in.charAt(position)){
				case 'l':
					state = 15;
					position--;
				break;
				case 'c':
					state = 14;
					position--;
				break;

				case 't':
					state = 16;
					position--;
				break;


				default:
					position = STOP;
				break;
				}
			break;
			case 4:
				switch(in.charAt(position)){
				case 'o':
					state = 60;
					position--;
				break;
				case 'e':
					state = 63;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 5:
				if(in.charAt(position) == 's'){
					state = 56;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 6:
				if(in.charAt(position) == 's'){
					state = 44;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 7:
				if(in.charAt(position) == 'o'){
					state = 38;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 8:
				if(in.charAt(position) == 'n'){
					state = 9;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 9:
				if(in.charAt(position) == 'o'){
					state = 10;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 10:
				if(in.charAt(position) == 'i'){
					state = 11;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 11:
				if(in.charAt(position) == 't'){
					state = 12;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 12:
				if(in.charAt(position) == 'a'){
					//rule ATIONAL
					if(m(in, position - 1) > 0){
						in = in.substring(0, position) + "ate";
					}
				}else{
					//rule TIONAL
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 5);
					}
				}
				position = STOP;
			break;
			case 14:
				if(in.charAt(position) == 'n'){
					state = 17;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 15:
				switch(in.charAt(position)){
				case 'l':
					state = 21;
					position--;
				break;
				case 'e':
					//rule ELI
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 1);
					}
					position = STOP;
				break;


				case 't':
					state = 22;
					position--;
				break;

				case 's':
					state = 23;
					position--;
				break;

				case 'b':
					state = 20;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 16:
				if(in.charAt(position) == 'i'){
					state = 31;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 17:
				switch(in.charAt(position)){
				case 'e':
					//rule ENCI
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3) + "e";
					}
				break;
				case 'a':
					//rule ANCI
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3) + "e";
					}
				break;
				}
				position = STOP;
			break;
			case 20:
				//rule ABLI
				if(in.charAt(position) == 'a'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3) + "e";
					}
				}
				position = STOP;
			break;
			case 21:
				//rule ALLI
				if(in.charAt(position) == 'a'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2);
					}
				}
				position = STOP;
			break;
			case 22:
				if(in.charAt(position) == 'n'){
					state = 27;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 23:
				if(in.charAt(position) == 'u'){
					state = 29;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 27:
				//rule ENTLI
				if(in.charAt(position) == 'e'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3);
					}
				}
				position = STOP;
			break;
			case 29:
				//rule OUSLI
				if(in.charAt(position) == 'o'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3);
					}
				}
				position = STOP;
			break;
			case 31:
				switch(in.charAt(position)){
				case 'l':
					state = 32;
					position--;
				break;
				case 'v':
					state = 33;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 32:
				switch(in.charAt(position)){
				case 'a':
					//rule ALITI
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2);
					}
					position = STOP;
				break;
				case 'i':
					state = 35;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 33:
				//rule IVITI
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2) + "e";
					}
				}
				position = STOP;
			break;
			case 35:
				//rule BILITI
				if(in.charAt(position) == 'b'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position) + "ble";
					}
				}
				position = STOP;
			break;
			case 38:
				if(in.charAt(position) == 'i'){
					state = 39;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 39:
				if(in.charAt(position) == 't'){
					state = 40;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 40:
				if(in.charAt(position) == 'a'){
					state = 41;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 41:
				if(accept){
					//rule ATION
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2) + "e";
					}
					position = STOP;
				}else{
					if(in.charAt(position) == 'z'){
						state = 42;
						position--;
					}else{
						if(m(in, position - 1) > 0){
							in = in.substring(0, position + 3) + "e";
						}
						position = STOP;
					}
				}
			break;
			case 42:
				//rule IZATION
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2) + "e";
					}
					position = STOP;
				}else{
					accept = true;
					position += 1;
					state = 41;
				}
			break;
			case 44:
				if(in.charAt(position) == 'e'){
					state = 45;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 45:
				if(in.charAt(position) == 'n'){
					state = 46;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 46:
				switch(in.charAt(position)){
				case 'e':
					state = 53;
					position--;
				break;
				case 's':
					state = 47;
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
			case 47:
				if(in.charAt(position) == 'u'){
					state = 48;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 48:
				//rule OUSNESS
				if(in.charAt(position) == 'o'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3);
					}
				}
				position = STOP;
			break;
			case 50:
				if(in.charAt(position) == 'u'){
					state = 51;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 51:
				//rule FULNESS
				if(in.charAt(position) == 'f'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3);
					}
				}
				position = STOP;
			break;
			case 53:
				if(in.charAt(position) == 'v'){
					state = 54;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 54:
				//rule IVENESS
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3);
					}
				}
				position = STOP;
			break;
			case 56:
				if(in.charAt(position) == 'i'){
					state = 57;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 57:
				if(in.charAt(position) == 'l'){
					state = 58;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 58:
				//rule ALISM
				if(in.charAt(position) == 'a'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2);
					}
				}
				position = STOP;
			break;
			case 60:
				if(in.charAt(position) == 't'){
					state = 61;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 61:
				//rule ATOR
				if(in.charAt(position) == 'a'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2) + "e";
					}
				}
				position = STOP;
			break;
			case 63:
				if(in.charAt(position) == 'z'){
					state = 64;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 64:
				//rule IZER
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 3);
					}
				}
				position = STOP;
			break;
			}

		}

		return this;
	}

	public Porter step3(){

		int state = 1;
		int position = in.length() - 1;

		while(position >= 0){
			switch(state){
			case 1:
				switch(in.charAt(position)){
				case 'l':
					state = 3;
					position--;
				break;
				case 'e':
					state = 2;
					position--;
				break;
				case 's':
					state = 5;
					position--;
				break;
				case 'i':
					state = 4;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 2:
				switch(in.charAt(position)){
				case 't':
					state = 18;
					position--;
				break;
				case 'v':
					state = 22;
					position--;
				break;
				case 'z':
					state = 26;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 3:
				switch(in.charAt(position)){
				case 'a':
					state = 14;
					position--;
				break;
				case 'u':
					state = 13;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 4:
				if(in.charAt(position) == 't'){
					state = 9;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 5:
				if(in.charAt(position) == 's'){
					state = 6;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 6:
				if(in.charAt(position) == 'e'){
					state = 7;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 7:
				//rule NESS
				if(in.charAt(position) == 'n'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 9:
				if(in.charAt(position) == 'i'){
					state = 10;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 10:
				if(in.charAt(position) == 'c'){
					state = 11;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 11:
				//rule ICITI
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2);
					}
				}
				position = STOP;
			break;
			case 13:
				//rule FUL
				if(in.charAt(position) == 'f'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 14:
				if(in.charAt(position) == 'c'){
					state = 15;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 15:
				//rule ICAL
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2);
					}
				}
				position = STOP;
			break;
			case 18:
				if(in.charAt(position) == 'a'){
					state = 19;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 19:
				if(in.charAt(position) == 'c'){
					state = 20;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 20:
				//rule ICATE
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2);
					}
				}
				position = STOP;
			break;
			case 22:
				if(in.charAt(position) == 'i'){
					state = 23;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 23:
				if(in.charAt(position) == 't'){
					state = 24;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 24:
				//rule ATIVE
				if(in.charAt(position) == 'a'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 26:
				if(in.charAt(position) == 'i'){
					state = 27;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 27:
				if(in.charAt(position) == 'l'){
					state = 28;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 28:
				//rule ALIZE
				if(in.charAt(position) == 'a'){
					if(m(in, position - 1) > 0){
						in = in.substring(0, position + 2);
					}
				}
				position = STOP;
			break;
			}
		}

		return this;
	}

	public Porter step4(){

		int state = 1;
		int position = in.length() - 1;

		while(position >= 0){
			switch(state){
			case 1:
				switch(in.charAt(position)){
				case 'e':
					state = 2;
					position--;
				break;
				case 'r':
					state = 20;
					position--;
				break;
				case 'l':
					state = 18;
					position--;
				break;
				case 't':
					state = 13;
					position--;
				break;
				case 'c':
					state = 22;
					position--;
				break;
				case 'i':
					state = 29;
					position--;
				break;
				case 'n':
					state = 35;
					position--;
				break;
				case 'u':
					state = 24;
					position--;
				break;
				case 'm':
					state = 26;
					position--;
				break;
				case 's':
					state = 32;
					position--;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 2:
				switch(in.charAt(position)){
				case 't':
					state = 9;
					position--;
				break;
				case 'v':
					state = 11;
					position--;
				break;
				case 'c':
					state = 3;
					position--;
				break;
				case 'z':
					state = 11;
					position--;
				break;
				case 'l':
					state = 6;
					position--;
				break;

				default:
					position = STOP;
				break;
				}
			break;
			case 3:
				if(in.charAt(position) == 'n'){
					state = 4;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 4:
				//rule ANCE e ENCE
				if(in.charAt(position) == 'a' || in.charAt(position) == 'e'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 6:
				if(in.charAt(position) == 'b'){
					state = 7;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 7:
				//rule IBLE e ABLE
				if(in.charAt(position) == 'a' || in.charAt(position) == 'i'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 9:
				//rule AT
				if(in.charAt(position) == 'a'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 11:
				//rule IVE e IZE
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 13:
				if(in.charAt(position) == 'n'){
					state = 14;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 14:
				switch(in.charAt(position)){
				case 'e':
					state = 15;
					position--;
				break;
				case 'a':
					//rule ANT
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
					position = STOP;
				break;
				default:
					position = STOP;
				break;
				}
			break;
			case 15:
				if(in.charAt(position) == 'm'){
					state = 16;
					position--;
				}else{
					//rule ENT
					if(m(in, position - 1) > 1){
						in = in.substring(0, position + 1);
					}
					position = STOP;
				}
			break;
			case 16:
				//rule EMENT
				if(in.charAt(position) == 'e'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}else{
					//rule MENT
					if(m(in, position - 1) > 1){
						in = in.substring(0, position + 1);
					}
				}
				position = STOP;
			break;
			case 18:
				//rule AL
				if(in.charAt(position) == 'a'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 20:
				if(in.charAt(position) == 'e'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 22:
				//rule IC
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 24:
				//rule OU
				if(in.charAt(position) == 'o'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 26:
				if(in.charAt(position) == 's'){
					state = 27;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 27:
				//rule ISM
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 29:
				if(in.charAt(position) == 't'){
					state = 30;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 30:
				//rule ITI
				if(in.charAt(position) == 'i'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 32:
				if(in.charAt(position) == 'u'){
					state = 33;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 33:
				//rule OUS
				if(in.charAt(position) == 'o'){
					if(m(in, position - 1) > 1){
						in = in.substring(0, position);
					}
				}
				position = STOP;
			break;
			case 35:
				if(in.charAt(position) == 'o'){
					state = 36;
					position--;
				}else{
					position = STOP;
				}
			break;
			case 36:
				//rule ION
				if(in.charAt(position) == 'i'){
					String stem = in.substring(0, position);
					if(stem.endsWith("t") || stem.endsWith("s")){
						if(m(in, position - 1) > 1){
							in = stem;
						}
					}
				}
				position = STOP;
			break;
			}
		}

		return this;
	}

	public Porter step5a(){
	     // (m > 1) E ->
        if ((stringMeasure(in.substring(0, in.length() - 1)) > 1) &&
            in.endsWith("e"))
            in = in.substring(0, in.length() -1);
        // (m = 1 and not *0) E ->
        else if ((stringMeasure(in.substring(0, in.length() - 1)) == 1) &&
                 (!endsWithCVC(in.substring(0, in.length() - 1))) &&
                 (in.endsWith("e")))
            in = in.substring(0, in.length() - 1);

		return this;
	}

	public Porter step5b(){
		if(in.length() >= 2 && in.endsWith("l")){
			if(in.charAt(in.length() - 2) == 'l'){
				if(m(in, in.length() - 1) > 1){
					in = in.substring(0, in.length() - 1);
				}
			}
		}
		return this;
	}

	public void test() {

		String file = "wiki_00_en.txt";// stemm_test_corpus
		String encoding = "ISO-8859-1";
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
					//System.out.println(this.apply(array[0]));
					this.apply(array[0]);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		long finalTime = System.nanoTime() - initialTime;
		double seconds = finalTime / 1000000000.0;
		System.out.println("Running time was " + seconds + " seconds");
		//System.out.println(finalTime - initialTime);

	}

}