import os
import sys
import subprocess

print("GENERATING ALL SAMPLES...")

try:
    file = open("samples.txt")
except FileNotFoundError:
    sys.exit("[ERROR] File samples.txt not found")

# Creates an input folder if it does not exist
if not os.path.exists("./input"):
    os.makedirs("./input")
os.chdir("./input")

for line in file:
    if not line.isspace() and not line.startswith("#"):  # If line is not empty or is comment
        print(line.rstrip())
        subprocess.call("python ../../" + line.rstrip())
