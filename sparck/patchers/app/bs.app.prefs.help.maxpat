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
		"rect" : [ 194.0, 287.0, 959.0, 480.0 ],
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
		"boxes" : [ 			{
				"box" : 				{
					"id" : "obj-32",
					"maxclass" : "newobj",
					"numinlets" : 2,
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"patching_rect" : [ 4.083344, 110.66568, 62.0, 22.0 ],
					"style" : "",
					"text" : "zl.change"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-40",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 3,
							"revision" : 4,
							"architecture" : "x64",
							"modernui" : 1
						}
,
						"rect" : [ 59.0, 104.0, 256.0, 341.0 ],
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
						"boxes" : [ 							{
								"box" : 								{
									"id" : "obj-1",
									"maxclass" : "newobj",
									"numinlets" : 2,
									"numoutlets" : 1,
									"outlettype" : [ "bang" ],
									"patching_rect" : [ 50.0, 141.0, 54.0, 22.0 ],
									"presentation_rect" : [ 50.0, 141.0, 54.0, 22.0 ],
									"style" : "",
									"text" : "delay 10"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-37",
									"maxclass" : "newobj",
									"numinlets" : 1,
									"numoutlets" : 2,
									"outlettype" : [ "bang", "" ],
									"patching_rect" : [ 50.0, 100.0, 101.0, 22.0 ],
									"presentation_rect" : [ 50.0, 100.0, 101.0, 22.0 ],
									"style" : "",
									"text" : "t b l"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-36",
									"maxclass" : "newobj",
									"numinlets" : 2,
									"numoutlets" : 2,
									"outlettype" : [ "", "" ],
									"patching_rect" : [ 50.0, 188.0, 101.0, 22.0 ],
									"presentation_rect" : [ 50.0, 188.0, 101.0, 22.0 ],
									"style" : "",
									"text" : "zl.reg"
								}

							}
, 							{
								"box" : 								{
									"comment" : "",
									"id" : "obj-38",
									"index" : 1,
									"maxclass" : "inlet",
									"numinlets" : 0,
									"numoutlets" : 1,
									"outlettype" : [ "" ],
									"patching_rect" : [ 50.0, 40.0, 30.0, 30.0 ],
									"presentation_rect" : [ 50.0, 40.0, 30.0, 30.0 ],
									"style" : ""
								}

							}
, 							{
								"box" : 								{
									"comment" : "",
									"id" : "obj-39",
									"index" : 1,
									"maxclass" : "outlet",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 50.0, 230.0, 30.0, 30.0 ],
									"presentation_rect" : [ 50.0, 230.0, 30.0, 30.0 ],
									"style" : ""
								}

							}
 ],
						"lines" : [ 							{
								"patchline" : 								{
									"destination" : [ "obj-36", 0 ],
									"source" : [ "obj-1", 0 ]
								}

							}
, 							{
								"patchline" : 								{
									"destination" : [ "obj-39", 0 ],
									"source" : [ "obj-36", 0 ]
								}

							}
, 							{
								"patchline" : 								{
									"destination" : [ "obj-1", 0 ],
									"source" : [ "obj-37", 0 ]
								}

							}
, 							{
								"patchline" : 								{
									"destination" : [ "obj-36", 1 ],
									"source" : [ "obj-37", 1 ]
								}

							}
, 							{
								"patchline" : 								{
									"destination" : [ "obj-37", 0 ],
									"source" : [ "obj-38", 0 ]
								}

							}
 ]
					}
,
					"patching_rect" : [ 4.083344, 82.0, 47.0, 22.0 ],
					"presentation_rect" : [ 4.083344, 82.0, 47.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p delay"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-28",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 494.083344, 73.33432, 159.0, 22.0 ],
					"presentation_rect" : [ 494.083344, 73.33432, 159.0, 22.0 ],
					"style" : "",
					"text" : "prepend checkLatestUpdate"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Courier New",
					"id" : "obj-30",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 524.0, 43.0, 174.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 297.0, 62.0, 255.0, 20.0 ],
					"style" : "",
					"text" : "Check for latest update"
				}

			}
, 			{
				"box" : 				{
					"clickedimage" : 1,
					"hint" : "",
					"id" : "obj-31",
					"maxclass" : "pictctrl",
					"mode" : 1,
					"name" : "bs.node.logo.default.png",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 494.083344, 40.0, 23.0, 23.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 597.0, 62.0, 19.0, 19.0 ],
					"varname" : "checkForLatestUpdate"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-23",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 494.083344, 206.66568, 250.0, 22.0 ],
					"presentation_rect" : [ 494.083344, 206.66568, 250.0, 22.0 ],
					"style" : "",
					"text" : "prepend showLicenseAboutToExpireWarning"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Courier New",
					"id" : "obj-26",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 524.0, 176.33136, 284.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 297.0, 35.0, 284.0, 20.0 ],
					"style" : "",
					"text" : "Show 'License about to expire' Warning"
				}

			}
, 			{
				"box" : 				{
					"clickedimage" : 1,
					"hint" : "",
					"id" : "obj-27",
					"maxclass" : "pictctrl",
					"mode" : 1,
					"name" : "bs.node.logo.default.png",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 494.083344, 173.33136, 23.0, 23.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 597.0, 35.0, 19.0, 19.0 ],
					"varname" : "showLicenseAboutToExpireWarning"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-19",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 494.083344, 141.0, 212.0, 22.0 ],
					"presentation_rect" : [ 494.083344, 141.0, 212.0, 22.0 ],
					"style" : "",
					"text" : "prepend showExpiredLicenseWarning"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Courier New",
					"id" : "obj-21",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 524.0, 110.66568, 255.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 297.0, 9.0, 255.0, 20.0 ],
					"style" : "",
					"text" : "Show 'License has Expired' Warning"
				}

			}
, 			{
				"box" : 				{
					"clickedimage" : 1,
					"hint" : "",
					"id" : "obj-22",
					"maxclass" : "pictctrl",
					"mode" : 1,
					"name" : "bs.node.logo.default.png",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 494.083344, 107.66568, 23.0, 23.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 597.0, 9.0, 19.0, 19.0 ],
					"varname" : "showExpiredLicenseWarning"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-29",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 494.083344, 287.0, 63.0, 22.0 ],
					"presentation_rect" : [ 494.083344, 287.0, 63.0, 22.0 ],
					"style" : "",
					"text" : "enable $1"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Courier New",
					"id" : "obj-24",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 524.0, 254.66568, 241.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 8.0, 90.0, 241.0, 20.0 ],
					"style" : "",
					"text" : "Show 3DViewer 'Navigation' Hint"
				}

			}
, 			{
				"box" : 				{
					"clickedimage" : 1,
					"hint" : "",
					"id" : "obj-25",
					"maxclass" : "pictctrl",
					"mode" : 1,
					"name" : "bs.node.logo.default.png",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 494.083344, 251.66568, 23.0, 23.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 256.0, 90.0, 19.0, 19.0 ],
					"varname" : "stageViewNaviHint"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-15",
					"linecount" : 3,
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 2,
					"outlettype" : [ "jit_matrix", "" ],
					"patching_rect" : [ 494.083344, 318.0, 457.0, 49.0 ],
					"presentation_linecount" : 3,
					"presentation_rect" : [ 494.083344, 318.0, 457.0, 49.0 ],
					"style" : "",
					"text" : "jit.gl.text @drawto viewer @color 1 1 1 1 @fontsize 15 @align 1 @layer 10 @position 0 0.7 0 @text navigate: ctrl - mouse -> zoom | alt - mouse -> rotate | shift - mouse -> shift"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-6",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 113.0, 380.0, 121.0, 22.0 ],
					"presentation_rect" : [ 113.0, 380.0, 121.0, 22.0 ],
					"style" : "",
					"text" : "prepend preferences"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-17",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 166.0, 426.0, 150.0, 20.0 ],
					"presentation_rect" : [ 166.0, 426.0, 150.0, 20.0 ],
					"style" : "",
					"text" : "send property to main app"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-18",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 4.083344, 53.0, 131.0, 22.0 ],
					"presentation_rect" : [ 4.083344, 53.0, 131.0, 22.0 ],
					"style" : "",
					"text" : "prepend splashScreen"
				}

			}
, 			{
				"box" : 				{
					"comment" : "",
					"id" : "obj-12",
					"index" : 0,
					"maxclass" : "outlet",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 113.0, 411.0, 30.0, 30.0 ],
					"presentation_rect" : [ 113.0, 411.0, 30.0, 30.0 ],
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-16",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 4.083344, 295.0, 29.5, 22.0 ],
					"presentation_rect" : [ 4.083344, 295.0, 29.5, 22.0 ],
					"style" : "",
					"text" : "set"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-14",
					"maxclass" : "newobj",
					"numinlets" : 3,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "" ],
					"patching_rect" : [ 4.083344, 263.0, 58.0, 22.0 ],
					"presentation_rect" : [ 4.083344, 263.0, 58.0, 22.0 ],
					"style" : "",
					"text" : "route 0 1"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-13",
					"maxclass" : "message",
					"numinlets" : 2,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 38.0, 295.0, 443.0, 22.0 ],
					"presentation_rect" : [ 38.0, 295.0, 443.0, 22.0 ],
					"style" : "",
					"text" : "set Create new Nodes: click inside this workspace-window and press <spacebar>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-3",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 4.083344, 345.0, 220.0, 22.0 ],
					"presentation_rect" : [ 4.083344, 345.0, 220.0, 22.0 ],
					"style" : "",
					"text" : "s bs::app::gui::workspaceSpaceBarHint"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Courier New",
					"id" : "obj-8",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 34.0, 231.66568, 228.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 8.0, 62.0, 228.0, 20.0 ],
					"style" : "",
					"text" : "Show Workspace 'Spacebar' Hint"
				}

			}
, 			{
				"box" : 				{
					"clickedimage" : 1,
					"hint" : "",
					"id" : "obj-10",
					"maxclass" : "pictctrl",
					"mode" : 1,
					"name" : "bs.node.logo.default.png",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 4.083344, 228.66568, 23.0, 23.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 256.0, 62.0, 19.0, 19.0 ],
					"varname" : "workspaceSpaceBarHint"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-1",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"patching_rect" : [ 182.083344, 136.0, 203.0, 22.0 ],
					"presentation_rect" : [ 182.083344, 136.0, 203.0, 22.0 ],
					"style" : "",
					"text" : "r bs::app::gui::folderwarning::disable"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-11",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 4.083344, 202.0, 161.0, 22.0 ],
					"presentation_rect" : [ 4.083344, 202.0, 161.0, 22.0 ],
					"style" : "",
					"text" : "s bs::app::gui::folderwarning"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-9",
					"linecount" : 3,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 243.0, 22.0, 150.0, 47.0 ],
					"presentation_linecount" : 3,
					"presentation_rect" : [ 243.0, 22.0, 150.0, 47.0 ],
					"style" : "",
					"text" : "IMPORTANT: changeing the scripting names must reflect inside Property.java"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-7",
					"maxclass" : "newobj",
					"numinlets" : 1,
					"numoutlets" : 4,
					"outlettype" : [ "", "", "", "" ],
					"patching_rect" : [ 403.0, 43.0, 58.0, 22.0 ],
					"presentation_rect" : [ 403.0, 43.0, 58.0, 22.0 ],
					"restore" : 					{
						"checkForLatestUpdate" : [ 0 ],
						"projectFolderWarning" : [ 0 ],
						"showExpiredLicenseWarning" : [ 0 ],
						"showLicenseAboutToExpireWarning" : [ 0 ],
						"splashWindowOnStartup" : [ 0 ],
						"stageViewNaviHint" : [ 0 ],
						"workspaceSpaceBarHint" : [ 0 ]
					}
,
					"style" : "",
					"text" : "autopattr",
					"varname" : "u070046771"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Courier New",
					"id" : "obj-4",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 34.0, 173.66568, 227.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 8.0, 35.0, 227.0, 20.0 ],
					"style" : "",
					"text" : "Show Project Folder Warning"
				}

			}
, 			{
				"box" : 				{
					"clickedimage" : 1,
					"hint" : "",
					"id" : "obj-5",
					"maxclass" : "pictctrl",
					"mode" : 1,
					"name" : "bs.node.logo.default.png",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 4.083344, 170.66568, 23.0, 23.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 256.0, 35.0, 19.0, 19.0 ],
					"varname" : "projectFolderWarning"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Courier New",
					"id" : "obj-2",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 34.0, 22.66568, 213.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 8.0, 9.0, 213.0, 20.0 ],
					"style" : "",
					"text" : "Show Spash Window on Startup"
				}

			}
, 			{
				"box" : 				{
					"clickedimage" : 1,
					"hint" : "",
					"id" : "obj-20",
					"maxclass" : "pictctrl",
					"mode" : 1,
					"name" : "bs.node.logo.default.png",
					"numinlets" : 1,
					"numoutlets" : 1,
					"outlettype" : [ "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 4.083344, 19.66568, 23.0, 23.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 256.0, 9.0, 19.0, 19.0 ],
					"varname" : "splashWindowOnStartup"
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-5", 0 ],
					"source" : [ "obj-1", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-14", 0 ],
					"source" : [ "obj-10", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-3", 0 ],
					"source" : [ "obj-13", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-13", 0 ],
					"source" : [ "obj-14", 1 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-16", 0 ],
					"source" : [ "obj-14", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-3", 0 ],
					"source" : [ "obj-16", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-40", 0 ],
					"source" : [ "obj-18", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-6", 0 ],
					"source" : [ "obj-19", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-18", 0 ],
					"source" : [ "obj-20", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-19", 0 ],
					"source" : [ "obj-22", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-6", 0 ],
					"source" : [ "obj-23", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-29", 0 ],
					"source" : [ "obj-25", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-23", 0 ],
					"source" : [ "obj-27", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-6", 0 ],
					"source" : [ "obj-28", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-15", 0 ],
					"source" : [ "obj-29", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-28", 0 ],
					"source" : [ "obj-31", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-6", 0 ],
					"source" : [ "obj-32", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-32", 0 ],
					"source" : [ "obj-40", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-11", 0 ],
					"source" : [ "obj-5", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-12", 0 ],
					"source" : [ "obj-6", 0 ]
				}

			}
 ],
		"dependency_cache" : [ 			{
				"name" : "bs.node.logo.default.png",
				"bootpath" : "~/Arbeiten/01_projekte/150816_SPARCK/01_dev/app_sparck/sparck/media",
				"patcherrelativepath" : "../../media",
				"type" : "PNG ",
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
