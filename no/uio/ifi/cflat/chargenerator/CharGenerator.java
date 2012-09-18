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
    private static String lastLine;

    private static final char EOFCHAR = 0x4;

    public static void init() {
        try {
            sourceFile = new LineNumberReader(new FileReader(Cflat.sourceName));
        } catch (FileNotFoundException e) {
            Error.error("Cannot read " + Cflat.sourceName + "!");
        }
        sourceLine = "";  sourcePos = 0;  curC = nextC = ' ';
        readNext();  readNext();
    }

    public static void test_chargenerator() {
        init();
        while (isMoreToRead()) {
            readNext();
        }
        finish();
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
            Log.noteSourceLine(curLineNum(), sourceLine);
            sourceLine = sourceFile.readLine();
            sourcePos = 0;
        } catch (IOException e) {
            Error.error(e.toString());
        }
    }

    private static boolean isAtEndOfLine() {
        // does not look at length - 1 but length because this function checks if one is beyond the length of the line
        // (which means that one is done reading every single character with readNext)
        return (sourcePos == sourceLine.length());
    }

    /** isMoreToRead
     * Checks if there is more content to read. Handles EOF as well.
     * NB! Has the side-effect of reading in one line!
     *
     * @returns true if there is another character available, false if not
     */
    public static boolean isMoreToRead() {
        return (!(sourceLine == null && curC == EOFCHAR));
    }

    public static int curLineNum() {
        return (sourceFile == null ? 0 : sourceFile.getLineNumber());
    }

    public static void readNext() {
        // part 0
        curC = nextC;
        // Not really needed if the callees check with isMoreToRead
        if (!isMoreToRead()) return;
        
        while (sourceLine != null && (sourceLine.startsWith("#") || sourceLine.isEmpty() || isAtEndOfLine())) {
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

