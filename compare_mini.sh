java -jar reference_compiler/Cflat.jar samples/mini/mini.cflat
cp samples/mini/mini.s ref_mini.s
./compile_and_test.sh && vimdiff samples/mini/mini.s ref_mini.s
