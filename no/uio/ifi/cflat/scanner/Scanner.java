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
    private static final String[] TOKEN_NAMES = new String[]{"+", "=", ",", "/",
        "double", "else", "", "==", "for", ">=", ">", "if", "int", "[", "{", "(", "<=", 
        "<", "*", "", "!=", "", "]", "}", ")", "return", ";", "-", "while"};

    public static void init() {
        //-- Must be changed in part 0:
        curName = "";
        nextName = "";
        nextNextName = "";
    }

    public static void finish() {
        //-- Must be changed in part 0:
    }

    /** 
     * Tries to find the token that corresponds to a given string
     *
     * @param tokenstring A string that may represent a token (like "int")
     * @return A token if match, or null
     */
    private static Token string2token(String tokenstring) {
        if (0 != tokenstring.length()) {
            for (int i=0; i < TOKEN_NAMES.length; i++) {
                if (TOKEN_NAMES[i].equals(tokenstring)) {
                    return Token.values()[i];
                }
            }
        }
        return null;
    }

    /** 
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

    /**
     * Collects a number from CharGenerator
     * @return a string representing the number
     */
    private static String collectNumber() {
        String temp = "";
        while (isDigit(CharGenerator.curC) || '-' == CharGenerator.curC || '\'' == CharGenerator.curC) {
            temp += CharGenerator.curC;
            CharGenerator.readNext();
        }
        return temp;
    }

    /**
     * Collects a symbol from CharGenerator
     * @return a string representing the symbol
     */
    private static String collectSymbols() {
        String temp = "";
        while (isCompoundSymbol(CharGenerator.curC)) {
            temp += CharGenerator.curC;
            CharGenerator.readNext();
        }
        return temp;
    }

    /**
     * Skips forward while CharGenerator.curC is a whitespace
     */
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

    /**
     * converts a char to a string representing its ascii value
     * @param the character to be converted
     * @return string representing the ascii value
     */
    public static String charToIntstring(char c) {
        byte numval = 0;
        String s = Character.toString(c);
        try {
            numval = s.getBytes("ISO-8859-1")[0];
        } catch (java.io.UnsupportedEncodingException e) {
            Error.error(nextNextLine, "Symbol from unsupported character set: " + s.charAt(0));
        }
        return Integer.toString(numval);
    }

    /**
     * Skips the chargenerator forward aslong as we are within a multilinenumber
     * NB! this might change the linenumber CharGenerator is at so calls to
     * CharGenerator.curLineNum() must happen after this
     */
    private static void skipPastMultilineComment() {
        if (!CharGenerator.isMoreToRead()) return;
        skipToNonWhitespace();
        if (CharGenerator.curC == '/' && CharGenerator.nextC == '*') {
            while (! (CharGenerator.curC == '*' && CharGenerator.nextC == '/')) {
                CharGenerator.readNext();
            }
            CharGenerator.readNext();
            CharGenerator.readNext();
            skipToNonWhitespace();
        }
    }

    public static void readNext() {

        curToken = nextToken;  nextToken = nextNextToken;
        curName = nextName;  nextName = nextNextName;
        curNum = nextNum;  nextNum = nextNextNum;
        curLine = nextLine;  nextLine = nextNextLine;
        nextNextToken = null;

        while (nextNextToken == null) {
            skipPastMultilineComment();
            nextNextLine = CharGenerator.curLineNum();
            if (!CharGenerator.isMoreToRead()) {
                nextNextToken = eofToken;
            } else if (isLetterAZ(CharGenerator.curC)) {
                nextNextName = collectWord();
                nextNextToken = string2token(nextNextName);
                if (nextNextToken == null) nextNextToken = nameToken;
            } else if (isDigit(CharGenerator.curC) || '\'' == CharGenerator.curC) {
                nextNextName = collectNumber();
                if (nextNextName.equals("'")) {
                    nextNextToken = numberToken;
                    nextNextName = charToIntstring(CharGenerator.curC);
                    CharGenerator.readNext();
                    CharGenerator.readNext(); // read past the closing quote
                } else nextNextToken = numberToken;
            } else {
                if (isCompoundSymbol(CharGenerator.curC)) {
                    nextNextName = collectSymbols();
                } else {
                    nextNextName = Character.toString(CharGenerator.curC);
                    CharGenerator.readNext();
                }
                nextNextToken = string2token(nextNextName);
                if (null == nextNextToken)
                    Error.error(nextNextLine,
                            "Illegal symbol: '" + CharGenerator.curC + "'!");
            }
        }
        Log.noteToken();
    }

    private static boolean isCompoundSymbol(char c) {
        if (!(isLetterAZ(c) || isDigit(c))) {
            for (int i=0; i < TOKEN_NAMES.length; i++) {
                // for all tokens length 2 or longer ("!=", ">=" etc), check if c is part of it
                if ((TOKEN_NAMES[i].length() > 1) && (-1 != TOKEN_NAMES[i].indexOf(c))) {
                    return true;
                } // else keep searching
            }
        }
        return false;
    }

    public static void test_isCompoundSymbol() {
        boolean pass = true;
        pass = pass && (isCompoundSymbol('z') == false);
        pass = pass && (isCompoundSymbol('!') == true);
        pass = pass && (isCompoundSymbol('<') == true);
        pass = pass && (isCompoundSymbol('>') == true);
        pass = pass && (isCompoundSymbol('=') == true);
        pass = pass && (isCompoundSymbol('i') == false);
        pass = pass && (isCompoundSymbol(' ') == false);
        if (pass) {
            System.out.println("isCompoundSymbol: All tests pass");
        }
    }

    /** isLetterAZ
     *
     * Checks if a character is a letter
     *
     * @param c The character to check
     * @return true if it is a-z or A-Z
     */
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
