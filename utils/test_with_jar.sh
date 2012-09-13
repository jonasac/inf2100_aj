#!/bin/sh

E=samples/mini/mini

f=$E.cflat
l=$E.log
s=$E.s
if [ ! -e "$f" ]; then
  echo "Could not find $f, aborting"
  exit 1
fi
if [ -e "$l" ]; then
  rm -f "$l"
fi
if [ ! -e Cflat.jar ]; then
  make
fi
java -jar $1 -testscanner "$f"
if [ -e "$l" ]; then
    cat "$l"
fi
if [ -e "$s" ]; then
  rm -f "$s"
fi
if [ -e "$E" ]; then
  rm -f "$E"
fi
