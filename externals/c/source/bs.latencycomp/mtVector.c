/**
 * Some basic vector calculations.
 * All operations are prefixed with 'mt' to avoid name clashes and get an
 * attempt for a unique prefix.
 *
 * @author Maurice Tollmien, maybites
 */

#include <stdio.h>
#include <math.h>

#include "mtVector.h"

/**
 * Prints a vector to stdout as: [0.1/0.2/0.3]
 */
void mtPrintVector (MTVec3D a)
{
    int i;
    printf("[%.1f/%.1f/%.1f]\n", a.x, a.y, a.z);
}

/**
 * Creates a new vector from three given values.
 */
MTVec3D mtToVector3D(float x, float y, float z)
{
    MTVec3D res;
    res.x = x;
    res.y = y;
    res.z = z;
    return res;
}

/**
 * Copy a new vector from vector.
 */
MTVec3D mtCopyVector3D(MTVec3D vector)
{
    MTVec3D res;
    res.x = vector.x;
    res.y = vector.y;
    res.z = vector.z;
    return res;
}

/**
 * Calculates the length of a given vector.
 */
float mtVectorLength3D(MTVec3D vector)
{
  return sqrt((vector.x*vector.x)+
              (vector.y*vector.y)+
              (vector.z*vector.z));
}

/**
 * Normalises a vector and returnes a new, normalised one.
 */
MTVec3D mtNormVector3D(MTVec3D vector)
{
    float l = mtVectorLength3D(vector);
    if (l >= .00001f)
        return mtToVector3D(vector.x/l, vector.y/l, vector.z/l);
    return vector;
}

/**
 * Computes the cross-product of the vectors axb and returnes a new vector.
 */
MTVec3D mtCrossProduct3D(MTVec3D a, MTVec3D b)
{
    MTVec3D product = mtToVector3D((a.y*b.z - a.z*b.y),
                      (a.z*b.x - a.x*b.z),
                      (a.x*b.y - a.y*b.x));
    return product;
}

/**
 * Multiplies vector with scalar and returnes new vector.
 */
MTVec3D mtMultiplyVectorScalar (MTVec3D a, double s)
{
    MTVec3D res;
    res.x = a.x * s;
    res.y = a.y * s;
    res.z = a.z * s;
    return res;
}

/**
 * Calculates the scalar (outer) product of the given vectors.
 */
double mtScalarProduct (MTVec3D a, MTVec3D b)
{
    return a.x*b.x + a.y*b.y + a.z*b.z;
}

/**
 * Subtracts vector b from vector a and returnes new vector.
 */
MTVec3D mtSubtractVectorVector (MTVec3D a, MTVec3D b)
{
    MTVec3D res;
    res.x = a.x - b.x;
    res.y = a.y - b.y;
    res.z = a.z - b.z;
    return res;
}

/**
 * Divides all values of the vector by s and returnes new vector.
 */
MTVec3D mtDivideVectorScalar (MTVec3D a, double s)
{
    return mtMultiplyVectorScalar(a, 1.0/s);
}

/**
 * Adds two vectors and returns a new vector.
 */
MTVec3D mtAddVectorVector (MTVec3D a, MTVec3D b)
{
    MTVec3D res;
    res.x = a.x + b.x;
    res.y = a.y + b.y;
    res.z = a.z + b.z;
    return res;
}

/**
 * calculate the lerp bewteen two vectors
 */
MTVec3D mtLerpMTVector(MTVec3D a, MTVec3D b, float t) {
    return mtToVector3D(
      a.x + t * (b.x - a.x),
      a.y + t * (b.y - a.y),
      a.z + t * (b.z - a.z)
    );
}

/**
 * Converts degree into radiant. Degree must be in the range of 0..359.
 */
double mtDegToRad (double deg)
{
    return deg*MT_PI/180.0;
}

/**
 * Converts radiant to degree.
 */
double mtRadToDeg (double rad)
{
    return rad*180.0/MT_PI;
}

/**
 * Calculates the angle between two vectors (not radiant!).
 */
double mtAngleVectorVector (MTVec3D a, MTVec3D b)
{
    return mtRadToDeg (acos (mtScalarProduct(a, b) / (mtVectorLength3D(a)*mtVectorLength3D(b))));
}
















