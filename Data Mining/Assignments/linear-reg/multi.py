###################################################################################################
# SAMPLE
###################################################################################################

print(__doc__)

# Code source: Jaques Grobler
# License: BSD 3 clause


import matplotlib.pyplot as plt
import numpy as np
from sklearn import datasets, linear_model

# Load the diabetes dataset
diabetes = datasets.load_diabetes()


# Use only one feature
diabetes_X = diabetes.data[:, np.newaxis, 2]

# Split the data into training/testing sets
diabetes_X_train = diabetes_X[:-20]
diabetes_X_test = diabetes_X[-20:]

# Split the targets into training/testing sets
diabetes_y_train = diabetes.target[:-20]
diabetes_y_test = diabetes.target[-20:]

# Create linear regression object
regr = linear_model.LinearRegression()

# Train the model using the training sets
regr.fit(diabetes_X_train, diabetes_y_train)

# The coefficients
print('Coefficients: \n', regr.coef_)
# The mean squared error
print("Mean squared error: %.2f"
      % np.mean((regr.predict(diabetes_X_test) - diabetes_y_test) ** 2))

# Plot outputs
plt.scatter(diabetes_X_test, diabetes_y_test,  color='black')
plt.plot(diabetes_X_test, regr.predict(diabetes_X_test), color='blue',
         linewidth=3)

plt.xticks(())
plt.yticks(())

plt.show()


###################################################################################################
# ASSIGNMENT 1 - SIMPLE REGRESSION
###################################################################################################
def predict (alpha, beta, xi):
    return float( beta * xi + alpha );

def error (alpha, beta, xi, yi):
    return float ( yi - predict(alpha, beta, xi) );

def sum_of_squared_errors (alpha, beta, x ,y):
    return float( sum(error(alpha, beta, xi, yi) ** 2 for xi, yi in zip(x,y)) );

def de_mean (y):
    rv = [];
    ymean = np.mean(y);
    for v in y:
        rv.append(float(v - ymean));
    return rv;

def total_sum_of_squares (y):
    return float( sum(v**2 for v in de_mean(y)) );

def r_squared (alpha, beta, x ,y):
    return float( 1.0 - (sum_of_squared_errors(alpha, beta, x, y) / total_sum_of_squares(y)) );

X = [];
Y = [];
with open("aerogerador.txt") as inf:    
    for line in inf:
        lsplit = line.split(",");
        X.append( float(lsplit[0]) );
        Y.append( float(lsplit[1]) );

# for a, b in zip(X, Y):
#     print "(", a, ", ", b, ")";
print "Diabetes:   ", r_squared(regr.intercept_, regr.coef_, diabetes_X_test, diabetes_y_test), "\n\n\n";


###################################################################################################
# ASSIGNMENT 2 - MULTI REGRESSION
###################################################################################################
def convertX (x, k):
    rv = np.zeros((len(x), k));
    for i in range(len(x)):
        for j in range(k):
            rv[i][j] = x[i] ** j;
    return rv;

def r_square_adj (alpha, beta, x ,y):
    return float( 1.0 - (sum_of_squared_errors(alpha, beta, x, y) * / total_sum_of_squares(y)) );

cvX = convertX(X, 3);
print cvX, "\n\n\n";