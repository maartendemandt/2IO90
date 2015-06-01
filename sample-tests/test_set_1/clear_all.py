import os

# Removes all input test samples
input = os.listdir("./input")
for _ in input:
	os.remove("./input/" + i)
	
# Removes all output
output = os.listdir("./output")
for _ in output:
	os.remove("./output/" + i)

# Clears results file
results_file = open("results.csv", "w")
results_file.write("")