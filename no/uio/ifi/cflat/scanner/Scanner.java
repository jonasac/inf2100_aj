
package no.uio.ifi.cflat.scanner;

/*
 * module Scanner
 */
import static no.uio.ifi.cflat.scanner.Token.eofToken;
import static no.uio.ifi.cflat.scanner.Token.nameToken;
import static no.uio.ifi.cflat.scanner.Token.numberToken;
import no.uio.ifi.cflat.chargenerator.CharGenerator;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.log.Log;


/**
 * Module for forming characters into tokens.
 */
public class Scanner {

    public static Token curToken, nextToken, nextNextToken;
    public static String curName, nextName, nextNextName;
    public static int curNum, nextNum, nextNextNum;
    public static int curLine, nextLine, nextNextLine;

    /**
     * This is the same order as the tokens in Token.java. The order is
     * significant!
     */
    private static final String[] TOKEN_NAMES = new String[] {
            "+", "=", ",", "/", "double", "else", "", "==", "for", ">=", ">", "if", "int", "[", "{", "(", "<=", "<", "*", "",
            "!=", "", "]", "}", ")", "return", ";", "-", "while" };

    /**
     * Initialize the scanner
     */
    public static void init() {
        curName = "";
        nextName = "";
        nextNextName = "";
        readNext();
        readNext();
        readNext();
    }

    /**
     * Cleanup
     */
    public static void finish() {
    }

    /**
     * Tries to find the token that corresponds to a given string
     * 
     * @param tokenstring
     *            string that may represent a token (like "int")
     * @return token if match, or null
     */
    public static Token string2token(String tokenstring) {
        if (0 != tokenstring.length()) {
            for (int i = 0; i < TOKEN_NAMES.length; i++) {
                if (TOKEN_NAMES[i].equals(tokenstring)) {
                    return Token.values()[i];
                }
            }
        }
        return null;
    }

    /**
     * Tries to find the string that corresponds to a given token
     * 
     * @param token
     *            a token, like intToken, for example
     * @return string if match, or null if not
     */
    public static String token2string(Token token) {
        for (int i = 0; i < TOKEN_NAMES.length; i++) {
            if (Token.values()[i] == token) {
                return TOKEN_NAMES[i];
            }
        }
        return null;
    }

    /**
     * Collects all characters that are letters, digits or underscore
     * characters, until a character that is not one of these is met. Both lower
     * and uppercase symbols are collected.
     * 
     * @return A string that only consists of letters, digits and underscore
     *         characters.
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
     * 
     * @return a string representing the number
     */
    private static String collectNumber() {
        String temp = "";
        if (CharGenerator.curC == '-') {
            temp += CharGenerator.curC;
            CharGenerator.readNext();
        }
        while (isDigit(CharGenerator.curC) || '\'' == CharGenerator.curC) {
            temp += CharGenerator.curC;
            CharGenerator.readNext();
        }
        return temp;
    }

    /**
     * Collects a symbol from CharGenerator
     * 
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
        String temp = Character.toString(CharGenerator.curC);
        while (0 == temp.trim().length()) { // if whitespace
            CharGenerator.readNext();
            temp = Character.toString(CharGenerator.curC);
            if (!CharGenerator.isMoreToRead()) {
                break; // in the case of trailing whitespace
            }
        }
    }

    /**
     * Converts a char to a string representing its ascii value
     * 
     * @param c
     *            to be converted
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
     * Skips the chargenerator forward as long as we are within a
     * multilinenumber NOTE: this might change the line number CharGenerator is
     * at so calls to CharGenerator.curLineNum() must happen after this
     */
    private static void skipPastMultilineComment() {
        if (!CharGenerator.isMoreToRead()) {
            return;
        }
        int ln = CharGenerator.curLineNum();
        if (CharGenerator.curC == '/' && CharGenerator.nextC == '*') {
            while (!(CharGenerator.curC == '*' && CharGenerator.nextC == '/')) {
                if (CharGenerator.curC == CharGenerator.EOFCHAR) {
                    Error.error("Comment starting on line " + ln + " never ends!");
                    System.exit(0);
                }
                CharGenerator.readNext();
            }
            CharGenerator.readNext();
            CharGenerator.readNext();
        }
    }

    private static boolean isMultiLineComment() {
        return (CharGenerator.curC == '/' && CharGenerator.nextC == '*');
    }

    private static boolean isWhiteSpace() {
        return (CharGenerator.curC == ' ' || CharGenerator.curC == '\t');
    }

    private static void prepareScanner() {
        boolean flag = false;
        while (!flag) {
            if (isWhiteSpace()) {
                skipToNonWhitespace();
            }
            if (isMultiLineComment()) {
                skipPastMultilineComment();
            }
            if (!isMultiLineComment() && !isWhiteSpace()) {
                flag = true;
            }
        }
    }

    private static int stringToInt(String s) {
        int i = -1;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            Error.error(nextNextLine, "Illegal character constant!");
        }
        return i;
    }

    /**
     * Fetch the next token
     */
    public static void readNext() {
        curToken = nextToken;
        nextToken = nextNextToken;
        curName = nextName;
        nextName = nextNextName;
        curNum = nextNum;
        nextNum = nextNextNum;
        curLine = nextLine;
        nextLine = nextNextLine;
        nextNextToken = null;

        while (nextNextToken == null) {
            prepareScanner();
            nextNextLine = CharGenerator.curLineNum();
            if (!CharGenerator.isMoreToRead()) {
                nextNextToken = eofToken;
            } else if (isLetterAZ(CharGenerator.curC)) {
                nextNextName = collectWord();
                nextNextToken = string2token(nextNextName);
                if (nextNextToken == null) {
                    nextNextToken = nameToken;
                }
            } else if (isDigit(CharGenerator.curC) || '\'' == CharGenerator.curC || CharGenerator.curC == '-'
                    && isDigit(CharGenerator.nextC)) {
                String tempnum = collectNumber();
                if (tempnum.equals("'")) {
                    nextNextToken = numberToken;
                    nextNextName = charToIntstring(CharGenerator.curC);
                    nextNextNum = stringToInt(nextNextName);
                    CharGenerator.readNext();
                    CharGenerator.readNext(); // read past the closing quote
                } else {
                    nextNextToken = numberToken;
                    nextNextName = tempnum;
                    nextNextNum = stringToInt(nextNextName);
                }
            } else if (isCompoundSymbol(CharGenerator.curC)) {
                nextNextName = collectSymbols();
                nextNextToken = string2token(nextNextName);
            } else if (isSymbol(CharGenerator.curC)) {
                nextNextName = Character.toString(CharGenerator.curC);
                nextNextToken = string2token(nextNextName);
                CharGenerator.readNext();
            } else {
                Error.error(nextNextLine, "Illegal symbol: '" + CharGenerator.curC + "'!");
            }
        }
        Log.noteToken();
    }

    /**
     * Checks if a given character could be part of a compound symbol (like >=
     * or * ==)
     * 
     * @param c
     *            character to check
     * @return If it could be part of a compound symbol or not
     */
    private static boolean isCompoundSymbol(char c) {
        if (!(isLetterAZ(c) || isDigit(c))) {
            for (int i = 0; i < TOKEN_NAMES.length; i++) {
                // for all tokens length 2 or longer ("!=", ">=" etc), check if
                // c is
                // part of it
                if ((TOKEN_NAMES[i].length() > 1) && (-1 != TOKEN_NAMES[i].indexOf(c))) {
                    return true;
                } // else keep searching
            }
        }
        return false;
    }

    private static boolean isSymbol(char c) {
        return (null != string2token(Character.toString(c)));
    }

    /**
     * A small test to see if isCompoundSymbol behaves as desired
     */
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

    /**
     * 
     * Checks if a character is a letter
     * 
     * @param c
     *            character to check
     * @return true if it is a-z or A-Z
     */
    private static boolean isLetterAZ(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * A small test to see if isLatterAZ behaves as desired
     */
    public static void test_isLetterAZ() {
        boolean pass = true;
        pass = pass && (isLetterAZ('!') == false);
        pass = pass && (isLetterAZ('a') == true);
        pass = pass && (isLetterAZ('Z') == true);
        if (pass) {
            System.out.println("isLetterAZ: All tests pass");
        }
    }

    /**
     * Checks if a given character is a digit
     * 
     * @param c
     *            character to check
     * @return If it represents a digit or not
     */
    private static boolean isDigit(char c) {
        return ('0' <= c && '9' >= c);
    }

    /**
     * Check if the given token is different from the current token
     * 
     * @param t
     */
    public static void check(Token t) {
        if (curToken != t) {
            Error.expected("A " + t);
        }
    }

    /**
     * Check if the given tokens are both different from the current token
     * 
     * @param t1
     * @param t2
     */
    public static void check(Token t1, Token t2) {
        if (curToken != t1 && curToken != t2) {
            Error.expected("A " + t1 + " or a " + t2);
        }
    }

    /**
     * Skip the given token
     * 
     * @param t
     */
    public static void skip(Token t) {
        check(t);
        readNext();
    }

    /**
     * Skip the given tokens
     * 
     * @param t1
     * @param t2
     */
    public static void skip(Token t1, Token t2) {
        check(t1, t2);
        readNext();
    }
}
