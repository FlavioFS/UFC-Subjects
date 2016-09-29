import math
import sys

class Univariate(object):
	"""Implements several metrics for Univariate datasets."""
	
	def __init__(self, filepath, column=0):
		"""(filepath, column=0) | Usage: univ = Univariate('iris-data.txt');"""
		with open(filepath) as inf:
			self.data = [];

			# Jump to first line of data and verifies column
			while True:
				line = inf.readline().split();
				try:
					done = float(line[column]);
					break;

				except Exception as ex:
					if line == []:
						print "This column does not contain numbers!\nClosing application!";
						sys.exit(0);
					else:
						maxcolumn = len(line) - 1;
						# Preventing invalid columns
						if maxcolumn < column:
							column = maxcolumn;
							print "Column out of range! Cropped to last column (%d)!" % column;
						elif column < 0:
							column = 0;
							print "Column out of range! Cropped to first column (%d)!" % column;

			inf.seek(0);
			for line in inf:
				try:
					self.data.append( float(line.split()[column]) );
				except Exception:
					continue;

	def count(self):
		"""() | Counts how many elements in dataset."""
		return len(self.data);

	def countElem(self, element):
		"""(element) | Counts how many occurrences of this element in dataset."""
		rv = 0;
		for x in self.data:
			if x == element:
				rv+=1;
		return rv;

	def min(self):
		"""() | The smallest value in dataset."""
		rv = self.data[0];
		for x in self.data:
			if x < rv:
				rv = x;
		return rv;

	def max(self):
		"""() | The largest value in dataset."""
		rv = self.data[0];
		for x in self.data:
			if x > rv:
				rv = x;
		return rv;

	def mean(self):
		"""() | The mean value for dataset."""
		rv = 0;
		for x in self.data:
			rv += x;
		return rv/self.count();

	def median(self):
		"""() | The element at the middle when dataset is sorted."""
		return sorted(self.data)[self.count()/2];

	def mode(self):
		"""() | The most frequent element in dataset."""
		myset = [];
		myindex = [];
		mycount = [];
		for i in xrange(self.count()):
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
		"""() | The element at the position of 1/4 whe dataset is sorted."""
		return sorted(self.data)[self.count()/4];

	def range(self):
		"""() | Every element in dataset is inside of this range."""
		return self.max() - self.min();

	def variance(self):
		"""() | Statistical 'variance' - Measure of dispersion."""
		mean = self.mean();
		rv = 0;
		for x in self.data:
			rv += (x - mean)*(x - mean);
		return rv / (self.count()-1);

	def sdeviation(self):
		"""() | Statistical 'standard deviation'."""
		return math.sqrt(self.variance());

	def varCoef(self):
		"""() | Statistical 'coefficient of variation' - Data dispersion divided by mean."""
		return 100 * self.sdeviation()/self.mean();

	def skewness(self):
		"""() | Statistical 'skewness' - Measure of symmetry."""
		n = self.count();
		mean = self.mean();
		S = self.sdeviation();
		rv = 0;
		for x in self.data:
			rv += (x - mean) ** 3;
		return rv * n / ( (n-1)*(n-2)*(S**3) );

	def kurtosis(self):
		"""() | Statistical 'variance' - Is the data peaked or flat relative to a normal distribution?"""
		n = self.count();
		mean = self.mean();
		S4 = self.sdeviation() ** 4;
		rv = 0;
		for x in self.data:
			rv += (x - mean) ** 4;
		return ( (rv * n*(n+1)/(S4*(n-1))) - (3 * (n-1)**2) ) / ((n-2)*(n-3));

	def showcase(self):
		"""() | Tests and display every function."""
		print "Count:      %g"   % self.count();
		print "Min:        %g"   % self.min();
		print "Max:        %g"   % self.max();
		print "Mean:       %g"   % self.mean();
		print "Median:     %g"   % self.median();
		print "Mode:       %g"   % self.mode();
		print "Quartile:   %g"   % self.quartile();
		print "Range:      %g"   % self.range();
		print "Variance:   %g"   % self.variance();
		print "Sdeviation: %g"   % self.sdeviation();
		print "DevCoef:    %g%%" % self.varCoef();
		print "Skewness:   %g"   % self.skewness();
		print "Kurtosis:   %g"   % self.kurtosis();