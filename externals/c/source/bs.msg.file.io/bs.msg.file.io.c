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

#include "ext.h"
#include "ext_obex.h"
#include "ext_path.h"

#define MAXLIST 4096

void *_bs_msg_file_io_class;

typedef struct _bs_msg_file_io {
	t_object        f_ob;
	t_filehandle    f_fh;
    char            **f_textHandle;
    char            **f_tokenHandle;
    t_ptr_size      f_textSize;
    char            **f_writeLineHandle;
    long            f_writeLineSize;
    long            f_lineCounter;
    char            *lineToken;
	short           f_open;			/* spool flag */
	short           f_spool;
    short           f_firstTimeLoad;
    void            *f_writedone;

    void            *f_out;
    void            *f_outDump;
} t__bs_msg_file_io;

t_symbol *ps_nothing,*ps_spool,*ps_int,*ps_in1,*ps_in2;
t_symbol *ps_eof, *ps_readdone, *ps_linecount, *ps_firstline, *ps_lastline, *ps_linenumber;

void _bs_msg_file_io_read(t__bs_msg_file_io *x, t_symbol *s);
void _bs_msg_file_io_doread(t__bs_msg_file_io *x, t_symbol *s);
void _bs_msg_file_io_openfile(t__bs_msg_file_io *x, char *filename, short path);

void _bs_msg_file_io_write(t__bs_msg_file_io *x, t_symbol *s);
void _bs_msg_file_io_dowrite(t__bs_msg_file_io *x, t_symbol *s);
void _bs_msg_file_io_writefile(t__bs_msg_file_io *x, char *filename, short path);
void _bs_msg_file_io_close(t__bs_msg_file_io *x);

void _bs_msg_file_io_start(t__bs_msg_file_io *x);
void _bs_msg_file_io_next(t__bs_msg_file_io *x);

void _bs_msg_file_io_bang(t__bs_msg_file_io *x);
void _bs_msg_file_io_anything(t__bs_msg_file_io *x, t_symbol *s, short ac, t_atom *av);

void _bs_msg_file_io_free(t__bs_msg_file_io *x);
void _bs_msg_file_io_assist(t__bs_msg_file_io *x, void *b, long m, long a, char *s);

void *_bs_msg_file_io_new(t_symbol *fn, t_symbol *spoolFlag);

void _bs_outlet_Sym(t__bs_msg_file_io *x, t_symbol *s);
void _bs_outlet_SymLong(t__bs_msg_file_io *x, t_symbol *s, long m);
void _bs_outlet_SymLines(t__bs_msg_file_io *x, t_symbol *s, char *tokens);

void ctopcpy(unsigned char *p1, char *p2);
//char *strcpy(char *s1, const char *s2);

void ext_main(void *r)
{
	t_class *c;

	c = class_new("bs.msg.file.io", (method)_bs_msg_file_io_new, (method)_bs_msg_file_io_free, (short)sizeof(t__bs_msg_file_io),
				  0L, A_DEFSYM, A_DEFSYM, 0);

    class_addmethod(c, (method)_bs_msg_file_io_start,       "start", 0);
    class_addmethod(c, (method)_bs_msg_file_io_next,        "next", 0);
    class_addmethod(c, (method)_bs_msg_file_io_bang,        "bang", 0);
    class_addmethod(c, (method)_bs_msg_file_io_anything,    "anything",     A_GIMME, 0);
    class_addmethod(c, (method)_bs_msg_file_io_read,        "read",         A_DEFSYM,0);
    class_addmethod(c, (method)_bs_msg_file_io_write,       "write",        A_DEFSYM,0);
    class_addmethod(c, (method)_bs_msg_file_io_close,       "close",        A_DEFSYM,0);
	class_addmethod(c, (method)_bs_msg_file_io_assist,	    "assist",	    A_CANT,0);
	class_register(CLASS_BOX, c);
	_bs_msg_file_io_class = c;

	ps_nothing = gensym("");
    ps_spool = gensym("spool");

    ps_eof = gensym("eof");
    ps_readdone = gensym("readdone");
    ps_linecount = gensym("linecount");
    ps_linenumber = gensym("linenumber");

    ps_firstline = gensym("firstline");
    ps_lastline = gensym("lastline");

	return (0);
}

void _bs_msg_file_io_read(t__bs_msg_file_io *x, t_symbol *s)
{
    defer(x, (method)_bs_msg_file_io_doread, s, 0, NULL);
}

void _bs_msg_file_io_doread(t__bs_msg_file_io *x, t_symbol *s)
{
    t_fourcc filetype = 'TEXT', outtype;
    char filename[512];
    short path;

    // TODO: check strcpy

    if (s == gensym("")) {
        if (open_dialog(filename, &path, &outtype, &filetype, 1))
            return;
    } else {
        strcpy(filename, s->s_name);
        if (locatefile_extended(filename, &path, &outtype, &filetype, 1)) {
            object_error((t_object *) x, "%s: not found", s->s_name);
            return;
        }
    }
    // we have a file
    _bs_msg_file_io_openfile(x, filename, path);
}

void _bs_msg_file_io_openfile(t__bs_msg_file_io *x, char *filename, short path) {

    t_filehandle fh;

    x->lineToken = NULL;

    if(x->f_textHandle != NULL){
        sysmem_freehandle(x->f_textHandle);
        sysmem_freehandle(x->f_tokenHandle);
    }

    if (path_opensysfile(filename, path, &fh, READ_PERM)) {
        object_error((t_object *) x, "error opening %s", filename);
        return;
    }

    if(sysfile_geteof(fh, &x->f_textSize)){
        object_error((t_object *) x, "error getting filesize for %s", filename);
        return;
    }

    x->f_textHandle = sysmem_newhandleclear(x->f_textSize + 1);
    x->f_tokenHandle = sysmem_newhandleclear(x->f_textSize + 1);

    if(sysfile_readtextfile(fh, x->f_textHandle, 0, TEXT_LB_NATIVE)){
        object_error((t_object *) x, "error reading content %s", filename);
        return;
    }

    sysfile_close(fh);

    sysmem_copyptr(*(x->f_textHandle), *(x->f_tokenHandle), x->f_textSize + 1);
    x->lineToken = strtok(*x->f_tokenHandle, "\n");

    char *lastNotNullToken = x->lineToken;

    _bs_outlet_SymLines(x, ps_firstline, lastNotNullToken);

    x->f_lineCounter = 0;

    while(x->lineToken != NULL){
        lastNotNullToken = x->lineToken;
        x->lineToken = strtok(NULL, "\n");
        x->f_lineCounter++;
    }

    _bs_outlet_SymLong(x, ps_linecount, x->f_lineCounter);

    _bs_outlet_SymLines(x, ps_lastline, lastNotNullToken);

    _bs_outlet_Sym(x, ps_readdone);
 }

void _bs_msg_file_io_start(t__bs_msg_file_io *x){
    if(!x->f_open){
        sysmem_copyptr(*(x->f_textHandle), *(x->f_tokenHandle), x->f_textSize + 1);
        x->lineToken = strtok(*x->f_tokenHandle, "\n");

        x->f_lineCounter = 0;

        if(x->lineToken != NULL){
            t_atom *av =  NULL;
            long ac = 0;
            t_max_err err = MAX_ERR_NONE;

            err = atom_setparse(&ac, &av, x->lineToken);

            _bs_outlet_SymLong(x, ps_linenumber, x->f_lineCounter);
            outlet_list(x->f_out, 0L, ac, av);
        } else {
            _bs_outlet_Sym(x, ps_eof);
        }
    } else {
        error("file is currently written to, no reading allowed.");
    }
}


void _bs_msg_file_io_bang(t__bs_msg_file_io *x){
    if(!x->f_open){
        if(x->lineToken != NULL){
            t_atom *av =  NULL;
            long ac = 0;
            t_max_err err = MAX_ERR_NONE;

            err = atom_setparse(&ac, &av, x->lineToken);

            outlet_list(x->f_out, 0L, ac, av);
        } else {
            _bs_outlet_Sym(x, ps_eof);
        }
    } else {
        error("file is currently written to, no reading allowed.");
    }
}

void _bs_msg_file_io_next(t__bs_msg_file_io *x){
    if(!x->f_open){
        // get next token
        x->lineToken = strtok(NULL, "\n");
        x->f_lineCounter++;

        if(x->lineToken != NULL){

            t_atom *av =  NULL;
            long ac = 0;
            t_max_err err = MAX_ERR_NONE;

            err = atom_setparse(&ac, &av, x->lineToken);

            _bs_outlet_SymLong(x, ps_linenumber, x->f_lineCounter);
            outlet_list(x->f_out, 0L, ac, av);
        } else {
            _bs_outlet_Sym(x, ps_eof);
       }
    } else {
        error("file is currently written to, no reading allowed.");
    }
}

void _bs_msg_file_io_write(t__bs_msg_file_io *x, t_symbol *s)
{
    defer(x, (method)_bs_msg_file_io_dowrite, s, 0, NULL);
}

void _bs_msg_file_io_dowrite(t__bs_msg_file_io *x, t_symbol *s)
{
    t_fourcc filetype = 'TEXT', outtype;
    char filename[MAX_FILENAME_CHARS];
    short path;

    if (s == gensym("")) {
        sprintf(filename, "_bs_msg_file_io_template.txt");
        saveas_promptset("Save template as...");
        if (saveasdialog_extended(filename, &path, &outtype, &filetype, 0))
            return;
    } else {
        strcpy(filename, s->s_name);
        path = path_getdefault();
    }
    _bs_msg_file_io_writefile(x, filename, path);
}

void _bs_msg_file_io_writefile(t__bs_msg_file_io *x, char *filename, short path)
{
    long err;

    err = path_createsysfile(filename, path, 'TEXT', &x->f_fh);

    if (err)
        return;

    sysfile_seteof(x->f_fh, 0);

    x->f_open = TRUE;
 }

void _bs_msg_file_io_anything(t__bs_msg_file_io *x, t_symbol *s, short ac, t_atom *av){

    if(x->f_open){
        atom_gettext(ac, av, &x->f_writeLineSize, x->f_writeLineHandle, OBEX_UTIL_ATOM_GETTEXT_DEFAULT);

        t_ptr_size buflen;

        buflen = strlen(*x->f_writeLineHandle);
        sysfile_write(x->f_fh, &buflen, *x->f_writeLineHandle);

        buflen = 1;
        sysfile_write(x->f_fh, &buflen, "\n");
    } else {
        error("no file open");
    }
}

void _bs_msg_file_io_close(t__bs_msg_file_io *x){
    if(x->f_open){
        sysfile_close(x->f_fh);
        x->f_open = FALSE;
    }
}

void _bs_msg_file_io_free(t__bs_msg_file_io *x)
{
    sysmem_freehandle(x->f_textHandle);
    sysmem_freehandle(x->f_tokenHandle);
    sysmem_freehandle(x->f_writeLineHandle);

    _bs_msg_file_io_close(x);
}

void _bs_msg_file_io_assist(t__bs_msg_file_io *x, void *b, long m, long a, char *s)
{
    // this system eliminates the need for a STR# resource
    if (m==1) {
        switch (a) {
            case 0: sprintf(s,"write messages to File"); break;
        }
    }
    else if (m==2) {
        switch (a) {
            case 0: sprintf(s,"output subsequent file line on bang"); break;
            case 1: sprintf(s,"bang On End of File"); break;
            case 2: sprintf(s,"bang When Read Finished"); break;
        }
    }
}

void *_bs_msg_file_io_new(t_symbol *fn, t_symbol *spoolFlag)
{
	t__bs_msg_file_io *x;

    //object_post((t_object*)fn, "_bs_msg_file_io_v02");

	x = object_alloc(_bs_msg_file_io_class);

    x->f_outDump = listout(x);
    x->f_out = listout(x);

	x->f_open = FALSE;
    x->f_firstTimeLoad = TRUE;
	x->f_fh = 0;
	x->f_spool = 0;
    x->f_writeLineSize = 200;
    x->f_writeLineHandle = sysmem_newhandleclear(x->f_writeLineSize);
    x->f_textHandle = NULL;
    x->f_tokenHandle = NULL;

	if (fn != ps_nothing) {
		if (spoolFlag==ps_spool)
			x->f_spool = TRUE;
		else
			x->f_spool = FALSE;
		_bs_msg_file_io_doread(x,fn);
	}
	return (x);
}


void _bs_outlet_Sym(t__bs_msg_file_io *x, t_symbol *s){
    t_atom myList[1];
    atom_setsym(myList, s);
    outlet_list(x->f_outDump, 0L, 1, myList);
}

void _bs_outlet_SymLong(t__bs_msg_file_io *x, t_symbol *s, long m){
    t_atom myList[2];
    atom_setsym(myList, s);
    atom_setlong(myList+1, m);
    outlet_list(x->f_outDump, 0L, 2, myList);
}

void _bs_outlet_SymLines(t__bs_msg_file_io *x, t_symbol *s, char *tokens){
    t_atom *av =  NULL;
    long ac = 0;
    t_max_err err = MAX_ERR_NONE;

    err = atom_setparse(&ac, &av, tokens);

    t_atom *argv = NULL;
    long argc = 0;
    char alloc;

    // allocate an array for 'argv' (and the number of atoms inside 'argc') with the length of number of atoms plus one symbol
    if (atom_alloc_array(ac + 1, &argc, &argv, &alloc) == MAX_ERR_NONE) {
        // assembles the message and the arguments array
        atom_setsym(argv, s); // sets the symbol into the atom
        sysmem_copyptr(av, argv + 1, sizeof(t_atom) * ac);// copies the content from 'av' to argv+1

        outlet_list(x->f_outDump, 0L, argc, argv);

        // but we need to free the atomarray
        sysmem_freeptr(argv);
    }

}

#ifndef WIN_VERSION
#ifndef TARGET_RT_MAC_MACHO
char *strcpy(char *s1, const char *s2)
{
	char *ret = s1;

	while ((*s1++ = *s2++) != 0)
		;

	return ret;
}
#endif// TARGET_RT_MAC_MACHO
#endif// WIN_VERSION
