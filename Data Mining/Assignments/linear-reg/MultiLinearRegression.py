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
from sklearn import linear_model

class MultiLinearRegression(object):
    # =========================================================================
    #   Constants
    # =========================================================================
    EUCLID2 = "euclid2"
    MANHATTAN = "manhattan"
    ANGULAR = "angular"
    PASSES = 100
    BASE_ALPHA = 0.1
    BASE_WEIGHT = 0.01

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
        self.updateCrossValidationSet()

    def setAnswerColumn(self, answerColumn):
        """Set the answer column"""
        self.answerColumn = answerColumn
        self.setFeatureColumns(answerColumn)

    def setFeatureColumns(self, answerColumn):
        """Caches a copy of trainingSet without the answer column"""
        # Prepends column of 1's
        self.features = np.empty([self.rows, self.columns])
        self.features[:, 1:] = np.delete(self.trainingSet.copy(), answerColumn, axis=1)
        self.features[0][0] = 1 # Prevents zscore from dividing by zero
        self.features = stats.zscore(self.features)
        self.features[:, 0] = np.ones([self.rows])

    def updateCrossValidationSet (self):
        splitPoint = 0.5*self.rows
        self.valFeatures = self.features[splitPoint:, :]
        self.valAnswers = self.trainingSet[splitPoint:, self.answerColumn]
        self.features = self.features[:splitPoint, :]

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
        lastValError = np.mean((self.predict(self.weights, self.valFeatures) - self.valAnswers) ** 2)
        for passIterator in xrange(self.PASSES):
            # Shuffles training set
            self.setTrainingSet(np.random.permutation(self.trainingSet))

            # Fits
            trainRows = len(self.features)

            for setIterator in xrange(trainRows):
                error = self.getAnswer(setIterator) - self.predict(self.weights, self.features[setIterator])
                newWeights = np.add(self.weights,
                        (self.BASE_ALPHA * trainRows/(trainRows+alphaAging)) * error * self.features[setIterator])

                # Validation
                newValError = np.mean((self.predict(newWeights, self.valFeatures) - self.valAnswers) ** 2)
                if (newValError <= lastValError):
                    self.weights = newWeights
                    lastValError = newValError
                    alphaAging += 1

            errorHistory[passIterator] = lastValError
            # errorHistory[passIterator] = np.mean((self.predict(self.weights, self.valFeatures) - self.valAnswers) ** 2)

        self.setTrainingSet(oldTrainingSet)
        return errorHistory

    def predict(self, weights, pointSamples):
        """Predicts the value of a given feature array"""
        return np.dot(pointSamples, weights)

    def naivePredict(self, weights, pointSamples):
        if (len(pointSamples[0]) < len(weights)):
            newSamples = np.empty([len(pointSamples), len(weights)])
            newSamples[:, 1:] = pointSamples
            newSamples[:, 0] = np.ones([len(pointSamples)])
            return self.predict(weights, newSamples)
        return self.predict(weights, pointSamples)

    # =========================================================================
    #   Analysis
    # =========================================================================
    def score(self, predictedValues, answers):
        """Compares two results"""
        if len(predictedValues) != len(answers):
            raise NameError("[knn] Impossible to generate score (different dimensions)")

        meanAbsoluteError = sum(abs(real - pred) for real, pred in zip(predictedValues, answers))[0]
        meanAbsoluteError /= len(answers)
        print "Error:    ", meanAbsoluteError
        return meanAbsoluteError


# --------------------------------------------------------------------------------------------


# ============================================================================
#                                   Usage
# ============================================================================

# Loading and Shuffling dataset
data = np.loadtxt("hour.csv", delimiter=",")
# data = data[:, 1:]

# Using only:
# holyday, weekday, workingday, weathersit, temp, atemp, hum, windspeed
trimedData = data[:, 6:15]
trimedData[:, 8] = data[:, 16]
data = trimedData
ndata = np.random.permutation(data)

# Separating features
ROW_COUNT = len(ndata)
COLUMN_COUNT = len(data[0])
nt = int(math.floor(ROW_COUNT * 0.01))
nf = int(math.floor(ROW_COUNT * 0.02))

# Prediction
ttfeatures = stats.zscore(ndata[nt:nf, 0:COLUMN_COUNT-1])  # Features
ttlabels = ndata[nt:nf, COLUMN_COUNT-1]                    # Answers


# Creating learner
regr = MultiLinearRegression(ndata[:nt, :])

# Euclidean distance
print "============================================================================"
print "  Euclidian Squared"
print "============================================================================"
regr.setDistanceFunction(regr.EUCLID2)
evolutionHistory = regr.fit()
print "Squared Mean Error Evolution History:"
print evolutionHistory
predictions = regr.naivePredict(regr.weights, ttfeatures)
# print "Absolute Error"
# regr.score(predictions, ttlabels)
# print ""
print('Coefficients: \n', regr.weights)
# The mean squared error
print("Mean squared error: %.2f"
      % np.mean((predictions - ttlabels) ** 2))
print ""

# Manhattan distance
print "============================================================================"
print "  Manhattan"
print "============================================================================"
regr.setDistanceFunction(regr.MANHATTAN)
print "Squared Mean Error Evolution History:"
evolutionHistory = regr.fit()
print evolutionHistory
predictions = regr.naivePredict(regr.weights, ttfeatures)
# print "Absolute Error"
# regr.score(predictions, ttlabels)
print ""
print('Coefficients: \n', regr.weights)
# The mean squared error
print("Mean squared error: %.2f"
      % np.mean((predictions - ttlabels) ** 2))
print ""

# Angular distance
print "============================================================================"
print "  Angular"
print "============================================================================"
regr.setDistanceFunction(regr.ANGULAR)
print "Squared Mean Error Evolution History:"
evolutionHistory = regr.fit()
print evolutionHistory
predictions = regr.naivePredict(regr.weights, ttfeatures)
# print "Absolute Error"
# regr.score(predictions, ttlabels)
# print ""
print('Coefficients: \n', regr.weights)
# The mean squared error
print("Mean squared error: %.2f"
      % np.mean((predictions - ttlabels) ** 2))
print ""


print "============================================================================"
print "  Scikit Learn"
print "============================================================================"
regr = linear_model.LinearRegression()
regr.fit(ndata[:nt, :COLUMN_COUNT-1], ndata[:nt, COLUMN_COUNT-1])
predictions = regr.predict(ttfeatures)
# The coefficients
print('Coefficients: \n', [regr.coef_ , regr.intercept_])
# The mean squared error
print("Mean squared error: %.2f"
      % np.mean((predictions - ttlabels) ** 2))
print ""