# ============================================================================
#                               Metric Utils
# ============================================================================

from __future__ import division
import math


def count(dataset):
    """() | Counts how many elements in dataset."""
    return len(dataset)


def mean(dataset):
    """() | The mean value for dataset."""
    rv = 0
    for x in dataset:
        rv += x
    return rv / count(dataset)


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


# --------------------------------------------------------------------------------------------


# ============================================================================
#                               Distance Utils
# ============================================================================
def distanceEuclid2(point1, point2):
    """Squared Euclidean distance"""
    rv = 0
    for i in xrange(len(point1)):
        rv += (point1[i] - point2[i]) ** 2
    return rv


def distanceManhattan(point1, point2):
    """Manhattan distance between two points"""
    rv = 0
    for i in xrange(len(point1)):
        rv += math.fabs(point1[i] - point2[i])
    return rv


def distanceAngular(point1, point2):
    """
    A value in range [0,1].
    0 means same angle;
    1 means opposite direction.
    0.5 is perpendicular.
    Formula: 0.5 * ( 1 - sign(cos(t)) * cos(t)**2 )
    """
    dot = 0  # Dot product
    n1 = 0  # Norm of point1 (squared)
    n2 = 0  # Norm of point2 (squared)
    for i in xrange(len(point1)):
        dot += point1[i] * point2[i]
        n1 += point1[i] * point1[i]
        n2 += point2[i] * point2[i]

    if 0 == dot:
        return 0.5
    else:
        return (1 - (math.copysign(dot * dot, dot) / (n1 * n2))) / 2


# --------------------------------------------------------------------------------------------


# ============================================================================
#                            Linear Regression Class
# ============================================================================

import numpy as np
from scipy import stats

class MultiLinearRegression(object):
    # =========================================================================
    #   Constants
    # =========================================================================
    EUCLID2 = "euclid2"
    MANHATTAN = "manhattan"
    ANGULAR = "angular"
    PASSES = 100
    BASE_ALPHA = 0.01
    BASE_WEIGHT = 0.1

    # =========================================================================
    #   Constructor
    # =========================================================================
    def __init__(self, trainingSet):
        """Knn classifier algorithm. knn(trainingSet)"""
        self.setTrainingSet(trainingSet)
        self.setDistanceFunction(self.EUCLID2)
        self.resetWeights(self.BASE_WEIGHT)

    # =========================================================================
    #   Setters
    # =========================================================================
    def setTrainingSet (self, trainingSet):
        self.trainingSet = trainingSet
        self.rows = len(self.trainingSet)
        self.columns = len(self.trainingSet[0])
        self.setAnswerColumn(self.columns - 1)

    def setAnswerColumn(self, answerColumn):
        """Set the answer column"""
        self.answerColumn = answerColumn
        self.setFeatureColumns(answerColumn)

    def setFeatureColumns(self, answerColumn):
        """Caches a copy of trainingSet without the answer column"""
        # Prepends column of 1's
        self.features = np.empty([self.rows, self.columns])
        self.features[:, 1:] = np.delete(self.trainingSet.copy(), answerColumn, axis=1)
        self.features = stats.zscore(self.features)
        self.features[:, 0] = np.ones([self.rows])

    def setDistanceFunction(self, distanceFunctionCode):
        """Sets the metric used"""
        if self.EUCLID2 == distanceFunctionCode:
            self.distanceFunction = distanceEuclid2
        elif self.MANHATTAN == distanceFunctionCode:
            self.distanceFunction = distanceManhattan
        else:
            self.distanceFunction = distanceAngular

    def resetWeights (self, value):
        """Reset weights to the given argument"""
        self.weights = np.ones(self.columns) * value

    # =========================================================================
    #   Getters
    # =========================================================================
    def getAnswer(self, i):
        """Returns classifier, given the row index"""
        return self.trainingSet[i][self.answerColumn]

    # =========================================================================
    #   Prediction
    # =========================================================================
    def fit(self):
        self.resetWeights(self.BASE_WEIGHT)
        oldTrainingSet = self.trainingSet.copy()    # Backup

        errorHistory = np.empty([self.PASSES])
        alphaAging = 1
        for passIterator in xrange(self.PASSES):
            # Shuffles training set
            self.setTrainingSet(np.random.permutation(self.trainingSet))

            # Fits
            meanSquaredError = 0
            for setIterator in xrange(self.rows):
                error = self.getAnswer(setIterator) - self.predict(self.features[setIterator])
                self.weights = np.add(self.weights, (self.BASE_ALPHA * self.rows/(self.rows+alphaAging)) * error * self.features[setIterator])
                meanSquaredError += error**2
                alphaAging += 1

            meanSquaredError /= self.rows
            errorHistory[passIterator] = meanSquaredError

        self.setTrainingSet(oldTrainingSet)
        return errorHistory

    def predict(self, pointSample):
        """Predicts the value of a given feature array"""
        return self.weights.dot(pointSample)

    def predictArray(self, pointSamples):
        """Predicts the values of all feature arrays in a given feature matrix"""
        rv = np.empty([len(pointSamples), 1])

        # Prepends column of 1's
        treatedSamples = np.empty([len(pointSamples), self.columns])
        treatedSamples[:, 1:] = pointSamples
        treatedSamples[:, 0] = np.ones([len(pointSamples)])

        for i in xrange(len(treatedSamples)):
            rv.put([i], self.predict(treatedSamples[i]))

        return rv

    # =========================================================================
    #   Analysis
    # =========================================================================
    def score(self, predictedValues, answers):
        """Compares two results"""
        if len(predictedValues) != len(answers):
            raise NameError("[knn] Impossible to generate score (different dimensions)")

        meanAbsoluteError = sum(abs(real - pred) for real, pred in zip(predictedValues, answers))[0]
        meanAbsoluteError /= len(answers)
        total = sum(ans for ans in answers)
        print "Error:    ", meanAbsoluteError
        print "Error(%): ", 100*meanAbsoluteError/total
        return meanAbsoluteError


# --------------------------------------------------------------------------------------------


# ============================================================================
#                                   Usage
# ============================================================================

# Loading and Shuffling dataset
data = np.loadtxt("hour.csv", delimiter=",")
# data = data[:, 1:]

# Using only:
# weekday, workingday, weathersit, temp
trimedData = data[:, 7:12]
trimedData[:, 4] = data[:, 16]
data = trimedData
ndata = np.random.permutation(data)

# Separating features
ROW_COUNT = len(ndata)
COLUMN_COUNT = len(data[0])
nt = int(math.floor(ROW_COUNT * 0.01))
nf = int(math.floor(ROW_COUNT * 0.02))

# Prediction
ttfeatures = ndata[nt:nf, 0:COLUMN_COUNT-1]  # Features
ttlabels = ndata[nt:nf, COLUMN_COUNT-1]      # Answers


# Creating learner
myKnn = MultiLinearRegression(ndata[nt:nf, :])

# Euclidean distance
print "============================================================================"
print "  Euclidian Squared"
print "============================================================================"
myKnn.setDistanceFunction(myKnn.EUCLID2)
evolutionHistory = myKnn.fit()
print "Squared Mean Error Evolution History:"
print evolutionHistory
predictions = myKnn.predictArray(ttfeatures)
print "Absolute Error"
myKnn.score(predictions, ttlabels)
print ""

# Manhattan distance
print "============================================================================"
print "  Manhattan"
print "============================================================================"
myKnn.setDistanceFunction(myKnn.MANHATTAN)
print "Squared Mean Error Evolution History:"
evolutionHistory = myKnn.fit()
print evolutionHistory
predictions = myKnn.predictArray(ttfeatures)
print "Absolute Error"
myKnn.score(predictions, ttlabels)
print ""

# Angular distance
print "============================================================================"
print "  Angular"
print "============================================================================"
myKnn.setDistanceFunction(myKnn.ANGULAR)
print "Squared Mean Error Evolution History:"
evolutionHistory = myKnn.fit()
print evolutionHistory
predictions = myKnn.predictArray(ttfeatures)
print "Absolute Error"
myKnn.score(predictions, ttlabels)