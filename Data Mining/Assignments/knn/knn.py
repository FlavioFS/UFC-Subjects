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
    """Knn classifier algorithm. knn(dataset)"""
    def __init__(self, dataset):
        self.dataset = dataset
        self.rows = len(self.dataset)
        self.columns = len(self.dataset[0])
        self.setAnswerColumn(self.columns-1)
        self.setDistanceFunction(self.EUCLID2)


    # =========================================================================
    #   Setters
    # =========================================================================
    """Set the answer column"""
    def setAnswerColumn (self, answerColumn):
        self.answerColumn = answerColumn
        self.setInputColumns(answerColumn)

    """Caches a copy of dataset without the answer column"""
    def setInputColumns (self, answerColumn):
        self.inputColumns = np.delete(self.dataset.copy(), answerColumn, axis=1)

    """Sets the metric used"""
    def setDistanceFunction (self, distanceFunctionCode):
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
        return self.dataset[i][self.answerColumn]


    # =========================================================================
    #   Prediction
    # =========================================================================
    def predict (self, pointSample, k=3):
        # Calculates array of distances to each point
        distances = np.empty([self.rows, 1])
        for i in xrange(self.rows):
            np.put(distances, [i], [self.distanceFunction(pointSample, self.inputColumns[i])] )

        # Creates an array of k nearest neighbors
        answers = np.empty([k, 1])
        for i in xrange(k):
            indexMin = distances.argmin()
            answers.put([i], self.getAnswer(indexMin))
            distances = np.delete(distances, indexMin)

        return metrics.mode(answers)