#!/bin/sh
SAMPLE=float-opers
DIR=samples/$SAMPLE
make clean && make
java -jar reference_compiler/Cflat.jar $DIR/$SAMPLE.cflat
mv $DIR/$SAMPLE.s $DIR/ref_$SAMPLE.s
java -jar Cflat.jar $DIR/$SAMPLE.cflat
vimdiff $DIR/$SAMPLE.s $DIR/ref_$SAMPLE.s
