import os
import sys
import subprocess

print("GENERATING ALL SAMPLES...")

try:
    file = open("samples.txt")
except FileNotFoundError:
    sys.exit("[ERROR] File samples.txt not found")

os.chdir("./input")

for line in file:
    if not line.isspace() and not line.startswith("#"):  # If line is not empty or is comment
        print(line.rstrip())
        subprocess.call("python ../../" + line.rstrip())
