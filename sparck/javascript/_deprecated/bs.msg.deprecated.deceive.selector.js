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

var outKeys = new Array(outKeysCount);
//owner.connect(this.box, 0, p_outlet, 0);

// global varables and code
var vpattern = ".*";
var vmodifier = "i";
var vregexp = new RegExp(vpattern,vmodifier);

//outlet(0, "bang");

var	myDict;

var displayToDict = new HashTable();

var appGlobal = new Global("bs::app::global");

var debug = (appGlobal.debug == 1)? true:false;

// set the debugger
function debugger(_val){
	debug = (_val == 1)? true: false;
}

function loadbang(){
}

function anything()
{
	if (inlet==2){
		//post("receive selector: inlet==2:" + messagename + " " + arrayfromargs(arguments) + "\n");
		if(messagename.indexOf("types") == 0){
			types = arrayfromargs(arguments);
            //debugpost("types = " + types);
		} else if(messagename.indexOf("offname") == 0){
			if(selection = offname)
				selection = arguments[0];
			offname = arguments[0];
            //debugpost("offname = " + offname);
		} else if(messagename.indexOf("select") == 0){
			checkSelection(arguments[0]);
            //debugpost("select = " + arguments[0]);
		} else if(messagename.indexOf("update") == 0){
			checkSelection(selection);
            //debugpost("updated\n");
		} else if(messagename.indexOf("done") == 0){
			checkSelection(selection);
            //debugpost("done");
		} else if(messagename.indexOf("hasoffselection") == 0){
			hasoffselection = arguments[0];
            //debugpost("hasoffselection = " + hasoffselection);
		} else if(messagename.indexOf("offwormhole") == 0){
			offwormholeAddress = arguments[0];
            //debugpost("offwormhole = " + offwormholeAddress);
		} else if(messagename.indexOf("ignore") == 0){
			ignore = arguments[0];
			checkSelection(selection);
            //debugpost("ignore = " + ignore);
		} else if(messagename.indexOf("filter") == 0){
			addressFilter = arrayfromargs(arguments);
			vpattern = "(" + addressFilter[0] + ")";
			for(var i = 1; i < addressFilter.length; i++){
				vpattern = vpattern + ".+(" + addressFilter[i] + ")";
			}
			vregexp.compile(vpattern,vmodifier);
            //debugpost("filter = " + addressFilter);
		} else if(messagename.indexOf("out") == 0){
			var indxStr = messagename.substring(3, messagename.length);
			var indx =  parseInt(indxStr) - 1;
			if(indx >= 0 && indx < outKeysCount)
				outKeys[indx] = arguments[0];
		}
	} else if (inlet==1){
		if(messagename.indexOf("bang") != -1){
            //debugpost("refresh menu");
//			post("receive bang on inlet 1\n");
			checkSelection(selection);
		} else if(messagename.indexOf("_enable") != -1){
			outlet(2, "about", "enable_remote", arguments[0]);
			outlet(2, "about", "enable", arguments[0]);
			if(arguments[0] == 1){
				refreshKeys();
 				outlet(0, "setitem", item, getEnabledSelection(selection));
			}
			else {
				outlet(0, "setitem", item, getDisabledSelection(selection));
			}
			outlet(2, "about", "done");
		} else if(messagename.indexOf("_title") != -1){
			//cpost("just bevore the crash: " + arguments[0] + "\n");
			checkSelection(arguments[0]);
//			post("receive _title inlet 1\n");
			//cpost("still no crash: " + arguments[0] + "\n");
		}
	} else if (inlet==0){
		if(messagename.indexOf("item") != -1){
			item = arguments[0];
			//post("select item: " + item + "\n");
		}else if(messagename.indexOf(offname) == 0 && messagename.length == offname.length){
			checkSelection(messagename);
            //debugpost("selected off " + offname);
		}else{
			checkSelection(messagename);
            //debugpost("selected " + messagename);
		}
	}
}

function debugpost(_poststring){
    if(debug){
        if(ignore != null){
            post("selector(" + ignore + "): " + _poststring + "\n");
        } else {
            post("selector<" + types + ">: " + _poststring + "\n");
        }
    }
}

function checkSelection(sel){
    //debugpost("checkSelection = " + sel);
	dumpNewMenu();

	if(sel != "_init_" && sel != offname){
        //post("selection "+sel+"\n");
		selection = getEnabledSelection(sel);
		address = displayToDict.getItem(selection);
		//post("address: " + address + " selection: " + selection + "\n");
		if(address != undefined){
			myDict = new Dict("msg::"+address);
			outlet(2, "about", "address", address);
			outlet(2, "about", "dict", "dictionary", "msg::"+address);
			outlet(2, "about", "enable_local", 1);
			outlet(2, "about", "types", myDict.get("_types"));
			if(dictIsEnabled(myDict)){
				outlet(0, "setsymbol", selection);
				outlet(2, "about", "enable_remote", 1);
				outlet(2, "about", "enable", 1);
			} else {
				outlet(0, "append", getDisabledSelection(selection));
				outlet(0, "setsymbol", getDisabledSelection(selection));
				outlet(2, "about", "enable_remote", 0);
				outlet(2, "about", "enable", 0);
			}
		}else{
			outlet(2, "about", "enable", 0);
			outlet(2, "about", "enable_local", 0);
		}
	} else {
		selection = offname;
		address = undefined;
		outlet(2, "about", "enable", 0);
		outlet(2, "about", "enable_local", 0);
	}

	outlet(3, "set", selection);
	if(address == undefined || address == null){
		outlet(1, "set", "msg_undefined::"+selection+"::_enable");
	} else {
		outlet(1, "set", "msg::"+address+"::_enable");
	}
	if(address == undefined && offwormholeAddress != undefined){
		outlet(2, "wormhole", "set", offwormholeAddress);
	}else{
		outlet(2, "wormhole", "set", "msg::"+address+"::wormhole");
	}

	refreshKeys();

	outlet(2, "about", "done");
}

function refreshKeys(){
    //post("refreshKeys()\n");
    for(var i = outKeys.length - 1; i >= 0; i--){
		var keyval = outKeys[i];
		//post("keyval: " + keyval + "\n");
		if(keyval != null)
			if(myDict != null && myDict.contains(keyval))
				outlet(2, "key"+(i+1), address, myDict.get(keyval));
			else
				outlet(2, "key"+(i+1), address);
	}
}

function getEnabledSelection(ad){
    //post("ad is :" + ad + "\n");
	if(ad.indexOf("<(") == 0)
		return ad.substring(2, ad.length -2);
	return ad;
}

function getDisabledSelection(ad){
	return "<(" + getEnabledSelection(ad) + ")>";
}

function dumpNewMenu(){
    //debugpost("dumpNewMenu");

	displayToDict.clear();
	outlet(0, "clear");
	var tempDict = new Dict(undefined);
	var names = tempDict.getnames();
	var menuList = new Array();
	for(var i = 0; i < names.length; i++){
		if(names[i].indexOf("msg::") == 0){
			var dictname = names[i].substring(5);
			if(dictMatchesFilter(dictname)){
				tempDict = new Dict(names[i]);
				if(dictIsValid(tempDict)){
					displayToDict.setItem(dicttitle(tempDict), dictname);
					if(dictIsEnabled(tempDict)){
                        //debugpost("add = "+ dicttitle(tempDict));
						menuList.push(dicttitle(tempDict));
						//outlet(0, "append", dicttitle(tempDict));
					}
				}
			}
		}
	}
	menuList.sort();
	for(var i = 0; i < menuList.length; i++){
        outlet(0, "append", menuList[i]);
	}
	if(hasoffselection == 1){
		outlet(0, "insert", 0, offname);
	}
}

function dicttitle(dict){
	return dict.get("_title");
}

function dictIsEnabled(dict){
	if(dict.contains("_enable") && dict.get("_enable")[1] == "1"){
		return true;
	}
	//post(dict.get("_enable")[1] + "\n");

	return false;
}

function dictIsValid(dict){
	if(dict.contains("_enable") && dict.contains("_title") && !keyMatchesIgnore(dict.get("_title"))){
		if(dict.contains("_types")){
//			post("parsing for msgs: " + dict.get("_title") + "\n");
			var _typ = "" + dict.get("_types") + ",";
//			post("   has types: " + _typ + "\n");
			if(types == undefined){
				return true;
			}
			for(var i = 0; i < types.length; i++){
//				post("looking for types: " + types[i] + "\n");
				if(_typ.indexOf(types[i] + ",") != -1){
//                   post("  found " + types[i] + "\n");
					return true;
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
	//post("dictname=" + _dictname + " + ignore=" + ignore + "\n");
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
//    outlet(0,a);
//  }
