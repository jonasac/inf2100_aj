#!/bin/sh

if [ "$1" == "" ]; then
  echo "First argument must be a .jar file"
  exit 1
fi

# Read sample.conf to figure out which sample to use
# This should result in $SAMPLE being set
source ./sample.conf

E="samples/$SAMPLE/$SAMPLE"
f=$E.cflat
l=$E.log
s=$E.s

echo "Testing with: $f"

if [ ! -e "$f" ]; then
  echo "Could not find $f, aborting"
  exit 1
fi
if [ -e "$l" ]; then
  rm -f "$l"
fi
if [ ! -e "$1" ]; then
  make --quiet
  if [ ! -e "$1" ]; then
    echo "Unable to build $1\!"
    exit 2
  fi
fi
echo java -jar $1 -testparser "$f"
java -jar $1 -testparser "$f"
if [ -e "$l" ]; then
    cat "$l"
fi
if [ -e "$s" ]; then
  rm -f "$s"
fi
if [ -e "$E" ]; then
  rm -f "$E"
fi
