package no.uio.ifi.cflat.error;

/*
 * module Error
 */

import no.uio.ifi.cflat.log.Log;
import no.uio.ifi.cflat.scanner.Scanner;

/*
 * Print error messages.
 */
public class Error {
    public static void error(String where, String message) {
	//-- Must be changed in part 0:

	System.exit(1);
    }
	
    public static void error(String message) {
	error("", message);
    }
	
    public static void error(int lineNum, String message) {
	error((lineNum>0 ? "in line "+lineNum : ""), message);
    }

    public static void panic(String where) {
	error("in method "+where, "PANIC! PROGRAMMING ERROR!");
    }

    public static void giveUsage() {
	System.err.println("Usage: cflat [-c] [-log{B|P|S|T}] " +
			   "[-test{scanner|parser}] file");
	System.exit(2);
    }

    public static void init() {
	//-- Must be changed in part 0:
    }
    public static void finish() {
	//-- Must be changed in part 0:
    }

    public static void expected(String exp) {
	error(Scanner.curLine, 
	      exp + " expected, but found a " + Scanner.curToken + "!");
    }
}
