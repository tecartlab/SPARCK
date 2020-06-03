{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 8,
			"minor" : 1,
			"revision" : 4,
			"architecture" : "x64",
			"modernui" : 1
		}
,
		"classnamespace" : "box",
		"rect" : [ 586.0, 85.0, 489.0, 480.0 ],
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
					"bordersize" : 6.0,
					"borderstyle" : 5,
					"fontface" : 0,
					"hint" : "inherited stereomode",
					"iconbackground" : 3,
					"iconbglinewidth" : 1.0,
					"iconsize" : 14.0,
					"iconstyle" : 2,
					"id" : "obj-4",
					"ignoreclick" : 1,
					"maxclass" : "icst.button",
					"numinlets" : 1,
					"numoutlets" : 2,
					"off_down_bg_color" : [ 1.0, 0.8, 0.0, 0.0 ],
					"off_down_fg_color" : [ 0.541176470588235, 0.474509803921569, 0.27843137254902, 1.0 ],
					"off_down_textcolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"off_up_bg_color" : [ 0.901960784313726, 0.901960784313726, 0.901960784313726, 0.0 ],
					"off_up_fg_color" : [ 0.541176470588235, 0.474509803921569, 0.27843137254902, 1.0 ],
					"off_up_textcolor" : [ 0.254901960784314, 0.250980392156863, 0.501960784313725, 1.0 ],
					"on_bordersize" : 15.0,
					"on_borderstyle" : 5,
					"on_down_bg_color" : [ 1.0, 1.0, 1.0, 0.0 ],
					"on_down_fg_color" : [ 0.541176470588235, 0.474509803921569, 0.27843137254902, 1.0 ],
					"on_up_bg_color" : [ 0.501960784313725, 0.501960784313725, 0.501960784313725, 0.0 ],
					"on_up_fg_color" : [ 0.541176470588235, 0.474509803921569, 0.27843137254902, 1.0 ],
					"outlettype" : [ "", "" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 231.73077400000011, 63.903809000000024, 16.0, 16.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 184.0, 0.0, 16.0, 16.0 ],
					"textcolor" : [ 0.254901960784314, 0.250980392156863, 0.501960784313725, 1.0 ]
				}

			}
, 			{
				"box" : 				{
					"color" : [ 0.12549, 0.796078, 0.894118, 1.0 ],
					"id" : "obj-13",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "" ],
					"patching_rect" : [ 231.73077400000011, 23.0, 161.0, 22.0 ],
					"text" : "ossia.remote #2/stereomode"
				}

			}
, 			{
				"box" : 				{
					"color" : [ 0.12549, 0.796078, 0.894118, 1.0 ],
					"id" : "obj-26",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "" ],
					"patching_rect" : [ 102.73077400000011, 23.0, 120.0, 22.0 ],
					"text" : "ossia.remote #2/type"
				}

			}
, 			{
				"box" : 				{
					"allowdrag" : 0,
					"arrow" : 0,
					"bgcolor" : [ 1.0, 1.0, 1.0, 0.0 ],
					"bgfillcolor_angle" : 270.0,
					"bgfillcolor_autogradient" : 0.0,
					"bgfillcolor_color" : [ 1.0, 1.0, 1.0, 0.0 ],
					"bgfillcolor_color1" : [ 0.862745, 0.870588, 0.878431, 1.0 ],
					"bgfillcolor_color2" : [ 0.862745, 0.870588, 0.878431, 1.0 ],
					"bgfillcolor_proportion" : 0.39,
					"bgfillcolor_type" : "color",
					"fontsize" : 9.0,
					"hint" : "inherited camera type",
					"id" : "obj-40",
					"ignoreclick" : 1,
					"items" : [ "unknown", ",", "camera", "capture", ",", "beamer", "capture", ",", "beamer", "mirror", ",", "special", "capture", ",", "special", "mirror", ",", "oculus", "rift", ",", "htc", "vive" ],
					"maxclass" : "umenu",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "int", "", "" ],
					"parameter_enable" : 0,
					"parameter_mappable" : 0,
					"patching_rect" : [ 102.73077400000011, 62.403809000000024, 100.0, 19.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 50.0, -1.0, 129.0, 19.0 ],
					"textcolor" : [ 0.239216, 0.254902, 0.278431, 1.0 ]
				}

			}
, 			{
				"box" : 				{
					"bgcolor" : [ 1.0, 1.0, 1.0, 0.0 ],
					"fontname" : "Arial",
					"fontsize" : 9.0,
					"hint" : "",
					"id" : "obj-2",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 14.805542000000116, 62.403809000000024, 42.0, 17.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 0.0, 0.0, 50.0, 17.0 ],
					"text" : "type",
					"textcolor" : [ 0.238638, 0.238638, 0.238638, 1.0 ]
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-4", 0 ],
					"source" : [ "obj-13", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-40", 0 ],
					"source" : [ "obj-26", 0 ]
				}

			}
 ],
		"dependency_cache" : [ 			{
				"name" : "ossia.remote.mxo",
				"type" : "iLaX"
			}
, 			{
				"name" : "icst.button.mxo",
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
		"clearcolor" : [ 1.0, 1.0, 1.0, 1.0 ],
		"bgcolor" : [ 0.996078431372549, 0.996078431372549, 0.996078431372549, 1.0 ]
	}

}
