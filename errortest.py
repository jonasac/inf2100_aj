import glob
import subprocess
our_compiler = "Cflat.jar"
testfiles = glob.glob('errortests/*/*.cflat')

for file in testfiles:
  comparison_file = file.replace(".cflat", ".message")
  print file + "#########################################"
  subprocess.call(["java", "-jar", our_compiler, file])
  subprocess.call(['cat', comparison_file])
  print "################################################\n"
