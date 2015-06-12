SYSTEM REQUIREMENTS
Python 3, preferably with python in system PATH

USAGE
Create test sample:
	python samplegen.py <-pm placement model> <-w width> <-h height> <-n number of points> <-random>/<-easiest>/<-hardest>/<-clustered> [-m number of clusters] [-cd cluster density] [-s seed] <-sa>/<-eil3>
	
Examples:
	python samplegen.py -pm 4pos -w 3 -h 2 -n 100 -random
	python samplegen.py -pm 4pos -w 3 -h 2 -n 100 -clustered -m 5 -cd 0.40
	
Arguments:
	-pm is either 2pos, 4pos or 1slider
	-w is an integer larger than 0
	-h is an integer larger than 0
	-n is an integer larger than or equal to 5
	distribution is either -random, -easiest, -hardest or -clustered
    algorithm is either -sa or -eil3
	
	if distribution is -random or -clustered:
        [OPTIONAL default = 1234] -s is a integer larger than 1
    
    if distribution is -clustered:
		[OPTIONAL default = sqrt(n)] -m is an integer larger than or equal to 1
		[OPTIONAL default = 0.80] -cd is a float between 0 and 1
    

The file create_all_samples.bat creates all samples needed (works only on Windows).