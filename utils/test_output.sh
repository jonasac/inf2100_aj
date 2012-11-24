#!/bin/bash

# If wanted, compare the assembly output
if [ "$1" == "asm" ]; then
  source sample.conf
  ASMNAME="samples/$SAMPLE/$SAMPLE.s"
fi

# Set the log filenames
LOGNAME1=log_our_solution.txt
LOGNAME2=log_reference_solution.txt

# Pre cleanup
rm -f "$LOGNAME1" "$LOGNAME2" "$ASMNAME" "$ASMNAME.reference"

# Run and log our code
./utils/compile_and_test.sh > "$LOGNAME1"

# If assembly output was produced, save a copy
if [ -e "$ASMNAME" ]; then
  cp "$ASMNAME" "$ASMNAME.reference"
fi

# Run and log the reference code
./utils/test_with_reference.sh > "$LOGNAME2" || ./utils/test_with_reference_old_java.sh > "$LOGNAME2"

# Compare the assembly output or the log output
if [ "$1" == "asm" ]; then
  vimdiff "$ASMNAME" "$ASMNAME.reference"
else
  vimdiff "$LOGNAME1" "$LOGNAME2"
fi

# Clean up
rm -f "$LOGNAME1" "$LOGNAME2" "$ASMNAME" "$ASMNAME.reference"
