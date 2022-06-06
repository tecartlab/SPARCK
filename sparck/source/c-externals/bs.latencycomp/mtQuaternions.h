#ifndef __MTQUATERNIONS_H__
#define __MTQUATERNIONS_H__
/**
 * Interface for some operations on Quaternions.
 * All operations are prefixed with 'mt' to avoid name clashes and get an
 * attempt for a unique prefix.
 *
 * @author Maurice Tollmien, maybites
 */

#include "mtVector.h"

/** Quaternion */
typedef struct
{
    double s;
    MTVec3D v;
} MTQuaternion;

/* Low level operations on MTQuaternions */

MTQuaternion mtCreateMTQuaternion(MTVec3D axis, double angle);

MTQuaternion mtMakeMTQuaternion(double qw, double qx, double qy, double qz);

MTQuaternion mtCopyMTQuaternion(const MTQuaternion* q1);

MTQuaternion mtMultMTQuaternionMTQuaternion (const MTQuaternion* q1, const MTQuaternion* q2);

MTQuaternion mtMultMTQuaternionScalar (const MTQuaternion* q1, double s);

MTQuaternion mtAddMTQuaternionMTQuaternion (const MTQuaternion* q1, const MTQuaternion* q2);

MTQuaternion mtSubtractMTQuaternionMTQuaternion (const MTQuaternion* q1, const MTQuaternion* q2);

MTQuaternion mtConjugateMTQuaternion (const MTQuaternion* q1);

MTQuaternion mtInverseMTQuaternion (const MTQuaternion* q1);

MTQuaternion mtLerpMTQuaternions (const MTQuaternion* q1, const MTQuaternion* q2, double t);

MTQuaternion mtSlerpMTQuaternions (const MTQuaternion* q1, const MTQuaternion* q2, double t);

void mtNormMTQuaternion (MTQuaternion* q1);

double mtLengthMTQuaternion (const MTQuaternion* q1);

int mtIsNormMTQuaternion (const MTQuaternion* q1);

/* Some higher level functions, using MTQuaternions */

MTVec3D mtRotatePointWithMTQuaternion(MTQuaternion q, MTVec3D point);
MTVec3D mtRotatePointAxis (MTVec3D axis, double angle, MTVec3D point);

#endif



