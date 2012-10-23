#!/bin/sh
LOGNAME1=log_our_solution.txt
LOGNAME2=log_reference_solution.txt
rm -f "$LOGNAME1" "$LOGNAME2"
./utils/compile_and_test_part0.sh | grep Scanner > "$LOGNAME1"
./utils/test_with_reference_part0.sh | grep Scanner > "$LOGNAME2" || ./utils/test_with_reference_old_java.sh | grep Scanner > "$LOGNAME2"
vimdiff "$LOGNAME1" "$LOGNAME2"
rm -f "$LOGNAME1" "$LOGNAME2"
