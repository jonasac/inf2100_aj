#!/bin/sh
make clean
make
exec ./test.sh
