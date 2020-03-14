{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 7,
			"minor" : 1,
			"revision" : 0,
			"architecture" : "x86",
			"modernui" : 1
		}
,
		"rect" : [ 100.0, 100.0, 919.0, 449.0 ],
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
		"boxes" : [ 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-3",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 155.0, 316.0, 113.0, 22.0 ],
					"style" : "",
					"text" : "read <selectname>"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-13",
					"linecount" : 2,
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 367.0, 337.0, 217.0, 35.0 ],
					"style" : "",
					"text" : "pStx - Pattrstorage Preset File (XML) - .xml"
				}

			}
, 			{
				"box" : 				{
					"allowdrag" : 0,
					"id" : "obj-10",
					"items" : [ "8BPS", "-", "Photoshop", "file", "-", ".psd", ",", "AFxB", "-", "FXB", "file", "-", ".fxb", ",", "AFxP", "-", "FXP", "file", "-", ".fxp", ",", "AIFF", "-", "AIFF", "audio", "file", "-", ".aif", ",", "AIFF", "-", "AIFF", "audio", "file", "-", ".aiff", ",", "ampf", "-", "Ableton", "Live", "Max", "Device", "-", ".amp", ",", "amxd", "-", "Ableton", "Live", "Max", "Device", "-", ".amxd", ",", "aPcs", "-", "VST", "Plug-In", "-", ".dll", ",", "aPcs", "-", "VST", "plug-in", "-", ".vst", ",", "APPL", "-", "Application", "-", ".app", ",", "APPL", "-", "Application", "-", ".exe", ",", "AUin", "-", "Audio", "Unit", "Plug-in", "-", ".auinfo", ",", "AUpi", "-", "Audio", "Unit", "Plug-in", "-", ".component", ",", "BMP", "-", "BMP", "file", "-", ".bmp", ",", "css", "-", "CSS", "file", "-", ".css", ",", "DATA", "-", "audio", "file", "-", ".data", ",", "FLAC", "-", "FLAC", "audio", "file", "-", ".flac", ",", "gDSP", "-", "dsp.gen", "File", "-", ".gendsp", ",", "GenX", "-", "GenExpr", "file", "-", ".genexpr", ",", "GIFf", "-", "GIF", "file", "-", ".gif", ",", "gJIT", "-", "jit.gen", "File", "-", ".genjit", ",", "iLaF", "-", "External", "object", "-", ".mxd", ",", "iLaF", "-", "External", "object", "-", ".mxe", ",", "iLaX", "-", "External", "object", "-", ".mxo", ",", "iLaX", "-", "Macintosh", "External", "Object", "-", ".mxo", ",", "J3dm", "-", "3D", "GameStudio", "Model", "File", "-", ".mdl", ",", "J3ds", "-", "3ds", "Max", "3DS", "Model", "File", "-", ".3ds", ",", "Jac", "-", "AC3D", "Model", "File", "-", ".ac", ",", "Jase", "-", "3ds", "Max", "ASE", "Model", "File", "-", ".ase", ",", "Jb3d", "-", "BlitzBasic", "3D", "Model", "File", "-", ".b3d", ",", "Jbln", "-", "Blender", "3D", "Model", "File", "-", ".blend", ",", "Jbvh", "-", "Biovision", "BVH", "Motion", "Capture", "File", "-", ".bvh", ",", "Jcob", "-", "TrueSpace", "COB", "Model", "File", "-", ".cob", ",", "Jcsm", "-", "CharacterStudio", "Motion", "Capture", "File", "-", ".csm", ",", "Jdae", "-", "Collada", "Model", "File", "-", ".dae", ",", "Jdxf", "-", "AutoCAD", "DXF", "Model", "File", "-", ".dxf", ",", "Jhmp", "-", "3D", "GameStudio", "Terrain", "File", "-", ".hmp", ",", "Jirm", "-", "Irrlicht", "Mesh", "File", "-", ".irrmesh", ",", "Jirr", "-", "Irrlicht", "Scene", "File", "-", ".irr", ",", "JiT!", "-", "Jitter", "data", "file", "-", ".jit", ",", "JiT!", "-", "Jitter", "data", "file", "-", ".jxf", ",", "Jlwo", "-", "LightWave", "Model", "File", "-", ".lwo", ",", "Jlxo", "-", "Modo", "Model", "File", "-", ".lxo", ",", "Jmd2", "-", "Quake", "II", "Mesh", "File", "-", ".md2", ",", "Jmd3", "-", "Quake", "III", "Mesh", "File", "-", ".md3", ",", "Jmd5", "-", "Doom", 3, "Model", "File", "-", ".md5", ",", "Jmdc", "-", "Return", "to", "Castle", "Wolfenstein", "Model", "File", "-", ".mdc", ",", "Jmdl", "-", "Quake", "I", "Mesh", "File", "-", ".mdl", ",", "Jmsd", "-", "Milkshape", "3D", "Model", "File", "-", ".ms3d", ",", "Jmtl", "-", "Jitter", "Material", "File", "-", ".jitmtl", ",", "Jndo", "-", "Izware", "Nendo", "Model", "File", "-", ".ndo", ",", "Jnff", "-", "Neutral", "File", "Format", "File", "-", ".nff", ",", "Jobj", "-", "Wavefront", "Object", "Model", "File", "-", ".obj", ",", "Joff", "-", "Object", "File", "Format", "File", "-", ".off", ",", "Jogr", "-", "Ogre3D", "XML", "Model", "File", "-", ".mesh.xml", ",", "JPEG", "-", "JPEG", "file", "-", ".jpeg", ",", "JPEG", "-", "JPEG", "file", "-", ".jpg", ",", "Jpk3", "-", "Quake", "III", "BSP", "File", "-", ".pk3", ",", "Jply", "-", "Stanford", "Polygon", "Library", "Model", "File", "-", ".ply", ",", "Jq3d", "-", "Quick3D", "Q3D", "Model", "File", "-", ".q3d", ",", "Jq3s", "-", "Quick3D", "Q3S", "Model", "File", "-", ".q3s", ",", "Jraw", "-", "PovRAY", "Raw", "Model", "File", "-", ".raw", ",", "Js8w", "-", "Sense8", "WorldToolKit", "File", "-", ".nff", ",", "Jscb", "-", "TrueSpace", "SCB", "Model", "File", "-", ".scb", ",", "Jsmd", "-", "Valve", "SMD", "Model", "File", "-", ".smd", ",", "JSON", "-", "Defaults", "definitions", "-", ".maxdefines", ",", "JSON", "-", "Defaults", "file", "-", ".maxdefaults", ",", "JSON", "-", "Help", "Patcher", "-", ".maxhelp", ",", "JSON", "-", "JSON", "-", ".json", ",", "JSON", "-", "Patcher", "-", ".maxpat", ",", "JSON", "-", "Preferences", "file", "-", ".maxpref", ",", "JSON", "-", "Preset", "file", "-", ".maxpresets", ",", "JSON", "-", "Prototype", "-", ".maxproto", ",", "JSON", "-", "Query", "file", "-", ".maxquery", ",", "JSON", "-", "Swatches", "file", "-", ".maxswatches", ",", "Jstl", "-", "Stereolithography", "Model", "File", "-", ".stl", ",", "Jter", "-", "Terragen", "Terrain", "File", "-", ".ter", ",", "Jvta", "-", "Valve", "VTA", "Model", "File", "-", ".vta", ",", "Jxmd", "-", "Direct", "X", "Model", "File", "-", ".x", ",", "maxb", "-", "Help", "file", "-", ".help", ",", "maxb", "-", "Patcher", "-", ".mxb", ",", "maxb", "-", "Patcher", "-", ".pat", ",", "maxc", "-", "Max", "Collective", "-", ".clct", ",", "maxc", "-", "Max", "Collective", "-", ".mxc", ",", "Midi", "-", "MIDI", "file", "-", ".mid", ",", "Midi", "-", "MIDI", "file", "-", ".midi", ",", "MooV", "-", "Video", "-", ".mov", ",", "Mp3", "-", "MP3", "audio", "file", "-", ".mp3", ",", "MPEG", "-", "Video", "-", ".mpeg", ",", "MPEG", "-", "Video", "-", ".mpg", ",", "mpg4", "-", "Video", "-", ".mp4", ",", "mPrj", "-", "Project", "-", ".maxproj", ",", "mx@c", "-", "Collective", "-", ".mxf", ",", "mZip", "-", "Packed", "Project", "-", ".maxzip", ",", "NxTS", "-", "NeXT/Sun", "audio", "file", "-", ".snd", ",", "PICS", "-", "PICS", "file", "-", ".pics", ",", "PICT", "-", "PICT", "file", "-", ".pct", ",", "PICT", "-", "PICT", "file", "-", ".pict", ",", "PNG", "-", "PNG", "file", "-", ".png", ",", "PNG", "-", "PNG", "file", "-", ".png", ",", "PNGf", "-", "PNG", "file", "-", ".png", ",", "pSto", "-", "Pattrstorage", "Preset", "File", "(JSON)", "-", ".json", ",", "pStx", "-", "Pattrstorage", "Preset", "File", "(XML)", "-", ".xml", ",", "svg", "-", "SVG", "file", "-", ".svg", ",", "SWFL", "-", "Flash", "file", "-", ".swf", ",", "TEXT", "-", "Help", "file", "-", ".help", ",", "TEXT", "-", "Java", "source", "file", "-", ".java", ",", "TEXT", "-", "Javascript", "file", "-", ".js", ",", "TEXT", "-", "OpenGL", "Shading", "Language", "file", "-", ".glsl", ",", "TEXT", "-", "Patcher", "-", ".mxt", ",", "TEXT", "-", "Patcher", "-", ".pat", ",", "TEXT", "-", "Text", "file", "-", ".txt", ",", "TEXT", "-", "Web", "page", "-", ".htm", ",", "TEXT", "-", "Web", "page", "-", ".html", ",", "TEXT", "-", "Web", "page", "-", ".xhtml", ",", "TEXT", "-", "XML", "file", "-", ".xml", ",", "TEXT", "-", "XML", "Reference", "file", "-", ".maxref.xml", ",", "TEXT", "-", "XML", "Shader", "Description", "file", "-", ".jxs", ",", "TEXT", "-", "XML", "Tutorial", "file", "-", ".maxtut.xml", ",", "TEXT", "-", "XML", "Vignette", "file", "-", ".maxvig.xml", ",", "TIFF", "-", "TIFF", "file", "-", ".tif", ",", "TIFF", "-", "TIFF", "file", "-", ".tiff", ",", "ULAW", "-", "SND", "file", "-", ".snd", ",", "VfW", "-", "AVI", "video", "-", ".avi", ",", "WAVE", "-", "WAVE", "audio", "file", "-", ".wav", ",", "xdll", "-", "Windows", "external", "object", "-", ".mxe", ",", "xQZZ", "-", "Support", "file", "-", ".hibundle", ",", "XSLT", "-", "XSLT", "file", "-", ".xsl", ",", "YAML", "-", "YAML", "-", ".yaml", ",", "YAML", "-", "YAML", "-", ".yml" ],
					"maxclass" : "umenu",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "int", "", "" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 373.0, 301.0, 205.0, 22.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-11",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 51.0, 400.0, 403.0, 22.0 ],
					"style" : "",
					"text" : "saveas /Volumes/path/filename.ext.xml"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-8",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 95.0, 316.0, 49.0, 22.0 ],
					"style" : "",
					"text" : "saveas"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-5",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 51.0, 316.0, 36.0, 22.0 ],
					"style" : "",
					"text" : "save"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-19",
					"linecount" : 20,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 13.0, 26.0, 661.0, 275.0 ],
					"style" : "",
					"text" : "HELP:\n#1 = Path output style [max, native, colon, slash, nativ_win]\n#2 = Path output type [ignore, absolute, relative, boot, C74]\n#3 = fileName command [i.e. read, load, file etc...]\n#4 = Save as command [i.e. saveas, write, store, etc...]\n#5 = Save command [i.e. save, writeagain, etc...]\n#6 = extension\n#7 = Subfolder name [* is for root folder]\n#8 = Subfolder depth\n\n@types <selectable file types>\n@fontsize <fontsize>\n@presentation_size <lengt> <height> of menu\n@setable <0/1(default)> sets if the selected menu item can be set\n@readonly <0(default)/1> sets if the set item can be saved \n@messageonselect <string> sets the message that is send when select... is selected\n@selecttext <string> sets the select text\n@readagain <0/1(default)>: when readagain = 0 -> it will only select a file that doesnt match the previously selected. \n@reset <string> the inlet command that sets the selector back to select (and subsequently sends the messageonselect)\n@filewatch <0(default)/1> when set to 1 it will trigger another fileName command if the file was altered in the meanwhile."
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-9",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 743.0, 86.0, 119.0, 22.0 ],
					"style" : "",
					"text" : "prepend projectPath"
				}

			}
, 			{
				"box" : 				{
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-7",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.msg.receive.select.maxpat",
					"numinlets" : 1,
					"numoutlets" : 8,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "", "", "", "", "", "", "", "" ],
					"patching_rect" : [ 676.0, 146.0, 205.0, 24.0 ],
					"varname" : "bs.msg.receive.select",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "native", "boot", "read", "saveas", "save", "xml", "projectors", 0, 120, "@types", "pStx" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-6",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.file.select.maxpat",
					"numinlets" : 1,
					"numoutlets" : 2,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 51.0, 349.0, 162.0, 25.0 ],
					"varname" : "bs.file.select",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-4",
					"maxclass" : "button",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "bang" ],
					"patching_rect" : [ 743.0, 31.0, 20.0, 20.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-2",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "bang" ],
					"patching_rect" : [ 743.0, 58.0, 103.0, 22.0 ],
					"style" : "",
					"text" : "opendialog folder"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-1",
					"maxclass" : "newobj",
					"numinlets" : 4,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 698.333374, 117.0, 153.0, 22.0 ],
					"style" : "",
					"text" : "bs.msg.send Proj::Settings"
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-13", 1 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-10", 1 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-9", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-2", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-6", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-3", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-2", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-4", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-6", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-5", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-11", 1 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-6", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-6", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-8", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-1", 1 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-9", 0 ]
				}

			}
 ],
		"parameters" : 		{
			"obj-6::obj-10::obj-72" : [ "toggle", "toggle", 0 ]
		}
,
		"dependency_cache" : [ 			{
				"name" : "bs.msg.send.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.sender.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.file.select.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.dialog.saveas.slim.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.global.keys.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.util.win.reactive.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.receive.key.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.key.receive.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.key.receive.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.receive.select.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.key.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.util.key.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.msg.receive.selector.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript",
				"type" : "TEXT",
				"implicit" : 1
			}
 ],
		"autosave" : 0
	}

}
