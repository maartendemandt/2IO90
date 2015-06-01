import os
import subprocess

print("EXTRACTING ALL SAMPLES...")

# Gets all samples from the output folder
samples = os.listdir("./output")

extraction = []

for sample in samples:
	sample_file = open("./output/" + sample)
	line_data = []
	for line in sample_file:
		if line.startswith(("placement model", "width", "height", "number of points", "number of labels", "The process took")):
			data = line.split(" ")
			line_data.append(data[-1:][0].rstrip())
	extraction.append(";".join(line_data))
	
csv_file = open("results.csv", "w")
csv_file.write("\n".join(extraction))