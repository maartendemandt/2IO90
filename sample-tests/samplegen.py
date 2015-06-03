import sys
from random import randint
from math import sqrt
from math import floor
from itertools import product

DEBUG = False

# Reads arguments
itr = iter(sys.argv)
while 1:
    try:
        next = itr.__next__()
    except StopIteration:
        break

    if next == "-pm":
        try:
            next = itr.__next__()
            if next == "2pos" or next == "4pos" or next == "1slider":
                model = next
            else:
                raise ValueError
        except:
            sys.exit("[ERROR] bad value for -pm (placement model), should be either 2pos, 4pos or 1slider")

    elif next == "-w":
        try:
            next = int(itr.__next__())
            if next > 0:
                width = next
            else:
                raise ValueError
        except:
            sys.exit("[ERROR] bad value for -w (width of labels), should be a natural number > 0")

    elif next == "-h":
        try:
            next = int(itr.__next__())
            if next > 0:
                height = next
            else:
                raise ValueError
        except:
            sys.exit("[ERROR] bad value for -h (height of labels), should be a natural number > 0")

    elif next == "-n":
        try:
            next = int(itr.__next__())
            if next >= 5:
                n = next
            else:
                raise ValueError
        except:
            sys.exit("[ERROR] bad value for -n (number of points), should be a natural number >= 5")

    elif next == "-random":
        distribution = "random"

    elif next == "-easiest":
        distribution = "easiest"

    elif next == "-hardest":
        distribution = "hardest"

    elif next == "-clustered":
        distribution = "clustered"

    elif next == "-m":
        try:
            next = int(itr.__next__())
            if next >= 1:
                m = next
            else:
                raise ValueError
        except:
            sys.exit("[ERROR] bad value for -m (number of clusters), should be a natural number >= 1")

    elif next == "-cd":
        try:
            next = float(itr.__next__())
            if 0 <= next <= 1:
                cd = next
            else:
                raise ValueError
        except:
            sys.exit("[ERROR] bad value for -cd (cluster density), should be 0 <= cd <= 1")

    elif next == "-sa":
        algorithm = "sa"

    elif next == "-eil3":
        algorithm = "eil3"

# Tests if all required arguments have been supplied
# If optional, sets them as default
try:
    model
except NameError:
    sys.exit("[ERROR] placement model (-pm) not defined")
try:
    width
except NameError:
    sys.exit("[ERROR] width of labels (-w) not defined")
try:
    height
except NameError:
    sys.exit("[ERROR] height of labels (-h) not defined")
try:
    n
except NameError:
    sys.exit("[ERROR] number of points (-n) not defined")
try:
    distribution
except NameError:
    sys.exit("[ERROR] distribution not defined")
if distribution == "clustered":
    try:
        m
    except NameError:
        print("[INFO] number of clusters (-m) not defined, taking sqrt(n) as default")
        m = floor(sqrt(n))
    try:
        cd
    except NameError:
        print("[INFO] cluster density (-cd) not defined, taking 0.80 as default")
        cd = 0.80
try:
    algorithm
except NameError:
    sys.exit("[ERROR] algorithm not defined")

# Adds input argument to output file
output = []
output.append("placement model: " + model)
output.append("width: " + str(width))
output.append("height: " + str(height))
output.append("number of points: " + str(n))

print("generating...")

if distribution == "random" or distribution == "clustered":
    # Creates a 2D-array that is used to make sure there are no duplicate points
    class Point:
        def __init__(self, x ,y):
            self.x = x
            self.y = y

    points = []


def append_point(x, y):
    output.append(str(x) + (5 - len(str(x))) * " " + str(y))

# Distributes points randomly
if distribution == "random":
    for i in range(n):
        while 1:
            x = randint(0, 9999)
            y = randint(0, 9999)
            # Makes sure there are no duplicate points
            for p in points:
                if x == p.x and y == p.y:
                    continue
            points.append(Point(x, y))
            append_point(x, y)
            break

# Distributes points so that there are no conflicts possible
elif distribution == "easiest":
    k = n  # points yet to be placed

    if model == "2pos" or model == "1slider":
        number_of_rows = floor(10000 / (height + 1))
        number_of_columns = floor(10000 / (2 * width + 1))
        for i, j in product(range(number_of_rows), range(number_of_columns)):
            if k > 0:
                y = i * (height + 1)
                x = width + j * (2 * width + 1)
                append_point(x, y)
                k -= 1
            else:
                break

    if model == "4pos":
        number_of_rows = floor(10000 / (2 * height + 1))
        number_of_columns = floor(10000 / (2 * width + 1))
        for i, j in product(range(number_of_rows), range(number_of_columns)):
            if k > 0:
                y = height + i * (2 * height + 1)
                x = width + j * (2 * width + 1)
                append_point(x, y)
                k -= 1
            else:
                break

# Distributes points so that there is exactly one solution in which each point is labeled
elif distribution == "hardest":
    k = n  # points yet to be placed

    if model == "2pos":
        number_of_rows = floor(10000 / (height + 1))
        number_of_columns = 2 * floor(10000 / (2 * width + 2))
        for i, j in product(range(number_of_rows), range(number_of_columns)):
            if k > 0:
                y = i * (height + 1)
                x = j * (width + 1) + (j + 1) % 2 * width
                append_point(x, y)
                k -= 1
            else:
                break

    elif model == "4pos":
        number_of_rows = 2 * floor(10000 / (2 * height + 1))
        number_of_columns = 2 * floor(10000 / (2 * width + 1))
        for i, j in product(range(number_of_rows), range(number_of_columns)):
            if k > 0:
                y = 1 + i * (height + 1) + (i + 1) % 2 * height
                x = j * (width + 1) + (j + 1) % 2 * width
                append_point(x, y)
                k -= 1
            else:
                break

    elif model == "1slider":
        number_of_rows = floor((10000 - 0.5 * height) / (height + 1))
        number_of_columns = floor((10000 - 0.5 * width) / (width + 1))
        for i, j in product(range(number_of_rows), range(number_of_columns)):
            if k > 0:
                y = i * (height + 1) + (j + 1) % 2 * floor(0.5 * height)
                x = width + j * (width + 1) + i % 2 * floor(0.5 * width)
                append_point(x, y)
                k -= 1
            else:
                break

# Distributes the points over m isolated clusters
elif distribution == "clustered":

    class Cluster:
        def __init__(self, new_j, new_x, new_y):
            self.j = new_j
            self.x = new_x
            self.y = new_y

        def distanceTo(self, c):
            return sqrt(pow(c.x - self.x, 2) + pow(c.y - self.y, 2))

    def isIsolated(c, min_distance):
        for d in clusters:
            if c.distanceTo(d) < min_distance:
                return False
        return True

    clusters = []
    radius = floor(0.5 * cd * sqrt(100000000 / m))
    if DEBUG:
        print("cluster radius = " + str(radius))
        print("cluster centers =")

    # Creates m clusters
    for j in range(0, m):
        while 1:
            cluster_x = randint(0, 9999)
            cluster_y = randint(0, 9999)
            new_cluster = Cluster(j, cluster_x, cluster_y)
            if isIsolated(new_cluster, 2 * radius):
                if DEBUG:
                    print("[" + str(new_cluster.j) + "]", new_cluster.x, new_cluster.y)
                clusters.append(new_cluster)

                # Creates points in each circular cluster
                for i in range(0, n // m + (n % m if j == 0 else 0)):
                    while 1:
                        local_x = randint(-radius, radius)
                        range_y = floor(sqrt(pow(radius, 2) - pow(local_x, 2)))
                        local_y = randint(-range_y, range_y)
                        x = cluster_x + local_x
                        y = cluster_y + local_y
                        # Makes sure potential location is within bounds
                        if not 0 <= x <= 10000 or not 0 <= y <= 10000:
                            continue
                        # Makes sure there are no duplicate points
                        for p in points:
                            if x == p.x and y == p.y:
                                continue
                        points.append(Point(x, y))
                        append_point(x, y)
                        break
                break

# Writes to file
file_name = "sample-" + model + "-w" + str(width) + "-h" + str(height) + "-n" + str(n) + "-" + distribution + "-" + algorithm + ".txt"
output_file = open(file_name, "w")
output_file.write("\n".join(output))

print("done")
