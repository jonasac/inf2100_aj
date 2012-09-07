// # vim: set ts=4 #

package no.uio.ifi.cflat.chargenerator;

/*
 * module CharGenerator
 */

import java.io.*;
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

	public static void init() {
		try {
			sourceFile = new LineNumberReader(new FileReader(Cflat.sourceName));
		} catch (FileNotFoundException e) {
			Error.error("Cannot read " + Cflat.sourceName + "!");
		}
		sourceLine = "";  sourcePos = 0;  curC = nextC = ' ';
		readNext();  readNext();
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

	private static void readOneLine() {
		try { 
			sourceLine = sourceFile.readLine();
			sourcePos = 0;
			System.out.println(sourceLine);
		} catch (IOException e) {
			// TODO: give a better error, use the error module
			System.out.println("CharGenerator: " + e.toString());
		}
	}

	/** isMoreToRead
	 * Checks if there is more content to read. Handles EOF as well.
	 *
	 * @returns true if there is another character available, false if not
	 */
	public static boolean isMoreToRead() {
		if (sourceLine == null) {
			return false;
		}
		while (sourceLine.startsWith("#") || sourceLine.isEmpty()) {
			readOneLine();
			// If end of line, return false
			if (sourceLine == null) {
				return false;
			}
		}
		return true;
	}

	public static int curLineNum() {
		return (sourceFile == null ? 0 : sourceFile.getLineNumber());
	}

	public static void readNext() {
		curC = nextC;
		if (!isMoreToRead()) return;

		// Must be changed in part 0:
		if (sourcePos < (sourceLine.length() - 1)) {
			sourcePos++;
		}
		assert(sourcePos <= sourceLine.length());
		nextC = sourceLine.charAt(sourcePos);
	}
}
