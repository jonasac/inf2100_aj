package no.uio.ifi.cflat.scanner;

/*
 * class Token
 */

/*
 * The different kinds of tokens read by Scanner.
 */
public enum Token { 
    addToken, assignToken, 
    commaToken, 
    divideToken, doubleToken,
    elseToken, eofToken, equalToken, 
    forToken, 
    greaterEqualToken, greaterToken, 
    ifToken, intToken, 
    leftBracketToken, leftCurlToken, leftParToken, lessEqualToken, lessToken, 
    multiplyToken, 
    nameToken, notEqualToken, numberToken, 
    rightBracketToken, rightCurlToken, rightParToken, returnToken, 
    semicolonToken, subtractToken, 
    whileToken;

    // Rel operators
    private static final Token[] RELOPR = {equalToken, notEqualToken, lessToken, lessEqualToken, greaterToken, greaterEqualToken};
    private static final Token[] TERMOPR = {addToken, subtractToken};
    private static final Token[] FACTOROPR = {multiplyToken, divideToken};

    /*
     * Check if a given token exists in a given list of tokens
     *
     * @param tokens A list of tokens
     * @param t A token
     * @return True or false
     */
    private static boolean has(Token[] tokens, Token t) {
	for (Token cur : tokens) {
	    if (t.equals(cur)) {
		return true;
	    }
	}
	return false;
    }

    /*
     * Check if a given token exists in a given list of tokens
     *
     * @param tokens A list of tokens
     * @param t A token
     * @return True or false
     */
    public static boolean isFactorOperator(Token t) {
	return has(FACTOROPR, t);
    }

    /*
     * Checks if a given token is a term opr
     *
     * @param t A token
     * @return True or false
     */
    public static boolean isTermOperator(Token t) {
	return has(TERMOPR, t);
    }

    /*
     * Checks if a given token is a rel opr
     *
     * @param t A token
     * @return True or false
     */
    public static boolean isRelOperator(Token t) {
	return has(RELOPR, t);
    }

    public static boolean isOperand(Token t) {
	//-- Must be changed in part 0:
	return false;
    }

    public static boolean isTypeName(Token t) {
	return (t.equals(intToken) || t.equals(doubleToken));
    }
}
