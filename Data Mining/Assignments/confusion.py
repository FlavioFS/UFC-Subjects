import math

class Confusion (object):
	"""Class for Confusion Matrix and its metrics"""
	
	def __init__(self, TP, TN, FP, FN):
		self.TP = float(TP); # True positive
		self.TN = float(TN); # True negative
		self.FP = float(FP); # False positive
		self.FN = float(FN); # False negative
	
	def tpr (self):
		"""Sensitivity or True Positive Rate (TPR)"""
		return self.TP / (self.TP + self.FN);

	def tnr (self):
		"""Specificity or True Negative Rate (TNR)"""
		return self.TN / (self.FP + self.TN);

	def ppv (self):
		"""Precision or Positive Predictive Value (PPV)"""
		return self.TP / (self.TP + self.FP);

	def npv (self):
		"""Negative Predictive Value (NPV)"""
		return self.TN / (self.TN + self.FN);

	def fpr (self):
		"""Fall-out or False Positive Rate (FPR)"""
		return self.FP / (self.FP + self.TN);

	def fdr (self):
		"""False Discovery Rate (FDR)"""
		return self.FP / (self.FP + self.TP);

	def fnr (self):
		"""Miss Rate or False Negative Rate (FNR)"""
		return self.FN / (self.FN + self.TP);

	def acc (self):
		"""Accuracy (ACC)"""
		return (self.TP + self.TN) / (self.TP + self.FP);

	def f1 (self):
		"""F1 Score (F1)"""
		return 2*self.TP / (2*self.TP + self.FP + self.FN);

	def mcc (self):
		"""Matthews Correlation Coefficient (MCC)"""
		return (self.TP * self.TN) / math.sqrt( (self.TP + self.FP) * (self.TP + self.FN) * (self.TN + self.FP) * (self.TN + self.FN) );

	def showcase (self):
		"""Displays all values"""
		print "Sensitivity:                      %g" % self.tpr();
		print "Specificity:                      %g" % self.tnr();
		print "Precision:                        %g" % self.ppv();
		print "Negative Predictive Value:        %g" % self.npv();
		print "Fall-out:                         %g" % self.fpr();
		print "False Discovery Rate:             %g" % self.fdr();
		print "Miss Rate:                        %g" % self.fnr();
		print "Accuracy:                         %g" % self.acc();
		print "F1 Score:                         %g" % self.f1();
		print "Matthews Correlation Coefficient: %g" % self.mcc();

# Demo
TP, TN, FP, FN = 5, 9, 2, 3;
myconf = Confusion(TP, TN, FP, FN);
myconf.showcase();