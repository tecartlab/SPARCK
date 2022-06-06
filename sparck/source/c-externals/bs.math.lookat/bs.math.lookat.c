/* MIT License
 *
 * Copyright (c) 2012-2020 tecartlab.com
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
 */

#include "ext.h"							// standard Max include, always required
#include "ext_obex.h"						// required for new style Max object

////////////////////////// object struct
typedef struct _bs_math_lookat
{
	t_object ob;			// the object itself (must be first)

    // instances global variable need to be declared here..

    t_atom mat[16];// = new Array(16);

    double eyeX;// = 0.;
    double eyeY;// = 0.;
    double eyeZ;// = 1.;

    double atX;// = 0.;
    double atY;// = 0.;
    double atZ;// = 0.;

    double upX;// = 0.;
    double upY;// = 0.;
    double upZ;// = 1.;

    double zaxisX;
    double zaxisY;
    double zaxisZ;

    double xaxisX;
    double xaxisY;
    double xaxisZ;

    double yaxisX;
    double yaxisY;
    double yaxisZ;

    double zAxisLength;
    double xAxisLength;
    double yAxisLength;

    int direction;// = 0;
    int hasChanged;// = 0;

    // this pointer are for the outlets...
    void *outlet1;
    void *outlet2;
    void *outlet3;

} t_bs_math_lookat; // struct name -> used inside the _new function

///////////////////////// function prototypes

//// standard set
void *bs_math_lookat_new(t_symbol *s, long argc, t_atom *argv);
void bs_math_lookat_free(t_bs_math_lookat *x);

// declaring all the custom functions.
// when receiving a LIST it needs => t_symbol *s, long argc, t_atom *argv
//      t_symbol *s     pointer
//      long argc       length of list
//      t_atom *argv    pointer to first list item

void bs_math_lookat_bang(t_bs_math_lookat *x);
void bs_math_lookat_eye(t_bs_math_lookat *x, t_symbol *s, long argc, t_atom *argv);
void bs_math_lookat_up(t_bs_math_lookat *x, int sel);
void bs_math_lookat_at(t_bs_math_lookat *x, t_symbol *s, long argc, t_atom *argv);
void bs_math_lookat_dir(t_bs_math_lookat *x, int val);
void bs_math_lookat_calc(t_bs_math_lookat *x);

void bs_math_lookat_assist(t_bs_math_lookat *x, void *b, long m, long a, char *s);

//////////////////////// global class pointer doubleiable
void *bs_math_lookat_class;


// main function to be called when the class is loaded by max for the first time
void ext_main(void *r)
{
	t_class *c;

	c = class_new("bs.math.lookat", (method)bs_math_lookat_new, (method)bs_math_lookat_free, (long)sizeof(t_bs_math_lookat), 0L /* leave NULL!! */, A_GIMME, 0);

	/* you CAN'T call this from the patcher */

    // linking the messages to the functions, specifing the values passed on
	class_addmethod(c, (method)bs_math_lookat_assist,		"assist",		A_CANT, 0);
    class_addmethod(c, (method)bs_math_lookat_bang,			"bang",         A_CANT, 0);
    class_addmethod(c, (method)bs_math_lookat_eye,			"eye",          A_GIMME, 0);
    class_addmethod(c, (method)bs_math_lookat_up,			"up",           A_LONG, 0);
    class_addmethod(c, (method)bs_math_lookat_at,			"at",           A_GIMME, 0);
    class_addmethod(c, (method)bs_math_lookat_dir,			"dir",          A_LONG, 0);
    // A_GIMME -> message list

	class_register(CLASS_BOX, c); /* CLASS_NOBOX */
	bs_math_lookat_class = c;

	//post("I am the bs.math.lookat object");
}

void bs_math_lookat_assist(t_bs_math_lookat *x, void *b, long m, long a, char *s)
{
	if (m == ASSIST_INLET) { // inlet
		sprintf(s, "I am inlet %ld", a);
	}
	else {	// outlet
		sprintf(s, "I am outlet %ld", a);
	}
}

void bs_math_lookat_free(t_bs_math_lookat *x)
{
	;
}


// the method called when a new box is created...

void *bs_math_lookat_new(t_symbol *s, long argc, t_atom *argv)
{

	t_bs_math_lookat *x = NULL;
	// long i;

    // creating the new box instance...
	if ((x = (t_bs_math_lookat *)object_alloc(bs_math_lookat_class))) {

        // if successfull...

        // instaniate outlets from right to left
        x->outlet3 = outlet_new((t_object *)x, 0L);
        x->outlet2 = outlet_new((t_object *)x, 0L);
        x->outlet1 = outlet_new((t_object *)x, 0L);

		//object_post((t_object *)x, "a new %s object was instantiated: %p", s->s_name, x);
		//object_post((t_object *)x, "it has %ld arguments", argc);


        /*
		for (i = 0; i < argc; i++) {
			if ((argv + i)->a_type == A_LONG) {
				object_post((t_object *)x, "arg %ld: long (%ld)", i, atom_getlong(argv+i));
			} else if ((argv + i)->a_type == A_FLOAT) {
				object_post((t_object *)x, "arg %ld: float (%f)", i, atom_getfloat(argv+i));
			} else if ((argv + i)->a_type == A_SYM) {
				object_post((t_object *)x, "arg %ld: symbol (%s)", i, atom_getsym(argv+i)->s_name);
			} else {
				object_error((t_object *)x, "forbidden argument");
			}
		}
         */

        // set the global variables

        x->eyeX = 0.;
        x->eyeY = 0.;
        x->eyeZ = 1.;

        x->atX = 0.;
        x->atY = 0.;
        x->atZ = 0.;

        x->upX = 0.;
        x->upY = 0.;
        x->upZ = 1.;



    }
	return (x);
}

// set up inlets/outlets/assist strings
/*
inlets = 1;
outlets = 3;
setinletassist(0,"data");
setoutletassist(0,"transform");
setoutletassist(1,"adapt (distance)");
setoutletassist(2,"refresh");
*/

// the custom functions always require 't_bs_math_lookat *x' to be passed on.

void bs_math_lookat_bang(t_bs_math_lookat *x){
    // all global variables need to be reference with x->
    if(x->hasChanged == 1)
        bs_math_lookat_calc(x);
    x->hasChanged = 0;
}

void bs_math_lookat_eye(t_bs_math_lookat *x, t_symbol *s, long argc, t_atom *argv){
    x->eyeX = atom_getfloat(argv+0); // argv+0 -> pointer -> &argv[0]
    x->eyeY = atom_getfloat(argv+1);
    x->eyeZ = atom_getfloat(argv+2);
    x->hasChanged = 1;
}

void bs_math_lookat_at(t_bs_math_lookat *x, t_symbol *s, long argc, t_atom *argv){
    x->atX = atom_getfloat(argv+0); // argv+0 -> pointer -> &argv[0]
    x->atY = atom_getfloat(argv+1);
    x->atZ = atom_getfloat(argv+2);
    x->hasChanged = 1;
}

void bs_math_lookat_up(t_bs_math_lookat *x, int sel){
    if(sel == 0){
        x->upX = 1.;
        x->upY = 0.;
        x->upZ = 0.;
    }else if(sel == 1){
        x->upX = 0.;
        x->upY = 1.;
        x->upZ = 0.;
    } else if(sel == 2){
        x->upX = 0.;
        x->upY = 0.;
        x->upZ = 1.;
    }else if(sel == 3){
        x->upX = -1.;
        x->upY = 0.;
        x->upZ = 0.;
    }else if(sel == 4){
        x->upX = 0.;
        x->upY = -1.;
        x->upZ = 0.;
    } else if(sel == 5){
        x->upX = 0.;
        x->upY = 0.;
        x->upZ = -1.;
    }
    x->hasChanged = 1;
    //outlet(2, "bang");
    outlet_bang(x->outlet3);
}

void bs_math_lookat_dir(t_bs_math_lookat *x, int val){
    x->direction = val;
    x->hasChanged = 1;
  //  outlet(2, "bang");
    outlet_bang(x->outlet3);

}

void bs_math_lookat_calc(t_bs_math_lookat *x){
    // zaxis = normal(At - Eye);

    if(x->direction == 0){
        x->zaxisX = x->atX - x->eyeX;
        x->zaxisY = x->atY - x->eyeY;
        x->zaxisZ = x->atZ - x->eyeZ;
    }else if(x->direction == 1){
        x->zaxisX = x->eyeX - x->atX;
        x->zaxisY = x->eyeY - x->atY;
        x->zaxisZ = x->eyeZ - x->atZ;
    }

    x->zAxisLength = sqrt(x->zaxisX * x->zaxisX + x->zaxisY * x->zaxisY + x->zaxisZ * x->zaxisZ);

    t_atom myAtom[1]; // creating a new atom
    atom_setfloat(myAtom + 0, x->zAxisLength); // filling it with the value
    outlet_anything(x->outlet2, gensym("adapt"), 1, myAtom); // send it to the outlet

    //outlet(1, "adapt", zAxisLength);

    x->zaxisX = (x->zAxisLength == 0)?x->zaxisX: x->zaxisX / x->zAxisLength;
    x->zaxisY = (x->zAxisLength == 0)?x->zaxisY: x->zaxisY / x->zAxisLength;
    x->zaxisZ = (x->zAxisLength == 0)?x->zaxisZ: x->zaxisZ / x->zAxisLength;

    //xaxis = normal(cross(Up, zaxis))

    x->xaxisX = x->upY * x->zaxisZ - x->upZ * x->zaxisY;
    x->xaxisY = x->upZ * x->zaxisX - x->upX * x->zaxisZ;
    x->xaxisZ = x->upX * x->zaxisY - x->upY * x->zaxisX;

    x->xAxisLength = sqrt(x->xaxisX * x->xaxisX + x->xaxisY * x->xaxisY + x->xaxisZ * x->xaxisZ);

    x->xaxisX = (x->xAxisLength == 0)?x->xaxisX: x->xaxisX / x->xAxisLength;
    x->xaxisY = (x->xAxisLength == 0)?x->xaxisY: x->xaxisY / x->xAxisLength;
    x->xaxisZ = (x->xAxisLength == 0)?x->xaxisZ: x->xaxisZ / x->xAxisLength;

    //post(" " + xaxisX + " " + xaxisY + " " + xaxisZ + " \n");

    //yaxis = cross(zaxis, xaxis)

    x->yaxisX =  x->zaxisY * x->xaxisZ - x->zaxisZ * x->xaxisY;
    x->yaxisY =  x->zaxisZ * x->xaxisX - x->zaxisX * x->xaxisZ;
    x->yaxisZ =  x->zaxisX * x->xaxisY - x->zaxisY * x->xaxisX;

    x->yAxisLength = sqrt(x->yaxisX * x->yaxisX + x->yaxisY * x->yaxisY + x->yaxisZ * x->yaxisZ);

    x->yaxisX = (x->yAxisLength == 0)? x->yaxisX: x->yaxisX / x->yAxisLength;
    x->yaxisY = (x->yAxisLength == 0)? x->yaxisY: x->yaxisY / x->yAxisLength;
    x->yaxisZ = (x->yAxisLength == 0)? x->yaxisZ: x->yaxisZ / x->yAxisLength;

    //double dotX = xaxisX * eyeX + xaxisY * eyeY + xaxisZ * eyeZ;
    //double dotY = yaxisX * eyeX + yaxisY * eyeY + yaxisZ * eyeZ;
    //double dotZ = zaxisX * eyeX + zaxisY * eyeY + zaxisZ * eyeZ;

    /*
     xaxis.x           yaxis.x           zaxis.x          0
     xaxis.y           yaxis.y           zaxis.y          0
     xaxis.z           yaxis.z           zaxis.z          0
     -dot(xaxis, eye)  -dot(yaxis, eye)  -dot(zaxis, eye)  l
     */

    atom_setfloat(&x->mat[0], x->xaxisX);
    atom_setfloat(&x->mat[1], x->xaxisY);
    atom_setfloat(&x->mat[2], x->xaxisZ);
    atom_setfloat(&x->mat[3], 0);
    atom_setfloat(&x->mat[4], x->yaxisX);
    atom_setfloat(&x->mat[5], x->yaxisY);
    atom_setfloat(&x->mat[6], x->yaxisZ);
    atom_setfloat(&x->mat[7], 0);
    atom_setfloat(&x->mat[8], x->zaxisX);
    atom_setfloat(&x->mat[9], x->zaxisY);
    atom_setfloat(&x->mat[10], x->zaxisZ);
    atom_setfloat(&x->mat[11], 0);
    /*
     mat[12] = -dotX;
     mat[13] = -dotY;
     mat[14] = -dotZ;
     */
    atom_setfloat(&x->mat[12], x->eyeX);
    atom_setfloat(&x->mat[13], x->eyeY);
    atom_setfloat(&x->mat[14], x->eyeZ);
    atom_setfloat(&x->mat[15], 1);

    // outlet(0, "transform", mat);
    outlet_anything(x->outlet1, gensym("transform"), 16, x->mat);

}
