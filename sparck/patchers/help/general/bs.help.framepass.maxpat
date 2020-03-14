{
	"patcher" : 	{
		"fileversion" : 1,
		"appversion" : 		{
			"major" : 7,
			"minor" : 3,
			"revision" : 4,
			"architecture" : "x86",
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
		"title" : "FramePass - Help",
		"boxes" : [ 			{
				"box" : 				{
					"id" : "obj-2",
					"linecount" : 2,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 216.0, 388.0, 69.0, 33.0 ],
					"presentation" : 1,
					"presentation_linecount" : 2,
					"presentation_rect" : [ 201.0, 283.0, 69.0, 33.0 ],
					"style" : "",
					"text" : "- update\n- execute"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-59",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 222.0, 220.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 297.0, 224.0, 206.0, 20.0 ],
					"style" : "",
					"text" : "SPARCK initilizes this FramePass"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-58",
					"linecount" : 3,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 1080.0, 226.0, 47.0 ],
					"presentation" : 1,
					"presentation_linecount" : 3,
					"presentation_rect" : [ 297.0, 1097.0, 212.0, 47.0 ],
					"style" : "",
					"text" : "Here this FramePass has finished and SPARCK will initilize the start of the next FramePass"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-57",
					"linecount" : 4,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 1005.0, 223.0, 60.0 ],
					"presentation" : 1,
					"presentation_linecount" : 4,
					"presentation_rect" : [ 297.0, 1022.0, 209.0, 60.0 ],
					"style" : "",
					"text" : "In the final step asynchronous texture sources like videos, Syhon- and Spout-Streams are polled for the next frame if available."
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-56",
					"linecount" : 2,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 959.0, 223.0, 33.0 ],
					"presentation" : 1,
					"presentation_linecount" : 2,
					"presentation_rect" : [ 297.0, 976.0, 215.0, 33.0 ],
					"style" : "",
					"text" : "Then the ViewPorts are rendered and sent to their linked Windows"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-55",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 924.0, 223.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 297.0, 941.0, 209.0, 20.0 ],
					"style" : "",
					"text" : "The Editor is updated"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-54",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 889.0, 223.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 297.0, 906.0, 209.0, 20.0 ],
					"style" : "",
					"text" : "The 3DViewer is updated"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-52",
					"linecount" : 12,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 705.0, 227.0, 167.0 ],
					"presentation" : 1,
					"presentation_linecount" : 13,
					"presentation_rect" : [ 297.0, 720.0, 215.0, 181.0 ],
					"style" : "",
					"text" : "For Nodes that render a scene, like SceneCapture and BoxMapCapture, or the asynchronous texture receivers like Video, SyphonReveice or SpoutReceive it is possible to choose the render pass. This is of importance especially if two rendered textures need to be passed on into an Effect-Node, where the sequence of the arrival of each texture has to be in a certain order. \nAt the end the Beamer and Calibrator Nodes are rendered"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-51",
					"linecount" : 10,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 553.0, 226.0, 141.0 ],
					"presentation" : 1,
					"presentation_linecount" : 11,
					"presentation_rect" : [ 297.0, 560.0, 209.0, 154.0 ],
					"style" : "",
					"text" : "The manual render passes are only executed when the user presses the Manual - Renderpass button or when a project has just been loaded and is started for the first time. Manual render passes are ideal for render-processes that only need to be executed once during the runtime of a project, for example generating a mask or a softedge for a multi projector installation."
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-50",
					"linecount" : 10,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 373.0, 231.0, 141.0 ],
					"presentation" : 1,
					"presentation_linecount" : 10,
					"presentation_rect" : [ 297.0, 383.0, 209.0, 141.0 ],
					"style" : "",
					"text" : "The user or a script might have manipulated a transformation node, or an external programm has sent a transformation message. This changes are now applied. After the transformation pass has passed the 'post'-step, all changes from external sources wil be stored and only applied once the next framepass executes the transformation pass."
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-48",
					"linecount" : 6,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 309.0, 266.0, 226.0, 87.0 ],
					"presentation" : 1,
					"presentation_linecount" : 6,
					"presentation_rect" : [ 297.0, 283.0, 211.0, 87.0 ],
					"style" : "",
					"text" : "First it updates the QueScripts. The QueScripts can change all properties of all nodes, so this step has to be done before anything else.\nIn the second step it executes all other script nodes."
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-45",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 1117.5, 98.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 1134.5, 98.0, 20.0 ],
					"style" : "",
					"text" : "next FramePass"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"grad2" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"id" : "obj-46",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 1110.0, 187.0, 35.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 1127.0, 187.0, 35.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-44",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 185.5, 121.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 191.5, 121.0, 20.0 ],
					"style" : "",
					"text" : "previous FramePass"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"grad2" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"id" : "obj-43",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 184.0, 187.0, 35.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 190.0, 187.0, 35.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-42",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 195.5, 959.0, 72.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 195.5, 976.0, 72.0, 20.0 ],
					"style" : "",
					"text" : "- ViewPorts"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-41",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 10.0, 233.0, 97.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 10.0, 250.0, 97.0, 20.0 ],
					"style" : "",
					"text" : "this FramePass"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-40",
					"linecount" : 4,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 195.5, 1005.0, 93.0, 60.0 ],
					"presentation" : 1,
					"presentation_linecount" : 4,
					"presentation_rect" : [ 195.5, 1022.0, 93.0, 60.0 ],
					"style" : "",
					"text" : "loads next \n- Video frame\n- Spout frame\n- Syphon frame"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-38",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 1005.0, 128.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 1022.0, 128.0, 20.0 ],
					"style" : "",
					"text" : "9. Asynchronous poll"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 1.0, 0.47557, 0.003892, 1.0 ],
					"grad2" : [ 1.0, 0.47557, 0.003892, 1.0 ],
					"id" : "obj-39",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 1001.0, 263.0, 70.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 1018.0, 263.0, 70.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-35",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 959.0, 104.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 976.0, 104.0, 20.0 ],
					"style" : "",
					"text" : "8. Window pass"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 1.0, 0.329944, 0.39119, 1.0 ],
					"grad2" : [ 1.0, 0.329944, 0.39119, 1.0 ],
					"id" : "obj-37",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 955.0, 263.0, 37.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 972.0, 263.0, 37.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-31",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 924.0, 104.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 941.0, 104.0, 20.0 ],
					"style" : "",
					"text" : "7. Editor pass"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.998689, 0.295721, 0.781682, 1.0 ],
					"grad2" : [ 0.998689, 0.295721, 0.781682, 1.0 ],
					"id" : "obj-33",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 920.0, 263.0, 28.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 937.0, 263.0, 28.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-27",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 889.0, 110.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 906.0, 110.0, 20.0 ],
					"style" : "",
					"text" : "6. 3DViewer pass"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.870727, 0.272926, 1.0, 1.0 ],
					"grad2" : [ 0.870727, 0.272926, 1.0, 1.0 ],
					"id" : "obj-29",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 885.0, 263.0, 28.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 902.0, 263.0, 28.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-21",
					"linecount" : 12,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 201.0, 705.0, 80.0, 167.0 ],
					"presentation" : 1,
					"presentation_linecount" : 12,
					"presentation_rect" : [ 201.0, 722.0, 80.0, 167.0 ],
					"style" : "",
					"text" : "- pre\n- passA\n- passB\n- passC\n- passD\n- passE\n- passF\n- passG\n- passH\n- passZ\n- beamer\n- post"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-22",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 705.0, 142.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 722.0, 142.0, 20.0 ],
					"style" : "",
					"text" : "5. Render pass"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.605335, 0.286958, 1.0, 1.0 ],
					"grad2" : [ 0.605335, 0.286958, 1.0, 1.0 ],
					"id" : "obj-25",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 701.0, 263.0, 177.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 718.0, 263.0, 177.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-18",
					"linecount" : 4,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 201.0, 553.0, 80.0, 60.0 ],
					"presentation" : 1,
					"presentation_linecount" : 4,
					"presentation_rect" : [ 201.0, 570.0, 80.0, 60.0 ],
					"style" : "",
					"text" : "- manualA\n- manualB\n- manualC\n- manualD"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-19",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 553.0, 155.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 570.0, 155.0, 20.0 ],
					"style" : "",
					"text" : "4. Render pass - Manual"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.35309, 0.344686, 1.0, 1.0 ],
					"grad2" : [ 0.35309, 0.344686, 1.0, 1.0 ],
					"id" : "obj-20",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 547.0, 263.0, 145.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 564.0, 263.0, 145.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-15",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 512.0, 104.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 529.0, 104.0, 20.0 ],
					"style" : "",
					"text" : "3. Physics pass"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.0, 0.433811, 1.0, 1.0 ],
					"grad2" : [ 0.0, 0.433811, 1.0, 1.0 ],
					"id" : "obj-17",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 508.0, 263.0, 28.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 525.0, 263.0, 28.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-10",
					"linecount" : 5,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 201.0, 373.0, 69.0, 74.0 ],
					"presentation" : 1,
					"presentation_linecount" : 5,
					"presentation_rect" : [ 201.0, 390.0, 69.0, 74.0 ],
					"style" : "",
					"text" : "- pre\n- pass1\n- pass2\n- pass3\n- post"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-12",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 373.0, 145.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 390.0, 145.0, 20.0 ],
					"style" : "",
					"text" : "2. Transformation pass"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.0, 0.730643, 1.0, 1.0 ],
					"grad2" : [ 0.0, 0.730643, 1.0, 1.0 ],
					"id" : "obj-13",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 369.0, 263.0, 131.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 386.0, 263.0, 131.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-8",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 37.0, 266.0, 109.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 37.0, 283.0, 55.0, 20.0 ],
					"style" : "",
					"text" : "1. Script"
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.0, 0.969586, 1.0, 1.0 ],
					"grad2" : [ 0.0, 0.969586, 1.0, 1.0 ],
					"id" : "obj-4",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 31.0, 263.0, 263.0, 98.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 31.0, 280.0, 263.0, 98.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"angle" : 270.0,
					"grad1" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"grad2" : [ 0.439216, 0.74902, 0.254902, 1.0 ],
					"id" : "obj-3",
					"maxclass" : "panel",
					"mode" : 1,
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 228.0, 187.0, 851.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 5.0, 245.0, 187.0, 851.0 ],
					"proportion" : 0.39,
					"style" : ""
				}

			}
, 			{
				"box" : 				{
					"fontface" : 1,
					"fontname" : "Verdana",
					"id" : "obj-1",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.5, 48.0, 518.0, 21.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 1.5, 30.0, 518.0, 21.0 ],
					"style" : "",
					"text" : "___________________________________________________________"
				}

			}
, 			{
				"box" : 				{
					"args" : [ "FramePass" ],
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
					"patching_rect" : [ 5.0, 1327.0, 522.0, 21.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 8.5, 1371.0, 522.0, 21.0 ],
					"style" : "",
					"text" : "*__________________________________________________________*",
					"varname" : "lastLine"
				}

			}
, 			{
				"box" : 				{
					"fontface" : 1,
					"fontname" : "Verdana",
					"id" : "obj-16",
					"linecount" : 3,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 5.0, 1152.0, 518.0, 50.0 ],
					"presentation" : 1,
					"presentation_linecount" : 3,
					"presentation_rect" : [ 5.0, 1200.0, 518.0, 50.0 ],
					"style" : "",
					"text" : "___________________________________________________________\n\nFeedback:"
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Verdana",
					"id" : "obj-11",
					"linecount" : 3,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 16.0, 1203.0, 492.0, 50.0 ],
					"presentation" : 1,
					"presentation_linecount" : 3,
					"presentation_rect" : [ 21.0, 1258.0, 485.0, 50.0 ],
					"style" : "",
					"text" : "Documentations take a lot of time and effort. Unfortunately both are scarce ressources. If you would like to have more info please drop a note. It helps to set priorities according to the user needs..."
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
					"patching_rect" : [ 66.5, 1289.0, 231.0, 31.0 ],
					"style" : "",
					"text" : ";\rmax launchbrowser http://tecartlab.com/feedback/"
				}

			}
, 			{
				"box" : 				{
					"align" : 0,
					"bgcolor" : [ 0.011765, 0.396078, 0.752941, 1.0 ],
					"hint" : "opens link inside webbrowser",
					"id" : "obj-7",
					"maxclass" : "textbutton",
					"numinlets" : 1,
					"numoutlets" : 3,
					"outlettype" : [ "", "", "int" ],
					"parameter_enable" : 0,
					"patching_rect" : [ 66.5, 1263.0, 261.0, 20.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 148.0, 1324.0, 168.0, 23.0 ],
					"rounded" : 10.0,
					"style" : "",
					"text" : "http://tecartlab.com/feedback/",
					"textcolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textjustification" : 0,
					"textoncolor" : [ 1.0, 1.0, 1.0, 1.0 ],
					"textovercolor" : [ 1.0, 1.0, 1.0, 1.0 ]
				}

			}
, 			{
				"box" : 				{
					"fontname" : "Verdana",
					"id" : "obj-23",
					"linecount" : 8,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 24.0, 147.0, 507.0, 123.0 ],
					"presentation" : 1,
					"presentation_linecount" : 8,
					"presentation_rect" : [ 21.0, 61.0, 487.0, 123.0 ],
					"style" : "",
					"text" : "The FramePass is another core principle inside SPARCK. It describes the process from the moment SPARCK initiates the creation of a new frame until it finally sends it to the chosen output device, be it a screen, projector, VR-glasses or LED-strips.\n\nWhile it is not essential to understand it in order to use SPARCK, it is certainly usefull if you want use the render-pass and transformation-pass properties to its full potential."
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"destination" : [ "obj-36", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-28", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-36", 1 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-30", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-24", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-36", 0 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-34", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-36", 1 ]
				}

			}
, 			{
				"patchline" : 				{
					"destination" : [ "obj-5", 0 ],
					"disabled" : 0,
					"hidden" : 0,
					"source" : [ "obj-7", 0 ]
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
