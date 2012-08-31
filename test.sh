#!/bin/sh
if [ -e samples/gcd/gcd.log ]; then
  rm -f samples/gcd/gcd.log
fi
if [ ! -e Cflat.jar ]; then
  make
fi
java -jar Cflat.jar -testscanner samples/gcd/gcd.cflat
cat samples/gcd/gcd.log
