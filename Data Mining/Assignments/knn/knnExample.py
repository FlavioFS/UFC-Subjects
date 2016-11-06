import numpy as np
import knn

data = np.loadtxt("haberman.DATA", delimiter=",")
myKnn = knn.knn(data)
myKnn.setDistanceFunction(myKnn.MANHATTAN)
print myKnn.predictArray(pointSamples=[[1, 1, 1], [1, 0, 0], [0, 1, 0], [69, 57, 1]], k=3)