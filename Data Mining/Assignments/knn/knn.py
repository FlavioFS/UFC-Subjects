from numpy import matrix as np
import math

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
		self.setAnswerColumn(len(dataset[0]))
		self.setDistanceFunction(self.EUCLID2)


	# =========================================================================
	#   Setters
	# =========================================================================
	def setAnswerColumn (self, answerColumn):
		self.answerColumn = answerColumn

	def setInputColumns (self, answerColumn):
		if (answerColumn == 0):
			self.inputColumns = self.dataset[1:len(self.dataset[0]), 0:len(self.dataset)]
	
	def setDistanceFunction (self, distanceFunctionCode):
		if (distanceFunctionCode == self.EUCLID2):
			self.distanceFunction = self.distanceEuclid2
		elif (distanceFunctionCode == self.MANHATTAN):
			self.distanceFunction = self.distanceManhattan
		else:
			self.distanceFunction = self.distanceAngular


	# =========================================================================
	#   Distance Section
	# =========================================================================
	def distanceEuclid2 (self, point1, point2):
		rv = 0
		for i in xrange(len(point1)):
			rv += (point1[i] - point2[i])**2
		return rv

	def distanceManhattan (self, point1, point2):
		rv = 0
		for i in xrange(len(point1)):
			rv += math.fabs(point1[i] - point2[i])
		return rv

	"""A value in range [0,1]. 0 means same angle; 1 means opposite direction. 0.5 is perpendicular."""
	def distanceAngular (self, point1, point2):
		dot = 0	# Dot product
		n1 = 0		# Norm of point1
		n2 = 0		# Norm of point2
		for i in xrange(len(point1)):
			dot += point1[i]*point2[i]
			n1  += point1[i]*point1[i]
			n2  += point2[i]*point2[i]
		return ( 1 - (dot/(n1*n2)) )/2


	# =========================================================================
	#   Prediction
	# =========================================================================
	def predict (self, sample, k=3):
		nearestNeighbors = []
		
		# Starts nearest neighbors with some possible values
		for i in xrange(k):
			nearestNeighbors.append(self.dataset[i])

		for element in self.dataset:
			pass;