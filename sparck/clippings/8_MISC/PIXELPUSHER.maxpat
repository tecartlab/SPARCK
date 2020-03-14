{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 7,
			"minor" : 0,
			"revision" : 5,
			"architecture" : "x86",
			"modernui" : 1
		}
,
		"rect" : [ 59.0, 103.0, 640.0, 480.0 ],
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
		"tags" : "BeamStreamer Node Output",
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
					"name" : "bs.node.output.pixelpusher.1.maxpat",
					"numinlets" : 3,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 0.0, 0.0, 212.0, 379.0 ],
					"varname" : "PixelPusher",
					"viewvisibility" : 1
				}

			}
 ],
		"lines" : [  ],
		"parameters" : 		{
			"obj-1::obj-19::obj-47::obj-19::obj-72" : [ "toggle", "toggle", 0 ]
		}
,
		"dependency_cache" : [ 			{
				"name" : "bs.node.output.pixelpusher.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.nodelogic.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.node.js",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.node.logo.default.png",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/media",
				"type" : "PNG ",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.dialogSlim.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.global.keys.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.win.reactive.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.global.keys.getlast.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.unique.js",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.go4help.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.util.inlet.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.node.output.pixelpusher.properties.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.receive.select.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.key.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.key.js",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.receive.selector.js",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.render.scene.receive.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "LayerCell.png",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/media",
				"type" : "PNG ",
				"implicit" : 1
			}
, 			{
				"name" : "LayerCell_shadow.png",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/media",
				"type" : "PNG ",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.windows.pops.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.windows.pops.js",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "pin.png",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/media",
				"type" : "PNG ",
				"implicit" : 1
			}
, 			{
				"name" : "bs.node.logo.wire.png",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/media",
				"type" : "PNG ",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.util.color.values.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/121121_BeamStreamer/00_dev/app_BeamStreamerTwo/BeamStreamer/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "mxj.mxo",
				"type" : "iLaX"
			}
 ],
		"autosave" : 0
	}

}
