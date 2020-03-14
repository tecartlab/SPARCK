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
#include "ext_dictobj.h"
#include "jpatcher_api.h"

////////////////////////// object struct
typedef struct __bs_msg_receive
{
	t_object					ob;			// the object itself (must be first)
    long                        m_in;     // space for the inlet number used by all the proxies
    void                        *m_proxy_inlet1;
    void                        *m_proxy_inlet2;
    t_symbol                    *s_statusAddress;

    t_symbol                    *s_myTitle;
    t_symbol                    *s_myAddress;
    t_symbol                    *s_myID;

    t_symbol                    *s_msgAddress;
    t_symbol                    *s_myMessage;
    t_symbol                    *s_myReplace;
    t_atomarray                 *s_myOutOff;

    long                        automatic;
    long                        enabled;
	void						*out;
} t__bs_msg_receive;

///////////////////////// function prototypes
void *_bs_msg_receive_new(t_symbol *s, long argc, t_atom *argv);
void _bs_msg_receive_free(t__bs_msg_receive *x);
void _bs_msg_receive_assist(t__bs_msg_receive *x, void *b, long m, long a, char *s);
void _bs_msg_receive_notify(t__bs_msg_receive *x, t_symbol *s, t_symbol *msg, void *sender, void *data);

// public
void _bs_msg_receive_bang(t__bs_msg_receive *x);
void _bs_msg_receive_anything(t__bs_msg_receive *x, t_symbol *s, long ac, t_atom *av);

// private
void _bs_msg_receive_set_attributes(t__bs_msg_receive *x, t_symbol *s, long ac, t_atom *av);
void _bs_msg_receive_updateAttributes(t__bs_msg_receive *x);
void _bs_msg_receive_updateSubscription(t__bs_msg_receive *x);
void _bs_msg_receive_updateSubscribeStatus(t__bs_msg_receive *x, t_symbol *id);
void _bs_msg_receive_output(t__bs_msg_receive *x, t_atomarray *aa);
void _bs_msg_receive_makeStatusAddress(t_symbol *message, t_symbol *address, t_symbol **result);
int _bs_msg_receive_set_attributes_similar(char *s_shorter, char *s_longer);
void _bs_msg_receive_outputCurrentValue(t__bs_msg_receive *x);

//////////////////////// global class pointer variable
t_class *_bs_msg_receive_class;

static t_symbol *ps_nothing;
static t_symbol *ps_conduit;

static t_symbol *ps_conduit_status;
static t_symbol *ps_conduit_wh;

static t_symbol *ps_msg_free;
static t_symbol *ps_msg_sendmessage;
static t_symbol *ps_msg_sendwormhole;
static t_symbol *ps_msg_subscribe_attach;

static t_symbol *ps_attr_title;
static t_symbol *ps_attr_wormhole;
static t_symbol *ps_attr_out;
static t_symbol *ps_attr_outoff;
static t_symbol *ps_attr_automatic;
static t_symbol *ps_attr_enabled;

static t_symbol *ps_attr_val_replace_drop;

static t_symbol *ps_skey_enable;
static t_symbol *ps_skey_title;
static t_symbol *ps_skey_types;
static t_symbol *ps_skey_id;

void ext_main(void *r)
{
	t_class *c;

	c = class_new("bs.msg.receive", (method)_bs_msg_receive_new, (method)_bs_msg_receive_free, sizeof(t__bs_msg_receive), 0L, A_GIMME, 0);

    class_addmethod(c, (method)_bs_msg_receive_bang,				"bang",			0);
    class_addmethod(c, (method)_bs_msg_receive_anything,			"anything",		A_GIMME, 0);

	class_addmethod(c, (method)_bs_msg_receive_notify,			"notify",			A_CANT, 0);
	class_addmethod(c, (method)_bs_msg_receive_assist,			"assist",			A_CANT, 0);

	// the _bs_msg_receive has no inlet
	// adding a 'set' message is left as an exercise for the reader
	c->c_noinlet = true;

	class_register(CLASS_BOX, c);
	_bs_msg_receive_class = c;

	ps_nothing = gensym("");
	ps_conduit = gensym("conduit");

    ps_conduit_status   = gensym("status");
    ps_conduit_wh       = gensym("@wormhole");

    ps_attr_title       = gensym("title");
    ps_attr_wormhole    = gensym("wormhole");
    ps_attr_out         = gensym("out");
    ps_attr_outoff      = gensym("outoff");
    ps_attr_automatic   = gensym("auto");
    ps_attr_enabled     = gensym("enable");

    ps_attr_val_replace_drop = gensym("><");

    ps_msg_free                 = gensym("free");
    ps_msg_subscribe_attach     = gensym("subscribe_attach");
    ps_msg_sendmessage          = gensym("sendmessage");
    ps_msg_sendwormhole         = gensym("sendwormhole");

    ps_skey_enable  = gensym("_enable");
    ps_skey_title   = gensym("_title");
    ps_skey_types   = gensym("_types");
    ps_skey_id      = gensym("_id");

}

/**********************************************************
 bang will cause to output the current values
 **********************************************************/
void _bs_msg_receive_bang(t__bs_msg_receive *x)
{
    _bs_msg_receive_outputCurrentValue(x);
}

/**********************************************************
 all messages enter here
 **********************************************************/
void _bs_msg_receive_anything(t__bs_msg_receive *x, t_symbol *s, long ac, t_atom *av){
    _bs_msg_receive_set_attributes(x, s, ac, av);
    _bs_msg_receive_outputCurrentValue(x);
}

/**********************************************************
 method to receive notification from the subscribed conduits
 **********************************************************/
void _bs_msg_receive_notify(t__bs_msg_receive *x, t_symbol *s, t_symbol *msg, void *sender, void *data)
{
	//object_post((t_object *)x, "_bs_msg_receive_notify: %s", msg->s_name);
    if (msg == ps_msg_sendmessage) { // the conduit is calling with a message from 'master'
        t_atomarray *aa = (t_atomarray *)data;

        _bs_msg_receive_output(x, aa);
    } else if (msg == ps_msg_sendwormhole) {
        t_atomarray *aa = (t_atomarray *)data;

        _bs_msg_receive_output(x, aa);
    } else if (msg == ps_msg_free) {
        // the conduit is disappearing, no more servers
        // but we remain subscribed, so there's nothing to do here
        // if a new master appears, we will be called with 'subscribe_attach'
        // and automatically be attached.
        //-post("freed conduit from subscriber: %s", x->s_msgAddress->s_name);
    } else if (msg == ps_msg_subscribe_attach) { // the conduit is calling with a message from 'master'
        // if set to automatic (by default) the object will receive status changes directly
        t_object *conduit = (t_object *)sender;
        t_symbol *senderID = object_method(conduit,gensym("getid"));

        // is senderID is 'ps_nothing', the status conduit has called this method and should be ignored
        if(senderID && senderID != ps_nothing){
            //subscribe to status conduit if id has changed
            _bs_msg_receive_updateSubscribeStatus(x, senderID);
            // the message conduit has been received, so we
            _bs_msg_receive_outputCurrentValue(x);
        }
    } else if (msg == ps_skey_title) { // the status conduit is calling with a title change from 'master'
        // if set to automatic (by default) the object will receive status changes directly
        if(x->automatic){
            t_symbol *m = (t_symbol *)data;
            //-post("received update about address change: %s", m->s_name);

            t_atom argv[1];
            atom_setsym(argv, m);
            _bs_msg_receive_set_attributes(x, ps_attr_title, 1, argv);
            _bs_msg_receive_outputCurrentValue(x);
        }
    } else if (msg == ps_skey_enable) { // the status conduit is calling with a enable change from 'master'
        if(x->automatic){
            long m = (long)data;

            t_atom argv[1];
            atom_setlong(argv, m);
            _bs_msg_receive_set_attributes(x, ps_attr_enabled, 1, argv);
            //-post("received update about enable: %ld", x->enabled);

            _bs_msg_receive_outputCurrentValue(x);
        }
    }
}

/**********************************************************
// create message address symbol <result> = <address>::<message>
 **********************************************************/
void _bs_msg_receive_makeStatusAddress(t_symbol *message, t_symbol *address, t_symbol **result){
    char* s_message = message->s_name;
    char* s_address = address->s_name;
    const char* prepend = "::";
    char* s_myAddress = sysmem_newptr(strlen(s_message)+strlen(s_address)+strlen(prepend) + 1);
    strcpy(s_myAddress, s_address); /* copy name into the new var */
    strcat(s_myAddress, prepend); /* add the appendix */
    strcat(s_myAddress, s_message); /* add the appendix */
    *result = gensym(s_myAddress);
    sysmem_freeptr(s_myAddress);
}


/**********************************************************
 output the current value of the subscribed message
 ***********************************************************/
void _bs_msg_receive_outputCurrentValue(t__bs_msg_receive *x){
    if (x->s_msgAddress && x->s_msgAddress != ps_nothing) {

        // get the conduit object
        t_object *conduit = object_findregistered(ps_conduit, x->s_msgAddress);

        // if successfull...
        if (conduit) {

            // get the current value
            t_atomarray *aa = object_method(conduit,gensym("getatomarray"));

            if(aa){
                _bs_msg_receive_output(x, aa);
            }
        }
    }
}

/**********************************************************
 general method to output the values
 **********************************************************/
void _bs_msg_receive_output(t__bs_msg_receive *x, t_atomarray *aa){
    if(x->enabled == 0){
        if(x->s_myOutOff){
            //-post("_bs_msg_receive_output: %s / %s", x->s_msgAddress->s_name, atom_getsym(x->s_myOutOff->av)->s_name);
            outlet_anything(x->out, NULL, x->s_myOutOff->ac, x->s_myOutOff->av);
            //outlet_anything(x->out, atom_getsym(x->s_myOutOff->av), x->s_myOutOff->ac - 1, x->s_myOutOff->av + 1);
        }
    } else {
        if(x->s_myReplace){
            if(x->s_myReplace == ps_attr_val_replace_drop){
                //-post("replace empty %ld", aa->ac - 1);
                if(A_SYM != atom_gettype(aa->av + 1)){
                    // if the first item of the list is not a symbol..
                    outlet_list(x->out, NULL, aa->ac - 1, aa->av + 1);
                } else {
                    if(aa->ac - 1 > 1){
                        outlet_anything(x->out, atom_getsym(aa->av + 1), aa->ac - 2, aa->av + 2);
                    } else {
                        outlet_anything(x->out, atom_getsym(aa->av + 1), 0, NIL);
                    }
                }
            } else {
                //-post("replace %s", x->s_myReplace->s_name);
                outlet_anything(x->out, x->s_myReplace, aa->ac - 1, aa->av + 1);
            }
        } else {
            outlet_anything(x->out, atom_getsym(aa->av), aa->ac - 1, aa->av + 1);
        }
    }
}

/**********************************************************
 sets the attributes
 ***********************************************************/
void _bs_msg_receive_set_attributes(t__bs_msg_receive *x, t_symbol *s, long ac, t_atom *av){
    if(s == ps_attr_title){
        if(ac == 1){
            x->s_myTitle = atom_getsym(&av[0]);

            //set s_myAddress
            _bs_msg_receive_updateAttributes(x);

            // subscribe to message DO THIS BEFORE STATUS SUBSCRIPTION
            _bs_msg_receive_updateSubscription(x);

            // find the dictionary with this address
            t_dictionary *dict = dictobj_findregistered_clone(x->s_myAddress);

            if(dict){
                t_symbol *newId = NULL;
                // get the id
                dictionary_getsym(dict, ps_skey_id, &newId);


                // get the enabled
                t_atom *newargv; // the atom array pointer
                long newargc = 0;       // the array size
                dictionary_getatoms(dict, ps_skey_enable, &newargc, &newargv);
                if(newargc == 2){
                    x->enabled = newargv[1].a_w.w_long;
                }

                //-post("found dictionary with id '%s'", newId->s_name);
                //-post(" set enabled to '%ld'", x->enabled);

                if(newId){
                    // subscribe to status conduit
                    _bs_msg_receive_updateSubscribeStatus(x, newId);
                }

                object_free(dict);
            }

        } else {
            post("invalid number of values (%i) for attribute '%s'", ac, s->s_name);
        }
    } else if(s == ps_attr_wormhole){
        if(ac == 1){
            if(atom_gettype(&av[0]) == A_LONG && atom_getlong(&av[0]) == 1){
                x->s_myReplace = NULL;
                x->s_myMessage = ps_conduit_wh;

                _bs_msg_receive_updateSubscription(x);
            }
        } else {
            post("invalid number of values (%i) for attribute '%s'", ac, s->s_name);
        }
    } else if(s == ps_attr_automatic){
        if(ac == 1){
            x->automatic = (long) atom_getlong(&av[0]);
        } else {
            post("invalid number of values (%i) for attribute '%s'", ac, s->s_name);
        }
    } else if(s == ps_attr_enabled){
        if(ac == 1){
            x->enabled = (long) atom_getlong(&av[0]);
        } else {
            post("invalid number of values (%i) for attribute '%s'", ac, s->s_name);
        }
    } else if(_bs_msg_receive_set_attributes_similar(ps_attr_outoff->s_name, s->s_name)){
        if(ac > 0){
            // first check for outoff -> since 'out' is also similar of outoff!!!
            if(x->s_myOutOff){
                object_free(x->s_myOutOff);
            }
            x->s_myOutOff = atomarray_new(ac, av);
        }
    } else if(_bs_msg_receive_set_attributes_similar(ps_attr_out->s_name, s->s_name)){
        if(ac > 1){
            x->s_myReplace = atom_getsym(&av[1]);
        }
        x->s_myMessage = atom_getsym(&av[0]);

        _bs_msg_receive_updateSubscription(x);

        if (ac > 2){
            post("invalid number of values (%i) for attribute '%s'", ac, s->s_name);
        }
    } else {
        post("unknown attribute '%s'", s->s_name);
    }
}

/**********************************************************
 checks if the shorter string looks the same as
 the beginning go the longer string

 returns 0 if they dont, 1 if they do.
 ***********************************************************/
int _bs_msg_receive_set_attributes_similar(char *s_shorter, char *s_longer){
    long t = strlen(s_shorter);
    if (t <= strlen(s_longer)){
        for (int i = 0; i < t; i++){
            if (s_shorter[i] != s_longer[i])
                return 0;  // They are different
        }
    } else {
        return 0; // the shorter needs to be <= then the longer...
    }
    return 1;  // They must be similar
}

/**********************************************************
 Helperfuntion for _bs_msg_receive_set_attributes
 to update the attributes 's_myAddress'
 ***********************************************************/
void _bs_msg_receive_updateAttributes(t__bs_msg_receive *x){
    char* msgAddress;
    const char* prepend = "msg::";
    msgAddress = sysmem_newptr(strlen(x->s_myTitle->s_name)+1+strlen(prepend));
    strcpy(msgAddress, prepend);
    strcat(msgAddress, x->s_myTitle->s_name);
    x->s_myAddress = gensym(msgAddress);
    sysmem_freeptr(msgAddress);
}

/**********************************************************
 updates the subscription for the conduit.
    this function needs to called each time
    's_myTitle' or 's_myMessage' have been changed
 ***********************************************************/
void _bs_msg_receive_updateSubscription(t__bs_msg_receive *x){
    if(x->s_myTitle && x->s_myMessage){

        // if 's_msgAddress' is set, we first have to unsubscribe
        if (x->s_msgAddress && x->s_msgAddress != ps_nothing) {
            object_unsubscribe(ps_conduit, x->s_msgAddress, ps_conduit, x);
        }

        char* msgAddress;
        char* s_message = x->s_myMessage->s_name;
        char* s_address = x->s_myTitle->s_name;
        const char* inbetween = "::";
        const char* prepend = "msg::";
        msgAddress = sysmem_newptr(strlen(prepend) + strlen(s_address) + strlen(inbetween) + strlen(s_message) + 1);
        strcpy(msgAddress, prepend);
        strcat(msgAddress, s_address);
        strcat(msgAddress, inbetween);
        strcat(msgAddress, s_message);
        x->s_msgAddress = gensym(msgAddress);
        sysmem_freeptr(msgAddress);

        if (x->s_msgAddress && x->s_msgAddress != ps_nothing) {
            // subscribe to this name in the conduit namespace
            // we will be automatically notified and attached when the server appears
            object_subscribe(ps_conduit, x->s_msgAddress, ps_conduit, x);
        }
    }
}

/**********************************************************
 subscribe to the status conduit
 if @auto = 1
 **********************************************************/
void _bs_msg_receive_updateSubscribeStatus(t__bs_msg_receive *x, t_symbol *id){
    if(id && x->automatic && id != x->s_myID){
        // subscribe to status conduit...
        //-post("_bs_msg_receive subscriber has found a conduit with id '%s'", id->s_name);
        x->s_myID = id;

        // if there is a 's_statusAddress', wt need first to unscubsribe
        if(x->s_statusAddress){
            object_unsubscribe(ps_conduit, x->s_statusAddress, ps_conduit, x);
        }
        // set the new s_statusAddress
        _bs_msg_receive_makeStatusAddress(ps_conduit_status, id, &x->s_statusAddress);

        //-post("_bs_msg_receive subscribing to conduit with id '%s'", x->s_statusAddress->s_name);
        // subscribe to the status
        object_subscribe(ps_conduit, x->s_statusAddress, ps_conduit, x);
    }
}

/**********************************************************
 SUPPORT: set assist info
 ***********************************************************/
void _bs_msg_receive_assist(t__bs_msg_receive *x, void *b, long m, long a, char *s)
{
    if (m == ASSIST_INLET) { // inlets, of which we have none...
        sprintf(s, "I am inlet %ld", a);
    }
    else {	// outlet
        sprintf(s, "Messages received from my dark master %s", x->s_msgAddress->s_name);
    }
}

/**********************************************************
 SUPPORT: free the object
 ***********************************************************/
void _bs_msg_receive_free(t__bs_msg_receive *x)
{
    // be neat and unsubscribe
    object_unsubscribe(ps_conduit, x->s_msgAddress, ps_conduit, x);
    if(x->s_statusAddress){
        object_unsubscribe(ps_conduit, x->s_statusAddress, ps_conduit, x);
    }

    if(x->s_myOutOff){
        object_free(x->s_myOutOff);
    }
}

/**********************************************************
 SUPPORT: create the new class
 ***********************************************************/
void *_bs_msg_receive_new(t_symbol *s, long argc, t_atom *argv)
{
    t__bs_msg_receive *x = NULL;

    if ((x = (t__bs_msg_receive *)object_alloc(_bs_msg_receive_class))) {

        //setup the inlets
        x->m_proxy_inlet2 = proxy_new((t_object *)x, 2, &x->m_in);
        x->m_proxy_inlet1 = proxy_new((t_object *)x, 1, &x->m_in);

        //setup the outlet;
        x->out = outlet_new(x, NULL);
        x->automatic = 1;
        x->enabled = 1;
        x->s_statusAddress = NULL;

        // Parse the box attributes
        long i, j;

        for (i=0; i < argc; i++) {
            //-post("argument type :%s", argv[i].a_w.w_sym->s_name);
            if(argv[i].a_type == A_SYM && argv[i].a_w.w_sym->s_name[0] == '@'){
                // the attribute name without the '@' at the beginning
				char* attributeName = sysmem_newptr(strlen(argv[i].a_w.w_sym->s_name));
				memcpy( attributeName, &argv[i].a_w.w_sym->s_name[1], strlen(argv[i].a_w.w_sym->s_name));
                attributeName[strlen(argv[i].a_w.w_sym->s_name) - 1] = '\0';

                // go through all the subsequent atoms and
                // check if they have an '@' at the beginning.
                // if not, count up
                i++;
                long count = 0;
                long exitIndex = i;
                for (j=i; j < argc; j++){
                    // if the atom is a symbol, check if it starts with an '@'
                    if(argv[j].a_type == A_SYM){
                        if(argv[j].a_w.w_sym->s_name[0] == '@'){
                            break;
                        }
                    }
                    count++;
                    exitIndex = j;
                }
                //-post("  found values :%i", count);

                t_symbol *news = gensym(attributeName);
                t_atom *newargv = NULL; // the atom array pointer
                long newargc = 0;       // the array size
                char alloc;             // a helper variable

                // allocate an atom array
                if(atom_alloc_array(count, &newargc, &newargv, &alloc)  == MAX_ERR_NONE){
                    // copy the relevant atoms into the atom array
                    sysmem_copyptr(&argv[i], newargv, sizeof(t_atom) * count);

                    // send the whole package to the set_attibute function
                    _bs_msg_receive_set_attributes(x, news, newargc, newargv);
                }
                // free the memory.
                if(newargv){
                    sysmem_freeptr(newargv);
                }
				sysmem_freeptr(attributeName);

                i = exitIndex;
            }
        }
    }

    return (x);
}
