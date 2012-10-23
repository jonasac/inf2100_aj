package no.uio.ifi.cflat.log;

/*
 * module Log
 */

import java.io.*;
import no.uio.ifi.cflat.cflat.Cflat;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.scanner.Scanner;
import static no.uio.ifi.cflat.scanner.Token.*;

/*
 * Produce logging information.
 */
public class Log {

    public static boolean
	doLogBinding = false,
	doLogParser  = false, 
	doLogScanner = false,
	doLogTree    = false;

    private static String logName, curTreeLine = "";
    private static int nLogLines = 0, parseLevel = 0, treeLevel = 0;
    private static String indent = "";

    public static void init() {
	logName = Cflat.sourceBaseName + ".log";
    }

    public static void finish() {
	//-- Must be changed in part 0:
    }

    private static void writeLogLine(String data) {
	try {
	    PrintWriter log = (nLogLines==0 ? new PrintWriter(logName) :
		    new PrintWriter(new FileOutputStream(logName,true)));
	    log.println(data);  ++nLogLines;
	    log.close();
	} catch (FileNotFoundException e) {
	    Error.error("Cannot open log file " + logName + "!");
	}
    }

    /**
     * Make a note in the log file that an error has occured.
     *
     * @param message  The error message
     */
    public static void noteError(String message) {
	if (nLogLines > 0) 
	    writeLogLine(message);
    }


    public static void enterParser(String symbol) {
	if (! doLogParser) return;

	indentTree();
	writeLogLine("Parser: "+ indent + symbol);
    }

    public static void leaveParser(String symbol) {
	if (! doLogParser) return;

	//-- Must be changed in part 1:
	writeLogLine("Parser: "+ indent + symbol);
	outdentTree();
    }

    /**
     * Make a note in the log file that another source line has been read.
     * This note is only made if the user has requested it.
     *
     * @param lineNum  The line number
     * @param line     The actual line
     */
    public static void noteSourceLine(int lineNum, String line) {
	if (! doLogParser && ! doLogScanner) return;
	writeLogLine(String.format("%4d: %s", lineNum, line));
    }

    /**
     * Make a note in the log file that another token has been read 
     * by the Scanner module into Scanner.nextNextToken.
     * This note will only be made if the user has requested it.
     */
    public static void noteToken() {
	if (! doLogScanner) return;
	String tokenstr = "Scanner:  " + Scanner.nextNextToken;
	if (Scanner.nextNextToken == nameToken) {
	    tokenstr += " " + Scanner.nextNextName;
	} else if (Scanner.nextNextToken == numberToken) {
	    tokenstr += " " + Scanner.nextNextNum;
	}
	writeLogLine(tokenstr);
    }

    public static void noteBinding(String name, int lineNum, int useLineNum) {
	if (! doLogBinding) return;
	// part 2
	writeLogLine("Binding: " + name + " " + lineNum + " " + useLineNum);
    }

    // !!! FOR DEBUGGING PART 2 !!!
    public static void w(String msg) {
	noteBinding(msg, 0, 0);
    }

    public static void wTree(String s) {
	if (curTreeLine.length() == 0) {
	    for (int i = 1;  i <= treeLevel;  ++i) curTreeLine += "  ";
	}
	curTreeLine += s;
    }

    public static void wTreeLn() {
	writeLogLine("Tree:     " + indent + curTreeLine);
	curTreeLine = "";
    }

    public static void wTreeLn(String s) {
	wTree(s);  wTreeLn();
    }

    public static void indentTree() {
	parseLevel++;
	indent += "  ";
    }

    public static void outdentTree() {
	parseLevel--;
	indent = indent.substring(0, indent.length() - 2);
    }
}

