
package no.uio.ifi.cflat.scanner;

/*
 * class Token
 */

/*
 * The different kinds of tokens read by Scanner.
 */
public enum Token {
	addToken, assignToken, commaToken, divideToken, doubleToken, elseToken, eofToken, equalToken, forToken, greaterEqualToken, greaterToken, ifToken, intToken, leftBracketToken, leftCurlToken, leftParToken, lessEqualToken, lessToken, multiplyToken, nameToken, notEqualToken, numberToken, rightBracketToken, rightCurlToken, rightParToken, returnToken, semicolonToken, subtractToken, whileToken;

	// Rel operators
	private static final Token[] RELOPR = {
			equalToken, notEqualToken, lessToken, lessEqualToken, greaterToken, greaterEqualToken };
	private static final Token[] TERMOPR = {
			addToken, subtractToken };
	private static final Token[] FACTOROPR = {
			multiplyToken, divideToken };
	private static final Token[] OPR = {
		numberToken };

	/**
	 * Check if a given token exists in a given list of tokens
	 * 
	 * @param tokens
	 *          list of tokens
	 * @param t
	 *          token
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

	/**
	 * Check if a given token exists in a given list of tokens
	 * 
	 * @param tokens
	 *          list of tokens
	 * @param t
	 *          token
	 * @return True or false
	 */
	public static boolean isFactorOperator(Token t) {
		return (t != null) && has(FACTOROPR, t);
	}

	/**
	 * Checks if a given token is a term opr
	 * 
	 * @param t
	 *          token
	 * @return True or false
	 */
	public static boolean isTermOperator(Token t) {
		return (t != null) && has(TERMOPR, t);
	}

	/**
	 * Checks if a given token is a rel opr
	 * 
	 * @param t
	 *          token
	 * @return True or false
	 */
	public static boolean isRelOperator(Token t) {
		return (t != null) && has(RELOPR, t);
	}

	/**
	 * Checks if a given token is an opr
	 * 
	 * @param t
	 *          token
	 * @return True or false
	 */
	public static boolean isOperand(Token t) {
		return (t != null) && has(OPR, t);
	}

	/**
	 * Checks if a given token is a type name
	 * 
	 * @param t
	 *          token
	 * @return True or false
	 */
	public static boolean isTypeName(Token t) {
		return (t != null) && (t.equals(intToken) || t.equals(doubleToken));
	}
}
