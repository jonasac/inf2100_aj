#!/usr/bin/python2

import glob
import subprocess

def main():
  our_compiler = "Cflat.jar"
  for filename in glob.glob('errortests/*/*.cflat'):
    comparison_file = filename.replace(".cflat", ".message")
    print("### " + filename + " " + "#" * 40)
    subprocess.call(["java", "-jar", our_compiler, filename])
    subprocess.call(['cat', comparison_file])
    print("#" * 45 + "\n")

if __name__ == "__main__":
  main()
