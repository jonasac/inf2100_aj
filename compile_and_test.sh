#!/bin/sh
make clean --quiet
make err.log
exec ./test.sh
