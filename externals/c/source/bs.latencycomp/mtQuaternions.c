/**
 * Implementation of most relevant functions on MTQuaternions.
 *
 * All operations are prefixed with 'mt' to avoid name clashes and get an
 * attempt for a unique prefix.
 *
 * @author Maurice Tollmien, maybites
 */

/* ---- System Header ---- */
#include <stdlib.h>
#include <math.h>

/* ---- My Header ---- */
#include "mtQuaternions.h"
#include "mtVector.h"

#define EPS     0.0001

/*
 * Low level operations on MTQuaternions
 */
MTQuaternion mtCreateMTQuaternion(MTVec3D axis, double angle) {
    MTQuaternion q;
    q.s = cos (angle/2.0);
    q.v = mtMultiplyVectorScalar(axis, sin(angle/2.0));
    return q;
}

/*
 * create quaternion
 */
MTQuaternion mtMakeMTQuaternion(double qw, double qx, double qy, double qz) {
    MTQuaternion q;
    q.s = qw;
    q.v = mtToVector3D(qx, qy, qz);
    return q;
}

/*
 * Copy MTQuaternions
 */
MTQuaternion mtCopyMTQuaternion(const MTQuaternion* q1) {
    return mtMakeMTQuaternion(q1->s, q1->v.x, q1->v.y, q1->v.z);
}

/**
 * Multiply to MTQuaternions with each other.
 * Careful! Not commutative!!!
 * Calculates: q1 * q2
 */
MTQuaternion mtMultMTQuaternionMTQuaternion (const MTQuaternion* q1, const MTQuaternion* q2)
{
    MTQuaternion res;
    res.s = q1->s*q2->s - mtScalarProduct(q1->v, q2->v);
    MTVec3D vres = mtCrossProduct3D(q1->v, q2->v);

    MTVec3D tmp = mtMultiplyVectorScalar (q2->v, q1->s);
    vres = mtAddVectorVector(vres, tmp);
    tmp = mtMultiplyVectorScalar(q1->v, q2->s);
    res.v = mtAddVectorVector(vres, tmp);
    return res;
}

/**
 * Multiplies a MTQuaternion and a scalar.
 * Therefore the scalar will be converted to a MTQuaternion.
 * After that the two MTQuaternions will be muliplied.
 */
MTQuaternion mtMultMTQuaternionScalar (const MTQuaternion* q1, double s)
{
    MTQuaternion q2;

    q2.s = s;
    q2.v = mtToVector3D(0,0,0);

    return mtMultMTQuaternionMTQuaternion (q1, &q2);
}

/**
 * Calculates: q1 + q2.
 */
MTQuaternion mtAddMTQuaternionMTQuaternion (const MTQuaternion* q1, const MTQuaternion* q2)
{
    MTQuaternion res;
    res.s = q1->s + q2->s;
    res.v = mtAddVectorVector(q1->v, q2->v);
    return res;
}

/**
 * Calculates q1 - q2.
 */
MTQuaternion mtSubtractMTQuaternionMTQuaternion (const MTQuaternion* q1, const MTQuaternion* q2)
{
    MTQuaternion res;
    res.s = q1->s - q2->s;
    res.v = mtSubtractVectorVector(q1->v, q2->v);
    return res;
}

/**
 * Complex conjugate the MTQuaternion.
 */
MTQuaternion mtConjugateMTQuaternion (const MTQuaternion* q1)
{
    MTQuaternion res;
    res.s = q1->s;
    res.v = mtMultiplyVectorScalar(q1->v, -1.0);
    return res;
}

/**
 * Invert the MTQuaternion.
 */
MTQuaternion mtInverseMTQuaternion (const MTQuaternion* q1)
{
    MTQuaternion res;
    double qlen = pow (mtLengthMTQuaternion (q1), 2);

    MTQuaternion tmp = mtConjugateMTQuaternion(q1);

    return mtMultMTQuaternionScalar (&tmp, 1.0 / qlen);
}

/**
 * calculate the lerp bewteen two quaternions
 */
MTQuaternion mtLerpMTQuaternions (const MTQuaternion* q1, const MTQuaternion* q2, double t){
    MTQuaternion tmp = mtSubtractMTQuaternionMTQuaternion(q2, q1);
    tmp = mtMultMTQuaternionScalar(&tmp, t);
    return mtAddMTQuaternionMTQuaternion(q1, &tmp);
    //return q1.Add(q2.Sub(q1).Scale(t))
}

/**
 * calculate the slerp bewteen two quaternions
 */
MTQuaternion mtSlerpMTQuaternions (const MTQuaternion* q1, const MTQuaternion* q2, double t)
{
    MTQuaternion result;
 
     // Based on http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/slerp/index.htm
     double cosHalfTheta = q1->s*q2->s + q1->v.x*q2->v.x + q1->v.y*q2->v.y + q1->v.z*q2->v.z;

     // if q1=q2 or qa=-q2 then theta = 0 and we can return qa
     if (abs(cosHalfTheta) >= 1.0) {
         return mtCopyMTQuaternion(q1);
     }

     double halfTheta = acos(cosHalfTheta);
     double sinHalfTheta = sqrt(1.0 - cosHalfTheta*cosHalfTheta);
     // If theta = 180 degrees then result is not fully defined
     // We could rotate around any axis normal to q1 or q2
     if (fabs(sinHalfTheta) < EPS) {
         result.s = (q1->s * 0.5 + q2->s * 0.5);
         result.v.x = (q1->v.x * 0.5 + q2->v.x * 0.5);
         result.v.y = (q1->v.y * 0.5 + q2->v.y * 0.5);
         result.v.z = (q1->v.z * 0.5 + q2->v.z * 0.5);
     }

     // Calculate Quaternion
     double ratioA = sin((1 - t) * halfTheta) / sinHalfTheta;
     double ratioB = sin(t * halfTheta) / sinHalfTheta;
     result.s = (q1->s * ratioA + q2->s * ratioB);
     result.v.x = (q1->v.x * ratioA + q2->v.x * ratioB);
     result.v.y = (q1->v.y * ratioA + q2->v.y * ratioB);
     result.v.z = (q1->v.z * ratioA + q2->v.z * ratioB);

    return result;
}

/**
 * Normalize the MTQuaternion to a length of 1.
 */
void mtNormMTQuaternion (MTQuaternion* q1)
{
    double qlen = mtLengthMTQuaternion (q1);

    if (qlen <= 1 + EPS && 1 - EPS <= qlen) {
        return;
    }
    if (qlen == 0) {
        q1->s = 1;
        q1->v = mtToVector3D(0, 0, 0);
        return;
    }

    q1->s /= qlen;
    q1->v = mtMultiplyVectorScalar(q1->v, 1.0 / qlen);
}

/**
 * Calculates the length of the MTQuaternion.
 */
double mtLengthMTQuaternion (const MTQuaternion* q1)
{
    return sqrt (q1->s*q1->s + q1->v.x*q1->v.x + q1->v.y*q1->v.y + q1->v.z*q1->v.z);
}

/**
 * Check if the MTQuaternion is normalized.
 */
int mtIsNormMTQuaternion (const MTQuaternion* q1)
{
    double res = q1->s*q1->s + q1->v.x*q1->v.x + q1->v.y*q1->v.y + q1->v.z*q1->v.z;
    return (res + EPS >= 1.0) && (res - EPS <= 1.0);
}

/* Some higher level functions, using MTQuaternions */

MTVec3D mtRotatePointWithMTQuaternion(MTQuaternion q, MTVec3D point)
{
    mtNormMTQuaternion(&q);

    // Create MTQuaternion of the point to rotate
    MTQuaternion p;
    p.s    = 0.0;
    p.v = point;

    // The actual calculations.
    //  ---  q p q*  ---
    MTQuaternion inverseQ = mtInverseMTQuaternion(&q);
    MTQuaternion res = mtMultMTQuaternionMTQuaternion (&q, &p);
    res = mtMultMTQuaternionMTQuaternion (&res, &inverseQ);

    // Write new rotated coordinates back to the point
    return res.v;
}

/**
 * Rotates a given point around a given axis by a given angle.
 * The rotations uses MTQuaternions internally and writes the rotated (modified)
 * coordinates back to the point.
 */
MTVec3D mtRotatePointAxis (MTVec3D axis, double angle, MTVec3D point)
{
    // create MTQuaternion from axis and angle
    MTQuaternion q;
    q.s = cos (angle/2.0);
    q.v = mtMultiplyVectorScalar(axis, sin(angle/2.0));

    return mtRotatePointWithMTQuaternion(q, point);

}
