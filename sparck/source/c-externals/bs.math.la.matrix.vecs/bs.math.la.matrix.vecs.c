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

/**
	@file
	bs_math_la_matrix_vecs - gets the direction / up / right vector from a matrix

        can take up to three attributes that are prepended to each vector message

	martin froehlich - martin@tecartlab.com

	@ingroup	examples
*/

#include "ext.h"							// standard Max include, always required
#include "ext_obex.h"						// required for new style Max object

////////////////////////// object struct
typedef struct _bs_math_la_matrix_vecs
{
	t_object ob;			// the object itself (must be first)

    // instances global variable need to be declared here..

    t_atom dir[3];// = new Array(3);
    t_atom up[3];// = new Array(3);
    t_atom right[3];// = new Array(3);

    t_symbol                    *s_dir;         //  message head for dir
    t_symbol                    *s_up;          //  message head for up
    t_symbol                    *s_right;       //  message head for right

    // this pointer are for the outlets...
    void *outlet1;
    void *outlet2;
    void *outlet3;

} t_bs_math_la_matrix_vecs; // struct name -> used inside the _new function

///////////////////////// function prototypes

//// standard set
void *bs_math_la_matrix_vecs_new(t_symbol *s, long argc, t_atom *argv);
void bs_math_la_matrix_vecs_free(t_bs_math_la_matrix_vecs *x);

// declaring all the custom functions.
// when receiving a LIST it needs => t_symbol *s, long argc, t_atom *argv
//      t_symbol *s     pointer
//      long argc       length of list
//      t_atom *argv    pointer to first list item

void bs_math_la_matrix_vecs_anything(t_bs_math_la_matrix_vecs *x, t_symbol *s, long argc, t_atom *argv);
void bs_math_la_matrix_vecs_assist(t_bs_math_la_matrix_vecs *x, void *b, long m, long a, char *s);

//////////////////////// global class pointer doubleiable
void *bs_math_la_matrix_vecs_class;


// main function to be called when the class is loaded by max for the first time
void ext_main(void *r)
{
	t_class *c;

	c = class_new("bs.math.la.matrix.vecs", (method)bs_math_la_matrix_vecs_new, (method)bs_math_la_matrix_vecs_free, (long)sizeof(t_bs_math_la_matrix_vecs), 0L /* leave NULL!! */, A_GIMME, 0);

	/* you CAN'T call this from the patcher */

    // linking the messages to the functions, specifing the values passed on
	class_addmethod(c, (method)bs_math_la_matrix_vecs_assist,		"assist",     A_CANT, 0);
    class_addmethod(c, (method)bs_math_la_matrix_vecs_anything,		"list",       A_GIMME, 0);
    class_addmethod(c, (method)bs_math_la_matrix_vecs_anything,		"anything",   A_GIMME, 0);
    // A_GIMME -> message list

	class_register(CLASS_BOX, c); /* CLASS_NOBOX */
	bs_math_la_matrix_vecs_class = c;

}

void bs_math_la_matrix_vecs_assist(t_bs_math_la_matrix_vecs *x, void *b, long m, long a, char *s)
{
	if (m == ASSIST_INLET) { // inlet
		sprintf(s, "I am inlet %ld", a);
	}
	else {	// outlet
		sprintf(s, "I am outlet %ld", a);
	}
}

void bs_math_la_matrix_vecs_free(t_bs_math_la_matrix_vecs *x)
{
	;
}


// the method called when a new box is created...

void *bs_math_la_matrix_vecs_new(t_symbol *s, long argc, t_atom *argv)
{

	t_bs_math_la_matrix_vecs *x = NULL;
	// long i;

    // creating the new box instance...
	if ((x = (t_bs_math_la_matrix_vecs *)object_alloc(bs_math_la_matrix_vecs_class))) {

        // if successfull...

        // instaniate outlets from right to left
        x->outlet3 = outlet_new((t_object *)x, 0L);
        x->outlet2 = outlet_new((t_object *)x, 0L);
        x->outlet1 = outlet_new((t_object *)x, 0L);

        long i;

        x->s_dir = gensym("dir");
        x->s_up = gensym("up");
        x->s_right = gensym("right");

        for (i=0; i < argc; i++) {
            //post("argument type :%s", argv[i].a_w.w_sym->s_name);
            if(argv[i].a_type == A_SYM){
                switch (i){
                    case 0: x->s_dir = gensym(argv[i].a_w.w_sym->s_name);
                        break;
                    case 1: x->s_up = gensym(argv[i].a_w.w_sym->s_name);
                        break;
                    case 2: x->s_right = gensym(argv[i].a_w.w_sym->s_name);
                        break;
                }
            }
        }
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

// the custom functions always require 't_bs_math_la_matrix_vecs *x' to be passed on.

void bs_math_la_matrix_vecs_anything(t_bs_math_la_matrix_vecs *x, t_symbol *s, long argc, t_atom *argv){
    atom_setfloat(&x->dir[0], atom_getfloat(argv+2));
    atom_setfloat(&x->dir[1], atom_getfloat(argv+6));
    atom_setfloat(&x->dir[2], atom_getfloat(argv+10));

    atom_setfloat(&x->up[0], atom_getfloat(argv+1));
    atom_setfloat(&x->up[1], atom_getfloat(argv+5));
    atom_setfloat(&x->up[2], atom_getfloat(argv+9));

    atom_setfloat(&x->right[0], atom_getfloat(argv+0));
    atom_setfloat(&x->right[1], atom_getfloat(argv+4));
    atom_setfloat(&x->right[2], atom_getfloat(argv+8));

    //x->dir[0] = atom_getfloat(argv+0); // argv+0 -> pointer -> &argv[0]
    outlet_anything(x->outlet1, x->s_dir, 3, x->dir);
    outlet_anything(x->outlet2, x->s_up, 3, x->up);
    outlet_anything(x->outlet3, x->s_right, 3, x->right);
}
