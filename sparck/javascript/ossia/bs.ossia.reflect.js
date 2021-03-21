
inlets = 1;
outlets = 2;

var root = new Dict("node");
var nodespaceSize = 0;

function bang(){
	outlet(0, "clear");
	outlet(1, 'clear');
}

function clear(){
	root.clear();
}

function namespace_size(_size){
	nodespaceSize = _size;
	root.clear();
}

function namespace(){
	args = arrayfromargs(arguments);
	var path = args.shift();
	var routes = path.split("/");
	var key = routes.pop();
	if(routes.length > 0 && routes[0] == 'node'){
		var node = root.get(routes[1]);
		if(node == null){
			node = new Dict();
		}
		node.set(path, args);
		root.set(routes[1], node);
	}
	nodespaceSize--;
	
	if(nodespaceSize == 0){
		var nodes = root.getkeys();
		outlet(1, 'clear');
		for(var i = 0; i < nodes.length; i++){
			outlet(1, 'append', nodes[i]);
		}
	}
	//post("path: " + path + " > ");
	//root = recursive(root, path, key, args);
}

function getnode(_nodename){
	var node = root.get(_nodename);
	outlet(0, "clear");
	var keys = node.getkeys();
	for(var i = 0; i < keys.length; i++){
		outlet(0, 'append', keys[i], node.get(keys[i]));
	}
	
}

function anything(){
	;
}