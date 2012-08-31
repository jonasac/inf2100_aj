BRUKER = $(USER)

Cflat.jar: ALWAYS
	(cd no/uio/ifi/cflat/cflat; make)
	jar cmf manifest.txt Cflat.jar no/uio/ifi/cflat/*/*.class

delivery: ALWAYS
	tar cf 'INF2100-$(BRUKER).tar' no

clean:
	rm -f *.jar *~
	for D in no/uio/ifi/cflat/*; do (cd $$D; make clean); done

ALWAYS:
