import os
import subprocess

print("TESTING ALL SAMPLES...")

# Gets all samples from the input folder
samples = os.listdir("./input")

for s in samples:
    subprocess.call("java -jar ../../dist/2IO90.jar --file=input/" + s + " " + " --out --image --results-are-important --force-algorithm=sa")
