import math


def count(dataset):
    """() | Counts how many elements in dataset."""
    return len(dataset)

def countElem(dataset, element):
    """(element) | Counts how many occurrences of this element in dataset."""
    rv = 0
    for x in dataset:
        if x == element:
            rv+=1
    return rv

def min(dataset):
    """() | The smallest value in dataset."""
    rv = dataset[0]
    for x in dataset:
        if x < rv:
            rv = x
    return rv

def max(dataset):
    """() | The largest value in dataset."""
    rv = dataset[0]
    for x in dataset:
        if x > rv:
            rv = x
    return rv

def mean(dataset):
    """() | The mean value for dataset."""
    rv = 0
    for x in dataset:
        rv += x
    return rv/count(dataset)

def median(dataset):
    """() | The element at the middle when dataset is sorted."""
    return sorted(dataset)[count(dataset)/2]

def mode(dataset):
    """() | The most frequent element in dataset."""
    myset = []
    myindex = []
    mycount = []
    for i in xrange(count(dataset)):
        elem = dataset[i]
        if elem in myset:
            pos = myset.index(elem)
            mycount[pos] += 1
        else:
            myset.append(dataset[i])
            myindex.append(i)
            mycount.append(1)

    rv = myset[0]
    freq = mycount[0]
    for j in xrange(len(mycount)):
        if freq < mycount[j]:
            freq = mycount[0]
            rv = myset[j]
    return rv

def quartile(dataset):
    """() | The element at the position of 1/4 whe dataset is sorted."""
    return sorted(dataset)[count(dataset)/4]

def range(dataset):
    """() | Every element in dataset is inside of this range."""
    return max(dataset) - min(dataset)

def variance(dataset):
    """() | Statistical 'variance' - Measure of dispersion."""
    meanv = mean(dataset)
    rv = 0
    for x in dataset:
        rv += (x - meanv)*(x - meanv)
    return rv / (count(dataset)-1)

def sdeviation(dataset):
    """() | Statistical 'standard deviation'."""
    return math.sqrt(variance(dataset))

def varCoef(dataset):
    """() | Statistical 'coefficient of variation' - Data dispersion divided by mean."""
    return 100 * sdeviation(dataset)/mean(dataset)

def skewness(dataset):
    """() | Statistical 'skewness' - Measure of symmetry."""
    n = count(dataset)
    meanv = mean(dataset)
    S = sdeviation(dataset)
    rv = 0
    for x in dataset:
        rv += (x - meanv) ** 3
    return rv * n / ( (n-1)*(n-2)*(S**3) )

def kurtosis(dataset):
    """() | Statistical 'variance' - Is the data peaked or flat relative to a normal distribution?"""
    n = count(dataset)
    meanv = mean(dataset)
    S4 = sdeviation(dataset) ** 4
    rv = 0
    for x in dataset:
        rv += (x - meanv) ** 4
    return ( (rv * n*(n+1)/(S4*(n-1))) - (3 * (n-1)**2) ) / ((n-2)*(n-3))