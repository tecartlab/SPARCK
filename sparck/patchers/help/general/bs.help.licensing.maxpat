{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 8,
			"minor" : 0,
			"revision" : 0,
			"architecture" : "x64",
			"modernui" : 1
		}
,
		"rect" : [ 5.0, 50.0, 540.0, 650.0 ],
		"bgcolor" : [ 0.862745, 0.870588, 0.878431, 1.0 ],
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
		"enablehscroll" : 1,
		"enablevscroll" : 1,
		"devicewidth" : 0.0,
		"description" : "",
		"digest" : "",
		"tags" : "",
		"style" : "",
		"subpatcher_template" : "",
		"title" : "Licensing - Help",
		"boxes" : [ 			{
				"box" : 				{
					"fontface" : 1,
					"fontname" : "Verdana",
					"id" : "obj-26",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 3.0, 1167.0, 522.0, 21.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 3.0, 1167.0, 522.0, 21.0 ],
					"style" : "",
					"text" : "*__________________________________________________________*",
					"varname" : "lastLine"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-1508",
					"linecount" : 2,
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 602.0, 8.0, 65.0, 35.0 ],
					"presentation_linecount" : 2,
					"presentation_rect" : [ 602.0, 8.0, 65.0, 35.0 ],
					"style" : "",
					"text" : "loadmess populate"
				}

			}
, 			{
				"box" : 				{
					"fontface" : 1,
					"fontname" : "Verdana",
					"id" : "obj-1507",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ -4.0, 15.0, 518.0, 21.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 1.5, 30.0, 518.0, 21.0 ],
					"style" : "",
					"text" : "___________________________________________________________"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Licensing" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-36",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.windowtitle.maxpat",
					"numinlets" : 2,
					"numoutlets" : 2,
					"offset" : [ 0.0, 0.0 ],
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 537.5, 102.0, 296.0, 44.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 3.0, 518.0, 43.0 ],
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"fontsize" : 8.0,
					"hidden" : 1,
					"id" : "obj-24",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 565.0, 155.0, 72.0, 17.0 ],
					"presentation_rect" : [ 565.0, 155.0, 72.0, 17.0 ],
					"save" : [ "#N", "thispatcher", ";", "#Q", "end", ";" ],
					"style" : "",
					"text" : "thispatcher"
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"hidden" : 1,
					"id" : "obj-34",
					"index" : 0,
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 814.5, 155.0, 17.0, 17.0 ],
					"presentation_rect" : [ 814.5, 155.0, 17.0, 17.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"hidden" : 1,
					"id" : "obj-32",
					"index" : 0,
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 537.5, 155.0, 17.0, 17.0 ],
					"presentation_rect" : [ 537.5, 155.0, 17.0, 17.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"hidden" : 1,
					"id" : "obj-30",
					"index" : 0,
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 814.5, 79.0, 17.0, 17.0 ],
					"presentation_rect" : [ 814.5, 79.0, 17.0, 17.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"hidden" : 1,
					"id" : "obj-28",
					"index" : 0,
					"maxclass" : "inlet",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 537.5, 79.0, 17.0, 17.0 ],
					"presentation_rect" : [ 537.5, 79.0, 17.0, 17.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"bgcolor" : [ 0.784314, 0.145098, 0.023529, 1.0 ],
					"fontsize" : 18.0,
					"id" : "obj-10",
					"maxclass" : "textbutton",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 296.0, 48.0, 108.0, 39.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 296.0, 55.0, 108.0, 32.0 ],
					"rounded" : 8.0,
					"style" : "",
					"text" : "MASTER",
					"textcolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textoncolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textovercolor" : [ 1.0, 1.0, 1.0, 1.0 ]
				}

			}
, 			{
				"box" : 				{
					"bgcolor" : [ 0.466667, 0.254902, 0.607843, 1.0 ],
					"fontsize" : 18.0,
					"id" : "obj-19",
					"maxclass" : "textbutton",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 406.0, 48.0, 108.0, 39.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 406.0, 55.0, 108.0, 32.0 ],
					"rounded" : 8.0,
					"style" : "",
					"text" : "PRO",
					"textcolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textoncolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textovercolor" : [ 1.0, 1.0, 1.0, 1.0 ]
				}

			}
, 			{
				"box" : 				{
					"bgcolor" : [ 0.870588, 0.415686, 0.062745, 1.0 ],
					"fontsize" : 18.0,
					"id" : "obj-18",
					"maxclass" : "textbutton",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 186.0, 48.0, 108.0, 39.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 186.0, 55.0, 108.0, 32.0 ],
					"rounded" : 8.0,
					"style" : "",
					"text" : "BASIC",
					"textcolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textoncolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textovercolor" : [ 1.0, 1.0, 1.0, 1.0 ]
				}

			}
, 			{
				"box" : 				{
					"bgcolor" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"fontsize" : 18.0,
					"id" : "obj-17",
					"maxclass" : "textbutton",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 76.0, 48.0, 108.0, 39.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 76.0, 55.0, 108.0, 32.0 ],
					"rounded" : 8.0,
					"style" : "",
					"text" : "FREE",
					"textcolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textoncolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textovercolor" : [ 1.0, 1.0, 1.0, 1.0 ]
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-1241",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 269.0, 1159.0, 150.0, 20.0 ],
					"presentation_rect" : [ 269.0, 1159.0, 150.0, 20.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-3",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 537.5, 15.0, 60.0, 22.0 ],
					"presentation_rect" : [ 537.5, 15.0, 60.0, 22.0 ],
					"style" : "",
					"text" : "loadbang"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-1",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 537.5, 48.0, 92.0, 22.0 ],
					"presentation_rect" : [ 537.5, 48.0, 92.0, 22.0 ],
					"saved_object_attributes" : 					{
						"filename" : "bs.vpl.help.js",
						"parameter_enable" : 0
					}
,
					"style" : "",
					"text" : "js bs.vpl.help.js"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"background" : 1,
					"grad1" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"grad2" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"id" : "obj-1821",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 80.0, 92.0, 104.0, 1077.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 76.0, 92.0, 108.0, 1071.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"background" : 1,
					"grad1" : [ 0.466667, 0.254902, 0.607843, 1.0 ],
					"grad2" : [ 0.372549, 0.196078, 0.486275, 1.0 ],
					"id" : "obj-1244",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 410.0, 92.0, 104.0, 1077.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 406.0, 92.0, 108.0, 1071.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"background" : 1,
					"grad1" : [ 0.784314, 0.145098, 0.023529, 1.0 ],
					"grad2" : [ 0.784314, 0.145098, 0.023529, 1.0 ],
					"id" : "obj-1243",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 300.0, 92.0, 104.0, 1077.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 296.0, 92.0, 108.0, 1071.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"background" : 1,
					"grad1" : [ 0.952941, 0.564706, 0.098039, 1.0 ],
					"grad2" : [ 0.952941, 0.564706, 0.098039, 1.0 ],
					"id" : "obj-1242",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 190.0, 92.0, 104.0, 1077.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 186.0, 92.0, 108.0, 1071.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Window" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-4",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 80.0, 104.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 80.0, 104.0, 100.0, 15.0 ],
					"varname" : "_lic_Window_1",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Window" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-6",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 104.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 104.0, 100.0, 15.0 ],
					"varname" : "_lic_Window_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Window" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-8",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 104.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 104.0, 100.0, 15.0 ],
					"varname" : "_lic_Window_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Window" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-11",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 104.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 104.0, 100.0, 15.0 ],
					"varname" : "_lic_Window_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "ViewPort" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-13",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 80.0, 121.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 80.0, 121.0, 100.0, 15.0 ],
					"varname" : "_lic_ViewPort_1",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "ViewPort" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-15",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 121.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 121.0, 100.0, 15.0 ],
					"varname" : "_lic_ViewPort_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "ViewPort" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-20",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 121.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 121.0, 100.0, 15.0 ],
					"varname" : "_lic_ViewPort_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "ViewPort" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-22",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 121.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 121.0, 100.0, 15.0 ],
					"varname" : "_lic_ViewPort_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Monitor" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-25",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 138.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 138.0, 100.0, 15.0 ],
					"varname" : "_lic_Monitor_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Monitor" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-29",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 138.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 138.0, 100.0, 15.0 ],
					"varname" : "_lic_Monitor_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Monitor" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-33",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 138.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 138.0, 100.0, 15.0 ],
					"varname" : "_lic_Monitor_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "OscSend" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-37",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 155.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 155.0, 100.0, 15.0 ],
					"varname" : "_lic_OscSend_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "OscSend" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-39",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 155.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 155.0, 100.0, 15.0 ],
					"varname" : "_lic_OscSend_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "OculusRift" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-41",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 172.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 172.0, 100.0, 15.0 ],
					"varname" : "_lic_OculusRift_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "OculusRift" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-43",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 172.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 172.0, 100.0, 15.0 ],
					"varname" : "_lic_OculusRift_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "HtcVive" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-45",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 189.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 189.0, 100.0, 15.0 ],
					"varname" : "_lic_HtcVive_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "HtcVive" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-47",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 189.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 189.0, 100.0, 15.0 ],
					"varname" : "_lic_HtcVive_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "OscMessage" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-49",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 206.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 206.0, 100.0, 15.0 ],
					"varname" : "_lic_OscMessage_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-51",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 102.0, 517.0, 122.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 102.0, 517.0, 122.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_Output_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-53",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 107.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 107.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "Output",
					"varname" : "_lic_Output_title"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Video" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-55",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 80.0, 227.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 80.0, 227.0, 100.0, 15.0 ],
					"varname" : "_lic_Video_1",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Video" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-57",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 227.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 227.0, 100.0, 15.0 ],
					"varname" : "_lic_Video_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Video" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-59",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 227.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 227.0, 100.0, 15.0 ],
					"varname" : "_lic_Video_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Video" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-61",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 227.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 227.0, 100.0, 15.0 ],
					"varname" : "_lic_Video_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Texture" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-63",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 80.0, 244.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 80.0, 244.0, 100.0, 15.0 ],
					"varname" : "_lic_Texture_1",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Texture" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-65",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 244.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 244.0, 100.0, 15.0 ],
					"varname" : "_lic_Texture_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Texture" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-67",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 244.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 244.0, 100.0, 15.0 ],
					"varname" : "_lic_Texture_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Texture" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-69",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 244.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 244.0, 100.0, 15.0 ],
					"varname" : "_lic_Texture_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SyphonReceive" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-71",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 261.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 261.0, 100.0, 15.0 ],
					"varname" : "_lic_SyphonReceive_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SyphonReceive" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-73",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 261.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 261.0, 100.0, 15.0 ],
					"varname" : "_lic_SyphonReceive_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SyphonReceive" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-75",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 261.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 261.0, 100.0, 15.0 ],
					"varname" : "_lic_SyphonReceive_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SpoutReceive" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-77",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 278.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 278.0, 100.0, 15.0 ],
					"varname" : "_lic_SpoutReceive_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SpoutReceive" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-79",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 278.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 278.0, 100.0, 15.0 ],
					"varname" : "_lic_SpoutReceive_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SpoutReceive" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-81",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 278.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 278.0, 100.0, 15.0 ],
					"varname" : "_lic_SpoutReceive_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SyphonSend" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-83",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 295.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 295.0, 100.0, 15.0 ],
					"varname" : "_lic_SyphonSend_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SyphonSend" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-85",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 295.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 295.0, 100.0, 15.0 ],
					"varname" : "_lic_SyphonSend_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SpoutSend" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-87",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 312.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 312.0, 100.0, 15.0 ],
					"varname" : "_lic_SpoutSend_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SpoutSend" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-89",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 312.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 312.0, 100.0, 15.0 ],
					"varname" : "_lic_SpoutSend_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureMask" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-91",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 329.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 329.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureMask_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureMask" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-93",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 329.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 329.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureMask_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-95",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 225.0, 517.0, 122.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 225.0, 517.0, 122.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_2Dee_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-97",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 230.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 230.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "2Dee",
					"varname" : "_lic_2Dee_title"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Grid" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-99",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 350.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 350.0, 100.0, 15.0 ],
					"varname" : "_lic_Grid_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Grid" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-101",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 350.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 350.0, 100.0, 15.0 ],
					"varname" : "_lic_Grid_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Grid" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-103",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 350.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 350.0, 100.0, 15.0 ],
					"varname" : "_lic_Grid_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Model" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-105",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 367.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 367.0, 100.0, 15.0 ],
					"varname" : "_lic_Model_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Model" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-107",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 367.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 367.0, 100.0, 15.0 ],
					"varname" : "_lic_Model_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Canvas" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-109",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 384.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 384.0, 100.0, 15.0 ],
					"varname" : "_lic_Canvas_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Canvas" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-111",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 384.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 384.0, 100.0, 15.0 ],
					"varname" : "_lic_Canvas_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Shape" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-113",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 401.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 401.0, 100.0, 15.0 ],
					"varname" : "_lic_Shape_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Shape" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-115",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 401.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 401.0, 100.0, 15.0 ],
					"varname" : "_lic_Shape_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Light" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-117",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 418.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 418.0, 100.0, 15.0 ],
					"varname" : "_lic_Light_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Light" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-119",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 418.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 418.0, 100.0, 15.0 ],
					"varname" : "_lic_Light_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SceneCamera" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-121",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 435.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 435.0, 100.0, 15.0 ],
					"varname" : "_lic_SceneCamera_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SceneCamera" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-123",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 435.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 435.0, 100.0, 15.0 ],
					"varname" : "_lic_SceneCamera_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SceneCapture" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-125",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 452.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 452.0, 100.0, 15.0 ],
					"varname" : "_lic_SceneCapture_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SceneCapture" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-127",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 452.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 452.0, 100.0, 15.0 ],
					"varname" : "_lic_SceneCapture_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "LookAtCamera" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-129",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 469.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 469.0, 100.0, 15.0 ],
					"varname" : "_lic_LookAtCamera_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "BoxMapCamera" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-131",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 486.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 486.0, 100.0, 15.0 ],
					"varname" : "_lic_BoxMapCamera_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "BoxMapCapture" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-133",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 503.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 503.0, 100.0, 15.0 ],
					"varname" : "_lic_BoxMapCapture_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-135",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 348.0, 517.0, 173.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 348.0, 517.0, 173.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_3Dee_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-137",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 353.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 353.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "3Dee",
					"varname" : "_lic_3Dee_title"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TfmNode" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-139",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 524.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 524.0, 100.0, 15.0 ],
					"varname" : "_lic_TfmNode_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TfmNode" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-141",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 524.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 524.0, 100.0, 15.0 ],
					"varname" : "_lic_TfmNode_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TfmNodeInfo" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-143",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 541.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 541.0, 100.0, 15.0 ],
					"varname" : "_lic_TfmNodeInfo_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TfmNodePath" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-145",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 558.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 558.0, 100.0, 15.0 ],
					"varname" : "_lic_TfmNodePath_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TfmNodeMerge" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-147",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 575.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 575.0, 100.0, 15.0 ],
					"varname" : "_lic_TfmNodeMerge_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TfmLookAt" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-149",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 592.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 592.0, 100.0, 15.0 ],
					"varname" : "_lic_TfmLookAt_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TfmMirror" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-151",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 609.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 609.0, 100.0, 15.0 ],
					"varname" : "_lic_TfmMirror_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TurnTable" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-153",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 626.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 626.0, 100.0, 15.0 ],
					"varname" : "_lic_TurnTable_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "HeadRoom" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-155",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 643.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 643.0, 100.0, 15.0 ],
					"varname" : "_lic_HeadRoom_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Optitrack" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-157",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 660.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 660.0, 100.0, 15.0 ],
					"varname" : "_lic_Optitrack_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-159",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 522.0, 517.0, 156.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 522.0, 517.0, 156.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_Transform_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-161",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 527.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 527.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "Transform",
					"varname" : "_lic_Transform_title"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "CornerPin" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-163",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 80.0, 681.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 80.0, 681.0, 100.0, 15.0 ],
					"varname" : "_lic_CornerPin_1",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "CornerPin" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-165",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 681.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 681.0, 100.0, 15.0 ],
					"varname" : "_lic_CornerPin_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "CornerPin" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-167",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 681.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 681.0, 100.0, 15.0 ],
					"varname" : "_lic_CornerPin_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "CornerPin" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-169",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 681.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 681.0, 100.0, 15.0 ],
					"varname" : "_lic_CornerPin_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "MeshWarp" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-171",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 698.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 698.0, 100.0, 15.0 ],
					"varname" : "_lic_MeshWarp_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "MeshWarp" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-173",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 698.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 698.0, 100.0, 15.0 ],
					"varname" : "_lic_MeshWarp_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "MeshWarp" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-175",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 698.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 698.0, 100.0, 15.0 ],
					"varname" : "_lic_MeshWarp_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Beamer" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-177",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 715.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 715.0, 100.0, 15.0 ],
					"varname" : "_lic_Beamer_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Beamer" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-179",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 715.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 715.0, 100.0, 15.0 ],
					"varname" : "_lic_Beamer_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Calibrator" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-181",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 732.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 732.0, 100.0, 15.0 ],
					"varname" : "_lic_Calibrator_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Calibrator" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-183",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 732.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 732.0, 100.0, 15.0 ],
					"varname" : "_lic_Calibrator_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-185",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 679.0, 517.0, 71.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 679.0, 517.0, 71.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_Mapping_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-187",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 684.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 684.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "Mapping",
					"varname" : "_lic_Mapping_title"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureOP" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-189",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 753.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 753.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureOP_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureOP" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-191",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 753.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 753.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureOP_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureOP" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-193",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 753.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 753.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureOP_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureBlur" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-195",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 770.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 770.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureBlur_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureBlur" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-197",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 770.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 770.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureBlur_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureBlur" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-199",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 770.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 770.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureBlur_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureColorMap" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-201",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 787.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 787.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureColorMap_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureColorMap" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-203",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 787.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 787.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureColorMap_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureColorMap" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-205",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 787.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 787.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureColorMap_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureZoom" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-207",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 804.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 804.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureZoom_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureZoom" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-209",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 804.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 804.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureZoom_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureZoom" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-211",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 804.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 804.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureZoom_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureBrCoSa" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-213",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 821.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 821.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureBrCoSa_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureBrCoSa" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-215",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 821.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 821.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureBrCoSa_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureBrCoSa" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-217",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 821.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 821.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureBrCoSa_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureAnaglyph" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-219",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 838.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 838.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureAnaglyph_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureAnaglyph" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-221",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 838.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 838.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureAnaglyph_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureProjection" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-223",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 855.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 855.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureProjection_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureProjection" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-225",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 855.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 855.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureProjection_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "RayMarcher" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-227",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 872.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 872.0, 100.0, 15.0 ],
					"varname" : "_lic_RayMarcher_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "RayMarcher" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-229",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 872.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 872.0, 100.0, 15.0 ],
					"varname" : "_lic_RayMarcher_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Material" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-231",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 889.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 889.0, 100.0, 15.0 ],
					"varname" : "_lic_Material_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Material" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-233",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 889.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 889.0, 100.0, 15.0 ],
					"varname" : "_lic_Material_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "TextureStitcher" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-235",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 906.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 906.0, 100.0, 15.0 ],
					"varname" : "_lic_TextureStitcher_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "BoxMapProjection" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-237",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 923.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 923.0, 100.0, 15.0 ],
					"varname" : "_lic_BoxMapProjection_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "BlendSoftedge" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-239",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 940.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 940.0, 100.0, 15.0 ],
					"varname" : "_lic_BlendSoftedge_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SpatialSoftedge" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-241",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 957.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 957.0, 100.0, 15.0 ],
					"varname" : "_lic_SpatialSoftedge_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "ShaderSelection" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-243",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 974.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 974.0, 100.0, 15.0 ],
					"varname" : "_lic_ShaderSelection_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "SSAO" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-245",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 991.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 991.0, 100.0, 15.0 ],
					"varname" : "_lic_SSAO_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-247",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 751.0, 517.0, 258.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 751.0, 517.0, 258.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_Effects_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-249",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 756.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 756.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "Effects",
					"varname" : "_lic_Effects_title"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "QueScript" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-251",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 80.0, 1012.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 80.0, 1012.0, 100.0, 15.0 ],
					"varname" : "_lic_QueScript_1",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "QueScript" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-253",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 190.0, 1012.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 190.0, 1012.0, 100.0, 15.0 ],
					"varname" : "_lic_QueScript_3",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "QueScript" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-255",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 1012.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 1012.0, 100.0, 15.0 ],
					"varname" : "_lic_QueScript_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "QueScript" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-257",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 1012.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 1012.0, 100.0, 15.0 ],
					"varname" : "_lic_QueScript_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Patcher" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-259",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 1029.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 1029.0, 100.0, 15.0 ],
					"varname" : "_lic_Patcher_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-261",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 1010.0, 517.0, 37.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 1010.0, 517.0, 37.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_Script_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-263",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 1015.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 1015.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "Script",
					"varname" : "_lic_Script_title"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "LedStrip" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-265",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 1050.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 1050.0, 100.0, 15.0 ],
					"varname" : "_lic_LedStrip_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "LedStrip" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-267",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 1050.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 1050.0, 100.0, 15.0 ],
					"varname" : "_lic_LedStrip_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "LedStripOne" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-269",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 1067.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 1067.0, 100.0, 15.0 ],
					"varname" : "_lic_LedStripOne_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "LedStripOne" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-271",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 1067.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 1067.0, 100.0, 15.0 ],
					"varname" : "_lic_LedStripOne_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "PixelPusher" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-273",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 300.0, 1084.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 300.0, 1084.0, 100.0, 15.0 ],
					"varname" : "_lic_PixelPusher_5",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "PixelPusher" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-275",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 1084.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 1084.0, 100.0, 15.0 ],
					"varname" : "_lic_PixelPusher_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-277",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 1048.0, 517.0, 54.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 1048.0, 517.0, 54.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_Led_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-279",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 1053.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 1053.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "Led",
					"varname" : "_lic_Led_title"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Volume" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-281",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 1105.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 1105.0, 100.0, 15.0 ],
					"varname" : "_lic_Volume_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "VolumeShader" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-283",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 1122.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 1122.0, 100.0, 15.0 ],
					"varname" : "_lic_VolumeShader_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"args" : [ "VolumeLoader" ],
					"bgmode" : 0,
					"border" : 0,
					"clickthrough" : 0,
					"enablehscroll" : 0,
					"enablevscroll" : 0,
					"id" : "obj-285",
					"lockeddragscroll" : 0,
					"maxclass" : "bpatcher",
					"name" : "bs.help.util.license.nodelink.maxpat",
					"numinlets" : 0,
					"numoutlets" : 0,
					"offset" : [ 0.0, 0.0 ],
					"patching_rect" : [ 410.0, 1139.0, 100.0, 15.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 410.0, 1139.0, 100.0, 15.0 ],
					"varname" : "_lic_VolumeLoader_7",
					"viewvisibility" : 1
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"bgcolor" : [ 0.5, 0.5, 0.5, 0.3 ],
					"border" : 1,
					"bordercolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"id" : "obj-287",
					"maxclass" : "panel",
					"mode" : 0,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 1103.0, 517.0, 54.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 1103.0, 517.0, 54.0 ],
					"proportion" : 0.39,
					"style" : "",
					"varname" : "_lic_Volume_panel"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-289",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 1108.0, 65.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 1108.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "Volume",
					"varname" : "_lic_Volume_title"
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-1", 0 ],
					"source" : [ "obj-1508", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-36", 0 ],
					"source" : [ "obj-28", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-1", 0 ],
					"source" : [ "obj-3", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-36", 1 ],
					"source" : [ "obj-30", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-24", 0 ],
					"source" : [ "obj-36", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-34", 0 ],
					"source" : [ "obj-36", 1 ]
				}

			}
 ],
		"dependency_cache" : [ 			{
				"name" : "bs.help.util.license.nodelink.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/help/util",
				"patcherrelativepath" : "../util",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.vpl.help.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript/vpl",
				"patcherrelativepath" : "../../../javascript/vpl",
				"type" : "TEXT",
				"implicit" : 1
			}
, 			{
				"name" : "bs.help.util.windowtitle.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/help/util",
				"patcherrelativepath" : "../util",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.help.util.getpos.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript/help",
				"patcherrelativepath" : "../../../javascript/help",
				"type" : "TEXT",
				"implicit" : 1
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
 ]
	}

}
