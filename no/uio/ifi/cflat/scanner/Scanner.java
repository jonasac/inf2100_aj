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
    }

    public static void finish() {
        //TODO: Figure out what must be changed here if there is anything at all
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
            System.out.println("error: symbol from unsupported character set: " + s.charAt(0));
        }
        return Integer.toString(numval);
    }


    public static void readNext() {

        curToken = nextToken;  nextToken = nextNextToken;
        curName = nextName;  nextName = nextNextName;
        curNum = nextNum;  nextNum = nextNextNum;
        curLine = nextLine;  nextLine = nextNextLine;

        // clear the nextNext variables
        //nextNextName = "";
        nextNextToken = null;
        int loopcounter = 0;
        while (nextNextToken == null) {
            nextNextLine = CharGenerator.curLineNum();

            if (!CharGenerator.isMoreToRead()) {
                nextNextToken = eofToken;
            } else {
                skipToNonWhitespace();

                // Speed ahead while in multiline comment
                if (CharGenerator.curC == '/' && CharGenerator.nextC == '*') {
                    while (! (CharGenerator.curC == '*' && CharGenerator.nextC == '/')) {
                        CharGenerator.readNext();
                    }
                    CharGenerator.readNext();
                    CharGenerator.readNext();
                    skipToNonWhitespace();
                }

                if (isLetterAZ(CharGenerator.curC)) {
                    nextNextName = collectWord();
                    nextNextToken = string2token(nextNextName);
                    if (nextNextToken == null) nextNextToken = nameToken;
                } else if (isDigit(CharGenerator.curC) || '-' == CharGenerator.curC || '\'' == CharGenerator.curC) {
                    nextNextName = collectNumber();
                    if (nextNextName.equals("-")) {
                        nextNextToken = subtractToken;
                    } else if (nextNextName.equals("'")) {
                        nextNextToken = numberToken;
                        nextNextName = charToIntstring(CharGenerator.curC);
                        CharGenerator.readNext();
                        if (CharGenerator.curC == '\'') {
                            CharGenerator.readNext();
                        }
                    } else {
                        nextNextToken = numberToken;
                    }
                } else if (isSymbol(CharGenerator.curC)) {
                    nextNextName = collectSymbols();
                    nextNextToken = string2token(nextNextName);
                    if (null == nextNextToken) {
                        System.out.println(nextNextName + ", what kind of symbol is that?");
                        //TODO: Give proper Error here
                    }
                } else {
                    nextNextName = Character.toString(CharGenerator.curC);
                    nextNextToken = string2token(nextNextName);
                    CharGenerator.readNext();
                }
                //TODO: Figure out what the hell this is supposed to do
                //Error.error(nextNextLine,
                //"Illegal symbol: '" + CharGenerator.curC + "'!");
            }
            Log.noteToken();
        }
    }

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
