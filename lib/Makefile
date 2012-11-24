all: libcflat.so.1.0.1

ificflatlib.o: ificflatlib.c
	gcc -m32 -c -fPIC ificflatlib.c -o ificflatlib.o

libcflat.so.1.0.1: ificflatlib.o
	gcc -m32 -shared -Wl,-soname,libcflat.so.1 -o libcflat.so.1.0.1 ificflatlib.o
	@#ln -s libcflat.so.1.0.1 libcflat.so.0
	@#ln -s libcflat.so.1.0.1 libcflat.so

clean:
	rm -f *.o *.so.* *.so
