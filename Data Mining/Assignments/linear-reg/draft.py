import numpy as np
from scipy import stats

N = 5

a = np.ones([N, N])
b = np.array([1, 2, 3, 4, 5])
# b = 2*np.ones([N])
a[:, 0] = b

# print a, "\n"

# print stats.zscore(a, ddof=0), "\n"
# print stats.zscore(a, ddof=1), "\n"
# print stats.zscore(a, ddof=2), "\n"
# print stats.zscore(a, ddof=3), "\n"
# print stats.zscore(a, ddof=4)

# print stats.zscore(a, axis=1, ddof=0), "\n"
# print stats.zscore(a, axis=1, ddof=1), "\n"
# print stats.zscore(a, axis=1, ddof=2), "\n"
# print stats.zscore(a, axis=1, ddof=3), "\n"
# print stats.zscore(a, axis=1, ddof=4)

# c = [[3, 1, 1, 1, 1],
#      [3, 3, 1, 1, 1],
#      [3, 3, 3, 1, 1],
#      [3, 3, 3, 3, 1],
#      [1, 2, 3, 4, 5]]

# print stats.zscore(np.array(c), axis=0)

#
# a = np.random.rand(N)
# print a
#
# b = np.zeros((N,N))
# print b
#
# b[:,0] = a
# print b

d = [1, 2, 3, 4, 5]
print d[1:3]