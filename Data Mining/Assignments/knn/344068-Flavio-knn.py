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
#                                   KNN Class
# ============================================================================

import numpy as np


class Knn(object):
    # =========================================================================
    #   Constants
    # =========================================================================
    EUCLID2 = "euclid2"
    MANHATTAN = "manhattan"
    ANGULAR = "angular"

    # =========================================================================
    #   Constructor
    # =========================================================================
    def __init__(self, trainingSet):
        """Knn classifier algorithm. knn(trainingSet)"""
        self.trainingSet = trainingSet
        self.rows = len(self.trainingSet)
        self.columns = len(self.trainingSet[0])
        self.setAnswerColumn(self.columns - 1)
        self.setDistanceFunction(self.EUCLID2)

    # =========================================================================
    #   Setters
    # =========================================================================
    def setAnswerColumn(self, answerColumn):
        """Set the answer column"""
        self.answerColumn = answerColumn
        self.setFeatureColumns(answerColumn)

    def setFeatureColumns(self, answerColumn):
        """Caches a copy of trainingSet without the answer column"""
        self.features = np.delete(self.trainingSet.copy(), answerColumn, axis=1)

    def setDistanceFunction(self, distanceFunctionCode):
        """Sets the metric used"""
        if self.EUCLID2 == distanceFunctionCode:
            self.distanceFunction = distanceEuclid2
        elif self.MANHATTAN == distanceFunctionCode:
            self.distanceFunction = distanceManhattan
        else:
            self.distanceFunction = distanceAngular

    # =========================================================================
    #   Getters
    # =========================================================================
    def getAnswer(self, i):
        """Returns classifier, given the row index"""
        return self.trainingSet[i][self.answerColumn]

    # =========================================================================
    #   Prediction
    # =========================================================================
    def fit(self, trainingFeatures, trainingAnswers):
        self.features = trainingFeatures
        self.answers = trainingAnswers

    def predict(self, pointSample, k=3):
        """Predicts the class of a given point"""
        if len(pointSample) != self.columns - 1:
            raise NameError("[knn] Impossible to predict (different dimensions)")

        # Calculates array of distances to each point
        distances = np.empty([self.rows, 1])
        for i in xrange(self.rows):
            distances.put([i], [self.distanceFunction(pointSample, self.features[i])])

        # Creates an array of k nearest neighbors
        answers = np.empty([k, 1])
        for i in xrange(k):
            indexMin = distances.argmin()
            answers.put([i], self.getAnswer(indexMin))
            distances = np.delete(distances, indexMin)

        return mode(answers)

    def predictArray(self, pointSamples, k=3):
        """Predicts the classes of all points in a given array"""
        rv = np.empty([len(pointSamples), 1])
        for i in xrange(len(pointSamples)):
            rv.put([i], self.predict(pointSamples[i], k))

        return rv

    # =========================================================================
    #   Analysis
    # =========================================================================
    def score(self, predictedValues, answers):
        """Compares two results"""
        if len(predictedValues) != len(answers):
            raise NameError("[knn] Impossible to generate score (different dimensions)")

        correct = 0
        for i in xrange(len(answers)):
            if (predictedValues[i] == answers[i]):
                correct += 1

        print "Correct: ", correct
        print "Total:   ", len(answers)
        print "Grade(%):", 100 * float(correct / len(answers))
        return float(correct / len(answers))


# --------------------------------------------------------------------------------------------


# ============================================================================
#                                   Usage
# ============================================================================

# Loading and Shuffling dataset
data = np.loadtxt("haberman.data", delimiter=",")
ndata = np.random.permutation(data)

# Separating features
size = len(ndata)
nt = int(math.floor(size * 0.7))

# Training already passed
# trfeatures = ndata[0:nt, 0:3]     # Features
# trlabels = ndata[0:nt, 3]         # Answers

# Prediction
ttfeatures = ndata[nt:size, 0:3]    # Features
ttlabels = ndata[nt:size, 3]        # Answers

# Creating learner
myKnn = Knn(ndata[0:nt, :])

# Euclidean distance
myKnn.setDistanceFunction(myKnn.EUCLID2)
predictions = myKnn.predictArray(ttfeatures, k=3)
myKnn.score(predictions, ttlabels)
print ""

# Manhattan distance
myKnn.setDistanceFunction(myKnn.MANHATTAN)
predictions = myKnn.predictArray(ttfeatures, k=3)
myKnn.score(predictions, ttlabels)
print ""

# Angular distance
myKnn.setDistanceFunction(myKnn.ANGULAR)
predictions = myKnn.predictArray(ttfeatures, k=3)
myKnn.score(predictions, ttlabels)
