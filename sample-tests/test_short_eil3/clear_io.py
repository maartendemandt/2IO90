import os

# Removes all input
input = os.listdir("./input")
for i in input:
    os.remove("./input/" + i)

# Removes all output
output = os.listdir("./output")
for i in output:
    os.remove("./output/" + i)
