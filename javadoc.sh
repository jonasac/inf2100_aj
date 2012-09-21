#!/bin/sh
dirname=javadoc
mkdir -p $dirname
javadoc no.uio.ifi.cflat.chargenerator -d $dirname/chargenerator
javadoc no.uio.ifi.cflat.scanner -d $dirname/scanner
javadoc no.uio.ifi.cflat.cflat -d $dirname/cflat
