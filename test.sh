#!/bin/sh
if [ -e samples/gdc/gdc.log ]; then
  rm -f samples/gdc/gdc.log
fi
if [ ! -e Cflat.jar ]; then
  make
fi
java -jar Cflat.jar -testscanner samples/gdc/gdc.cflat
cat samples/gdc/gdc.log
