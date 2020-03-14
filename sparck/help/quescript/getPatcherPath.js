autowatch = 1;

inlets = 1;
outlets = 1;

var patcherName;

// patcher arguments
if (jsarguments.length > 1){
    patcherName = jsarguments[1];
} else {
 	error("getPatcherPath needs one argument specifying the patcher's name\n");
}

function bang(){
	var parentlevel = 0;
	var owner = this.patcher.box;
	if (owner) {
//		post("owner : " + owner.varname + " level : " + parentlevel+"\n");
		if(owner.patcher.name.indexOf("APP") != 0){
			outlet(0, owner.patcher.filepath.substring(0, owner.patcher.filepath.lastIndexOf("/")));
		}
	}
}	
