{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 8,
			"minor" : 2,
			"revision" : 0,
			"architecture" : "x64",
			"modernui" : 1
		}
,
		"classnamespace" : "box",
		"rect" : [ 381.0, 101.0, 510.0, 611.0 ],
		"bglocked" : 0,
		"openinpresentation" : 0,
		"default_fontsize" : 12.0,
		"default_fontface" : 0,
		"default_fontname" : "Arial",
		"gridonopen" : 1,
		"gridsize" : [ 15.0, 15.0 ],
		"gridsnaponopen" : 1,
		"objectsnaponopen" : 1,
		"statusbarvisible" : 2,
		"toolbarvisible" : 1,
		"lefttoolbarpinned" : 0,
		"toptoolbarpinned" : 0,
		"righttoolbarpinned" : 0,
		"bottomtoolbarpinned" : 0,
		"toolbars_unpinned_last_save" : 0,
		"tallnewobj" : 0,
		"boxanimatetime" : 200,
		"enablehscroll" : 1,
		"enablevscroll" : 1,
		"devicewidth" : 0.0,
		"description" : "",
		"digest" : "",
		"tags" : "",
		"style" : "",
		"subpatcher_template" : "",
		"assistshowspatchername" : 0,
		"boxes" : [ 			{
				"box" : 				{
					"id" : "obj-2",
					"linecount" : 37,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 55.0, 35.0, 372.0, 516.0 ],
					"text" : "Sequence of PatcherApp startup:\n\n1. setup textures\n2. gather system settings\n   a) getSystem (macintosh / windows)\n   b) getAppPath (path to main app)\n   c) architecture (x64)\n   d) glengine (gl2 / gl3)\n3. backbone init\n  a) enable render for main render context\n  b) enable all render contexts, bang and erease them\n  c) set all windows to invisible\n  d) disable all render contexts \n4. package dependency test\n  a) java 13+\n  b) max-mxj \n  c) ossia\n  d) gl3\n  e) syphon / spout\n  f) viddll\n  g) cnmat externals\n  h) VR\n  i) ia.facestream\n5. initialize project\n  a) check / load registration\n  b) find and load preferences\n     -> if no preference exist, create and fill with default\n  c) setup project settings\n  d) propagate project settings\n  e) create project folder structure\n  f) send 'bs::app::started'\n6. load properties - derived from the main patcher name\n  a) load and apply ossia properties\n  b) load and apply pattrstorage properties\n  c) load and apply window properties (only used for standalone app)\n\n\n"
				}

			}
 ],
		"lines" : [  ],
		"dependency_cache" : [  ],
		"autosave" : 0
	}

}
