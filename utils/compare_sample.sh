#!/bin/sh

# Find out which sample to use
source sample.conf

# And where it is
DIR=samples/$SAMPLE

# Build
make clean && make

# Create asm output with reference compiler
java -jar reference_compiler/Cflat.jar $DIR/$SAMPLE.cflat

# And save the asm output so it won't be overwritten
mv $DIR/$SAMPLE.s $DIR/ref_$SAMPLE.s

# Create asm output with our compiler
java -jar Cflat.jar $DIR/$SAMPLE.cflat

# Compare the two
vimdiff $DIR/$SAMPLE.s $DIR/ref_$SAMPLE.s
