import math

class Univariate(object):
	"""Implements several metrics for Univariate"""
	def __init__(self, data):
		self.data = data;

	def count(self):
		rv = 0;
		for x in self.data:
				rv+=1;
		return rv;

	def countElem(self, element):
		rv = 0;
		for x in self.data:
			if x == element:
				rv+=1;
		return rv;

	def min(self):
		rv = self.data[0];
		for x in self.data:
			if x < rv:
				rv = x;
		return rv;

	def max(self):
		rv = self.data[0];
		for x in self.data:
			if x > rv:
				rv = x;
		return rv;

	def mean(self):
		rv = self.data[0];
		for x in self.data:
			rv += x;
		return rv/len(self.data);

	def median(self):
		return sorted(self.data)[len(self.data)/2];

	def mode(self):
		myset = [];
		myindex = [];
		mycount = [];
		for i in xrange(len(self.data)):
			elem = self.data[i];
			if elem in myset:
				pos = myset.index(elem);
				mycount[pos] += 1;
			else:
				myset.append(self.data[i]);
				myindex.append(i);
				mycount.append(1);

		rv = myset[0];
		freq = mycount[0];
		for j in xrange(len(mycount)):
			if freq < mycount[j]:
				freq = mycount[0];
				rv = myset[j];

		return rv;

	def quartile(self):
		return sorted(self.data)[len(self.data)/4];

	def range(self):
		return self.max() - self.min();

	def variance(self):
		mn = self.mean();
		rv = 0;
		for x in self.data:
			rv += (x - mn)*(x - mn);

		return rv / (len(self.data)-1);

	def sdev(self):
		return math.sqrt(self.variance());

	def devCoef(self):
		return 100 * self.sdev()/self.mean();



################################## Debugging ##################################
mydata = [];
with open("iris-dataset.txt") as inf:
	inf.readline();
	for line in inf:
		mydata.append( float(line.split()[0]) );

mycl = Univariate(mydata);
print "count:    ", mycl.count();
print "min:      ", mycl.min();
print "max:      ", mycl.max();
print "mean:     ", mycl.mean();
print "median:   ", mycl.median();
print "mode:     ", mycl.mode();
print "quartile: ", mycl.quartile();
print "variance: ", mycl.variance();
print "sdev:     ", mycl.sdev();
print "devCoef:  ", mycl.devCoef();
