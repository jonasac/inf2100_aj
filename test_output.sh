#!/bin/sh
./compile_and_test.sh | grep Scanner > log_our_solution.txt
./test_with_reference.sh || ./test_with_reference_old_java.sh | grep Scanner > log_reference_solution.txt
vimdiff log_our_solution.txt log_reference_solution.txt
