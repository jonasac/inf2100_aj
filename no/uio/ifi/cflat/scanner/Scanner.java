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
    private static final String[] TOKEN_NAMES = new String[]{"+", "=", ",", "/", "double", "else", "", "==", "for", ">=", ">", "if", "int", "[", "{", "(", "<=", "<", "*", "", "!=", "", "]", "}", ")", "return", ";", "-", "while"};

    private static boolean inComment;
    private static int eofCounter;

    public static void init() {
	//-- Must be changed in part 0:
	curName = "";
	nextName = "";
	nextNextName = "";

	inComment = false;
	eofCounter = 0;
    }

    public static void finish() {
	//-- Must be changed in part 0:
    }

    /** string2token
     *
     * Tries to find the token that corresponds to a given string
     *
     * @param tokenstring A string that may represent a token (like "int")
     * @return A token if match, or null
     */
    private static Token string2token(String tokenstring) {
	if (0 == tokenstring.length()) {
	    return null;
	}
	for (int i=0; i < TOKEN_NAMES.length; i++) {
	    if (TOKEN_NAMES[i].equals(tokenstring)) {
		return Token.values()[i];
	    }
	}
	return null;
    }

    /** collectWord
     *
     * Collects all characters that are letters, digits or underscore characters,
     * until a character that is not one of these is met. Both lower and uppercase
     * symbols are collected.
     *
     * @return A string that only consists of letters, digits and underscore characters.
     */
    private static String collectWord() {
	String temp = "";
	while (isLetterAZ(CharGenerator.curC) || isDigit(CharGenerator.curC) || '_' == CharGenerator.curC) {
	    temp += CharGenerator.curC;
	    CharGenerator.readNext();
	}
	return temp;
    }

    private static String collectNumber() {
	String temp = "";
	while (isDigit(CharGenerator.curC) || '-' == CharGenerator.curC || '\'' == CharGenerator.curC) {
	    temp += CharGenerator.curC;
	    CharGenerator.readNext();
	}
	return temp;
    }

    private static String collectSymbols() {
	String temp = "";
	while (isSymbol(CharGenerator.curC)) {
	    temp += CharGenerator.curC;
	    CharGenerator.readNext();
	}
	return temp;
    }

    private static void skipToNonWhitespace() {
	// Avoid skipping spaces within '
	if ((CharGenerator.curC == ' ') && (CharGenerator.nextC == '\'')) {
	    return;
	}
	// Skip to non-whitespace
	String temp = Character.toString(CharGenerator.curC);
	while (0 == temp.trim().length()) { // if whitespace
	    CharGenerator.readNext();
	    temp = Character.toString(CharGenerator.curC);
	}
    }

    public static void readNext() {

    	curToken = nextToken;  nextToken = nextNextToken;
    	curName = nextName;  nextName = nextNextName;
    	curNum = nextNum;  nextNum = nextNextNum;
    	curLine = nextLine;  nextLine = nextNextLine;

	// clear the nextNext variables
	//nextNextName = "";
	nextNextToken = null;

	while (nextNextToken == null) {
	    nextNextLine = CharGenerator.curLineNum();

	    if (!CharGenerator.isMoreToRead()) {
	      eofCounter++;
	    }

	    if (eofCounter == 4) {
	      // Using the fact that the very last token must be a single character
	      nextNextName = Character.toString(CharGenerator.nextC);
	      nextNextToken = string2token(nextNextName);
	    }

	    if (eofCounter == 5) {
	      curToken = eofToken;
	      nextNextToken = eofToken;
	    }

	    
	    if (eofCounter < 5) {

		// part 0

		// If we are at a whitespace character, read a bit further
		skipToNonWhitespace();

		// Check if we can collect a token consisting of only letters
		if (isLetterAZ(CharGenerator.curC) && (!nextName.equals("'"))) {

		    // Collect characters until a non-variablename-character is reached
		    nextNextName = collectWord();
		    // Try to represent the string as a token, gives null if no representation is found
		    nextNextToken = string2token(nextNextName);
		    if (null == nextNextToken) {
			// We know know that we have a word that is not the name of one of the tokens, so it is a nameToken
			nextNextToken = nameToken;
			if (!inComment) {
			    // variable names etc
			    //System.out.println("Scanner:  " + nextNextToken + " " + nextNextName);
			    // success
			}
			break;
		    }
		    if (!inComment) {
			// Keywords like "if"
			//System.out.println("Scanner:  " + nextNextToken);
			//System.out.println("success: " + nextNextName + " is a " + nextNextToken);
			// success
		    }
		    break;
		} else if (isDigit(CharGenerator.curC) || '-' == CharGenerator.curC || '\'' == CharGenerator.curC) { // digit, - or '
		    // TODO: make sure collectNumber only accepts - as first character
		    nextNextName = collectNumber();
		    if (nextNextName.equals("-")) {
			nextNextToken = subtractToken;
		    } else if (nextNextName.equals("'")) {
			nextNextToken = null;
		   } else {
			nextNextToken = numberToken;
		    }
		    if ((!inComment) && (isDigit(nextNextName.charAt(0)))) {
			//System.out.println("Scanner:  " + nextNextToken + " " + nextNextName);
			//System.out.println("success: got number: " + nextNextName);
		    }
		    // success
		    break;
		} else if (isSymbol(CharGenerator.curC)) {
		    nextNextName = collectSymbols();
		    nextNextToken = string2token(nextNextName);
		    if (null == nextNextToken) {
			// We know know that we have a collection of symbols that is not recognized
			System.out.println(nextNextName + ", what kind of symbol is that?");
		    } else {
			// symbols like < and >
			if (!inComment) {
			    //System.out.println("Scanner:  " + nextNextToken);
			}
			// success
			break;
		    }
		} else {
		    // We are at a place where there is neither a word nor a number
		    // Check for various symbols and assign tokens
		    nextNextName = Character.toString(CharGenerator.curC);
		    CharGenerator.readNext();
		    nextNextToken = string2token(nextNextName);
		    // We are possibly at a symbol, like * or +

		    // Catch tokens that need more interpretation, like 'a'
		    if (null == nextNextToken) {
			if (nextName.equals("'") && !nextNextName.equals("'")) {
			    // We are at a number that is being represented by a symbol enclosed in '
			    byte numval = 0;
			    try {
				numval = nextNextName.getBytes("ISO-8859-1")[0];
			    } catch (java.io.UnsupportedEncodingException e) {
				System.out.println("error: symbol from unsupported character set: " + nextNextName.charAt(0));
			    }
			    nextNextName = new Integer(numval).toString();
			    nextNextToken = numberToken;
			    // won't reach here if in comment
			    //System.out.println("success2: number from symbol: " + nextNextName);
			    // success
			    break;
			} else {
			    // won't reach here if in comment
			    if (inComment) {
				// SKIPPING ERROR WHILE IN COMMENT
				break;
			    } else {
				System.out.println("error: unknown letter, digit or symbol: " + nextNextName);
			    }
			    // error
			}
		    } else {
			// symbols like ( and )
			if (!inComment) {
    			    //System.out.println("ScannerY:  " + nextNextToken);
			} else {
    			    //System.out.println("ScannerF:  " + nextNextToken);
			}
    			// success
    			break;
		    }
		}

		if (inComment) {
		    // SKIPPING ERROR WHILE IN COMMENT
		    break;
		}

		System.out.println("ERROR");

		// Error stops everything
		//Error.error(nextNextLine,
		//	"Illegal symbol: '" + CharGenerator.curC + "'!");
	    }

	}

	if ((!inComment) && (curToken != null)) {
	    if ((curToken == nameToken) || (curToken == numberToken)) {
		System.out.println("Scanner:  " + curToken + " " + curName);
	    } else {
		System.out.println("Scanner:  " + curToken);
	    }
	    //System.out.println("Scanner:  " + curToken + " " + curName);
	    //System.out.println("Scanner:  " + nextToken + " " + nextName);
	    //System.out.println("Scanner:  " + nextNextToken + " " + nextNextName);
	    //Log.noteToken();
	}

	// Check if we are in a comment or not

	if ((nextToken == divideToken) && (nextNextToken == multiplyToken)) {
	    // We are at the start of a multiline comment
	    inComment = true;
	    //System.out.println("INTO COMMENT");
	    nextToken = null;
	    nextNextToken = null;
	}

	if ((nextToken == multiplyToken) && (nextNextToken == divideToken)) {
	    inComment = false;
	    //System.out.println("OUT OF COMMENT");
	    nextToken = null;
	    nextNextToken = null;
	}

	// TODO: Find a way to catch the two last characters as well!

    }

    // Checks if a char could be part of a compound symbol, like != or >=
    private static boolean isSymbol(char c) {
        if (isLetterAZ(c) || isDigit(c)) {
	  return false;
	}
	for (int i=0; i < TOKEN_NAMES.length; i++) {
	    // for all tokens length 2 or longer ("!=", ">=" etc), check if c is part of it
	    if ((TOKEN_NAMES[i].length() > 1) && (-1 != TOKEN_NAMES[i].indexOf(c))) {
		return true;
	    } // else keep searching
	}
	return false;
    }

    public static void test_isSymbol() {
	boolean pass = true;
	pass = pass && (isSymbol('z') == false);
	pass = pass && (isSymbol('!') == true);
	pass = pass && (isSymbol('<') == true);
	pass = pass && (isSymbol('>') == true);
	pass = pass && (isSymbol('=') == true);
	pass = pass && (isSymbol('i') == false);
	pass = pass && (isSymbol(' ') == false);
	if (pass) {
	    System.out.println("isSymbol: All tests pass");
	}
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

    private static boolean isDigit(char c) {
	return ('0' <= c && '9' >= c);
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
