#!/bin/sh
LOGNAME1=log_our_solution.txt
LOGNAME2=log_reference_solution.txt
rm -f "$LOGNAME1" "$LOGNAME2"
./utils/compile_and_test.sh > "$LOGNAME1"
./utils/test_with_reference.sh > "$LOGNAME2" || ./utils/test_with_reference_old_java.sh > "$LOGNAME2"
vimdiff "$LOGNAME1" "$LOGNAME2"
rm -f "$LOGNAME1" "$LOGNAME2"
