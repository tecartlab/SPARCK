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
 
 
VPL.DNA.export_V0toMax = function () {
    this.appGlobal = new Global("bs::app::global");
    // set the debugger : 0=off, 1=all, 2=app, 3=io, 4=db, 5=node, 6=select
    this.debug = (this.appGlobal.debug == 1 || this.appGlobal.debug == 4)? true:false;
    this.nodeAccess = new Dict("bs::app::nodes");
    this.accessLevel = this.nodeAccess.get("_level");
    
    this.localVar = 0;
    this.baseDB = null;
    
    this.importDB = null;    
    this.init();
};

VPL.DNA.export_V0toMax.prototype = {

	constructor: VPL.DNA.export_V0toMax,

    init: function ( ) {
        this.baseDB = new Dict();
    },

    load: function ( _path ) {
        this.importDB = new Dict();
        this.importDB.readany(_path);
        
        if (this.importDB.contains("_enable")){
            //it is the old fileformat
            post("... its the old file format. \n");
            
                        
            this.clearGlobalReport();
            var patcher = this.buildPatcher( [200, 200, 500, 500] );
            patcher = this.build(patcher, "root", 0);
            patcher.set("autosave", 0);
            this.baseDB.set("patcher", patcher);
            
            this.baseDB.export_json(_path + ".maxpat");
        } else {
            post("... its an unknown file format, rejecting \n");
        }
    },
    
    buildPatcher: function ( _rect ){
        var appversion = new Dict();
        appversion.set("major", 8);
        appversion.set("minor", 1);
        appversion.set("revision", 4);
        appversion.set("architecture", "x64");
        appversion.set("modernui", 1);
        var patcher = new Dict();
        patcher.set("fileversion", 1);
        patcher.set("appversion", appversion);
        patcher.set("classnamespace", "box");
        patcher.set("rect", _rect);
        return patcher;
    },
    
    addIOlets: function ( boxes ){
        
    },
    
    build: function ( patcher, vpl_NodeSpaceName, vpl_NodeSpaceLevel ){
        
        var newIDlist = new Dict();
        var idIndex = 0;
        
        var nodeTypes = this.importDB.getkeys();
    
        // Creating the list (buildarray) of nodes to be build inside this nodespace

        //dpost("building nodespace: " +vpl_NodeSpaceName + "\n");
        if(nodeTypes != null){
            //dpost("types: " +nodeTypes + "\n");
            // Create all the nodes inside this workspace
            for(var i = 0; i < nodeTypes.length; i++){
                //dpost("build() node types: " + nodeTypes[i] + "\n");
                if(nodeTypes[i].indexOf("_") != 0 && this.importDB.contains(nodeTypes[i])){
                    var typeDB = this.importDB.get(nodeTypes[i]);
                    //dpost("build() type: " + nodeTypes[i] + "\n");
                    var nodeBoxes = typeDB.getkeys();
                    //dpost("build() nodeBox keys: " + nodeBoxes + "\n");
                    if(nodeBoxes != null){
                        for(var j = 0; j < nodeBoxes.length; j++){
                            if((nodeBoxes[j].indexOf("_") != 0) && typeDB.contains(nodeBoxes[j])){
                                var nodeBoxDB = typeDB.get(nodeBoxes[j]);
                                // then we look only at the nodes inside this nodespace, check them and add them to the buildArray.
                                if(nodeBoxDB.get("_nodespace") == vpl_NodeSpaceName){
                                    var thisNodeName = nodeBoxDB.get("_name");
                                    var thisNodeType = nodeBoxDB.get("_type");
                                    // the following checks if the name and type are the same. if not it will correct
                                    if(thisNodeName.substring(0, thisNodeName.indexOf("_")) != thisNodeType){
                                        var newNodeName = makeNodeName(thisNodeType, nodeBoxDB.get("_id"));
                                        this.dpost("build() found currupted data: " + thisNodeName + " should be " + newNodeName + " because of its type: '" + thisNodeType + "'. fixing it...\n");
                                        nodeBoxDB.replace("_name", newNodeName);
                                        typeDB.remove(nodeBoxes[j]);
                                        nodeBoxes[j] = newNodeName;
                                        typeDB.set(nodeBoxes[j], nodeBoxDB);
                                        baseDB.replace(nodeTypes[i], typeDB);
                                    }

                                     //check if all creation tests are passed:
                                    //dpost("build() node '"+nodeBoxes[j]+"' of type " + nodeTypes[i] + " is in nodespace '" + vpl_NodeSpaceName + "', level '"+vpl_NodeSpaceLevel+"'\n");
                                    //dpost("build() node '"+nodeBoxes[j]+"' has type "+ nodeBoxDB.get("_type") +"\b")
                                    if(this.creationCheck(typeDB, nodeTypes[i], nodeBoxDB.get("_version"), thisNodeName, vpl_NodeSpaceName, vpl_NodeSpaceLevel, 1)){
                                        var thisNodeVersion = nodeBoxDB.get("_version");
                                        if(this.getNode_version(nodeTypes[i]) == 0){
                                            // if this nodes version is 0, it means it is deprecated and we stick to this version max
                                            this.dpost(vpl_NodeSpaceName + " - keeping deprecated node: " + nodeBoxDB.get("_title"));
                                        } else if(thisNodeVersion < this.getNode_version(nodeTypes[i])){
                                            // we have now to check if this node is the most current version, if not we have to update it.
                                            this.dpost(vpl_NodeSpaceName + " - updated deprecated node: " + nodeBoxDB.get("_title"));
                                            nodeBoxDB.replace("_version", this.getNode_version(nodeTypes[i]));
                                        }
                                        
                                        idIndex++;
                                        id = "obj-"+idIndex;
                                        newIDlist.set(nodeBoxDB.get("_name"), id);
                                        var box = new Dict();
                                        var boxattr = new Dict();
                                        boxattr.set("id", id);
                                        
                                        if(nodeBoxDB.get("_type")=="subnode"){
                                            var newPatcher = this.buildPatcher( [200, 200, 500, 500] );
                                            boxattr.set("maxclass", "newobj");
                                            boxattr.set("text", "p " + nodeBoxDB.get("_title"));
                                            boxattr.set("patcher", this.build(newPatcher, "root::" + nodeBoxDB.get("_id"), vpl_NodeSpaceLevel + 1));
                                            var rect = nodeBoxDB.get("_rect");
                                            boxattr.set("patching_rect", [rect[0], rect[1], 100, 20]);
                                        } else {
                                            boxattr.set("maxclass", "bpatcher");
                                            boxattr.set("name", this.getNode_fileName(nodeBoxDB.get("_type")) + "." + nodeBoxDB.get("_version"));
                                            boxattr.set("patching_rect", nodeBoxDB.get("_rect"));
                                        }
                                        boxattr.set("varname", nodeBoxDB.get("_name"));
                                        box.set("box", boxattr);
                                        patcher.append("boxes", box);
                                        
                                        //buildArray.push(makeCreationMessage(nodeBoxDB));
                                    } else { // creation check failed
                                        // we are the root workspace..
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                } 
            }
        }
        this.dpost("exiting method");
        return patcher;
    },

    // creation check is looking at the node database and validates if
    //  - the requested node is created in the correct level
    //  - the requested node is not created more than the maximal global requiremens
    //  - the requested node is not created more than the maximal subnode requirements
    // with validation = 1 it checks if the node db is valid for this node
    creationCheck: function (_nodeTypeDB, _nodeType, _nodeVersion, _nodeName, _spacename, _spacelevel, _validation){
     //	post("enter creation check for nodeType : " + _nodeType + " | _spacename : " + _spacename + " | _spacelevel : "+_spacelevel+" | _validation : "+_validation+"\n");
        if(this.hasNode_access(_nodeType)){
    //		post("...hasNode_access...\n");
            if(_nodeVersion != -1){
                if(this.getNode_version(_nodeType) != 0 && this.getNode_version(_nodeType) < _nodeVersion){
                    this.addGlobalReport_Error("["+ _nodeType + "]: version unavailable","You try to load a project that was created with a newer version of SPARCK","Update SPARCK and try again");
                    return false;
                } else if(this.getNode_version(_nodeType) > _nodeVersion || this.getNode_version(_nodeType) == 0){
                    this.addGlobalReport_Info("["+ _nodeType + "]: updated","Your project was updated from a deprecated version of the ["+ _nodeType + "]-node","To make sure your project is working flawlessly, check this node and see if the node is setup properly", _nodeName);
                }
            }
            if(this.getNode_branchLevel(_nodeType) == -1 ||
                this.getNode_branchLevel(_nodeType) == _spacelevel ||
                (this.getNode_branchLevel(_nodeType) == 1 && _spacelevel > 1)){

    //			post("...hasBranchLevel_access..\n");

                var nodes = _nodeTypeDB.getkeys();
                if(nodes != null){
     //             post("...got Keys..\n");
                    var counterMax = 0;
                    var counterSub = 0;
                    //counting the already existing nodes in the global- and the sub-nodespaces
                    for(var i = 0; i < nodes.length; i++){
                        if(nodes[i].indexOf("_") != 0 && _nodeTypeDB.contains(nodes[i])){
                            nodeBox = _nodeTypeDB.get(nodes[i]);
                            counterMax++;
                            if(nodeBox.get("_nodespace") == _spacename)
                                counterSub++;
                        }
                    }
    //				post("...counterMax = " +  counterMax +"..\n");
    //              post("...counterSub = " +  counterSub +"..\n");
                    if(this.getNode_maxNodes(_nodeType) == 0){
                        this.addGlobalReport_Fatal("["+ _nodeType + "]: nodes access denied","You try to load a project that contains a node which is not covered by your license","Try again in Demo Mode");
                        return false;
                    } else {
                        if(this.getNode_maxNodes(_nodeType) > 0 && counterMax >= this.getNode_maxNodes(_nodeType) + _validation){
                            this.addGlobalReport_Warning("["+ _nodeType + "]: maximum nodes reached","You can only create up to " + this.getNode_maxNodes(_nodeType) + " [" + _nodeType + "]-nodes","Not much you can do about.");
                            return false;
                        } else if(this.getNode_branchMaxNodes(_nodeType) >= 0 && counterSub >= this.getNode_branchMaxNodes(_nodeType) + _validation){
    //                      post("<"+ _nodeType + ">: maximum nodes in this nodespace level reached (max: " + counterSub + ")\n");
                            this.addGlobalReport_Warning("["+ _nodeType + "]: maximum nodes reached","You can only create up to " + this.getNode_branchMaxNodes(_nodeType) + " [" + _nodeType + "]-nodes in one workspace-level","Try again in another workspace-level");
                            return false;
                        }
                    }
                }
            }else{
                this.addGlobalReport_Warning("["+ _nodeType + "]: cannot be created","You tried to create a [" + _nodeType + "]-node in a workspace-level that is unsuitable for this node","Try to create this node in a different workspace-level.");
                return false;
            }
    // 		post("... valid creation check for nodeType : " + _nodeType + " | _spacename : " + _spacename + " | _spacelevel : "+_spacelevel+" | _validation : "+_validation+"\n");
            return true;
        }
        this.addGlobalReport_Fatal("["+ _nodeType + "]: cannot be created","Your try to load a project with an unknown/invalid node"," Update SPARCK and try again");
        return false;
    },

    // set the debugger
    debugger: function (_val){
        this.debug = _val;
    },

    dpost: function (_message){
        if(this.debug){
            post("VPL.DNA.convert: " + _message + " \n");
        }
    },

    clearGlobalReport: function (){
        this.appGlobal.globalReport = [];
    },

    // Carefull: only special characters - ! + ? ( ) are allowed
    addGlobalReport_Info: function (_title, _msg, _fix, _node){
        this.appGlobal.globalReport.push(_title + "|" + _msg + "|" + _fix + "|" + _node + "|info");
        this.dpost(_title + "|" + _msg + "|" + _fix + "|info")
    },

    // Carefull: only special characters - ! + ? ( ) are allowed
    addGlobalReport_Warning: function (_title, _msg, _fix, _node){
        this.appGlobal.globalReport.push(_title + "|" + _msg + "|" + _fix + "|" + _node + "|warning");
        this.dpost(_title + "|" + _msg + "|" + _fix + "|warning")
    },

    // Carefull: only special characters - ! + ? ( ) are allowed
    addGlobalReport_Error: function (_title, _msg, _fix, _node){
        this.appGlobal.globalReport.push(_title + "|" + _msg + "|" + _fix + "|" + _node + "|error");
        this.dpost(_title + "|" + _msg + "|" + _fix + "|error")
    },

    // Carefull: only special characters - ! + ? ( ) are allowed
    addGlobalReport_Fatal: function (_title, _msg, _fix, _node){
        this.appGlobal.globalReport.push(_title + "|" + _msg + "|" + _fix + "|" + _node + "|fatal");
        this.dpost(_title + "|" + _msg + "|" + _fix + "|fatal")
    },

    getGlobalReport: function (){
        if(this.appGlobal.globalReport.length > 0){
            this.dpost("send global report");
        }
    },

    hasNode_access: function (_nodeType){
        return this.nodeAccess.contains(_nodeType);
    },

    getNode_branchLevel: function (_nodeType){
        return this.nodeAccess.get(_nodeType + "::nodeCreation::_branchLevel")
    },

    getNode_maxNodes: function (_nodeType){
    //	post("accesslevel = "+accessLevel + "\n");
    //	post("_level (from dict)= "+nodeAccess.get(_nodeType + "::nodeCreation::_level") + "\n");
    //	post("_maxNodes (from dict)= "+nodeAccess.get(_nodeType + "::nodeCreation::_maxNodes") + "\n");
        this.dpost("getNode_maxNodes: " + _nodeType);
        this.dpost("current access level: " + this.accessLevel);
        this.dpost("node level: " + this.nodeAccess.get(_nodeType + "::nodeCreation::_level"));
        if(this.nodeAccess.get(_nodeType + "::nodeCreation::_level").indexOf(""+this.accessLevel) != -1){
            return this.nodeAccess.get(_nodeType + "::nodeCreation::_maxNodes");
        } else {
            return 0;
        }
    },

    getNode_branchMaxNodes: function (_nodeType){
        return this.nodeAccess.get(_nodeType + "::nodeCreation::_branchMaxNodes")
    },

    getNode_fileName: function (_nodeType){
        return this.nodeAccess.get(_nodeType + "::nodeCreation::_fileName")
    },

    getNode_version: function (_nodeType){
        return this.nodeAccess.get(_nodeType + "::nodeCreation::_version")
    },

    getNode_titlePattrn: function (_nodeType){
        return this.nodeAccess.get(_nodeType + "::nodeCreation::_titlePattrn")
    }

};
 