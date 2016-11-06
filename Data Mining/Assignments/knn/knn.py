import numpy as np
import metrics
import distances as dist

class knn(object):
    # =========================================================================
    #   Constants
    # =========================================================================
    EUCLID2 = "euclid2"
    MANHATTAN = "manhattan"
    ANGULAR = "angular"


    # =========================================================================
    #   Constructor
    # =========================================================================
    def __init__(self, dataset):
        """Knn classifier algorithm. knn(dataset)"""
        self.dataset = dataset
        self.rows = len(self.dataset)
        self.columns = len(self.dataset[0])
        self.setAnswerColumn(self.columns-1)
        self.setDistanceFunction(self.EUCLID2)


    # =========================================================================
    #   Setters
    # =========================================================================
    def setAnswerColumn (self, answerColumn):
        """Set the answer column"""
        self.answerColumn = answerColumn
        self.setInputColumns(answerColumn)

    def setInputColumns (self, answerColumn):
        """Caches a copy of dataset without the answer column"""
        self.inputColumns = np.delete(self.dataset.copy(), answerColumn, axis=1)

    def setDistanceFunction (self, distanceFunctionCode):
        """Sets the metric used"""
        if (distanceFunctionCode == self.EUCLID2):
            self.distanceFunction = dist.distanceEuclid2
        elif (distanceFunctionCode == self.MANHATTAN):
            self.distanceFunction = dist.distanceManhattan
        else:
            self.distanceFunction = dist.distanceAngular


    # =========================================================================
    #   Getters
    # =========================================================================
    def getAnswer (self ,i):
        """Returns classifier, given the row index"""
        return self.dataset[i][self.answerColumn]


    # =========================================================================
    #   Prediction
    # =========================================================================
    def predict (self, pointSample, k=3):
        """Predicts the class of a given point"""
        if len(pointSample) != self.columns-1:
            raise NameError("[knn] Impossible to predict (different dimensions)")

        # Calculates array of distances to each point
        distances = np.empty([self.rows, 1])
        for i in xrange(self.rows):
            distances.put( [i], [self.distanceFunction(pointSample, self.inputColumns[i])] )

        # Creates an array of k nearest neighbors
        answers = np.empty([k, 1])
        for i in xrange(k):
            indexMin = distances.argmin()
            answers.put([i], self.getAnswer(indexMin))
            distances = np.delete(distances, indexMin)

        return metrics.mode(answers)

    def predictArray (self, pointSamples, k=3):
        """Predicts the classes of all points in a given array"""
        rv = np.empty([len(pointSamples), 1])
        for i in xrange(len(pointSamples)):
            rv.put([i], self.predict(pointSamples[i],k))

        return rv