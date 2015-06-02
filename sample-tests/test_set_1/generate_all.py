import os
import sys
import subprocess

print("GENERATING ALL SAMPLES...")

# Gets data on the samples to be created from the samples.csv file
try:
    sample_file = open("samples.csv")
except FileNotFoundError:
    sys.exit("[ERROR] File samples.csv not found")

# Creates an input folder if it does not yet exist
if not os.path.exists("./input"):
    os.makedirs("./input")
    print("[INFO] Output folder created")
os.chdir("./input")

# Creates a sample from each line
for line in sample_file.readlines()[1:]:
    params = line.rstrip().split(";")
    command = "samplegen.py"
    command += " -pm " + params[0]
    command += " -w " + params[1]
    command += " -h " + params[2]
    command += " -n " + params[3]
    command += " -" + params[4]
    command += " -" + params[5]
    print(command)
    subprocess.call("python ../../" + command)

sample_file.close()
