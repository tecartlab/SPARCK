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

// autowatch = 1;

var owner = this.patcher;

// set up inlets/outlets/assist strings
inlets = 3;
outlets = 4;
setinletassist(0,"settings");
setinletassist(1,"refresh");
setinletassist(0,"selection");
setoutletassist(0,"umenu");
setoutletassist(1,"receive");
setoutletassist(2,"set");
setoutletassist(3,"store");

var outKeysCount = 6;
if (jsarguments.length > 1)
    outKeysCount = jsarguments[1];

var IN_0 = 0;
var IN_1 = 1;
var IN_2 = 2;

var OUT_UMENU = 0;
var OUT_STORAGE = 1;
var OUT_MESSAGES = 2;
var OUT_SETUP = 3;

var undefined = "off";
var address = undefined;
var selection = undefined;
var addressFilter = undefined;
var types = undefined;
var offwormholeAddress = undefined;
var item = 0;
var offname = "select..."
var ignore = null;
var hasoffselection = 1;

var isInitialized = false;

var outKeys = new Array(outKeysCount);
var menuList = null;
//owner.connect(this.box, 0, p_outlet, 0);

// global varables and code
var vpattern = ".*";
var vmodifier = "i";
var vregexp = new RegExp(vpattern,vmodifier);

//outlet(OUT_UMENU, "bang");

var	myDict;

var displayToDict = new HashTable();

var appGlobal = new Global("bs::app::global");
var debug = (appGlobal.debug == 1 || appGlobal.debug == 7)? true:false;

// set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select, 7=receive
function debugger(_val){
	debug = (_val == 1 || _val == 7)? true: false;
}

function dpost(_poststring){
    if(debug){
        if(ignore != null){
            post("selector(" + ignore + "): " + _poststring + "\n");
        } else {
            post("selector<" + types + ">: " + _poststring + "\n");
        }
    }
}

function loadbang(){
    //post("selection on initialization = " + selection + " ... reseting...\n");
    //selection = undefined;
}

function anything()
{
    //post("receive selector for inlet="+inlet + ": " + messagename + " = " + arguments[0] + "\n");
 	if (inlet==IN_1){
		if(messagename.indexOf("types") == 0){
			types = arrayfromargs(arguments);
            dpost("types = " + types);
            if(isInitialized){
                // in this case the types message is recieved
                // after the initial initialisation
                checkSelection(selection);
            }
 		} else if(messagename.indexOf("offname") == 0){
            dpost("offname = " + arguments[0]);
			if(selection = offname)
				selection = arguments[0];
			offname = arguments[0];
 		} else if(messagename.indexOf("select") == 0){
            dpost("select = " + arguments[0]);
            if(arguments[0] != null){
                checkSelection(arguments[0]);
            } else {
                checkSelection(offname);
            }
		} else if(messagename.indexOf("update") == 0){
            dpost("update..");
			checkSelection(selection);
		} else if(messagename.indexOf("done") == 0){
            dpost("done..");
            isInitialized = true;
			checkSelection(selection);
		} else if(messagename.indexOf("hasoffselection") == 0){
            dpost("hasoffselection = " + arguments[0]);
			hasoffselection = arguments[0];
		} else if(messagename.indexOf("offwormhole") == 0){
            dpost("offwormhole = " + arguments[0]);
			offwormholeAddress = arguments[0];
		} else if(messagename.indexOf("ignore") == 0){
            dpost("ignore = " + arguments[0]);
			ignore = arguments[0];
			checkSelection(selection);
		} else if(messagename.indexOf("filter") == 0){
			addressFilter = arrayfromargs(arguments);
            dpost("filter = " + addressFilter);
            /*
			vpattern = "(" + addressFilter[0] + ")";
			for(var i = 1; i < addressFilter.length; i++){
				vpattern = vpattern + ".+(" + addressFilter[i] + ")";
			}
			vregexp.compile(vpattern,vmodifier);
            */
		} else {
            outlet(OUT_MESSAGES, messagename, arrayfromargs(arguments));
        }
	} else if (inlet==IN_2){
		if(messagename.indexOf("bang") != -1){
            dpost("refresh menu");
//			dpost("receive bang on inlet 1\n");
			checkSelection(selection);
		} else if(messagename.indexOf("_enable") != -1){
            dpost("received internal enable message.");
            checkSelection(selection);
		} else if(messagename.indexOf("_title") != -1){
			dpost("receive _title "+arguments[0]+" for item: "+ item +"\n");
			checkSelection(arguments[0]);
			//cpost("still no crash: " + arguments[0] + "\n");
		}
	} else if (inlet==IN_0){
		if(messagename.indexOf("item") != -1){
			dpost("select item: " + arguments[0]);
			item = arguments[0];
		}else if(messagename.indexOf("select") != -1){
            dpost("select = " + arguments[0]);
			checkSelection(arguments[0]);
		}else if(messagename.indexOf("text") != -1){
//           dpost("loaded text: " + arguments[0] + "\n");
            if(arguments[0] != selection){
                dpost("loaded = " + arguments[0]);
                checkSelection(arguments[0]);
            }
		}
	}
}

function checkSelection(sel){
    dpost("checkSelection = " + sel);
	dumpNewMenu();

	if(sel != "_init_" && sel != offname){
        dpost("selection "+sel+"\n");
		selection = getEnabledSelection(sel);
		address = displayToDict.getItem(selection);
		dpost("address: " + address + " selection: " + selection + "\n");
		if(address != undefined){
            dpost("... address is defined");
			myDict = new Dict("msg::"+address);
			outlet(OUT_SETUP, "about", "address", address);
			outlet(OUT_SETUP, "about", "dict", "dictionary", "msg::"+address);
			outlet(OUT_SETUP, "about", "enable_local", 1);
			outlet(OUT_SETUP, "about", "types", myDict.get("_types"));
			if(dictIsEnabled(myDict)){
                dpost("... dict is enabled");
				outlet(OUT_UMENU, "setsymbol", selection);
				outlet(OUT_SETUP, "about", "enable_remote", 1);
				outlet(OUT_SETUP, "about", "enable", 1);
               if(offwormholeAddress != undefined){
                    outlet(OUT_SETUP, "wormhole", "set");
                }
			} else {
                dpost("... dict is NOT enabled");
                selection = getDisabledSelection(selection);
				outlet(OUT_UMENU, "append", selection);
				outlet(OUT_UMENU, "setsymbol", selection);
				menuList.push(selection);
				outlet(OUT_SETUP, "about", "enable_remote", 0);
				outlet(OUT_SETUP, "about", "enable", 0);
                if(offwormholeAddress != undefined){
                    outlet(OUT_SETUP, "wormhole", "set", offwormholeAddress);
                }
			}
		}else{
            dpost("... address is not defined");
            // this address doesnt exist yet because it was set during loading the project
            // but we add it anyway to our list and select it
            address = sel;
            selection = getDisabledSelection(selection);
            outlet(OUT_UMENU, "append", selection);
            outlet(OUT_UMENU, "setsymbol", selection);
			menuList.push(selection);
            outlet(OUT_SETUP, "about", "enable_remote", 0);
			outlet(OUT_SETUP, "about", "enable_local", 0);
			outlet(OUT_SETUP, "about", "enable", 0);
            if(offwormholeAddress != undefined){
                outlet(OUT_SETUP, "wormhole", "set", offwormholeAddress);
            }
		}
	} else {
        dpost("selection is OFF");
		selection = offname;
		address = undefined;
        outlet(OUT_SETUP, "about", "address");
		outlet(OUT_SETUP, "about", "enable", 0);
		outlet(OUT_SETUP, "about", "enable_local", 0);
	}

    //storing the selection
	outlet(OUT_STORAGE, "symbol", selection);
	outlet(OUT_STORAGE, "range", menuList);

    if(sel != offname){
        outlet(OUT_SETUP, "receiver", "title", address);
    } else if(isInitialized){
        outlet(OUT_SETUP, "receiver", "title", "@titleoff");
    }


	outlet(OUT_SETUP, "about", "done");
}

function getEnabledSelection(ad){
    dpost("ad is 1:" + ad + "\n");
	if(ad.indexOf("<(") == 0)
		return ad.substring(2, ad.length -2);
    dpost("ad is 2:" + ad + "\n");
	return ad;
}

function getDisabledSelection(ad){
	return "<(" + getEnabledSelection(ad) + ")>";
}

function dumpNewMenu(){
    dpost("dumpNewMenu");

	displayToDict.clear();
	outlet(OUT_UMENU, "clear");
	var tempDict = new Dict(undefined);
	var names = tempDict.getnames();
	menuList = new Array();
	for(var i = 0; i < names.length; i++){
		if(names[i].indexOf("msg::") == 0){
			var dictname = names[i].substring(5);
			if(dictMatchesFilter(dictname)){
				tempDict = new Dict(names[i]);
				if(dictIsValid(tempDict)){
					displayToDict.setItem(dicttitle(tempDict), dictname);
					if(dictIsEnabled(tempDict)){
                        dpost("add = "+ dicttitle(tempDict));
						menuList.push(dicttitle(tempDict));
						//outlet(OUT_UMENU, "append", dicttitle(tempDict));
					}
				}
			}
		}
	}
	menuList.sort();
	for(var i = 0; i < menuList.length; i++){
        outlet(OUT_UMENU, "append", menuList[i]);
	}
	if(hasoffselection == 1){
		menuList.unshift(offname);
		outlet(OUT_UMENU, "insert", 0, offname);
	}
}

function dicttitle(dict){
	return dict.get("_title");
}

function dictIsEnabled(dict){
	if(dict.contains("_enable") && dict.get("_enable")[1] == "1"){
		return true;
	}
	dpost(dict.get("_enable")[1] + "\n");

	return false;
}

function dictIsValid(dict){
	if(dict.contains("_enable") && dict.contains("_title") && !keyMatchesIgnore(dict.get("_title"))){
		if(dict.contains("_types")){
			dpost("parsing for msgs: " + dict.get("_title") + "\n");
			var _typ = dict.get("_types");
			//dpost("   has types: " + _typ + "\n");
            if(typeof _typ == "string"){
                _typ = new Array(_typ);
            }
			if(types == undefined){
				return true;
			}
			for(var i = 0; i < types.length; i++){
				dpost("looking for types: " + types[i] + "\n");
                for(var j = 0; j < _typ.length; j++){
				    if(types[i] === _typ[j]){
                        dpost("  found " + types[i] + "\n");
				        return true;
				    }
                }
			}
		} else {
			if(types == undefined)
				return true; // if dict has no type defined and no filter is set
		}
		return false;
	}
	return false;
}

function dictMatchesFilter(dictname){
	if (addressFilter == undefined || vregexp.exec(dictname))
		return true;
	return false;
}

function keyMatchesIgnore(keyName){
 //   post("keyName is " + typeof keyName + "\n");
	dpost("dictname=" + keyName + " + ignore=" + ignore + "\n");
	if (ignore != null && keyName.indexOf(ignore) == 0 && keyName.length == ignore.length)
		return true;
	return false;
}

function HashTable(obj)
{
    this.length = 0;
    this.items = {};
    for (var p in obj) {
        if (obj.hasOwnProperty(p)) {
            this.items[p] = obj[p];
            this.length++;
        }
    }

    this.setItem = function(key, value)
    {
        var previous = undefined;
        if (this.hasItem(key)) {
            previous = this.items[key];
        }
        else {
            this.length++;
        }
        this.items[key] = value;
        return previous;
    }

    this.getItem = function(key) {
        return this.hasItem(key) ? this.items[key] : undefined;
    }

    this.hasItem = function(key)
    {
        return this.items.hasOwnProperty(key);
    }

    this.removeItem = function(key)
    {
        if (this.hasItem(key)) {
            previous = this.items[key];
            this.length--;
            delete this.items[key];
            return previous;
        }
        else {
            return undefined;
        }
    }

    this.keys = function()
    {
        var keys = [];
        for (var k in this.items) {
            if (this.hasItem(k)) {
                keys.push(k);
            }
        }
        return keys;
    }

    this.values = function()
    {
        var values = [];
        for (var k in this.items) {
            if (this.hasItem(k)) {
                values.push(this.items[k]);
            }
        }
        return values;
    }

    this.each = function(fn) {
        for (var k in this.items) {
            if (this.hasItem(k)) {
                fn(k, this.items[k]);
            }
        }
    }

    this.clear = function()
    {
        this.items = {}
        this.length = 0;
    }
}

// error("What just happened?\n");
// post(1,2,3,"violet",a);
// cpost (any arguments); -> to system console:
// messnamed (Max object name, message name, any arguments)
// messnamed("flower","bang”);
// a = new Array(900,1000,1100);
// jsthis -> this instance
//  autowatch (1) -> global code -> autoload after js-file was edited and saved

// patcher arguments
//if (jsarguments.length >= 2)
//    outlets = jsarguments[1];

// var stuff;
//  function anything(val)
//  {
//    if (arguments.length) // were there any arguments?
//      stuff[messagename] = val;
//  }

// function anything()
//  {
//    var a = arrayfromargs(messagename,arguments);

//    a.sort();
//    outlet(OUT_UMENU,a);
//  }
