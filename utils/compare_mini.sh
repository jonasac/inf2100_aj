#!/bin/sh
SAMPLE=mini
DIR=samples/$SAMPLE
java -jar reference_compiler/Cflat.jar $DIR/$SAMPLE.cflat
cp $DIR/$SAMPLE.s $DIR/ref_$SAMPLE.s
./compile_and_test.sh && vimdiff $DIR/$SAMPLE.s $DIR/ref_$SAMPLE.s
