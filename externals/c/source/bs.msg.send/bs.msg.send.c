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
	@send
	bs_msg_send - sparck messageing system - sending messages
*/

#include "ext.h"
#include "ext_obex.h"
#include "ext_strings.h"
#include "ext_dictobj.h"
#include "ext_common.h"
#include "jpatcher_api.h"
#include "jpatcher_utils.h"

#define MAX_NUM_ITEMS 12

////////////////////////// object struct
//
//  s_myTitle           =        <title>
//  s_myAppendix        =               <appendix>
//  s_myName            =        <title><appendix>
//  s_myAddress         =   msg::<title><appendix>
//  message             =                           <message>
//  message address     =   msg::<title><appendix>::<message>

typedef struct _bs_msg_send
{
	t_object					ob;         // the object itself (must be first)
    long                        m_in;     // space for the inlet number used by all the proxies
    void                        *m_proxy_inlet1;
    void                        *m_proxy_inlet2;
    void                        *m_proxy_inlet3;

    t_dictionary                *dictionary;	// the actual dictionary

    t_symbol                    *s_myID;        //  unique id of this node
    t_symbol                    *s_myTitle;     //        <title>
    t_symbol                    *s_myAppendix;  //               <appendix>
    t_symbol                    *s_myName;      //        <title><appendix>
    t_symbol                    *s_myAddress;   //   msg::<title><appendix>
    t_hashtab                   *s_myMessages;  //                           <message>
    // message address                               msg::<title><appendix>::<message>

    bool                        addressHasChanged;
    long                        numTypes;
    t_symbol                    *s_myTypes[MAX_NUM_ITEMS];

    long                        unique;
    long                        enabled;

    t_object					*s_myStatusConduit;     // conduit to send status messages
    t_object					*s_myWormholeConduit;   // conduit to send wormhole messages
} t_bs_msg_send;

typedef struct _conduit
{
	t_object					ob;
    t_atomarray                 *lastList;
    t_symbol                    *id;
    t_symbol                    *message;
    t_symbol                    *address;
    long						usagecount;
    long						setcount;
} t_conduit;

///////////////////////// function prototypes
void *bs_msg_send_new(t_symbol *s, long argc, t_atom *argv);
void bs_msg_send_free(t_bs_msg_send *x);
void bs_msg_send_assist(t_bs_msg_send *x, void *b, long m, long a, char *s);
void bs_msg_send_notify(t_bs_msg_send *x, t_symbol *s, t_symbol *msg, void *sender, void *data);

// public functions:
void bs_msg_send_bang(t_bs_msg_send *x);
void bs_msg_send_int(t_bs_msg_send *x, long n);
void bs_msg_send_float(t_bs_msg_send *x, double f);
void bs_msg_send_anything(t_bs_msg_send *x, t_symbol *s, long ac, t_atom *av);

//private functions
bool bs_msg_send_make_unique_address(t_bs_msg_send *x, t_symbol *title, t_symbol *appendix);

bool bs_msg_send_attr_title_set(t_bs_msg_send *x, long ac, t_atom *av);
bool bs_msg_send_attr_appendix_set(t_bs_msg_send *x, long ac, t_atom *av);
bool bs_msg_send_attr_unique_set(t_bs_msg_send *x, long ac, t_atom *av);
bool bs_msg_send_attr_enable_set(t_bs_msg_send *x, long ac, t_atom *av);
bool bs_msg_send_attr_types_set(t_bs_msg_send *x, long ac, t_atom *av);

void bs_msg_send_set_attributes(t_bs_msg_send *x, t_symbol *s, long ac, t_atom *av);
void bs_msg_send_update_attributes(t_bs_msg_send *x);
void bs_msg_send_update_dictionary(t_bs_msg_send *x);
void bs_msg_send_makeMessageAddress(t_symbol *message, t_symbol *address, t_symbol **result);
t_max_err bs_msg_send_attr_update_set(t_bs_msg_send *x, t_object *attr, long argc, t_atom *argv);


void *conduit_new(t_symbol *id, t_symbol *message, t_symbol *address);
void conduit_free(t_conduit *x);
void conduit_retain(t_conduit *x);
void conduit_release(t_conduit *x);
int conduit_reregister(t_conduit *x, t_symbol *address);
void conduit_setatomarray(t_conduit *x, t_atomarray *lastValue);
t_atomarray* conduit_getatomarray(t_conduit *x);
t_symbol* conduit_getid(t_conduit *x);


//////////////////////// global class pointer variable
t_class *bs_msg_send_class;
t_class *conduit_class;

static t_symbol *ps_nothing;
static t_symbol *ps_conduit;

static t_symbol *ps_conduit_status;
static t_symbol *ps_conduit_wh;

static t_symbol *ps_attr_title;
static t_symbol *ps_attr_appendix;
static t_symbol *ps_attr_enable;
static t_symbol *ps_attr_setTitleMsg;
static t_symbol *ps_attr_types;
static t_symbol *ps_attr_unique;

static t_symbol *ps_skey_enable;
static t_symbol *ps_skey_title;
static t_symbol *ps_skey_types;
static t_symbol *ps_skey_id;

static t_symbol *ps_msg_sendmessage;
static t_symbol *ps_msg_sendwormhole;

void ext_main(void *r)
{
	t_class *c;

	// Create the "bs_msg_send" class. This is roughly equivalent to the 'send' object in Max.
	// It takes a single symbolic argument and sends messages on to 'servant' objects with the same name.
	c = class_new("bs.msg.send", (method)bs_msg_send_new, (method)bs_msg_send_free, sizeof(t_bs_msg_send), 0L, A_GIMME, 0);

	class_addmethod(c, (method)bs_msg_send_bang,				"bang",			0);
	class_addmethod(c, (method)bs_msg_send_int,				"int",			A_LONG, 0);
	class_addmethod(c, (method)bs_msg_send_float,			"float",		A_FLOAT, 0);
	class_addmethod(c, (method)bs_msg_send_anything,			"list",			A_GIMME, 0);

	class_addmethod(c, (method)bs_msg_send_anything,			"anything",		A_GIMME, 0);

	class_addmethod(c, (method)bs_msg_send_assist,			"assist",		A_CANT, 0);
	class_addmethod(c, (method)bs_msg_send_notify,			"notify",		A_CANT, 0);

    /*
     We don't define any attributes, because we want to be able to receive attribute names as messages.
     */

	class_register(CLASS_BOX, c);
	bs_msg_send_class = c;

	// Create the "conduit" class. This class is actually responsible for the communication
	// with the 'servant' objects, and we need it in order to support multiple 'bs_msg_send' objects
	// sending to one or several 'servant' objects.
	c = class_new("conduit", (method)conduit_new, (method)conduit_free, sizeof(t_conduit), 0L, A_CANT, 0);

    class_addmethod(c, (method)conduit_retain,          "retain", 0);
    class_addmethod(c, (method)conduit_release,         "release", 0);
    class_addmethod(c, (method)conduit_reregister,      "reregister", 0);
    class_addmethod(c, (method)conduit_setatomarray,	"setatomarray", 0);
    class_addmethod(c, (method)conduit_getatomarray,    "getatomarray", A_CANT, 0);
    class_addmethod(c, (method)conduit_getid,           "getid", A_CANT, 0);

	class_register(CLASS_NOBOX, c);
	conduit_class = c;

	ps_nothing          = gensym("");
	ps_conduit          = gensym("conduit");

    ps_conduit_status   = gensym("status");
    ps_conduit_wh       = gensym("@wormhole");

    ps_attr_title       = gensym("title");
    ps_attr_appendix    = gensym("appendix");
    ps_attr_enable      = gensym("enable");
    ps_attr_setTitleMsg = gensym("setmsgtitle");
    ps_attr_types       = gensym("types");
    ps_attr_unique      = gensym("unique");

    ps_skey_enable  = gensym("_enable");
    ps_skey_title   = gensym("_title");
    ps_skey_types   = gensym("_types");
    ps_skey_id      = gensym("_id");

    ps_msg_sendmessage          = gensym("sendmessage");
    ps_msg_sendwormhole         = gensym("sendwormhole");

}

void bs_msg_send_bang(t_bs_msg_send *x)
{
	bs_msg_send_anything(x, gensym("bang"), 0, NULL);
}

void bs_msg_send_int(t_bs_msg_send *x, long n)
{
	t_atom a;

	atom_setlong(&a, n);
    switch (proxy_getinlet((t_object *)x)) {
        case 0:
            bs_msg_send_anything(x, ps_attr_enable, 1, &a);
            break;
        case 3:
            bs_msg_send_anything(x, gensym("int"), 1, &a);
            break;
    }
}

void bs_msg_send_float(t_bs_msg_send *x, double f)
{
	t_atom a;

	atom_setfloat(&a, f);
	bs_msg_send_anything(x, gensym("float"), 1, &a);
}


// t_symbol *s      message selector
// long ac          length of atom array
// t_atom *av       argument atom array

void bs_msg_send_anything(t_bs_msg_send *x, t_symbol *s, long ac, t_atom *av)
{
    t_object *conduit = NULL;
    long hasNoConduit;
    switch (proxy_getinlet((t_object *)x)) {
        case 0:
            //-post("message received in inlet 0: '%s'", s->s_name);
            // if the message arrives at the inlet 0, it is an attribute
            if(s != ps_attr_appendix){
                bs_msg_send_set_attributes(x, s, ac, av);
            } else {
                object_post((t_object *)x, "cannot set '%s' via messages", s);
            }
            break;
        case 1: case 2:
            //-post("message received in inlet 1 or 2");
            hasNoConduit = hashtab_lookup(x->s_myMessages, s, &conduit);

            if(hasNoConduit && x->s_myAddress){
                //-post("->has no conduit, looking for '%s'", s->s_name);
                //found no result -> create a conduit for this message
                t_symbol *globalMsgAddress = NULL;
                bs_msg_send_makeMessageAddress(s, x->s_myAddress, &globalMsgAddress);

                conduit = object_findregistered(ps_conduit, globalMsgAddress);
                if(!conduit){
                    //-post("->creating conduit for '%s'", globalMsgAddress->s_name);

                    object_new(CLASS_NOBOX, ps_conduit, x->s_myID, s, x->s_myAddress);
                    conduit = object_findregistered(ps_conduit, globalMsgAddress);

                    //-post("->registered conduit '%s'", globalMsgAddress->s_name);

                    // tell the conduit that we are using it
                    // store it inside the hashtable
                    object_method(conduit, gensym("retain"));

                    //-post("->retained conduit '%s'", globalMsgAddress->s_name);
                } else {
                    //-post("->found conduit and store it");
                }

                hashtab_store(x->s_myMessages, s, conduit);
            } else {
                //-post("->found conduit");
            }
            if (conduit) {

                // and now store/send the message
                t_atomarray *aa = NULL;
                t_atom *argv = NULL;
                long argc = 0;
                char alloc;

                // allocate an array for 'argv' (and the number of atoms inside 'argc') with the length of number of atoms plus one symbol
                if (atom_alloc_array(ac + 1, &argc, &argv, &alloc) == MAX_ERR_NONE) {

                    // assembles the message and the arguments array
                    atom_setsym(argv, s); // sets the symbol into the atom
                    sysmem_copyptr(av, argv + 1, sizeof(t_atom) * ac);// copies the content from 'av' to argv+1
                    aa = atomarray_new(argc, argv); // allocate a new atomarray with the new array of atoms from 'argv'
                    if (aa) {
                        if(proxy_getinlet((t_object *)x) == 1){
                            // and sends it to the conduit object if it comes from inlet 1
                            object_method(conduit, gensym("setatomarray"), aa);
                        }
                        // and notifies the servant objects that subscribed to this conduit object
                        object_notify(conduit, ps_msg_sendmessage, aa);

                        //object_free(aa); We dont need to free this object since it lives on insied the conduit object
                    }
                    // but we need to free the atomarray
                    sysmem_freeptr(argv);
                }
            }


            break;
        case 3:
            //-post("message received in inlet 3");
            if (x->s_myWormholeConduit) {

                //-post("bs_msg_send_anything: sending message via wormhole...");
                // and now store/send the message
                t_atomarray *aa = NULL;
                t_atom *argv = NULL;
                long argc = 0;
                char alloc;

                // allocate an array for 'argv' (and the number of atoms inside 'argc') with the length of number of atoms plus one symbol
                if (atom_alloc_array(ac + 1, &argc, &argv, &alloc) == MAX_ERR_NONE) {

                    // assembles the message and the arguments array
                    atom_setsym(argv, s); // sets the symbol into the atom
                    sysmem_copyptr(av, argv + 1, sizeof(t_atom) * ac);// copies the content from 'av' to argv+1
                    aa = atomarray_new(argc, argv); // allocate a new atomarray with the new array of atoms from 'argv'
                    if (aa) {
                        // and notifies the servant objects that subscribed to this conduit object
                        object_notify(x->s_myWormholeConduit, ps_msg_sendwormhole, aa);

                        //We need to free this object
                        object_free(aa);
                    }
                    // and we need to free the atomarray
                    sysmem_freeptr(argv);
                }
                //-post("bs_msg_send_anything: ... done sending");
            }
            break;
    }

    //post(" title value :%s", x->s_myTitle->s_name);


}

/**********************************************************
    makes message address symbol
 ***********************************************************/
void bs_msg_send_makeMessageAddress(t_symbol *message, t_symbol *address, t_symbol **result){
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

bool bs_msg_send_make_unique_address(t_bs_msg_send *x, t_symbol* myTitle, t_symbol* myAppendix){
    t_symbol *oldAddress = x->s_myAddress;

    bool valid = false;
    char* msgName;
    msgName = sysmem_newptr(strlen(myTitle->s_name)+1+strlen(myAppendix->s_name));
    strcpy(msgName, myTitle->s_name); /* copy name into the new var */
    strcat(msgName, myAppendix->s_name); /* add the appendix */
    //x->s_myName = gensym(msgName);
 
    char* msgAddress;
    const char* prepend = "msg::";
    msgAddress = sysmem_newptr(strlen(msgName)+1+strlen(prepend));
    strcpy(msgAddress, prepend);
    strcat(msgAddress, msgName);
    
    
    t_dictionary* dictionary = dictobj_findregistered_retain(gensym(msgAddress));
    if(!dictionary || x->unique == 0 || x->s_myTitle->s_name == myTitle->s_name){
        //-post("address unoccupied: %s", msgAddress);
        // we apply these values if the address is unique or @unique = 0.
        x->s_myName = gensym(msgName);
        x->s_myAddress = gensym(msgAddress);
        if(oldAddress != x->s_myAddress){
            x->addressHasChanged = true;
        };
        valid = true;
    } else {
        if(x->unique != 0){
            //_post("address occupied: %s", msgAddress);
        }
    }
    if (dictionary){
        dictobj_release(dictionary);
    }
    
    sysmem_freeptr(msgAddress);
    sysmem_freeptr(msgName);

    return valid;
}

bool bs_msg_send_attr_title_set(t_bs_msg_send *x, long ac, t_atom *av){
    if(ac == 1){
         x->s_myTitle = atom_getsym(&av[0]);
         return true;
    }
    object_error((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, ps_attr_title->s_name);
    return false;
}

bool bs_msg_send_attr_appendix_set(t_bs_msg_send *x, long ac, t_atom *av){
    if(ac == 1){
         x->s_myAppendix = atom_getsym(&av[0]);
         return true;
    }
    object_error((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, ps_attr_appendix->s_name);
    return false;
}

bool bs_msg_send_attr_unique_set(t_bs_msg_send *x, long ac, t_atom *av){
    if(ac == 1){
        x->unique = (long) atom_getlong(&av[0]);
        return true;
    }
    object_error((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, ps_attr_unique->s_name);
    return false;
}

bool bs_msg_send_attr_enable_set(t_bs_msg_send *x, long ac, t_atom *av){
    if(ac == 1){
        x->enabled = (long) atom_getlong(&av[0]);
        return true;
    }
    object_error((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, ps_attr_enable->s_name);
    return false;
}

bool bs_msg_send_attr_types_set(t_bs_msg_send *x, long ac, t_atom *av){
    if(ac > 0 && ac <= MAX_NUM_ITEMS){
        long i;
        for(i=0; i < ac; i++){
            x->s_myTypes[i] = atom_getsym(&av[i]);
        }
        x->numTypes = ac;
        return true;
    }
    object_error((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, ps_attr_types->s_name);
    return false;
}

/**********************************************************
    sets the attributes
 ***********************************************************/
void bs_msg_send_set_attributes(t_bs_msg_send *x, t_symbol *s, long ac, t_atom *av){
    //-post("set attribute: '%s'", s->s_name);
    if(s == ps_attr_title || s == ps_attr_setTitleMsg){
        if(x->unique == 1){
            if(ac == 1 && bs_msg_send_make_unique_address(x, atom_getsym(&av[0]), x->s_myAppendix)){
                if(bs_msg_send_attr_title_set(x, ac, av)){
                    bs_msg_send_update_attributes(x);
                }
            } else {
                 object_error((t_object *)x, "title+appendix is not unique. value not accepted");
            }
        } else {
            object_error((t_object *)x, "title can only be set when @unique = 1 (default)");
        }
    } else if(s == ps_attr_appendix){
        object_error((t_object *)x, "@appendix can only be set during initialization");
    } else if(s == ps_attr_unique){
        object_error((t_object *)x, "@unique can only be set during initialization");
    } else if(s == ps_attr_enable){
        if(bs_msg_send_attr_enable_set(x, ac, av)){
            // the dictionary needs to be updated before the status conduit is updated
            bs_msg_send_update_dictionary(x);

            // send a status message to subscribed objects about the change:
            if(x->s_myStatusConduit){
                //post("sending notification on enable...");
                object_notify(x->s_myStatusConduit, ps_skey_enable, x->enabled);
            }
        }
    } else if(s == ps_attr_types){
        if(bs_msg_send_attr_types_set(x, ac, av)){
            // the dictionary needs to be updated before the status conduit is updated
            bs_msg_send_update_dictionary(x);

            // send a status message to subscribed objects about the change: we send again an enable
            // message, since this will cause the receiver to check if all is matching....
            if(x->s_myStatusConduit){
                //post("sending notification on enable...");
                object_notify(x->s_myStatusConduit, ps_skey_enable, x->enabled);
            }
        }
    } else {
        object_error((t_object *)x, "unknown attribute '%s'", s->s_name);
    }
}

/**********************************************************

    Helperfuntion for bs_msg_send_set_attributes
        to update the attributes 's_myName' and 's_myAddress'

***********************************************************/
void bs_msg_send_update_attributes(t_bs_msg_send *x){
    //post("bs_msg_send_update_attributes: ....");
    // if the address has changed
    if(x->addressHasChanged){
        // manage dictionary
        if(!x->dictionary){
            //-post("create new directory at address: '%s'", x->s_myAddress->s_name);
            // we only create the dictionary once there is a address
            x->dictionary = dictobj_findregistered_retain(x->s_myAddress);
            if(!x->dictionary){
                //-post("create dictionary: '%s'", x->s_myAddress->s_name);
                x->dictionary = dictionary_new();
                dictobj_register(x->dictionary, &x->s_myAddress);
                //cpost(" making dict: create new dict: '%s'", x->s_myAddress->s_name);
           } else {
                //-post(" making dict: it already exist: '%s'", x->s_myAddress->s_name);
            }
        } else if (x->unique == 1) {
            //-post("move directory to new address: '%s'", x->s_myAddress->s_name);
            // reregister the dictionary
            dictobj_unregister(x->dictionary);
            //cpost("set new dict address: '%s'", x->s_myAddress->s_name);
            dictobj_register(x->dictionary, &x->s_myAddress);
        }

        // the dictionary needs to be updated after the attributes
        bs_msg_send_update_dictionary(x);

        // manage wormhole conduit
        if(!x->s_myWormholeConduit){
            // create the wormhole conduit
            t_symbol *globalMsgAddress = NULL;
            bs_msg_send_makeMessageAddress(ps_conduit_wh, x->s_myAddress, &globalMsgAddress);

            //-post("bs_msg_send_update_attributes: created address for wormhole conduit: '%s'", globalMsgAddress->s_name);

            x->s_myWormholeConduit = object_findregistered(ps_conduit, globalMsgAddress);
            if(!x->s_myWormholeConduit){
                object_new(CLASS_NOBOX, ps_conduit, x->s_myID, ps_conduit_wh, x->s_myAddress);
                x->s_myWormholeConduit = object_findregistered(ps_conduit, globalMsgAddress);
                // tell the conduit that we are using it
                object_method(x->s_myWormholeConduit, gensym("retain"));
            } else {
                //post("found old wormhole conduit: '%s'", globalMsgAddress->s_name);
            }
        }

        //post("bs_msg_send_update_attributes: reregister address for conduits from '%s' to '%s'", oldAddress, x->s_myAddress->s_name);

        if(x->s_myMessages){
            // reregister all the conduits if address has changed
            if(hashtab_methodall(x->s_myMessages, gensym("reregister"), x->s_myAddress)!= 0){
                // there was a serious error:
                object_error((t_object *)x, "This error should not happen: You are trying to rename a bs.msg.send object to an already used name ('%s'). This is NOT allowed since it can generate unmanageable results. Max may crash upon closing this parent patcher.", x->s_myTitle->s_name);
            }
            if(object_method(x->s_myWormholeConduit, gensym("reregister"), x->s_myAddress)!= 0){
                // there was a serious error:
                object_error((t_object *)x, "This error should not happen: You are trying to rename a bs.msg.send object to an already used name ('%s'). This is NOT allowed since it will generate unmanageable results. Max may crash upon closing this parent patcher.", x->s_myTitle->s_name);
            }
        }

        // send a status message to subscribed objects about the change:
        object_notify(x->s_myStatusConduit, ps_skey_title, x->s_myName);
        
        x->addressHasChanged = false;
    }
}


/**********************************************************
 Helperfuntion for bs_msg_send_set_attributes
 to update the dictionary
 ***********************************************************/
void bs_msg_send_update_dictionary(t_bs_msg_send *x){
    // update dictionary
    if(x->dictionary){
        t_atom temp;

        {
            t_atom *newargv; // the atom array pointer
            long newargc = 0;       // the array size
            char alloc;             // a helper variable

            // remove previous entry
            if(dictionary_hasentry(x->dictionary, ps_skey_enable)){
                dictionary_deleteentry(x->dictionary, ps_skey_enable);
            }

            if(atom_alloc_array(2, &newargc, &newargv, &alloc) == MAX_ERR_NONE){
                atom_setsym(&temp, ps_skey_enable);
                sysmem_copyptr(&temp, &newargv[0], sizeof(t_atom));
                // as long as the title is not set, the message is officiall disabled,
                // though it is able to gather messages...
                if(x->s_myTitle == x->s_myID){
                    atom_setlong(&temp, 0);
                } else {
                    atom_setlong(&temp, x->enabled);
                }
                sysmem_copyptr(&temp, &newargv[1], sizeof(t_atom));
                dictionary_appendatoms(x->dictionary, ps_skey_enable, newargc, newargv);
                //object_free(newargv);
            }
        }

        if(x->s_myTypes){
            t_atom *newargv; // the atom array pointer
            long newargc = 0;       // the array size
            char alloc;             // a helper variable

            // remove previous entry
            if(dictionary_hasentry(x->dictionary, ps_skey_types)){
                dictionary_deleteentry(x->dictionary, ps_skey_types);
            }

            if(atom_alloc_array(x->numTypes, &newargc, &newargv, &alloc) == MAX_ERR_NONE){
                //atom_setsym(&temp, ps_skey_types);
                //sysmem_copyptr(&temp, &newargv[0], sizeof(t_atom));
                atom_setsym_array(x->numTypes, &newargv[0], x->numTypes, x->s_myTypes);
                dictionary_appendatoms(x->dictionary, ps_skey_types, newargc, newargv);
                //object_free(newargv);
            }
        }

        dictionary_appendsym(x->dictionary, ps_skey_title, x->s_myName);
        dictionary_appendsym(x->dictionary, ps_skey_id, x->s_myID);
    }
}

//////////////////////////////////////////////////////////
//
//                MANAGE FUNCTIONS
//
//////////////////////////////////////////////////////////


void bs_msg_send_notify(t_bs_msg_send *x, t_symbol *s, t_symbol *msg, void *sender, void *data)
{
    object_post((t_object *)x, "bs_msg_send_notify: %s", msg->s_name);
    if (msg == gensym("free")) {
        // the conduit is going away, this would be fatal, but will never actually happen
    }
}

void bs_msg_send_assist(t_bs_msg_send *x, void *b, long m, long a, char *s)
{
    if (m == ASSIST_INLET) { // inlet
        sprintf(s, "Messages for inlet %ld", m);
    }
    else {	// outlets, of which we have none...
        sprintf(s, "I am outlet %ld", a);
    }
}

void bs_msg_send_free(t_bs_msg_send *x)
{
    if(x->unique == 0){
        // if is NOT a unique sender, the dictionary should
        //  only be release, but not unregistered
        dictobj_release(x->dictionary);
    }
    if(x->unique == 1){
        // if is a unique sender, the dictionary should
        //  removed (and therefore unregistered)

        // but first we disable this dictionary, so the receiver is not deceived..
        t_atom temp;
        atom_setlong(&temp, 0);
        bs_msg_send_set_attributes(x, ps_attr_enable, 1, &temp);


        // despited the documentation that dictobj_unregister() doesnt need to be called:
        dictobj_unregister(x->dictionary);
        // we need to call it, otherwise it doesnt seem to cleanup properly.

        object_free(x->dictionary); // and then remove it for certain...

        // tell the conduit that we are finished with it. When the number of
        // users drops to 0, the conduit self-destructs to avoid leaking memory.
        hashtab_methodall(x->s_myMessages, gensym("release"));

        if(x->s_myStatusConduit)
            object_method(x->s_myStatusConduit, gensym("release"));

        if(x->s_myWormholeConduit)
            object_method(x->s_myWormholeConduit, gensym("release"));
    }

    hashtab_chuck(x->s_myMessages);
}

void *bs_msg_send_new(t_symbol *s, long argc, t_atom *argv)
{
    t_bs_msg_send *x = NULL;

    if ((x = (t_bs_msg_send *)object_alloc(bs_msg_send_class))) {


        // setting up the inlets...
        x->m_proxy_inlet3 = proxy_new((t_object *)x, 3, &x->m_in);
        x->m_proxy_inlet2 = proxy_new((t_object *)x, 2, &x->m_in);
        x->m_proxy_inlet1 = proxy_new((t_object *)x, 1, &x->m_in);

        x->unique = 1;
        x->enabled = 1;
        x->addressHasChanged = false;
        
        x->s_myID = symbol_unique();
        x->s_myTitle = x->s_myID;
        x->s_myAppendix = ps_nothing;
        x->s_myName = NULL;
        x->s_myAddress = NULL;
        x->s_myMessages = NULL;
        
        // Parse the box attributes
        long i, j;

        for (i=0; i < argc; i++) {
            //post("argument type :%s", argv[i].a_w.w_sym->s_name);
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
                //post("  found values :%i", count);

                t_symbol *news = gensym(attributeName);
                t_atom *newargv = NULL; // the atom array pointer
                long newargc = 0;       // the array size
                char alloc;             // a helper variable

                // allocate an atom array
                if(atom_alloc_array(count, &newargc, &newargv, &alloc)  == MAX_ERR_NONE){
                    // copy the relevant atoms into the atom array
                    sysmem_copyptr(&argv[i], newargv, sizeof(t_atom) * count);
                    // send the whole package to the set_attibute function
                    if(news == ps_attr_title || news == ps_attr_setTitleMsg){
                        bs_msg_send_attr_title_set(x, newargc, newargv);
                    } else if(news == ps_attr_appendix){
                        bs_msg_send_attr_appendix_set(x, newargc, newargv);
                    } else if(news == ps_attr_unique){
                        bs_msg_send_attr_unique_set(x, newargc, newargv);
                    } else if(news == ps_attr_enable){
                        bs_msg_send_attr_enable_set(x, newargc, newargv);
                    } else if(news == ps_attr_types){
                        bs_msg_send_attr_types_set(x, newargc, newargv);
                    }
                }
                // free the memory.
                sysmem_freeptr(newargv);
				sysmem_freeptr(attributeName);
                i = exitIndex;
            }
        }
        
        // if this node is not unique, than the id is set to the name
        if(x->unique == 0){
            if(x->s_myTitle == x->s_myID){
                object_error((t_object *)x, "when using attribute @unique = 0, the attribute @title needs to be an argument");
                return NULL;
            }
        }
 
        // create the address, and test if the address is
        // unique - if not and @unique = 1, then bail.
        if(!bs_msg_send_make_unique_address(x, x->s_myTitle, x->s_myAppendix)){
            if(x->unique == 1){
                object_error((t_object *)x, "when using attribute @unique = 1, the address (= @title + @appendix) needs to be unique");
                return NULL;
            }
        }
        
        // now its time to update the attributes
        bs_msg_send_update_attributes(x);
        // the dictionary needs to be updated aftre the attributes
        bs_msg_send_update_dictionary(x);

        // initialize new hashtable
        x->s_myMessages = (t_hashtab *)hashtab_new(0);

        //
        // create the status conduit
        //
        t_symbol *globalMsgAddress = NULL;
        bs_msg_send_makeMessageAddress(ps_conduit_status, x->s_myID, &globalMsgAddress);

        //-post("created address for status conduit: '%s'", globalMsgAddress->s_name);
        
        x->s_myStatusConduit = object_findregistered(ps_conduit, globalMsgAddress);

        if(!x->s_myStatusConduit){
            object_new(CLASS_NOBOX, ps_conduit, NULL, ps_conduit_status, x->s_myID);
            // We need to set the ID of this conduit to 'NULL' to make sure the subscriber
            // doesnt try to subscribe to the status conduit again and again and....
            x->s_myStatusConduit = object_findregistered(ps_conduit, globalMsgAddress);
        }
         
        // finally tell the conduit that we are using it
        object_method(x->s_myStatusConduit, gensym("retain"));
    }
    //post("created object done: '%s'", x->s_myTitle);

    return (x);
}

//////////////////////////////////////////////////////////
//
//                      CONDUIT
//
//////////////////////////////////////////////////////////

// custom method to set a variable inside the conduit object
void conduit_setatomarray(t_conduit *x, t_atomarray *lastValue){
    object_free(x->lastList);

    x->lastList = lastValue;
}

t_atomarray* conduit_getatomarray(t_conduit *x){
    return x->lastList;
}

t_symbol* conduit_getid(t_conduit *x){
    return x->id;
}

void conduit_retain(t_conduit *x){
    t_symbol *globalMsgAddress = NULL;
    bs_msg_send_makeMessageAddress(x->message, x->address, &globalMsgAddress);

    //post("retaining conduit '%s'", globalMsgAddress->s_name);

    x->usagecount++;
}

void conduit_release(t_conduit *x){
    t_symbol *globalMsgAddress = NULL;
    bs_msg_send_makeMessageAddress(x->message, x->address, &globalMsgAddress);

    //post("releasing conduit '%s'", globalMsgAddress->s_name);

    x->usagecount--;
	if (x->usagecount <= 0) { // commit suicide
        object_free(x->lastList);
		object_free(x);
	}
}

// is beeing called when the object is cleaned up
// or when the 'object_register' call was unsuccessfull...
void conduit_free(t_conduit *x){
    t_symbol *globalMsgAddress = NULL;
    bs_msg_send_makeMessageAddress(x->message, x->address, &globalMsgAddress);

    //post("freeeing conduit: ID '%s'", x->id->s_name);
    //post("freeeing conduit '%s'", globalMsgAddress->s_name);
    object_unregister(x);
}

int conduit_reregister(t_conduit *x, t_symbol *address){
    int error = 0;

    t_symbol *globalMsgAddress = NULL;

    x->address = address;
    // first register under the new address
    bs_msg_send_makeMessageAddress(x->message, x->address, &globalMsgAddress);

    long acnt = 0;          // the array size
    t_symbol ***allNames;   // a helper variable
    //allNames = (t_symbol***) malloc(sizeof(t_symbol));
    allNames = (t_symbol***) sysmem_newptr(sizeof(t_symbol));

    // check if address has already been used:
    object_register_getnames(ps_conduit, &acnt, allNames);
    for (long i=0; i < acnt; i++) {
        if(strcmp((*allNames)[i]->s_name, globalMsgAddress->s_name) == 0){
            error = 1;
            //post("conduit_reregister ('%s') warning: already existing ID '%s'", globalMsgAddress->s_name, (*allNames)[i]->s_name);
        }
    }
    sysmem_freeptr(allNames);

    if(error == 0){
        //post("conduit_reregister: ID '%s'", x->id->s_name);
        object_unregister(x);
        object_register(ps_conduit, globalMsgAddress, x);
        //post("conduit_reregister: registered conduit at '%s'", globalMsgAddress->s_name);
    }
    return error;
}

void *conduit_new(t_symbol *id, t_symbol *message, t_symbol *address){
	t_conduit *x = NULL;
    //cpost("creating conduit ...");

    if (message && message != ps_nothing){// && address && address != ps_nothing) {
		x = (t_conduit *)object_alloc(conduit_class);
        //post("creating conduit for message: '%s'", message->s_name);
		if (x) {
			x->usagecount = 0;
            x->id      = id;
            x->address = address;
            x->message = message;

            t_symbol *globalMsgAddress = NULL;
            bs_msg_send_makeMessageAddress(message, address, &globalMsgAddress);

            //post("created conduit '%s'", globalMsgAddress->s_name);

			object_register(ps_conduit, globalMsgAddress, x);
            //post("registered conduit '%s'", globalMsgAddress->s_name);
		}
	}
	return x;
}
