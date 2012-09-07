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
			Log.noteSourceLine(curLineNum(), sourceLine);
		} catch (IOException e) {
			Error.error(e.toString());
		}
	}

	private static boolean isAtEndOfLine() {
		 return (sourcePos == (sourceLine.length() - 1));
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
		while (sourceLine.startsWith("#") || sourceLine.isEmpty() || isAtEndOfLine()) {
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
		// part 0
		curC = nextC;

		if (!isMoreToRead()) return;

		nextC = sourceLine.charAt(sourcePos);
		sourcePos++;
	}
}
