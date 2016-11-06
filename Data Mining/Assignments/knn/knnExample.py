import numpy as np
import knn

data = np.loadtxt("haberman.DATA", delimiter=",")
myobj = knn.knn(data)
myobj.setDistanceFunction(myobj.MANHATTAN)
# print myobj.dataset
# print myobj.inputColumns
# print len(myobj.dataset)
# print len(myobj.dataset[0])
# print myobj.distanceFunction
print myobj.predict([1, 1, 1])