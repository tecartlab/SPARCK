{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 8,
			"minor" : 2,
			"revision" : 2,
			"architecture" : "x64",
			"modernui" : 1
		}
,
		"classnamespace" : "box",
		"rect" : [ 147.0, 447.0, 579.0, 421.0 ],
		"bglocked" : 0,
		"openinpresentation" : 1,
		"default_fontsize" : 12.0,
		"default_fontface" : 0,
		"default_fontname" : "Arial",
		"gridonopen" : 1,
		"gridsize" : [ 15.0, 15.0 ],
		"gridsnaponopen" : 1,
		"objectsnaponopen" : 1,
		"statusbarvisible" : 2,
		"toolbarvisible" : 0,
		"lefttoolbarpinned" : 0,
		"toptoolbarpinned" : 0,
		"righttoolbarpinned" : 0,
		"bottomtoolbarpinned" : 0,
		"toolbars_unpinned_last_save" : 0,
		"tallnewobj" : 0,
		"boxanimatetime" : 200,
		"enablehscroll" : 0,
		"enablevscroll" : 0,
		"devicewidth" : 0.0,
		"description" : "",
		"digest" : "",
		"tags" : "",
		"style" : "",
		"subpatcher_template" : "",
		"assistshowspatchername" : 0,
		"boxes" : [ 			{
				"box" : 				{
					"args" : [ "#1", "texture/planes", "@text", "planes", "@type", "int", "@size", 59, 19, "@items", "wmmm", "mwww", "mmmm" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 1,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-46",
					"lockeddragscroll" : 0,
					"lockedsize" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.ossia.remote.select.maxpat",
					"numinlets" : 3,
					"numoutlets" : 4,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "int", "", "", "" ],
					"patching_rect" : [ 277.25, 240.0, 125.0, 21.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 40.0, 120.0, 19.0 ],
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "#1", "render/pass", "@text", "pass", "@items", "passA", "passB", "passC", "passD", "passE", "passF", "passG", "passH", "passZ", "manualA", "manualB", "manualC", "manualD", "beamer", "@type", "string", "@size", 179, 18 ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 1,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-20",
					"lockeddragscroll" : 0,
					"lockedsize" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.ossia.remote.select.maxpat",
					"numinlets" : 3,
					"numoutlets" : 4,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "int", "", "", "" ],
					"patching_rect" : [ 277.25, 206.0, 208.0, 17.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 60.0, 242.0, 19.0 ],
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "#1", "texture/dim/size", "@text", "dimension" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 1,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-10",
					"lockeddragscroll" : 0,
					"lockedsize" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.ossia.remote.vec2i.maxpat",
					"numinlets" : 3,
					"numoutlets" : 3,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "", "", "" ],
					"patching_rect" : [ 279.0, 289.0, 208.5, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 20.0, 240.0, 19.0 ],
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "#1", "edit", "@uselabeltext", 1, "@onlabeltext", "Output", "@offlabeltext", "edit...", "@size", 119, 19, "@mode", 1, "@hint", "edit mesh warp" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 1,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-22",
					"lockeddragscroll" : 0,
					"lockedsize" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.ossia.remote.impulse.maxpat",
					"numinlets" : 3,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 271.0, 106.0, 200.0, 19.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 120.0, 40.0, 120.0, 19.0 ],
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "#1", "image/file", "@text", "image", "@size", 139, 19, "@menutype", "file" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 1,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-5",
					"lockeddragscroll" : 0,
					"lockedsize" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.ossia.remote.menu.maxpat",
					"numinlets" : 3,
					"numoutlets" : 4,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "", "int", "", "" ],
					"patching_rect" : [ 277.25, 145.5, 225.0, 17.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 0.0, 220.0, 19.0 ],
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "#1", "image/filewatch" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 1,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-19",
					"lockeddragscroll" : 0,
					"lockedsize" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.ossia.remote.toggle.maxpat",
					"numinlets" : 3,
					"numoutlets" : 3,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "", "", "" ],
					"patching_rect" : [ 529.75, 145.5, 32.0, 17.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 220.0, 0.0, 20.0, 19.0 ],
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"color" : [ 0.12549, 0.796078, 0.894118, 1.0 ],
					"id" : "obj-17",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 301.0, 63.0, 64.0, 22.0 ],
					"text" : "ossia.view"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-4",
					"maxclass" : "newobj",
					"numinlets" : 2,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 301.0, 34.0, 107.0, 22.0 ],
					"text" : "routepass address"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-2",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 248.0, 5.0, 72.0, 22.0 ],
					"text" : "patcherargs"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"border" : 2,
					"bordercolor" : [ 0.0, 0.0, 0.0, 0.8 ],
					"grad1" : [ 0.65098, 0.666667, 0.662745, 0.0 ],
					"grad2" : [ 0.65098, 0.666667, 0.662745, 1.0 ],
					"hidden" : 1,
					"id" : "obj-3",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 0.0, 0.0, 242.0, 60.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 0.0, 242.0, 60.0 ],
					"proportion" : 0.39,
					"varname" : "vpl_canvas_folded"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"border" : 2,
					"bordercolor" : [ 0.0, 0.0, 0.0, 0.8 ],
					"grad1" : [ 0.65098, 0.666667, 0.662745, 1.0 ],
					"grad2" : [ 0.65098, 0.666667, 0.662745, 1.0 ],
					"hidden" : 1,
					"id" : "obj-1",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 0.0, 0.0, 242.0, 80.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 0.0, 242.0, 80.0 ],
					"proportion" : 0.39,
					"varname" : "vpl_canvas_unfolded"
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-4", 0 ],
					"source" : [ "obj-2", 1 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-17", 0 ],
					"source" : [ "obj-4", 0 ]
				}

			}
 ],
		"dependency_cache" : [ 			{
				"name" : "bs.ossia.remote.toggle.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/ossia",
				"patcherrelativepath" : "../../ossia",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.ossia.remote.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript/ossia",
				"patcherrelativepath" : "../../../javascript/ossia",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.ossia.remote.menu.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/ossia",
				"patcherrelativepath" : "../../ossia",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.gui.svg.button.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/gui",
				"patcherrelativepath" : "../../gui",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.ossia.remote.impulse.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/ossia",
				"patcherrelativepath" : "../../ossia",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.ossia.remote.vec2i.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/ossia",
				"patcherrelativepath" : "../../ossia",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "2int.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/ossia/basic/util",
				"patcherrelativepath" : "../../ossia/basic/util",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.ossia.remote.select.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/ossia",
				"patcherrelativepath" : "../../ossia",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "ossia.view.mxo",
				"type" : "iLaX"
			}
, 			{
				"name" : "ossia.remote.mxo",
				"type" : "iLaX"
			}
, 			{
				"name" : "icst.button.mxo",
				"type" : "iLaX"
			}
, 			{
				"name" : "mxj.mxo",
				"type" : "iLaX"
			}
, 			{
				"name" : "icst.intui.mxo",
				"type" : "iLaX"
			}
 ],
		"autosave" : 0,
		"styles" : [ 			{
				"name" : "AudioStatus_Menu",
				"default" : 				{
					"bgfillcolor" : 					{
						"type" : "color",
						"color" : [ 0.294118, 0.313726, 0.337255, 1 ],
						"color1" : [ 0.454902, 0.462745, 0.482353, 0.0 ],
						"color2" : [ 0.290196, 0.309804, 0.301961, 1.0 ],
						"angle" : 270.0,
						"proportion" : 0.39,
						"autogradient" : 0
					}

				}
,
				"parentstyle" : "",
				"multi" : 0
			}
, 			{
				"name" : "Jamoma_highlighted_orange",
				"default" : 				{
					"accentcolor" : [ 1.0, 0.5, 0.0, 1.0 ]
				}
,
				"parentstyle" : "",
				"multi" : 0
			}
, 			{
				"name" : "STYLE1",
				"parentstyle" : "",
				"multi" : 0
			}
, 			{
				"name" : "jpatcher001",
				"parentstyle" : "",
				"multi" : 0
			}
 ],
		"bgcolor" : [ 1.0, 1.0, 1.0, 0.0 ]
	}

}
