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
		"rect" : [ 541.0, 146.0, 755.0, 606.0 ],
		"bgcolor" : [ 1.0, 1.0, 1.0, 0.0 ],
		"bglocked" : 0,
		"openinpresentation" : 1,
		"default_fontsize" : 10.0,
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
					"fontsize" : 10.0,
					"id" : "obj-4",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 24.222214, 109.0, 32.5, 16.0 ],
					"text" : "init"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "#1", "#2", "#3" ],
					"clickthrough" : 1,
					"id" : "obj-19",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.node.logic.maxpat",
					"numinlets" : 3,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "int" ],
					"patching_rect" : [ 32.222214, 143.0, 142.555542, 37.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 0.0, 112.5, 31.0 ],
					"varname" : "vpl_nodelogic"
				}

			}
, 			{
				"box" : 				{
					"color" : [ 1.0, 0.010851, 0.0, 1.0 ],
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-15",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 32.222214, 210.0, 69.0, 20.0 ],
					"save" : [ "#N", "thispatcher", ";", "#Q", "end", ";" ],
					"text" : "thispatcher",
					"varname" : "vpl_ThisNodePatcher"
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-34",
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 631.333313, 539.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-32",
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 90.999985, 539.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-30",
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 688.0, 11.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-25",
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 147.0, 11.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"args" : [ 5, "type" ],
					"hint" : "",
					"id" : "obj-8",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.outlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "bang" ],
					"patching_rect" : [ 469.111115, 539.0, 48.0, 30.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 86.333336, 50.0, 10.0, 10.0 ],
					"varname" : "vpl_outlet[5/7]"
				}

			}
, 			{
				"box" : 				{
					"args" : [ 4, "type" ],
					"hint" : "",
					"id" : "obj-9",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.outlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "bang" ],
					"patching_rect" : [ 372.777771, 539.0, 48.0, 30.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 66.0, 50.0, 10.0, 10.0 ],
					"varname" : "vpl_outlet[4/7]"
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-18",
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 532.333313, 539.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-20",
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 436.0, 539.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"args" : [ 6, "type", 1, "@ignoreclick", 0 ],
					"hint" : "",
					"id" : "obj-7",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.inlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 604.0, 11.0, 44.0, 31.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 106.666664, 0.0, 10.0, 10.0 ],
					"varname" : "vpl_inlet[6/7]"
				}

			}
, 			{
				"box" : 				{
					"args" : [ 3, "type", 1 ],
					"hint" : "",
					"id" : "obj-6",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.inlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 319.0, 11.0, 44.0, 31.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 45.666668, 0.0, 10.0, 10.0 ],
					"varname" : "vpl_inlet[3/7]"
				}

			}
, 			{
				"box" : 				{
					"args" : [ 5, "type", 1 ],
					"hint" : "",
					"id" : "obj-1",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.inlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 504.555542, 11.0, 44.0, 31.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 86.333336, 0.0, 10.0, 10.0 ],
					"varname" : "vpl_inlet[5/7]"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 10.0,
					"frgb" : 0.0,
					"id" : "obj-39",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 215.999985, 92.0, 171.0, 18.0 ],
					"text" : "(c) by maybites.ch 2013 CC-BY-NC"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 10.0,
					"frgb" : 0.0,
					"id" : "obj-37",
					"linecount" : 23,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 215.999985, 128.0, 492.0, 264.0 ],
					"text" : "How to create a functioning vpl-node: #1 = nodename , #2 = nodetitle, #3 = nodespace, #4 etc userdefined\n\n1. Add main logic with a bpatcher <bs.vpl.util.node.logic> and the arguments <#1 #2 #3>\n\n2. Add a thispatcher-object with scriptname \"vpl_ThisNodePatcher\"\n\n3. Add Inlets wtith bpatcher-object <bs.vpl.util.inlet> and the following settings:\n        scriptname = \"vpl_inlet[<inletnumber>/<totalNumberOfInlets>]\"  i.e. vpl_inlet[2/5]\n        arguments = \"<inletnumber> <typeNames> <maxConnections> i.e. \"2 number 1\"\n\n       the typenames can contains multiple types, separated by underscores \"_\". if the type name is\n       \"type\", then it connects to any other outlet, no matter what type. (Same applies to outlets)\n\n      The first defined type also defines the color, so if you wanna give an individual color but wanna \n      make sure its still connecting to everything, set type to \"yourType_type\"\n\n4. Add Outlets wtith bpatcher <bs.vpl.util.outlet> and the following settings:\n        scriptname = \"vpl_outlet[<outletnumber>/<totalNumberOfOutlets>]\"\n        arguments = \"<outletsnumber> <typeNames>\n  \n5. Name an object with scriptname \"vpl_canvas\" and place it in the upper left corner at x=0, y=4. Make sure both rect - for presentation and edit - are the same. Ideally its a textbutton that makes it dragable when connected to the second inlet of the main logic"
				}

			}
, 			{
				"box" : 				{
					"args" : [ 3, "type" ],
					"hint" : "",
					"id" : "obj-16",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.outlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "bang" ],
					"patching_rect" : [ 276.444458, 539.0, 48.0, 30.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 45.666668, 50.0, 10.0, 10.0 ],
					"varname" : "vpl_outlet[3/7]"
				}

			}
, 			{
				"box" : 				{
					"args" : [ 2, "type" ],
					"hint" : "",
					"id" : "obj-13",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.outlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "bang" ],
					"patching_rect" : [ 180.111115, 539.0, 48.0, 30.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 25.333334, 50.0, 10.0, 10.0 ],
					"varname" : "vpl_outlet[2/7]"
				}

			}
, 			{
				"box" : 				{
					"args" : [ 4, "type", 1 ],
					"hint" : "",
					"id" : "obj-10",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.inlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 411.777771, 11.0, 44.0, 31.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 66.0, 0.0, 10.0, 10.0 ],
					"varname" : "vpl_inlet[4/7]"
				}

			}
, 			{
				"box" : 				{
					"args" : [ 6, "type" ],
					"hint" : "",
					"id" : "obj-28",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.outlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "bang" ],
					"patching_rect" : [ 565.0, 539.0, 48.0, 30.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 106.666664, 50.0, 10.0, 10.0 ],
					"varname" : "vpl_outlet[6/7]"
				}

			}
, 			{
				"box" : 				{
					"args" : [ 2, "type", 1 ],
					"hint" : "",
					"id" : "obj-27",
					"maxclass" : "bpatcher",
					"name" : "bs.vpl.util.inlet.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 226.222214, 11.0, 44.0, 31.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 25.333334, 0.0, 10.0, 10.0 ],
					"varname" : "vpl_inlet[2/7]"
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-17",
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 567.111084, 11.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-22",
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 474.333374, 11.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-14",
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 339.666656, 539.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-12",
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 381.555542, 11.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-11",
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 288.777771, 11.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-5",
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 196.000015, 11.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-3",
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 243.333328, 539.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-2",
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 147.0, 539.0, 25.0, 25.0 ]
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-29",
					"maxclass" : "textbutton",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 0.0, 4.0, 140.0, 50.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 4.0, 140.0, 50.0 ],
					"text" : "",
					"varname" : "vpl_canvas"
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-19", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-4", 0 ]
				}

			}
 ],
		"dependency_cache" : [ 			{
				"name" : "bs.vpl.util.inlet.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/app/lib",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.outlet.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/app/lib",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.node.logic.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/app/lib",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.node.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/app/lib",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.node.js",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/app/lib/_js",
				"patcherrelativepath" : "./_js",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.dialog.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/app/lib",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.global.keys.maxpat",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/app/lib",
				"patcherrelativepath" : ".",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.title.js",
				"bootpath" : "/Users/maybites/Arbeiten/02_code/eclipse/git/Prj_ARPS/Prj_ARPS/extDev/app/lib/_js",
				"patcherrelativepath" : "./_js",
				"type" : "TEXT",
				"implicit" : 1
			}
 ]
	}

}
