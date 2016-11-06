import math

def distanceEuclid2(point1, point2):
    """Squared Euclidean distance"""
    rv = 0
    for i in xrange(len(point1)):
        rv += (point1[i] - point2[i]) ** 2
    return rv

def distanceManhattan(point1, point2):
    """Manhattan distance between two points"""
    rv = 0
    for i in xrange(len(point1)):
        rv += math.fabs(point1[i] - point2[i])
    return rv

def distanceAngular(point1, point2):
    """
    A value in range [0,1].
    0 means same angle;
    1 means opposite direction.
    0.5 is perpendicular.
    Formula: 0.5 * ( 1 - sign(cos(t)) * cos(t)**2 )
    """
    dot = 0  # Dot product
    n1 = 0  # Norm of point1 (squared)
    n2 = 0  # Norm of point2 (squared)
    for i in xrange(len(point1)):
        dot += point1[i] * point2[i]
        n1 += point1[i] * point1[i]
        n2 += point2[i] * point2[i]

    if (dot == 0):
        return 0.5
    else:
        return (1 - (math.copysign(dot * dot, dot) / (n1 * n2))) / 2