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
		"rect" : [ 59.0, 104.0, 640.0, 480.0 ],
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
					"id" : "obj-28",
					"maxclass" : "textedit",
					"numinlets" : 1,
					"numoutlets" : 4,
					"outlettype" : [ "", "int", "", "" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 99.600000000000023, 357.0, 234.000000000000028, 46.0 ],
					"text" : "node/Beamer_Links/identify 0"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-27",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 99.600000000000023, 317.0, 72.0, 22.0 ],
					"text" : "prepend set"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-26",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 99.600000000000023, 286.0, 71.0, 22.0 ],
					"text" : "fromsymbol"
				}

			}
, 			{
				"box" : 				{
					"fontface" : 0,
					"fontname" : "Arial",
					"fontsize" : 12.0,
					"id" : "obj-20",
					"items" : [ "node/Beamer_Links/calibrate", 0, ",", "node/Beamer_Links/enable", 1, ",", "node/Beamer_Links/identify", 0, ",", "node/Beamer_Links/calibration/file", "Beamer_links.xml", ",", "node/Beamer_Links/calibration/file/path", "*", ",", "node/Beamer_Links/gizmo/renderGroup", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ",", "node/Beamer_Links/internal/body", "expand", ",", "node/Beamer_Links/internal/nodeid", "2472_id", ",", "node/Beamer_Links/lens/stereo/enable", 0, ",", "node/Beamer_Links/render/renderGroup", 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ",", "node/Beamer_Links/render/antialias/enable", 0, ",", "node/Beamer_Links/render/background/color", 0, 0, 0, 1, ",", "node/Beamer_Links/render/blend/enable", 0, ",", "node/Beamer_Links/render/texture/dim/size", 1920, 1200, ",", "node/Beamer_Links/tfm/parent", "<none>" ],
					"maxclass" : "chooser",
					"multiselect" : 0,
					"numinlets" : 1,
					"numoutlets" : 6,
					"outlettype" : [ "", "", "", "", "", "" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 49.0, 133.0, 272.0, 122.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 6.0, 387.0, 172.0 ]
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-15",
					"items" : [ "360Camera", ",", "Beamer_Links", ",", "Beamer_Mitte", ",", "Beamer_Rechts", ",", "BlendSoftedge", ",", "Camera_Center", ",", "Camera_Left", ",", "Camera_Right", ",", "Canvas_360preview", ",", "Canvas_ScreenOne", ",", "Capture_Mask_Links", ",", "Capture_Mask_Mitte", ",", "Capture_Mask_Rechts", ",", "Grid", ",", "Mask_ScreenOne", ",", "OptiTrack", ",", "OptiTrack_Dortmund", ",", "Output", ",", "RB_Insta360", ",", "RigidBody_ScreenOne", ",", "SpatialSoftedge", ",", "SpoutReceiver_360", ",", "SpoutReceiver_Center", ",", "SpoutReceiver_Left", ",", "SpoutReceiver_Right", ",", "TextureProjectory", ",", "Viewport", ",", "Viewport_1", ",", "Viewport_2" ],
					"maxclass" : "umenu",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "int", "", "" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 131.0, 89.0, 144.0, 22.0 ],
					"prefix" : "getnode",
					"prefix_mode" : 1
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-10",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 49.0, 45.0, 101.0, 22.0 ],
					"saved_object_attributes" : 					{
						"filename" : "bs.ossia.reflect",
						"parameter_enable" : 0
					}
,
					"text" : "js bs.ossia.reflect"
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-15", 0 ],
					"source" : [ "obj-10", 1 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-20", 0 ],
					"source" : [ "obj-10", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-10", 0 ],
					"midpoints" : [ 203.0, 121.0, 291.75, 121.0, 291.75, 34.0, 58.5, 34.0 ],
					"source" : [ "obj-15", 1 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-26", 0 ],
					"source" : [ "obj-20", 1 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-27", 0 ],
					"source" : [ "obj-26", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-28", 0 ],
					"source" : [ "obj-27", 0 ]
				}

			}
 ],
		"dependency_cache" : [ 			{
				"name" : "bs.ossia.reflect.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript/ossia",
				"patcherrelativepath" : "../../javascript/ossia",
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
, 			{
				"name" : "rnbodefault",
				"default" : 				{
					"fontsize" : [ 12.0 ],
					"accentcolor" : [ 0.343034118413925, 0.506230533123016, 0.86220508813858, 1.0 ],
					"fontname" : [ "Lato" ],
					"elementcolor" : [ 0.357540726661682, 0.515565991401672, 0.861786782741547, 1.0 ],
					"bgcolor" : [ 0.031372549019608, 0.125490196078431, 0.211764705882353, 1.0 ],
					"color" : [ 0.929412, 0.929412, 0.352941, 1.0 ],
					"stripecolor" : [ 0.258338063955307, 0.352425158023834, 0.511919498443604, 1.0 ],
					"bgfillcolor" : 					{
						"type" : "color",
						"color" : [ 0.031372549019608, 0.125490196078431, 0.211764705882353, 1.0 ],
						"color1" : [ 0.031372549019608, 0.125490196078431, 0.211764705882353, 1.0 ],
						"color2" : [ 0.263682, 0.004541, 0.038797, 1.0 ],
						"angle" : 270.0,
						"proportion" : 0.39,
						"autogradient" : 0.0
					}

				}
,
				"parentstyle" : "",
				"multi" : 0
			}
, 			{
				"name" : "rnbohighcontrast",
				"default" : 				{
					"fontsize" : [ 13.0 ],
					"accentcolor" : [ 0.666666666666667, 0.666666666666667, 0.666666666666667, 1.0 ],
					"elementcolor" : [ 0.223386004567146, 0.254748553037643, 0.998085916042328, 1.0 ],
					"selectioncolor" : [ 0.301960784313725, 0.694117647058824, 0.949019607843137, 1.0 ],
					"locked_bgcolor" : [ 0.258823529411765, 0.258823529411765, 0.258823529411765, 1.0 ],
					"editing_bgcolor" : [ 0.258823529411765, 0.258823529411765, 0.258823529411765, 1.0 ],
					"bgcolor" : [ 0.0, 0.0, 0.0, 1.0 ],
					"color" : [ 1.0, 0.874509803921569, 0.141176470588235, 1.0 ],
					"textcolor_inverse" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textcolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"stripecolor" : [ 0.258823529411765, 0.258823529411765, 0.258823529411765, 1.0 ],
					"clearcolor" : [ 1.0, 1.0, 1.0, 0.0 ],
					"bgfillcolor" : 					{
						"type" : "color",
						"color" : [ 0.0, 0.0, 0.0, 1.0 ],
						"color1" : [ 0.090196078431373, 0.090196078431373, 0.090196078431373, 1.0 ],
						"color2" : [ 0.156862745098039, 0.168627450980392, 0.164705882352941, 1.0 ],
						"angle" : 270.0,
						"proportion" : 0.5,
						"autogradient" : 0.0
					}

				}
,
				"parentstyle" : "",
				"multi" : 0
			}
 ]
	}

}
