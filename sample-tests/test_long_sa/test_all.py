import os
import subprocess
import sys
import re

print("TESTING ALL SAMPLES...")

# Gets all samples from the input folder
try:
    samples = os.listdir("./input")
except FileNotFoundError:
    sys.exit("[ERROR] Input missing")
    
# from http://blog.codinghorror.com/sorting-for-humans-natural-sort-order/
def nat_sort(l):
    convert = lambda text: int(text) if text.isdigit() else text.lower() 
    alphanum_key = lambda key: [ convert(c) for c in re.split('([0-9]+)', key) ] 
    return sorted(l, key = alphanum_key)
    
# Sorts samples naturally
samples = nat_sort(samples)
    
# Creates an output folder if it does not yet exist
if not os.path.exists("./output"):
    os.makedirs("./output")
    print("[INFO] Input folder created")

# Runs all samples through labeling program
for s in samples:
    algorithm = s.rstrip().split("-")[6][0:-4]
    command = "java -jar ../../dist/2IO90.jar --file=input/" + s + " --out --force-algorithm=" + algorithm
    print(command)
    subprocess.call(command)

extraction = []

# Extraction is started
for sample_name in samples:
    sample_file = open("./output/" + sample_name)
    line_data = ["" for _ in range(9)]

    # Retrieves distribution from file name
    distribution = sample_name.split("-")[5]
    line_data[4] = distribution

    # Retrieves relevant values from output file
    for line in sample_file:
        # Gets the value at the end of the line
        val = line.rstrip().split(" ")[-1]
        if line.startswith("placement model"):
            line_data[0] = val
        if line.startswith("width"):
            line_data[1] = val
        if line.startswith("height"):
            line_data[2] = val
        if line.startswith("number of points"):
            line_data[3] = val
        if line.startswith("Algorithm used"):
            line_data[5] = val
        if line.startswith("Number of labels placed"):
            line_data[6] = val
            line_data[7] = str(int(val) / int(line_data[3]))
        if line.startswith("Running time"):
            line_data[8] = val

    sample_file.close()

    # Appends one comma separated line to the extraction
    extraction.append(";".join(line_data))

csv_file = open("samples.csv", "w")
csv_file.write("Placement model;Width;Height;Number of points;Distribution;Algorithm;Labels placed;Quality;Running time(ns)\n")
csv_file.write("\n".join(extraction))
csv_file.close()
