#!/bin/sh
if [ -e samples/gcd/gcd.cflat ]; then
  echo "Could not find gdc.cflat, aborting"
  exit 1
fi
if [ -e samples/gcd/gcd.log ]; then
  rm -f samples/gcd/gcd.log
fi
if [ ! -e Cflat.jar ]; then
  make
fi
java -jar Cflat.jar -testscanner samples/gcd/gcd.cflat
cat samples/gcd/gcd.log
