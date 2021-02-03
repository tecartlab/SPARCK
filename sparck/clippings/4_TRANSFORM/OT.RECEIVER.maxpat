{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 8,
			"minor" : 1,
			"revision" : 8,
			"architecture" : "x64",
			"modernui" : 1
		}
,
		"classnamespace" : "box",
		"rect" : [ 84.0, 128.0, 640.0, 480.0 ],
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
		"tags" : "BeamStreamer Node Transform",
		"style" : "",
		"subpatcher_template" : "",
		"assistshowspatchername" : 0,
		"boxes" : [ 			{
				"box" : 				{
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-3",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.node.OptiTrack.maxpat",
					"numinlets" : 2,
					"numoutlets" : 2,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 0.0, 0.0, 180.0, 36.0 ],
					"varname" : "OptiTrack",
					"viewvisibility" : 1
				}

			}
 ],
		"lines" : [  ],
		"dependency_cache" : [ 			{
				"name" : "bs.node.OptiTrack.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/nodes/ossia",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.node.logic.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/vpl",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.node.logic.js",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/javascript/vpl",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.node.title.js",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/javascript/vpl",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.node.gui.button.menu.png",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/media",
				"type" : "PNG",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.node.pbody.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/vpl",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.node.pbody.js",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/javascript/vpl",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.node.logo.power.png",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/media",
				"type" : "PNG",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.inlet.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/vpl",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.node.OptiTrack.stream.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/nodes/ossia",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.anim.node.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/utils",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.ossia.parameter.msg.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/ossia/msg",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.ossia.parameter.msg.js",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/javascript/ossia",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.svg.button.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/gui",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.outlet.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/vpl",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.doubleBang.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/utils",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "ossia.parameter.mxe64",
				"type" : "mx64"
			}
, 			{
				"name" : "ossia.model.mxe64",
				"type" : "mx64"
			}
, 			{
				"name" : "bs.msg.send.mxe64",
				"type" : "mx64"
			}
, 			{
				"name" : "bs.latencycomp.mxe64",
				"type" : "mx64"
			}
, 			{
				"name" : "bs.msg.receive.selector.mxe64",
				"type" : "mx64"
			}
 ],
		"autosave" : 0
	}

}
