#!/bin/sh
LOGNAME1=log_our_solution.txt
LOGNAME2=log_reference_solution.txt
rm -f "$LOGNAME1" "$LOGNAME2"
./compile_and_test.sh | grep Scanner > "$LOGNAME1"
./utils/test_with_reference.sh | grep Scanner > "$LOGNAME2" || ./utils/test_with_reference_old_java.sh | grep Scanner > "$LOGNAME2"
vimdiff "$LOGNAME1" "$LOGNAME2"
rm -f "$LOGNAME1" "$LOGNAME2"
