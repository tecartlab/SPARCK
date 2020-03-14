{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 6,
			"minor" : 1,
			"revision" : 6,
			"architecture" : "x86"
		}
,
		"rect" : [ 118.0, 85.0, 451.0, 376.0 ],
		"bgcolor" : [ 1.0, 1.0, 1.0, 0.0 ],
		"bglocked" : 0,
		"openinpresentation" : 0,
		"default_fontsize" : 12.0,
		"default_fontface" : 0,
		"default_fontname" : "Arial",
		"gridonopen" : 0,
		"gridsize" : [ 15.0, 15.0 ],
		"gridsnaponopen" : 0,
		"statusbarvisible" : 2,
		"toolbarvisible" : 1,
		"boxanimatetime" : 200,
		"imprint" : 0,
		"enablehscroll" : 1,
		"enablevscroll" : 1,
		"devicewidth" : 0.0,
		"description" : "",
		"digest" : "",
		"tags" : "",
		"boxes" : [ 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-1",
					"maxclass" : "newobj",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 98.0, 338.0, 125.0, 20.0 ],
					"text" : "bs.vpl.node.util.select",
					"varname" : "bs.vpl.node.util.select"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"frgb" : 0.0,
					"id" : "obj-14",
					"linecount" : 24,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 7.999985, 5.0, 442.0, 328.0 ],
					"text" : "<level restriction> <max> <max/subnode> <patcher-filename.maxpat> <$.#> <more arguments>\n\nrequires \n<patcher-filename.maxpat> a filename inside the search path \n<level restriction>                 a number specifying the level on which this node \n                                             can be created (-1 = unlimited)\n<max>                                  a number specifying the maximal count \n                                             of total nodes in the system (-1 = unlimited)\n<max/subnode>                   a number specifying the maximal count of \n                                             nodes inside a subnode (-1 = unlimited)\n<$.#>                                    a title for the node\n\n$ stands for the node type (comming from the menu)\n# stands for the node id. each node becomes its own number from the system.\n& stands for a character of the alphabet. ie. if # is 1, & will be a Uppercase A\n\nSo if the node type was \"Function\" and the assigned Number would be \"3\" then the first argument would be \"Function.3\"\n\nVPL also allows some other special characters, like \"_\" as separators, or the combinations with a character: ie:  $_& would be \"Function_C\"\n\nits also possible to pass on more arguments in a similar fashion."
				}

			}
 ],
		"lines" : [  ],
		"parameters" : 		{
			"obj-1::obj-72" : [ "toggle[1]", "toggle", 0 ],
			"obj-1::obj-99::obj-10::obj-72" : [ "toggle", "toggle", 0 ]
		}
,
		"dependency_cache" : [ 			{
				"name" : "bs.vpl.node.util.select.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/patchers",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.db.js",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/javascript",
				"patcherrelativepath" : "../javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.windows.pops.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/patchers",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.windows.pops.js",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/javascript",
				"patcherrelativepath" : "../javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.win.reactive.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/patchers",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.global.keys.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/patchers",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "pin.png",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/media",
				"patcherrelativepath" : "../media",
				"type" : "PNG ",
				"implicit" : 1
			}
, 			{
				"name" : "bs.file.select.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/patchers",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.dialog.saveas.slim.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/patchers",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.receive.key.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/patchers",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.key.receive.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/patchers",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.key.receive.js",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/javascript",
				"patcherrelativepath" : "../javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.select.js",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/package/beamStreamer/javascript",
				"patcherrelativepath" : "../javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
 ]
	}

}
