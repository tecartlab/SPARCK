/* MIT License
 *
 * Copyright (c) 2012-2021 tecartlab.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author maybites
 *
 * @credits: this external would not exist without the work by
 * joel gähwiler, who implemented this algorithm first in go
 * -> https://github.com/256dpi/max-tools/blob/master/perftrack/perftrack.go
 * 
 */

#include "ext.h"
#include "ext_common.h"
#include "ext_expr.h"
#include "ext_obex.h" //for atom routines
#include "mtVector.h"
#include "mtQuaternions.h"

void *latencycomp_class;

#define MAXLIST 4096

typedef struct bs_latencycomp
{
	t_object v_ob;
	void *v_outlet;
    MTVec3D v_pos;
    MTQuaternion v_rot;
    MTVec3D v_posSpeed;
    MTQuaternion v_rotSpeed;
    float v_compensation;
    float v_filter;
    float v_time;
    t_atom v_outputList[8];
} t_bs_latencycomp;

void ext_main(void *r);
void bs_latencycomp_list (t_bs_latencycomp *x, t_symbol *s, short ac, t_atom *av);
void bs_latencycomp_assist(t_bs_latencycomp *x, void *b, long m, long a, char *s);
void bs_latencycomp_inletinfo(t_bs_latencycomp *x, void *b, long a, char *t);
void bs_latencycomp_free (t_bs_latencycomp *x);
void *bs_latencycomp_new (t_symbol *s, short ac, t_atom *av);

// attribute setter and getter
t_max_err bs_filter_set(t_bs_latencycomp *x, t_object *attr, long argc, t_atom *argv);

void ext_main(void *r)
{
	t_class *c;

	c = class_new("bs.latencycomp",(method)bs_latencycomp_new,(method)bs_latencycomp_free,(long)sizeof(t_bs_latencycomp),0L,A_GIMME, 0);
	class_addmethod(c, (method)bs_latencycomp_list,		"list",		A_GIMME,0);
	class_addmethod(c, (method)bs_latencycomp_assist,	"assist",	A_CANT,0);
	class_addmethod(c, (method)bs_latencycomp_inletinfo, "inletinfo", A_CANT, 0);

	CLASS_ATTR_FLOAT(c, "compensation", 0, t_bs_latencycomp, v_compensation);
	CLASS_ATTR_CATEGORY(c, "compensation", 0, "Factor");
	CLASS_ATTR_BASIC(c, "compensation", 0);

    CLASS_ATTR_FLOAT(c, "filter", 0, t_bs_latencycomp, v_filter);
    CLASS_ATTR_CATEGORY(c, "filter", 0, "Factor");
    CLASS_ATTR_BASIC(c, "filter", 0);
    CLASS_ATTR_ACCESSORS(c, "filter", NULL, bs_filter_set);

    
	class_register(CLASS_BOX, c);
	latencycomp_class = c;

	return;
}

t_max_err bs_filter_set(t_bs_latencycomp *x, t_object *attr, long argc, t_atom *argv)
{
    long size = atom_getlong(argv);
    if (size < 0)       // bad size, don't change anything
        return 0;
    
    float filter = atom_getfloat(argv);
    if (filter >= 0. && 1.0 >= filter){
        x->v_filter = atom_getfloat(argv);
    } else if (filter < 0.){
        x->v_filter = 0.;
    }else if (filter > 1.0){
        x->v_filter = 1.;
    }
    return 0;
}


void bs_latencycomp_list(t_bs_latencycomp *x, t_symbol *s, short argc, t_atom *argv)
{
    if (argc != 8) {
        object_error((t_object *)x, "8 float values required");
        return;
    }
    
    // get time
    float time = atom_getfloat(argv);

    // get position
    float px = atom_getfloat(argv + 1);
    float py = atom_getfloat(argv + 2);
    float pz = atom_getfloat(argv + 3);
    MTVec3D pos = mtToVector3D(px, py, pz);

    // get rotation
    float qx = atom_getfloat(argv + 4);
    float qy = atom_getfloat(argv + 5);
    float qz = atom_getfloat(argv + 6);
    float qw = atom_getfloat(argv + 7);
    MTQuaternion rot = mtMakeMTQuaternion(qw, qx, qy, qz);

    // set initial values and return
    if (x->v_time == 0) {
        x->v_time = time;
        x->v_pos = mtCopyVector3D(pos);
        x->v_rot = mtCopyMTQuaternion(&rot);
        return;
    }

    // get delta
    float delta = time - x->v_time;
    
    if (delta != 0.0){
 
        // get position and rotation speed
        MTVec3D dPos = mtSubtractVectorVector(pos, x->v_pos);
        MTVec3D posSpeed = mtMultiplyVectorScalar(dPos, 1. / delta);
        
        MTQuaternion dRot = mtSubtractMTQuaternionMTQuaternion(&rot, &x->v_rot);
        MTQuaternion rotSpeed = mtMultMTQuaternionScalar(&dRot, 1. / delta);
     
        /* Speed Smoothing */

        // smooth speeds
        if (x->v_filter > 0.0) {
            posSpeed = mtLerpMTVector(x->v_posSpeed, posSpeed, 1.0 - x->v_filter);
            rotSpeed = mtLerpMTQuaternions(&x->v_rotSpeed, &rotSpeed, 1.0 - x->v_filter);
        }
        
        /* Prediction */

        // prepare output
        float pTime = time;
        MTVec3D pPos = pos;
        MTQuaternion pRot = rot;

        // continue if there is positive time difference
        if (delta > 0) {
            // predict position and rotation
            pTime = time + x->v_compensation;
            MTVec3D pPosPredict = mtMultiplyVectorScalar(posSpeed, x->v_compensation);
            pPos = mtAddVectorVector(pos, pPosPredict);
            MTQuaternion pRotPredict = mtMultMTQuaternionScalar(&rotSpeed, x->v_compensation);
            pRot = mtAddMTQuaternionMTQuaternion(&rot, &pRotPredict);
            mtNormMTQuaternion(&pRot);

        }

        // update state
        x->v_time = time;
        x->v_pos = pos;
        x->v_rot = rot;
        x->v_posSpeed = posSpeed;
        x->v_rotSpeed = rotSpeed;

        // send output

        atom_setfloat(x->v_outputList,pTime);
        atom_setfloat(x->v_outputList + 1,pPos.x);
        atom_setfloat(x->v_outputList + 2,pPos.y);
        atom_setfloat(x->v_outputList + 3,pPos.z);
        atom_setfloat(x->v_outputList + 4,pRot.v.x);
        atom_setfloat(x->v_outputList + 5,pRot.v.y);
        atom_setfloat(x->v_outputList + 6,pRot.v.z);
        atom_setfloat(x->v_outputList + 7,pRot.s);

        outlet_list(x->v_outlet, 0L, 8, x->v_outputList);
    }
}

void bs_latencycomp_inletinfo(t_bs_latencycomp *x, void *b, long a, char *t)
{
	if (a)
		*t = 1;
}

void bs_latencycomp_assist(t_bs_latencycomp *x, void *b, long m, long a, char *s)
{
	if (m == ASSIST_INLET)
        sprintf(s,"Evaluate latency compensation, list with 8 floats: time, px, py, pz, qx, qy, qz, qw"); // leftmost inlet
	else
		sprintf(s,"Compensation results");
}

void bs_latencycomp_free(t_bs_latencycomp *x)
{

}

void *bs_latencycomp_new(t_symbol *s, short argc, t_atom *argv)
{
	t_bs_latencycomp *x;

	x = (t_bs_latencycomp *)object_alloc(latencycomp_class);
	x->v_outlet = outlet_new(x,0L);
    x->v_compensation = 0.0;
    x->v_filter = 0.0;
    x->v_pos = mtToVector3D(0, 0, 0);
    x->v_posSpeed = mtToVector3D(0, 0, 0);
    x->v_rot = mtMakeMTQuaternion(1, 0, 0, 0);
    x->v_rotSpeed = mtMakeMTQuaternion(1, 0, 0, 0);
    x->v_time = 0;
    
	return (x);
}

