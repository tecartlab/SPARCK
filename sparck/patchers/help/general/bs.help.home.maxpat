{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 7,
			"minor" : 3,
			"revision" : 4,
			"architecture" : "x64",
			"modernui" : 1
		}
,
		"rect" : [ 5.0, 50.0, 540.0, 650.0 ],
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
		"enablevscroll" : 1,
		"devicewidth" : 0.0,
		"description" : "",
		"digest" : "",
		"tags" : "",
		"style" : "default",
		"subpatcher_template" : "",
		"title" : "Home - Help",
		"boxes" : [ 			{
				"box" : 				{
					"fontface" : 1,
					"fontname" : "Verdana",
					"id" : "obj-1",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 24.0, 360.0, 518.0, 21.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 1.5, 30.0, 518.0, 21.0 ],
					"style" : "",
					"text" : "___________________________________________________________"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "Home" ],
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
					"patching_rect" : [ 5.5, 25.0, 296.0, 44.0 ],
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
					"patching_rect" : [ 33.0, 78.0, 72.0, 17.0 ],
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
					"patching_rect" : [ 282.5, 78.0, 17.0, 17.0 ],
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
					"patching_rect" : [ 5.5, 78.0, 17.0, 17.0 ],
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
					"patching_rect" : [ 282.5, 2.0, 17.0, 17.0 ],
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
					"patching_rect" : [ 5.5, 2.0, 17.0, 17.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"fontface" : 1,
					"fontname" : "Verdana",
					"id" : "obj-26",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 660.0, 522.0, 21.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 3.0, 724.0, 522.0, 21.0 ],
					"style" : "",
					"text" : "*__________________________________________________________*",
					"varname" : "lastLine"
				}

			}
, 			{
				"box" : 				{
					"fontsize" : 10.0,
					"id" : "obj-5",
					"linecount" : 2,
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 70.5, 481.0, 231.0, 31.0 ],
					"style" : "",
					"text" : ";\rmax launchbrowser http://tecartlab.com/feedback/"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Verdana",
					"id" : "obj-23",
					"linecount" : 28,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 24.0, 103.0, 511.0, 415.0 ],
					"presentation" : 1,
					"presentation_linecount" : 29,
					"presentation_rect" : [ 21.0, 61.0, 486.0, 429.0 ],
					"style" : "",
					"text" : "SPARCK is a very versatile application for the creation of Spatial Augmented Reality and Virtual Reality installations. It has a rich toolset to solve (almost) any videomapping problem you might encounter.\n\nThe core principle of working with SPARCK are the Nodes. The Nodes are the building-blocks and a way to understand a Node is to think of them as little tools that help solve a particular problem. And you as the author can choose from a wide variety of tools. All you need to do is to drop the needed node into the workspace and tell SPARCK in what relationship it works with other nodes. \n\nThis makes SPARCK very similar to visual programming languages, in fact it is based on one (MaxMSP) itself, except that it is NOT a programming-language. You dont need any understanding in programming to start with SPARCK.\n\nThere are three important ways to make the nodes work with each other:\n\n1. patching\n2. linking\n3. rendergroup\n\nThe nodes are organized in different categories, so you can find the required tool fast and efficient.\n\nAnd the different Licenses give you access to a certain set of nodes. Choose the license with the tools you need for you projects. You always can get a FREE license. The FREE license also allows you to access the DEMO mode, in which you can use and evaluate all the available nodes, with the limitation of saving beeing disabled."
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-36", 0 ],
					"source" : [ "obj-28", 0 ]
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
				"name" : "bs.help.util.windowtitle.maxpat",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/patchers/help/util",
				"type" : "JSON",
				"implicit" : 1
			}
, 			{
				"name" : "bs.help.util.getpos.js",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/javascript/help",
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
