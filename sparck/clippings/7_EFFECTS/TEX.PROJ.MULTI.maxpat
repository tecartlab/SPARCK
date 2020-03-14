{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 8,
			"minor" : 0,
			"revision" : 4,
			"architecture" : "x64",
			"modernui" : 1
		}
,
		"classnamespace" : "box",
		"rect" : [ 103.0, 218.0, 598.0, 480.0 ],
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
		"tags" : "BeamStreamer Node Shader",
		"style" : "",
		"subpatcher_template" : "",
		"boxes" : [ 			{
				"box" : 				{
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-1",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.node.shader.tex.proj.2.maxpat",
					"numinlets" : 15,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 0.0, 0.0, 180.0, 185.0 ],
					"varname" : "ShrdTexProj",
					"viewvisibility" : 1
				}

			}
 ],
		"lines" : [  ],
		"dependency_cache" : [ 			{
				"name" : "bs.node.shader.tex.proj.2.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/nodes/effects",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/nodes/effects",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.nodelogic.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/vpl",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/vpl",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.node.js",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/javascript/vpl",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/javascript/vpl",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.unique.js",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/javascript/vpl",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/javascript/vpl",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.svg.button.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/gui",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/gui",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.inlet.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/vpl",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/vpl",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.node.shader.tex.proj.2.properties.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/nodes/effects",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/nodes/effects",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.windows.pops.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/gui",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/gui",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.windows.pops.js",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/javascript/gui",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/javascript/gui",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.win.reactive.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/utils",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/utils",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.node.logo.power.png",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/media",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/media",
				"type" : "PNG",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.svg.toggle.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/gui",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/gui",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.util.color.values.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/gui",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/gui",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.receive.select.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/msg",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/msg",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.receive.selector.js",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/javascript/msg",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/javascript/msg",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.shader.tex.proj.mulit.math.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/utils",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/utils",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.anim.node.maxpat",
				"bootpath" : "~/Documents/Max 8/Packages/sparck/patchers/utils",
				"patcherrelativepath" : "../../../../../../../../Documents/Max 8/Packages/sparck/patchers/utils",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.send.mxe64",
				"type" : "mx64"
			}
, 			{
				"name" : "bs.msg.receive.selector.mxe64",
				"type" : "mx64"
			}
, 			{
				"name" : "bs.math.la.matrix.vecs.mxe64",
				"type" : "mx64"
			}
 ],
		"autosave" : 0
	}

}
