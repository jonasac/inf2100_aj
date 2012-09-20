#!/bin/sh
LOGNAME1=log_our_solution.txt
LOGNAME2=log_reference_solution.txt
./compile_and_test.sh | grep Scanner > "$LOGNAME1"
./utils/test_with_reference.sh | grep Scanner > "$LOGNAME2"
if [ ! -e "$LOGNAME2" ]; then
  ./utils/test_with_reference_old_java.sh | grep Scanner > "$LOGNAME2"
fi
vimdiff "$LOGNAME1" "$LOGNAME2"
rm -f "$LOGNAME1" "$LOGNAME2"
