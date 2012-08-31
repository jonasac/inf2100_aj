#!/bin/sh
if [ ! -e samples/gcd/gcd.cflat ]; then
  echo "Could not find gcd.cflat, aborting"
  exit 1
fi
if [ -e samples/gcd/gcd.log ]; then
  rm -f samples/gcd/gcd.log
fi
if [ ! -e Cflat.jar ]; then
  make
fi
java -jar $1 -testscanner samples/gcd/gcd.cflat
cat samples/gcd/gcd.log
if [ -e samples/gcd/gcd.s ]; then
  rm -f samples/gcd/gcd.s
fi
if [ -e samples/gcd/gcd ]; then
  rm -f samples/gcd/gcd
fi
