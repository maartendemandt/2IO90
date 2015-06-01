import os
import subprocess

print("TESTING ALL SAMPLES...")

# Gets all samples from the input folder
samples = os.listdir("./input")

# Creates an output folder if it does not exist
if not os.path.exists("./output"):
    os.makedirs("./output")

for s in samples:
    subprocess.call("java -jar ../../dist/2IO90.jar --file=input/" + s + " " + " --out --results-are-important --force-algorithm=sa")

#"\"C:\\Program Files\\Java\\jdk1.8.0_45\\bin\\java.exe\"