package no.uio.ifi.cflat.scanner;

/*
 * module Scanner
 */

import no.uio.ifi.cflat.chargenerator.CharGenerator;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.log.Log;
import static no.uio.ifi.cflat.scanner.Token.*;

/*
 * Module for forming characters into tokens.
 */

public class Scanner {
	public static Token curToken, nextToken, nextNextToken;
	public static String curName, nextName, nextNextName;
	public static int curNum, nextNum, nextNextNum;
	public static int curLine, nextLine, nextNextLine;

	// this is the same order as the tokens in Token.java
	private static final String[] tokenNames = new String[]{"+", "=", ",", "/", "double", "else", "?", "==", "for", ">=", ">", "if", "int", "[", "{", "(", "<=", "<", "*", "?", "!=", "?", "]", "}", ")", "return", ";", "-", "while"};

	public static void init() {
		//-- Must be changed in part 0:
		curName = "";
		nextName = "";
		nextNextName = "";
	}

	public static void finish() {
		//-- Must be changed in part 0:
	}

	public static void readNext() {
		curToken = nextToken;  nextToken = nextNextToken;
		curName = nextName;  nextName = nextNextName;
		curNum = nextNum;  nextNum = nextNextNum;
		curLine = nextLine;  nextLine = nextNextLine;

		nextNextToken = null;
		while (nextNextToken == null) {
			nextNextLine = CharGenerator.curLineNum();

			if (! CharGenerator.isMoreToRead()) {
				nextNextToken = eofToken;
			} else {

				// curName is the string of the token we are collecting so far
				curName = "";


				// first, check if we can collect a token consisting of only letters
				if (isLetterAZ(CharGenerator.curC)) {
					while (isLetterAZ(CharGenerator.curC)) {
						curName += CharGenerator.curC;
						CharGenerator.readNext();
					}

					// Assign the right tokens, based on the content of curName
					// a long list of checks:
					for (int i=0; i < tokenNames.length; i++) {
						if (tokenNames[i].equals(curName)) {
							curToken = Token.values()[i];
							System.out.println(curName + " er en " + curToken);
						}
					}


				} else {
					// we are at a non-letter, skip to the next letter
					while (!isLetterAZ(CharGenerator.curC)) {
						//System.out.print(".");
						if (CharGenerator.isMoreToRead()) {
							CharGenerator.readNext();
						} else {
							break;
						}
					}
				}

				// ok, we have a token!

				// TODO: print out the token

				// part 0

				/* ------ placeholder code ---------- */
				//boolean allGood = true;
				//if (allGood) {
				//	curToken = forToken;
				//	return;
				//}
				/* ---------------------------------- */

				// Error breaks out
				//Error.error(nextNextLine,
				//		"Illegal symbol: '" + CharGenerator.curC + "'!");
			}
		}
		Log.noteToken();
	}

	private static boolean isLetterAZ(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	public static void test_isLetterAZ() {
		boolean pass = true;
		pass = pass && (isLetterAZ('!') == false);
		pass = pass && (isLetterAZ('a') == true);
		pass = pass && (isLetterAZ('Z') == true);
		if (pass) {
			System.out.println("isLetterAZ: All tests pass");
		}
	}

	public static void check(Token t) {
		if (curToken != t)
			Error.expected("A " + t);
	}

	public static void check(Token t1, Token t2) {
		if (curToken != t1 && curToken != t2)
			Error.expected("A " + t1 + " or a " + t2);
	}

	public static void skip(Token t) {
		check(t);  readNext();
	}

	public static void skip(Token t1, Token t2) {
		check(t1,t2);  readNext();
	}
}
