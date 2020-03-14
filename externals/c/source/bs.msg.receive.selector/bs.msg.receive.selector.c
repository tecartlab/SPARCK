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
	@recieve selector
	bs_msg_receiveselector - sparck messageing system - sending messages
 */

#include "ext.h"
#include "ext_obex.h"
#include "ext_dictobj.h"
#include "jpatcher_api.h"
#include <math.h>

typedef struct {
    void                        *m_out;
    t_symbol                    *s_myMessage;
    t_symbol                    *s_myReplace;

    /// needs to carry multiple values !!!!!


    t_atomarray                 *s_myOutOff;
    t_symbol                    *s_msgAddress;
    short                       m_on;
} t_message;


////////////////////////// object struct
typedef struct _bs_msg_receiveselector
{
	t_object					ob;			// the object itself (must be first)
    long                        m_in;     // space for the inlet number used by all the proxies
    void                        *m_proxy_inlet1;
    void                        *m_proxy_inlet2;

    t_symbol                    *s_statusAddress;

    t_symbol                    *s_myID;        //  unique id of this node
    t_symbol                    *s_myTitle;     //        <title>
    t_symbol                    *s_myAddress;   //   msg::<title>

    // message address                               msg::<title>::<message>

    long                        automatic;
    long                        enabled_remote;
    long                        enabled_local;
	void						*statusOutlet;
    long                        s_msgnum;
    t_message                   *s_messages;
} t_bs_msg_receiveselector;


///////////////////////// function prototypes
void *bs_msg_receiveselector_new(t_symbol *s, long argc, t_atom *argv);
void bs_msg_receiveselector_free(t_bs_msg_receiveselector *x);
void bs_msg_receiveselector_assist(t_bs_msg_receiveselector *x, void *b, long m, long a, char *s);
void bs_msg_receiveselector_notify(t_bs_msg_receiveselector *x, t_symbol *s, t_symbol *msg, void *sender, void *data);

// public
void bs_msg_receiveselector_bang(t_bs_msg_receiveselector *x);
void bs_msg_receiveselector_anything(t_bs_msg_receiveselector *x, t_symbol *s, long ac, t_atom *av);

// private
void bs_msg_receiveselector_set_attributes(t_bs_msg_receiveselector *x, t_symbol *s, long ac, t_atom *av);
void bs_msg_receiveselector_updateAttributes(t_bs_msg_receiveselector *x);
void bs_msg_receiveselector_updateAllSubscriptions(t_bs_msg_receiveselector *x);
void bs_msg_receiveselector_updateSubscription(t_bs_msg_receiveselector *x, t_message *m);
void bs_msg_receiveselector_updateSubscribeStatus(t_bs_msg_receiveselector *x, t_symbol *id);
void bs_msg_receiveselector_outputCurrentValue(t_bs_msg_receiveselector *x);
void bs_msg_receiveselector_output(t_bs_msg_receiveselector *x, t_message *m, t_atomarray *aa);
void bs_msg_receiveselector_makeStatusAddress(t_symbol *message, t_symbol *address, t_symbol **result);
int bs_msg_receiveselector_set_attributes_similar(char *s_shorter, char *s_longer);

long bs_msg_receiveselector_get_attribute_index(char *s_shorter, char *s_longer);

//////////////////////// global class pointer variable
t_class *bs_msg_receiveselector_class;

static t_symbol *ps_nothing;
static t_symbol *ps_conduit;

static t_symbol *ps_conduit_status;
static t_symbol *ps_conduit_wh;

static t_symbol *ps_msg_offtitle;
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

	c = class_new("bs.msg.receive.selector", (method)bs_msg_receiveselector_new, (method)bs_msg_receiveselector_free, sizeof(t_bs_msg_receiveselector), 0L, A_GIMME, 0);

    class_addmethod(c, (method)bs_msg_receiveselector_bang,				"bang",			0);
    class_addmethod(c, (method)bs_msg_receiveselector_anything,			"anything",		A_GIMME, 0);

	class_addmethod(c, (method)bs_msg_receiveselector_notify,			"notify",			A_CANT, 0);
	class_addmethod(c, (method)bs_msg_receiveselector_assist,			"assist",			A_CANT, 0);

	class_register(CLASS_BOX, c);
	bs_msg_receiveselector_class = c;

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

    ps_msg_offtitle             = gensym("@titleoff");
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
void bs_msg_receiveselector_bang(t_bs_msg_receiveselector *x)
{
    bs_msg_receiveselector_outputCurrentValue(x);
}

/**********************************************************
 all messages enter here
 **********************************************************/
void bs_msg_receiveselector_anything(t_bs_msg_receiveselector *x, t_symbol *s, long ac, t_atom *av){
    // any change to the running receiver will be treated as if it was switched off
    if(s == ps_attr_enabled){
        if(ac == 1){
            x->enabled_local = (long) atom_getlong(&av[0]);
        } else {
            object_error((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, ps_attr_enabled);
        }
    } else {
        bs_msg_receiveselector_set_attributes(x, s, ac, av);
    }

    bs_msg_receiveselector_outputCurrentValue(x);
}

/**********************************************************
 method to receive notification from the subscribed conduits
 **********************************************************/
void bs_msg_receiveselector_notify(t_bs_msg_receiveselector *x, t_symbol *s, t_symbol *msg, void *sender, void *data){
	//-post("bs_msg_receiveselector_notify: %s", msg->s_name);
    if (msg == ps_msg_sendmessage) { // the conduit is calling with a message from 'master'
        t_atomarray *aa = (t_atomarray *)data;
        t_message *m;
        for (long i = 0; i < x->s_msgnum; i++){
            m = x->s_messages + i;
            if (m->s_myMessage == atom_getsym(aa->av)){
                bs_msg_receiveselector_output(x, m, aa);
                break;
            }
        }
    } else if (msg == ps_msg_sendwormhole) {
        t_atomarray *aa = (t_atomarray *)data;
        t_message *m;
        // the last index is the wormhole message
        m = x->s_messages + x->s_msgnum - 1;
        //-post("bs_msg_receiveselector_notify: wormhole: %s", m->s_msgAddress->s_name);
        bs_msg_receiveselector_output(x, m, aa);
    } else if (msg == ps_msg_free) {
        // the conduit is disappearing, no more servers
        // but we remain subscribed, so there's nothing to do here
        // if a new master appears, we will be called with 'subscribe_attach'
        // and automatically be attached.
    } else if (msg == ps_msg_subscribe_attach) { // the conduit is calling with a message from 'master'
        //-post("bs_msg_receiveselector_notify: received subscibe attach");

        // if set to automatic (by default) the object will receive status changes directly
        t_object *conduit = (t_object *)sender;
        t_symbol *senderID = object_method(conduit,gensym("getid"));

        // is senderID is 'ps_nothing', the status conduit has called this method and should be ignored
        if(senderID && senderID != ps_nothing){

            //subscribe to status conduit if id has changed
            bs_msg_receiveselector_updateSubscribeStatus(x, senderID);

            // get the current value
            t_atomarray *aa = object_method(conduit,gensym("getatomarray"));

            if(aa){
                // the message conduit has been received, so we output the array
                t_message *m;
                for (long i = 0; i < x->s_msgnum; i++){
                    m = x->s_messages + i;
                    if (m->s_myMessage == atom_getsym(aa->av)){
                        bs_msg_receiveselector_output(x, m, aa);
                        break;
                    }
                }
            }
        }

    } else if (msg == ps_skey_title) { // the status conduit is calling with a title change from 'master'
        // if set to automatic (by default) the object will receive status changes directly
        if(x->automatic){
            t_symbol *m = (t_symbol *)data;
            //-post("bs_msg_receiveselector_notify: received status update about address change: %s", m->s_name);

            t_atom argv[1];
            atom_setsym(argv, m);
            // output to the status outlet
            outlet_anything(x->statusOutlet, ps_skey_title, 1, argv);

            bs_msg_receiveselector_set_attributes(x, ps_attr_title, 1, argv);
            bs_msg_receiveselector_outputCurrentValue(x);
        }
    } else if (msg == ps_skey_enable) { // the status conduit is calling with a title change from 'master'
        if(x->automatic){
            long m = (long)data;
            //-post("bs_msg_receiveselector_notify: received status update about enable: %ld", m);

            t_atom argv[1];
            atom_setlong(argv, m);
            // output to the status outlet
            outlet_anything(x->statusOutlet, ps_skey_enable, 1, argv);

            bs_msg_receiveselector_set_attributes(x, ps_attr_enabled, 1, argv);
            bs_msg_receiveselector_outputCurrentValue(x);
        }
    }
}

/**********************************************************
// create message address symbol <result> = <address>::<message>
 **********************************************************/
void bs_msg_receiveselector_makeStatusAddress(t_symbol *message, t_symbol *address, t_symbol **result){
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
void bs_msg_receiveselector_outputCurrentValue(t_bs_msg_receiveselector *x){
    //-post("there are = '%ld' messages", x->s_msgnum);
    t_message *m;
    for (long i = 0; i < x->s_msgnum - 1; i++){ // we skip the wormhole (x->s_msgnum - 1)
        m = x->s_messages + i;
        if (m->s_msgAddress && m->s_msgAddress != ps_nothing) {

            // get the conduit object
            t_object *conduit = object_findregistered(ps_conduit, m->s_msgAddress);

            // if successfull...
            if (conduit) {

                // get the current value
                t_atomarray *aa = object_method(conduit,gensym("getatomarray"));

                if(aa){
                    bs_msg_receiveselector_output(x, m, aa);
                }
            } else {
                // if there is no conduit, we check if
                if(x->enabled_remote == 0 || x->enabled_local == 0 || x->s_myTitle == ps_msg_offtitle){
                    // in this case we get away settin the atom array to NULL
                    bs_msg_receiveselector_output(x, m, NULL);
                }
            }
        }
    }
}

/**********************************************************
 general method to output the values
 **********************************************************/
void bs_msg_receiveselector_output(t_bs_msg_receiveselector *x, t_message *m, t_atomarray *aa){
    //-post("bs_msg_receiveselector_output: ouput = '%s'", m->s_msgAddress->s_name);
    if(x->enabled_remote == 0 || x->enabled_local == 0 || x->s_myTitle == ps_msg_offtitle){
        if(m->s_myOutOff){
            if(A_SYM == atom_gettype(m->s_myOutOff->av)){
                // it is a symbol
                if(m->s_myOutOff->ac > 1){
                    outlet_anything(m->m_out, atom_getsym(m->s_myOutOff->av), m->s_myOutOff->ac - 1, m->s_myOutOff->av + 1);
                } else {
                    outlet_anything(m->m_out, atom_getsym(m->s_myOutOff->av), 0, NIL);
                }
            } else {
                //-post("bs_msg_receiveselector_output: ouputoff-list: %ld", m->s_myOutOff->ac);
                // if the first item of the list is not a symbol..
                outlet_list(m->m_out, 0L, m->s_myOutOff->ac, m->s_myOutOff->av);
            }
        } else {
            //-post("there is no outoff defined");
        }
    } else {
        if(m->s_myReplace){
            if(m->s_myReplace == ps_attr_val_replace_drop){
                if(A_SYM != atom_gettype(aa->av + 1)){
                    // if the first item of the list is not a symbol..
                    outlet_list(m->m_out, NULL, aa->ac - 1, aa->av + 1);
                } else {
                    // it is a symbol
                    if(aa->ac - 1 > 1){
                        outlet_anything(m->m_out, atom_getsym(aa->av + 1), aa->ac - 2, aa->av + 2);
                    } else {
                        outlet_anything(m->m_out, atom_getsym(aa->av + 1), 0, NIL);
                    }
                }
                //-post("replace empty %ld", aa->ac - 1);
            } else {
                //-post("replace %s", m->s_myReplace->s_name);
                //-post("bs_msg_receiveselector_output: %s / %s", m->s_msgAddress->s_name, m->s_myReplace->s_name);
                outlet_anything(m->m_out, m->s_myReplace, aa->ac - 1, aa->av + 1);
            }
        } else {
            //-post("normal");
            outlet_anything(m->m_out, atom_getsym(aa->av), aa->ac - 1, aa->av + 1);
        }
    }
}

/**********************************************************
 sets the attributes
 ***********************************************************/
void bs_msg_receiveselector_set_attributes(t_bs_msg_receiveselector *x, t_symbol *s, long ac, t_atom *av){
    if(s == ps_attr_title){
        if(ac == 1){
            // only change the title if it is a different string
            if(x->s_myTitle != atom_getsym(&av[0])){
                x->s_myTitle = atom_getsym(&av[0]);

                //set s_myAddress
                bs_msg_receiveselector_updateAttributes(x);

                // subscribe to message DO THIS BEFORE STATUS SUBSCRIPTION
                bs_msg_receiveselector_updateAllSubscriptions(x);

                // find the dictionary with this address
                t_dictionary *dict = dictobj_findregistered_clone(x->s_myAddress);

                if(dict){
                    t_symbol *newId = NULL;
                    // get the id
                    dictionary_getsym(dict, ps_skey_id, &newId);

                    // get the enabled_remote
                    t_atom *newargv; // the atom array pointer
                    long newargc = 0;       // the array size
                    dictionary_getatoms(dict, ps_skey_enable, &newargc, &newargv);
                    x->enabled_remote = newargv[1].a_w.w_long;

                    //-post("found dictionary with id '%s'", newId->s_name);
                    //-post(" set enabled_remote to '%ld'", x->enabled_remote);

                    // subscribe to status conduit
                    bs_msg_receiveselector_updateSubscribeStatus(x, newId);

                    object_free(dict);
                }
            }
        } else {
            object_post((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, s->s_name);
        }
    } else if(s == ps_attr_enabled){
        if(ac == 1){
            x->enabled_remote = (long) atom_getlong(&av[0]);
        } else {
            object_post((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, s->s_name);
        }
    } else if(bs_msg_receiveselector_set_attributes_similar(ps_attr_outoff->s_name, s->s_name)){
        if(ac > 0){
            long index = bs_msg_receiveselector_get_attribute_index(ps_attr_outoff->s_name, s->s_name);
            // first check for outoff -> since 'out' is also similar of outoff!!!
            t_message *m;
            m = x->s_messages + index;
            if(m->s_myOutOff){
                object_free(m->s_myOutOff);
            }
            m->s_myOutOff = atomarray_new(ac, av);
        }
    } else if(bs_msg_receiveselector_set_attributes_similar(ps_attr_out->s_name, s->s_name)){
        long index = bs_msg_receiveselector_get_attribute_index(ps_attr_out->s_name, s->s_name);
        t_message *m;
        m = x->s_messages + index;

        if(ac > 1){
            m->s_myReplace = atom_getsym(&av[1]);
        }
        m->s_myMessage = atom_getsym(&av[0]);

        bs_msg_receiveselector_updateSubscription(x, m);

        if (ac > 2){
            object_post((t_object *)x, "invalid number of values (%i) for attribute '%s'", ac, s->s_name);
        }
    } else {
        object_post((t_object *)x, "unknown attribute '%s'", s->s_name);
    }
}

/**********************************************************
 checks if the shorter string looks the same as
 the beginning go the longer string

 returns 0 if they dont, 1 if they do.
 ***********************************************************/
int bs_msg_receiveselector_set_attributes_similar(char *s_shorter, char *s_longer){
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
 Helperfuntion for bs_msg_receiveselector_set_attributes
 to get the attributes index (i.e. out1 -> 1)
 ***********************************************************/
long bs_msg_receiveselector_get_attribute_index(char *s_shorter, char *s_longer){
    long t = strlen(s_shorter);
    if (t < strlen(s_longer)){
        long s = strlen(s_shorter);
        long l = strlen(s_longer);
        long c = 0;
        long result = 0;
        for (long i = l - 1; i >= s; i--){
            result += (s_longer[i] - '0') * pow(10, c);
            c++;
        }
        return result - 1;
    }
    return 0;
}

/**********************************************************
 Helperfuntion for bs_msg_receiveselector_set_attributes
 to update the attributes 's_myAddress'
 ***********************************************************/
void bs_msg_receiveselector_updateAttributes(t_bs_msg_receiveselector *x){
    char* msgAddress;
    const char* prepend = "msg::";
    msgAddress = sysmem_newptr(strlen(x->s_myTitle->s_name)+1+strlen(prepend));
    strcpy(msgAddress, prepend);
    strcat(msgAddress, x->s_myTitle->s_name);
    x->s_myAddress = gensym(msgAddress);
    sysmem_freeptr(msgAddress);
}

/**********************************************************
 updates all subscriptions for the messages.
    this function needs to called each time
    's_myTitle' has been changed
 ***********************************************************/
void bs_msg_receiveselector_updateAllSubscriptions(t_bs_msg_receiveselector *x){
    for (long i = 0; i < x->s_msgnum; i++){
        bs_msg_receiveselector_updateSubscription(x, &x->s_messages[i]);
    }
}

/**********************************************************
 updates the subscription for the message.
    this function needs to called each time
    's_message' has been changed
 ***********************************************************/
void bs_msg_receiveselector_updateSubscription(t_bs_msg_receiveselector *x, t_message *m){
    if(x->s_myTitle && m->s_myMessage && m->s_myMessage != ps_nothing){

        // if 's_msgAddress' is set, we first have to unsubscribe
        if (m->s_msgAddress && m->s_msgAddress != ps_nothing) {
            object_unsubscribe(ps_conduit, m->s_msgAddress, ps_conduit, x);
        }

        char* msgAddress;
        char* s_message = m->s_myMessage->s_name;
        char* s_address = x->s_myTitle->s_name;
        const char* inbetween = "::";
        const char* prepend = "msg::";
        msgAddress = sysmem_newptr(strlen(prepend) + strlen(s_address) + strlen(inbetween) + strlen(s_message) + 1);
        strcpy(msgAddress, prepend);
        strcat(msgAddress, s_address);
        strcat(msgAddress, inbetween);
        strcat(msgAddress, s_message);
        m->s_msgAddress = gensym(msgAddress);
        sysmem_freeptr(msgAddress);

        if (m->s_msgAddress && m->s_msgAddress != ps_nothing) {
            //-post("bs_msg_receiveselector_updateSubscription: subscribing to conduit with address '%s'", m->s_msgAddress->s_name);
            // subscribe to this name in the conduit namespace
            // we will be automatically notified and attached when the server appears
            object_subscribe(ps_conduit, m->s_msgAddress, ps_conduit, x);
        }
    }
}

/**********************************************************
 subscribe to the status conduit
 if @auto = 1
 **********************************************************/
void bs_msg_receiveselector_updateSubscribeStatus(t_bs_msg_receiveselector *x, t_symbol *id){
    if(id && x->automatic && id != x->s_myID){
        // subscribe to status conduit...
        //-post("bs_msg_ng_receive subscriber has found a conduit with id '%s'", id->s_name);
        x->s_myID = id;

        // if there is a 's_statusAddress', wt need first to unscubsribe
        if(x->s_statusAddress){
            object_unsubscribe(ps_conduit, x->s_statusAddress, ps_conduit, x);
        }
        // set the new s_statusAddress
        bs_msg_receiveselector_makeStatusAddress(ps_conduit_status, id, &x->s_statusAddress);

        //-post("subscribing to status conduit with id '%s'", x->s_statusAddress->s_name);
        // subscribe to the status
        object_subscribe(ps_conduit, x->s_statusAddress, ps_conduit, x);
    }
}

/**********************************************************
 SUPPORT: set assist info
 ***********************************************************/
void bs_msg_receiveselector_assist(t_bs_msg_receiveselector *x, void *b, long m, long a, char *s)
{
    /*
     if (m == ASSIST_INLET) { // inlets, of which we have none...
     sprintf(s, "I am inlet %ld", a);
     }
     else {	// outlet
     sprintf(s, "Messages received from my dark master %s", x->s_msgAddress->s_name);
     }

     */
}

/**********************************************************
 SUPPORT: free the object
 ***********************************************************/
void bs_msg_receiveselector_free(t_bs_msg_receiveselector *x)
{
    // be neat and unsubscribe
    for (long i = 0; i < x->s_msgnum; i++){
        object_unsubscribe(ps_conduit, (x->s_messages + i)->s_msgAddress, ps_conduit, x);
        if((x->s_messages + i)->s_myOutOff){
            object_free((x->s_messages + i)->s_myOutOff);
        }
    }

    if(x->s_statusAddress){
        object_unsubscribe(ps_conduit, x->s_statusAddress, ps_conduit, x);
    }

    sysmem_freeptr(x->s_messages);
}

/**********************************************************
 SUPPORT: create the new class
 ***********************************************************/
void *bs_msg_receiveselector_new(t_symbol *s, long argc, t_atom *argv)
{
    t_bs_msg_receiveselector *x = NULL;

    if ((x = (t_bs_msg_receiveselector *)object_alloc(bs_msg_receiveselector_class))) {

        //setup the inlets
        x->m_proxy_inlet2 = proxy_new((t_object *)x, 2, &x->m_in);
        x->m_proxy_inlet1 = proxy_new((t_object *)x, 1, &x->m_in);

        //setup the status outlet;
        x->statusOutlet = outlet_new(x, 0L);

        // setup message outlets
        if(argv[0].a_type == A_LONG){
            x->s_msgnum = argv[0].a_w.w_long + 1;
        } else {
            error("invalid argument!");
        }
        long i, j;
        t_message *m;
        x->s_messages = (t_message *)sysmem_newptr((unsigned short)(x->s_msgnum * sizeof(t_message)));
        for (i=x->s_msgnum-1,m = x->s_messages + i; i >= 0; i--,m--) {
            m->m_out = outlet_new(x,0L);
            m->m_on = 0;
            m->s_msgAddress = NULL;
            m->s_myMessage = NULL;
            m->s_myOutOff = NULL;
            m->s_myReplace = NULL;
        }

        x->automatic = 1;
        x->s_myTitle = ps_msg_offtitle;
        x->enabled_local = 1;
        x->enabled_remote = 1;
        x->s_statusAddress = NULL;

        // Parse the box attributes
        for (i=1; i < argc; i++) {
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
                    bs_msg_receiveselector_set_attributes(x, news, newargc, newargv);
                }
                // free the memory.
                sysmem_freeptr(newargv);
				sysmem_freeptr(attributeName);

                i = exitIndex;
            }
        }

        // setting up wormhole
        m = x->s_messages + x->s_msgnum - 1;
        m->s_myMessage = ps_conduit_wh;
        bs_msg_receiveselector_updateSubscription(x, m);

        // if the title is still set to offtitle, we will force set it
        if(x->s_myTitle == ps_msg_offtitle){
            // set the default title
            t_atom argv[1];
            atom_setsym(argv, ps_msg_offtitle);
            bs_msg_receiveselector_set_attributes(x, ps_attr_title, 1, argv);
        }
    }

    return (x);
}
