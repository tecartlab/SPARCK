
inlets = 1;
outlets = 1;

var root = new Dict("node");

function clear(){
	root.clear();
}

function namespace(){
	args = arrayfromargs(arguments);
	var msg = args.shift();
	var path = msg.split("/");
	var key = path.pop();
	
	//post("path: " + path + " > ");
	root = recursive(root, path, key, args);
}

function recursive(parent, path, key, args){

	if (parent == null) {
//		post(" > newDict > ");
		parent = new Dict();
	}
	if(path.length > 0) {
		var levelname = path.shift();
//		post(levelname + "/");
		parent.set(levelname, recursive(parent.get(levelname), path, key, args));
	} else {
//		post(key + "=" + args + "\n");
		var newDict = new Dict();
		newDict.set('value', args);
		parent.set(key, newDict);
	}
	return parent;
}
