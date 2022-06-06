#ifndef __MTVECTOR_H__
#define __MTVECTOR_H__
/**
 * Interface for basic vector calculations.
 * All operations are prefixed with 'mt' to avoid name clashes and get an
 * attempt for a unique prefix.
 *
 * @author Maurice Tollmien, maybites
 */

#include <stdarg.h>

typedef struct {
    float x;
    float y;
    float z;
} MTVec3D;

#define MT_PI 3.14159265

MTVec3D mtToVector3D(float x, float y, float z);
MTVec3D mtCopyVector3D(MTVec3D vector);
float mtVectorLength3D(MTVec3D vector);
MTVec3D mtNormVector3D(MTVec3D vector);
MTVec3D mtCrossProduct3D(MTVec3D a, MTVec3D b);
MTVec3D mtMultiplyVectorScalar (MTVec3D a, double s);
MTVec3D mtSubtractVectorVector (MTVec3D a, MTVec3D b);
MTVec3D mtDivideVectorScalar (MTVec3D a, double s);
MTVec3D mtAddVectorVector (MTVec3D a, MTVec3D b);
MTVec3D mtLerpMTVector(MTVec3D a, MTVec3D b, float t);
void  mtPrintVector (MTVec3D a);
double mtAngleVectorVector (MTVec3D a, MTVec3D b);
double mtRadToDeg (double rad);
double mtDegToRad (double deg);
double mtScalarProduct (MTVec3D a, MTVec3D b);

#endif
