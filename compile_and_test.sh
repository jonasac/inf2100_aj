#!/bin/sh
make clean --quiet
make err.log
exec ./utils/test_run.sh
