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

autowatch = 1;
// set up inlets/outlets/assist strings
inlets = 2;
outlets = 2;
setinletassist(0,"load");
setinletassist(1,"add");
setoutletassist(0,"createbox");
setoutletassist(1,"dump");

var LEVEL_OFF = -1;
var LEVEL_UNREG = 0;
var LEVEL_FREE = 1;
var LEVEL_BASIC = 3;
var LEVEL_MASTER = 3;
var LEVEL_PRO = 7;
var LEVEL_DEV = 9;

var LICENSE_LEVEL = ["Unreg", "FREE", "nan", "BASIC", "nan", "MASTER", "nan", "PRO", "nan", "DEVELOPER"];
var LICENSE_LEVEL_SHOW = [false, false, false, true, true, true, true, true, true, false];

var vpl_subNode = "bs.vpl.node.util.subnode.1";

// space::nodes::node::space::
var outlet_dump = 1;

var baseDB = null;

var	nodeAccess;
var accessLevel = LEVEL_OFF;

var error;
var licenseNeeded;

// set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select
var debug;

// set the debugger
function debugger(_val){
	debug = (_val == 1 || _val == 4)? true: false;
}

function dpost(_message){
    if(debug){
        post("bs.vpl.check: " + _message + " \n");
    }
}

// this message is called by the main patcher
function check(path){
    var appGlobal = new Global("bs::app::global");
    // set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select
    debug = (appGlobal.debug == 1 || appGlobal.debug == 3)? true:false;

    error = null;
    licenseNeeded = null;

	baseDB = new Dict("vpl::check::db");
	baseDB.set("_enable", 1);

    nodeAccess = new Dict("bs::app::nodes");
	accessLevel = nodeAccess.get("_level");

    baseDB.readany(path);

    scan();

    if(licenseNeeded != null){
        if(accessLevel >= LEVEL_FREE){
            // license error has precedence over all the other errors
            error = new Array("Your project is using a node that is not available to your current license.", "You would need a "+ LICENSE_LEVEL[licenseNeeded] +"-license OR you can load it in demo mode.", "demo");
        } else {
            error = new Array("Your project is using a node that is not available to your current license.", "You would need a "+ LICENSE_LEVEL[licenseNeeded] +"-license OR register for a FREE-license and switch to Demo-mode", "abort");
        }
    }

    if (error != null){
        outlet(0, error);
    } else {
        outlet (1, "loadproject", path);
    }

    clearDB(); //clear the database
}


// builds this nodespace and all its subspaces
// should only be called after a clear().
function scan(){
    dpost("start checking...");

    var nodeTypes = baseDB.getkeys();

	//dpost("building nodespace: " +vpl_NodeSpaceName + "\n");
	if(nodeTypes != null){
		//dpost("types: " +nodeTypes + "\n");
        // Create all the nodes inside this workspace
		for(var i = 0; i < nodeTypes.length; i++){
			//dpost("build() node types: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				//dpost("build() type: " + nodeTypes[i] + "\n");
				var nodeBoxes = typeDB.getkeys();
				//dpost("build() nodeBox keys: " + nodeBoxes + "\n");
				if(nodeBoxes != null){
					for(var j = 0; j < nodeBoxes.length; j++){
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
							//dpost("build() node found: " + nodeBoxes[j] + " in nodespace :" + nodeBoxDB.get("_nodespace") + "\n");
                            var thisNodeName = nodeBoxDB.get("_name");
                            var thisNodeType = nodeBoxDB.get("_type");
                            // we check if there is an invalid type..
                            if(nodeTypes[i] != "" && !validation(typeDB, nodeTypes[i], nodeBoxDB.get("_version"))){
                                return false;
                            }
 						}
					}
				}
			}
		}
 	}
}

function validation(nodeTypeDB, _nodeType, _nodeVersion){
    dpost("validation for nodeType : " + _nodeType);
    if(hasNode_access(_nodeType)){
        // first we check the license - it has preference
        var nodes = nodeTypeDB.getkeys();
        if(nodes != null){
            if(getNode_maxNodes(_nodeType) == 0){
                var levelID = hasNode_getLevelID(_nodeType);
                if(licenseNeeded == null || licenseNeeded < levelID){
                    licenseNeeded = levelID;
                }
                // here we keep carring on, since we want to go through all the nodes to see if there is another node that requires
                // a higher license.
                return true;
           }
        }
//		post("...hasNode_access...\n");
        if(_nodeVersion != -1){
            if(getNode_version(_nodeType) != 0 && getNode_version(_nodeType) < _nodeVersion){
                error = new Array("You try to load a project that was created with a newer version of SPARCK.", "Update SPARCK and try again.", "abort");
                return false;
            } else if(getNode_version(_nodeType) > _nodeVersion || getNode_version(_nodeType) == 0){
                error = new Array("Your project is using at least one deprecated node.", "Ignore this warning and load the project in its original intent OR update your project with the most current nodes. WARNING: this could break your project and you should check each updated node to adjust the settings.", "deprecation");
                return false;
            }
        }
    } else {
        error = new Array("Your try to load a project with an unknown/invalid node: ["+ _nodeType + "].", "Update SPARCK and try again.", "abort");
        return false;
    }
    return true;
}

function hasNode_access(_nodeType){
    return nodeAccess.contains(_nodeType);
}

function hasNode_getLevelID(_nodeType){
 	var levels = (nodeAccess.get(_nodeType + "::nodeCreation::_level"));
    var levArray = levels.split("|");
    return levArray[0];
}

function hasNode_getLevelName(_id){
    getLevelsText(levArray)
}

function getNode_maxNodes(_nodeType){
//	post("accesslevel = "+accessLevel + "\n");
//	post("_level (from dict)= "+nodeAccess.get(_nodeType + "::nodeCreation::_level") + "\n");
//	post("_maxNodes (from dict)= "+nodeAccess.get(_nodeType + "::nodeCreation::_maxNodes") + "\n");

	if(nodeAccess.get(_nodeType + "::nodeCreation::_level").indexOf(""+accessLevel) != -1){
    	return nodeAccess.get(_nodeType + "::nodeCreation::_maxNodes");
    } else {
		return 0;
    }
}

function getNode_version(_nodeType){
    return nodeAccess.get(_nodeType + "::nodeCreation::_version")
}

function getLevelText(_levels){
    var result = null;
    for (var i = 0 ; i < _levels.length ; i++ ){
        if(LICENSE_LEVEL_SHOW[_levels[i]]){
            // we dont want to see the developer text.
            if(result == null){
                result = LICENSE_LEVEL[_levels[i]];
            }
        } else if( _levels[i] == LEVEL_UNREG){
            return "";
        } else if( _levels[i] == LEVEL_FREE){
            result = LICENSE_LEVEL[_levels[i]];
            break;
        }
    }
    if(result == null){
        // if no level is inside the brackets (i.e. its a node only for developer)
        return LICENSE_LEVEL[LEVEL_DEV];
    }
    return result;
}

// This method only clears the db, and not the nodespace!
function clearDB(){
	var nodeTypes = baseDB.getkeys();
	if(nodeTypes != null){
		//post("types: " +nodeTypes + "\n");
		for(var i = 0; i < nodeTypes.length; i++){
			//post("type keys: " + nodeTypes[i] + "\n");
			if(nodeTypes[i].indexOf("_") != 0 && baseDB.contains(nodeTypes[i])){
				var typeDB = baseDB.get(nodeTypes[i]);
				var nodeBoxes = typeDB.getkeys();
				if(nodeBoxes != null){
					for(var j = 0; j < nodeBoxes.length; j++){
						if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
							var nodeBoxDB = typeDB.get(nodeBoxes[j]);
							//post("clear nodebox: " + nodeBoxes[j] + "\n");
							nodeBoxDB.clear();
						}
					}
				}
				//post("clear nodetype: " + nodeTypes[i] + "\n");
				typeDB.clear();
			}
		}
	}
	baseDB.clear();
}
