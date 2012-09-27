
package no.uio.ifi.cflat.chargenerator;

/*
 * module CharGenerator
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import no.uio.ifi.cflat.cflat.Cflat;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.log.Log;


/*
 * Module for reading single characters.
 */
public class CharGenerator {

	public static char curC, nextC;

	private static LineNumberReader sourceFile = null;
	private static String sourceLine;
	private static int sourcePos;

	private static final char EOFCHAR = 0x4;

	public static void init() {
		try {
			sourceFile = new LineNumberReader(new FileReader(Cflat.sourceName));
		} catch (FileNotFoundException e) {
			Error.error("Cannot read " + Cflat.sourceName + "!");
		}
		sourceLine = "";
		sourcePos = 0;
		curC = nextC = ' ';
		readNext();
		readNext();
	}

	public static void finish() {
		if (sourceFile != null) {
			try {
				sourceFile.close();
			} catch (IOException e) {
				Error.error("Could not close source file!");
			}
		}
	}

	/**
	 * reads one line from sourceFile, will reset sourcePos to null and log the
	 * new line
	 */
	private static void readOneLine() {
		try {
			Log.noteSourceLine(curLineNum(), sourceLine);
			sourceLine = sourceFile.readLine();
			sourcePos = 0;
		} catch (IOException e) {
			Error.error(e.toString());
		}
	}

	/**
	 * Checks if we are beyond the end of the current line
	 * 
	 * @returns true if we have read the whole sourceLine
	 */
	private static boolean isEOL() {
		/*
		 * Does not look at length - 1 but length because this function checks if
		 * one is beyond the length of the line (which means that we are done
		 * reading every single character with readNext)
		 */
		return (sourcePos == sourceLine.length());
	}

	/**
	 * Checks if there is more content to read. Handles EOF as well.
	 * 
	 * @returns true if there is another character available, false if not
	 */
	public static boolean isMoreToRead() {
		return (!(sourceLine == null && curC == EOFCHAR));
	}

	public static int curLineNum() {
		return (sourceFile == null ? 0 : sourceFile.getLineNumber());
	}

	/**
	 * reads the next char from sourceLine, will set the nextC variable to EOFCHAR
	 * if there are no more chars in the file
	 */
	public static void readNext() {
		curC = nextC;
		if (!isMoreToRead())
			return;

		while (sourceLine != null && (sourceLine.startsWith("#") || sourceLine.isEmpty() || isEOL())) {
			readOneLine();
		}

		if (sourceLine == null) {
			nextC = EOFCHAR;
			return;
		}

		nextC = sourceLine.charAt(sourcePos);
		sourcePos++;
	}
}
