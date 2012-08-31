#!/bin/sh
if [ -e samples/gdc/gdc.log ]; then
  rm -f samples/gdc/gdc.log
fi
java -jar Cflat.jar -testscanner samples/gdc/gdc.cflat
cat samples/gdc/gdc.log
