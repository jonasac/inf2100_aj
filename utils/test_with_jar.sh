#!/bin/bash

if [ "$1" == '' ]; then
  echo 'First argument must be a .jar file'
  exit 1
fi

# Read sample.conf to figure out which sample to use
# This should result in $SAMPLE being set
source ./sample.conf

if [ "$2" == '' ]; then
  # Read params.conf to figure out which paramers to use
  # This should result in $PARAMS being set
  source ./params.conf
else
  # Use the second to fifth argument as PARAMS instead
  PARAMS="$2 $3 $4 $5"
fi

E="samples/$SAMPLE/$SAMPLE"
f=$E.cflat
l=$E.log
s=$E.s

echo
echo '===[ Testing ]========================================================'
echo
echo -e "  Sample:\t$f"
echo -e "  Parameters:\t$PARAMS"
echo -e "  Commandline:\tjava -jar $1 $PARAMS $f"
echo

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

java -jar $1 "$PARAMS" "$f"
if [ -e "$l" ]; then
    cat "$l"
fi
#if [ -e "$s" ]; then
#  rm -f "$s"
#fi
if [ -e "$E" ]; then
  rm -f "$E"
fi
