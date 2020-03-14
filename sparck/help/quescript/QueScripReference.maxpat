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
		"rect" : [ 397.0, 223.0, 917.0, 575.0 ],
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
					"id" : "obj-51",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 87.0, 315.0, 191.0, 20.0 ],
					"style" : "",
					"text" : "send osc messages"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-52",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 315.0, 51.0, 20.0 ],
					"style" : "",
					"text" : "<osc>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-50",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 83.0, 356.0, 639.0, 208.0 ],
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
									"id" : "obj-7",
									"linecount" : 9,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 211.0, 59.0, 411.0, 127.0 ],
									"style" : "",
									"text" : "the following domains are available:\n\nlocal\nque\nglobal\n\nif you want to see all domains, you can write:\n\nvardomain=\"local, que, global\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-4",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 47.0, 59.0, 145.0, 20.0 ],
									"style" : "",
									"text" : "vardomain=\"domains\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 21.0, 23.0, 478.0, 33.0 ],
									"style" : "",
									"text" : "<debugger> only prints to the console if the debug is switched on inside the properties. \ntakes following attributes:"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 525.0, 71.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p debugger"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-48",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 87.0, 525.0, 284.0, 20.0 ],
					"style" : "",
					"text" : "prints out debugging informations"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-49",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 525.0, 75.0, 20.0 ],
					"style" : "",
					"text" : "<debugger>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-45",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 59.0, 104.0, 705.0, 600.0 ],
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
									"id" : "obj-6",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 51.0, 325.0, 119.0, 20.0 ],
									"style" : "",
									"text" : "<expr>, <debugger>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-10",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 51.0, 301.0, 276.0, 20.0 ],
									"style" : "",
									"text" : "<play>, <pause>, <resume>, <shutdown>, <stop>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-16",
									"linecount" : 4,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 19.0, 528.0, 648.0, 60.0 ],
									"style" : "",
									"text" : "Notice: the start-attributes expression {??whilevar = 0} sets the variable 'whilevar' to 0. the questionmarks at the beginning of the variable name indicate that if the variable doesnt exist yet, the script will create the 'whilevar' variable in the local variable domain. the <while> node (like the <anim> node) have a separate local variable domain to the <que>. this way you can create variables that dont interfere with each other."
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-14",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 19.0, 367.0, 61.0, 20.0 ],
									"style" : "",
									"text" : "Example:"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-11",
									"linecount" : 9,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 19.0, 394.0, 648.0, 127.0 ],
									"style" : "",
									"text" : "<while name=\"while\" repeat=\"{whilevar lt 100}\" start=\"{??whilevar = 0}\" step=\"{whilevar = (whilevar + 1)}\">\n  <if true=\"{whilevar lt 50}\">\n    <print>inside while below 50: {whilevar}</print>\n     <else>\n      <print>inside while above 50: {whilevar}</print>\n    </else>\n  </if>\n</while>\n<wait anim=\"while\"/>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-8",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 36.0, 183.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "name=\"while_name\""
								}

							}
, 							{
								"box" : 								{
									"hidden" : 1,
									"id" : "obj-9",
									"linecount" : 3,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 216.0, 183.0, 436.0, 47.0 ],
									"style" : "",
									"text" : "if name is set, <while> will sends an anim-message once the loop has exited (repeat == false). this message can be caught by a <wait anim=\"while_name\"/> object"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-4",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 36.0, 146.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "step=\"{expr}\""
								}

							}
, 							{
								"box" : 								{
									"hidden" : 1,
									"id" : "obj-7",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 216.0, 146.0, 375.0, 33.0 ],
									"style" : "",
									"text" : "this {expr} is executed after a loop is over (all the nodes children are executed)"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-3",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 36.0, 120.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "start=\"{expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-1",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 216.0, 81.0, 381.0, 33.0 ],
									"style" : "",
									"text" : "if {expr} is true, it executes the nodes that are inside the <while> node. this is the only required attribute."
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-28",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 51.0, 279.0, 236.0, 20.0 ],
									"style" : "",
									"text" : "<send>, <print>, <trigger>, <out>, <osc>   "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-24",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 51.0, 257.0, 34.0, 20.0 ],
									"style" : "",
									"text" : "<if> "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-21",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 24.0, 235.0, 302.0, 20.0 ],
									"style" : "",
									"text" : " following nodes can be nested inside <while>:"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-17",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 24.0, 54.0, 202.0, 20.0 ],
									"style" : "",
									"text" : "it takes the following attributes:"
								}

							}
, 							{
								"box" : 								{
									"hidden" : 1,
									"id" : "obj-12",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 216.0, 120.0, 364.0, 20.0 ],
									"style" : "",
									"text" : "this {expr} is executed before the <while> node starts with its loop"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-5",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 36.0, 81.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "repeat=\"{expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 18.0, 14.0, 394.0, 33.0 ],
									"style" : "",
									"text" : "<while> defines an animation that functions like a loop. you can compare its functionality to a classical 'while' or a 'for'"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 242.0, 48.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p while"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-46",
					"linecount" : 3,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 84.0, 242.0, 274.0, 47.0 ],
					"style" : "",
					"text" : "starts an animation that runs as long as the repeat-condition is true or it receives a <fade> message"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-47",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 242.0, 52.0, 20.0 ],
					"style" : "",
					"text" : "<while>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-44",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
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
						"boxes" : [ 							{
								"box" : 								{
									"id" : "obj-10",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 62.0, 222.0, 276.0, 20.0 ],
									"style" : "",
									"text" : "<play>, <pause>, <resume>, <shutdown>, <stop>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-7",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 62.0, 197.0, 236.0, 20.0 ],
									"style" : "",
									"text" : "<send>, <print>, <trigger>, <out>, <osc>   "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-6",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 62.0, 274.0, 119.0, 20.0 ],
									"style" : "",
									"text" : "<expr>, <debugger>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-4",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 62.0, 246.0, 99.0, 20.0 ],
									"style" : "",
									"text" : "<anim>, <while>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-3",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 36.0, 108.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "false=\"{expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-1",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 216.0, 81.0, 359.0, 20.0 ],
									"style" : "",
									"text" : "if {expr} is true, it executes the nodes that are inside the <if> node"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-27",
									"linecount" : 3,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 62.0, 300.0, 108.0, 47.0 ],
									"style" : "",
									"text" : "<else>\n   .....\n</else>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-24",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 62.0, 175.0, 34.0, 20.0 ],
									"style" : "",
									"text" : "<if> "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-21",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 24.0, 146.0, 302.0, 20.0 ],
									"style" : "",
									"text" : " following nodes can be nested inside <if>:"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-17",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 24.0, 54.0, 181.0, 20.0 ],
									"style" : "",
									"text" : "it only takes one attribute, either"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-14",
									"linecount" : 7,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 216.0, 302.0, 348.0, 100.0 ],
									"style" : "",
									"text" : "<else> is executed if the above condition is not to the liking of the attribute. <else> can nest the same nodes as <if> except <else>.\n\nThere is one limitation in this game: it is not possible to nest  <anim> inside <if> inside <anim> ad infinitum. the script will give an error if this is tried."
								}

							}
, 							{
								"box" : 								{
									"hidden" : 1,
									"id" : "obj-12",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 216.0, 108.0, 364.0, 20.0 ],
									"style" : "",
									"text" : "if {expr} is false, it executes the nodes that are inside the <if> node"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-5",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 36.0, 81.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "true=\"{expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 18.0, 14.0, 217.0, 20.0 ],
									"style" : "",
									"text" : "<if> works as one would expect"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 485.0, 27.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p if"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-42",
					"linecount" : 2,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 87.0, 485.0, 274.0, 33.0 ],
					"style" : "",
					"text" : "if an {expr} is true (or false) some things will be done, else..."
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-43",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 15.0, 485.0, 52.0, 20.0 ],
					"style" : "",
					"text" : "<if>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-41",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 671.0, 379.0, 412.0, 237.0 ],
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
									"id" : "obj-5",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 908.0, 378.0, 435.0, 300.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-11",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 143.0, 253.0, 222.0, 20.0 ],
													"style" : "",
													"text" : "milliseconds since the timer was started"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-10",
													"linecount" : 4,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 143.0, 139.0, 100.0, 60.0 ],
													"style" : "",
													"text" : "current hours\ncurrent minutes\ncurrent seconds\ncurrent millis"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-9",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 59.0, 253.0, 67.0, 20.0 ],
													"style" : "",
													"text" : "$TIMER"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-8",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 25.0, 222.0, 247.0, 20.0 ],
													"style" : "",
													"text" : "<timer> node also provides a variable:"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-7",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 25.0, 114.0, 247.0, 20.0 ],
													"style" : "",
													"text" : "it generates frame dependent variables:"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-5",
													"linecount" : 4,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 59.0, 139.0, 67.0, 60.0 ],
													"style" : "",
													"text" : "$HOUR\n$MIN\n$SEC\n$MILLI"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-4",
													"linecount" : 3,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 59.0, 45.0, 67.0, 47.0 ],
													"style" : "",
													"text" : "PI\nTRUE\nFALSE"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-2",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 25.0, 17.0, 237.0, 20.0 ],
													"style" : "",
													"text" : "QueScript provides some Variables to use:"
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 11.0, 187.0, 199.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p QueScript/{expr}/systemVariables"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-4",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 198.0, 323.0, 1005.0, 373.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-3",
													"linecount" : 23,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 500.0, 47.0, 485.0, 315.0 ],
													"style" : "",
													"text" : "Variables:\n\nIt is not necessary to create variables befor using, although it is recommended. If the variable doesnt exist, a new one is created and set to value 0.\n\n{var1} = 0\n\nyou can assign values to a variable:\n\n{var1 = 5} = 5\n\nif the expression is an assignment to a variable, it also returns the value of the variable.\n\nif you want to set a variable only if it wasnt set before than you can do this:\n\n{var1 = if(var1 == 0, 34, var1)} = 34 (if var1 didnt exist before)\n\nthere are multiple domains for variables. with the '?' and the '??' prefix you can tell in which domain you want to create the variable if it doesnt exist:\n\n{var1 = 4} will create var1 inside the global domain\n{?var1 = 4} will create var1 inside the que domain\n{??var1 = 4} will create var1 inside the local domain "
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-2",
													"linecount" : 23,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 19.0, 7.0, 477.0, 315.0 ],
													"style" : "",
													"text" : "{expressions} always need to be inside a {} bracket. They can use variables, strings, operands and functions.\n\nStrings can be added to each other like numbers, and if the result is a string, a string is returned.\n\nfor example if index has the value of 5:\n\n{'text[' + (index * 3) + ']'} = 'text[15]'\n\nbut in this case (if value is 'text'):\n\n{if('text' == value, 1, 0)} = 1\n\nfurther examples:\n\n{sin(rad(angle))}\n \n{if(value == 10, value / 2, value ^ 2)}\n\nif var1=1, var2=10, var3=9 then\n\n{max(var1, var2, var3, 7)} = 10\n"
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 11.0, 94.0, 193.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p QueScript/{expr}/expr_examples"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-3",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 504.0, 168.0, 534.0, 326.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-3",
													"linecount" : 21,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 311.0, 15.0, 201.0, 288.0 ],
													"style" : "",
													"text" : "returns result\nreturns 0..1\nsine of angle\ncosine of angle\ntangens of angle\nArcSine of value\nArcCosin of value\nArcTangens of value\nHyperbolic Sine\nHyperbolic Cosine\nHyperbolic Tangesn\nreturns radian\nreturns degrees\nnatural logarithm (base e)\nbase 10 logarithm\nreturns to the closest integer\nreturns to the closest integer <= val\nreturns to the closest integer >= val\nsquare root\nreturns the biggest value\nreturns the smallest value"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-2",
													"linecount" : 21,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 15.0, 15.0, 292.0, 288.0 ],
													"style" : "",
													"text" : "IF(<condition>, <execute if true>, <execute if false>)\nRANDOM()\nSIN(rad)\nCOS(rad)\nTAN(rad)\nASIN(val)\nACOS(val)\nATAN(val)\nSINH(val)\nCOSH(val)\nTANH(val)\nRAD(deg)\nDEG(rad)\nLOG(val)\nLOG10(val)\nROUND(val)\nFLOOR(val)\nCEILING(val)\nSQRT(val)\nMAX(val, val, val, ...)\nMIN(val, val, val, ...)",
													"textjustification" : 2
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 231.0, 153.0, 164.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p QueScript/{expr}/Functions"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-1",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 716.0, 374.0, 223.0, 256.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-3",
													"linecount" : 17,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 58.0, 15.0, 154.0, 234.0 ],
													"style" : "",
													"text" : "power of\nmodulo\nmultiply\ndivision\naddition\nsubstraction\nequal\nnot equal\n(>) greater than\n(<) smaller than\n(>=) greater equal than\n(<=) smaller equal than\nlogical and\nlogical or\n\nassign value to a variable:\nvar = value"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-2",
													"linecount" : 16,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 15.0, 15.0, 48.0, 234.0 ],
													"style" : "",
													"text" : "^\n%\n*\n/\n+ \n- \n==\n!=\ngt \nst\nge\nse\n&&\n||\n\n=\n",
													"textjustification" : 2
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 231.0, 125.0, 165.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p QueScript/{expr}/Operators"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-9",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 212.0, 142.0, 1083.0, 576.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-4",
													"linecount" : 3,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 571.0, 488.0, 469.0, 47.0 ],
													"style" : "",
													"text" : "Note: You can use the <debugger> node to see the current content of all the domains. Important: if you place the <debugger> inside an animation node like <anim> or <while> you will see different local variables than if you place it outside of it."
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-3",
													"linecount" : 31,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 561.0, 34.0, 506.0, 422.0 ],
													"style" : "",
													"text" : "the use of the domain assignmen prefix (?) also works when you dont explicitly set variables:\n\n{sin(pow(??variable, 2))}\n\nif the variable doesnt exist in any domain yet, it makes sure the variable is created inside the local domain.\n\nVariables generated by <anim> nodes belong to either the que-domain or the local-domain. Assuming an example like the following:\n\n<anim name=\"a1\">\n   <track name=\"t1\">0 1.</track>\n   <track name=\"t2\">0 1.</track>\n   <send>adress command {t1} {t2}</track>\n   <expr>{g1.a1.t1 = t1}</expr>\n</anim>\n<send>adress command {a1.t1} {a1.t2}</track>\n<expr>{g2.a1.t2 = a1.t2}</expr>\n\n<anim> generates for each track two variables:\n\n-> t1 and t2 for the local domain. they can only be accessed by the <send> node inside the <anim> node\n-> a1.t1 and a1.t2 for the que domain. they can be accessed by the <send> node outside the <anim> node\n\nin order to make the values accessible globaly, you can use the <expr> node. Please note though: if you want to make sure that all the current itterated values inside the <anim> node are passed on to a global variable, the <expr> node needs to be inside the <anim> node, because the <expr>{g2.a1.t2 = a1.t2}</expr> will only be called once with the current value (which would be 0 in this case) and not change there after."
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-2",
													"linecount" : 40,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 17.0, 18.0, 543.0, 543.0 ],
													"style" : "",
													"text" : "Inside QueScript there are three domains in which variables exists:\n\n- the global domain\n- the que domain\n- the local domain\n\nIf Variables of the same name exist in multiple domains, the expression will take the first one it finds with the following preference: local -> que -> global.\n\nThe variables inside the global domain are accessible by all {expressions} inside all ques. All Variables generated by the user through a message-list like 'var <name> <value>' belong to the global domain. \n\nThe variables inside the que domain can only be accessed by {expressions} inside the ques.\nThe variables inside the local domain can only be accessed by {expressions} inside certain nodes.\n\nIt is not necessary to create variables before using it, although it is recommended. If the variable doesnt exist, a new one is created and set to value 0.\n\n{var1} = 0\n\nyou can assign values to a variable:\n\n{var1 = 5} = 5\n\nif the expression is an assignment to a variable, it also returns the value of the variable.\n\nif you want to set a variable only if it wasnt set before than you can do this:\n\n{var1 = if(var1 == 0, 34, var1)} = 34 (if var1 was already set with the value of 5 then the result = 5)\n\nsince there are multiple domains for variables. it is possible to assign a variable through the prefix '?' and '??' prefix in which domain you want to create the variable if it doesnt exist:\n\nif no variable of a certain name exists inside any domain, then.\n\n<expr>{var = 1}</expr> creates the variable called 'var' with the value 1 inside the global domain. \n<expr>{?var = 1}</expr> creates the variable called 'var' with the value 1 inside the que domain\n<expr>{??var = 1}</expr> creates the variable called 'var' with the value 1 inside the local domain\n\n"
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 231.0, 94.0, 160.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p QueScript/{expr}/variables"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-8",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 191.0, 225.0, 902.0, 606.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-22",
													"linecount" : 6,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 368.0, 490.0, 422.0, 87.0 ],
													"style" : "",
													"text" : "<wait until=\"{anim1_track1 > (var_1 / 2)}\"/>\n\nthe <wait> node waits until the value of the variable anim1_track1 is greater than half of val_1. If this <wait> node was right after the <anim> node, it would wait for 5s before the script carries on, because it takes the <anim> node 10 seconds to reach var_1 for the variable anim1_track1. "
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-21",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 490.0, 207.0, 20.0 ],
													"style" : "",
													"text" : "<wait until=\"{expr}\"/>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-20",
													"linecount" : 4,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 368.0, 380.0, 374.0, 60.0 ],
													"style" : "",
													"text" : "<send>/sendadress command {track1} {anim1_track1}</send>\n\nthis <send> node references the created values and creates a send message."
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-19",
													"linecount" : 10,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 368.0, 209.0, 392.0, 141.0 ],
													"style" : "",
													"text" : "<track name=\"track1\" fadeto=\"{var_10}\">0. {var_1}</track>\n\n<track> in combination with <anim> creates two variables that changes its value over time, from the first set value (0) to the last set value ({var_1}) in 10 seconds.\n\nthe name of the first variable is track1 and can only be used inside the <anim> node.\nthe name of the second variable is anim1_track1 and can be used inside the <que> node. (as soon as the <anim> node was executed)"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-17",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 439.0, 54.0, 20.0 ],
													"style" : "",
													"text" : "</anim>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-15",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 47.0, 380.0, 285.0, 20.0 ],
													"style" : "",
													"text" : "<send>/sendadress command {expr} {expr}</send>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-13",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 47.0, 209.0, 304.0, 20.0 ],
													"style" : "",
													"text" : "<track name=\"track1\" fadeto=\"{expr}\">0. {expr}</track>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-10",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 187.0, 368.0, 20.0 ],
													"style" : "",
													"text" : "<anim name=\"anim1\" duration=\"10s | {expr}\" fadeout=\"2s | {expr}\">"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-7",
													"linecount" : 5,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 368.0, 52.0, 334.0, 74.0 ],
													"style" : "",
													"text" : "<expr>{global = if(global==0, 45, global)}</expr>\n\nAllows to execute expressions. Since it is also possible to assign variables inside expressions, more complex algorithmic constructions are possible."
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-5",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 52.0, 202.0, 20.0 ],
													"style" : "",
													"text" : "<expr>{expr}</expr>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-2",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 14.0, 11.0, 453.0, 20.0 ],
													"style" : "",
													"text" : "expressions almost be used everywhere, here a short (and probaly incomplete) list:"
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 11.0, 125.0, 189.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p QueScript/{expr}/use_examples"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-7",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 303.0, 258.0, 475.0, 375.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-14",
													"linecount" : 6,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 231.0, 276.0, 128.0, 87.0 ],
													"style" : "",
													"text" : "for messages any combination of {expr} and strings are allowed, as long as they are speparated by a space"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-11",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 343.0, 176.0, 20.0 ],
													"style" : "",
													"text" : "<trigger>{expr} {expr}</trigger>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-9",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 319.0, 107.0, 20.0 ],
													"style" : "",
													"text" : "<out>{expr}</out>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-8",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 295.0, 194.0, 20.0 ],
													"style" : "",
													"text" : "<send>/sendadress {expr}</send>"
												}

											}
, 											{
												"box" : 												{
													"hidden" : 1,
													"id" : "obj-7",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 271.0, 120.0, 20.0 ],
													"style" : "",
													"text" : "<print>{expr}</print>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-3",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 46.0, 400.0, 20.0 ],
													"style" : "",
													"text" : "this is a complete list where inside the quescript expressions can be used"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-1",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 247.0, 127.0, 20.0 ],
													"style" : "",
													"text" : "<wait while=\"{expr}\"/>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-21",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 221.0, 121.0, 20.0 ],
													"style" : "",
													"text" : "<wait until=\"{expr}\"/>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-17",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 196.0, 54.0, 20.0 ],
													"style" : "",
													"text" : "</anim>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-15",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 47.0, 172.0, 285.0, 20.0 ],
													"style" : "",
													"text" : "<send>/sendadress command {expr} {expr}</send>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-13",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 47.0, 148.0, 304.0, 20.0 ],
													"style" : "",
													"text" : "<track name=\"track1\" fadeto=\"{expr}\">0. {expr}</track>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-10",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 126.0, 214.0, 20.0 ],
													"style" : "",
													"text" : "<anim name=\"anim1\" duration=\"10s\">"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-5",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 16.0, 96.0, 119.0, 20.0 ],
													"style" : "",
													"text" : "<expt>{expr}</expr>"
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 11.0, 156.0, 145.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p QueScript/{expr}/usage"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"linecount" : 5,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 11.0, 11.0, 380.0, 74.0 ],
									"style" : "",
									"text" : "{expr} is short for the expressions that can be used inisde quescript. expressions are in realtime evaluated operators and functions that can use variables that are set with nodes like <var> or <anim> and <track> and some Attributes.\n\n"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 89.0, 44.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p expr"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-40",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 84.0, 89.0, 277.0, 20.0 ],
					"style" : "",
					"text" : "evaluates an {expression} string"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-39",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 89.0, 53.0, 20.0 ],
					"style" : "",
					"text" : "<expr>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-37",
					"linecount" : 3,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 87.0, 378.0, 284.0, 47.0 ],
					"style" : "",
					"text" : "sends messages to the second outlet AND sends a trigger message to all running ques (including the one calling)"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-38",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 378.0, 59.0, 20.0 ],
					"style" : "",
					"text" : "<trigger>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-35",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 87.0, 357.0, 197.0, 20.0 ],
					"style" : "",
					"text" : "outputs messages to the first outlet"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-36",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 357.0, 51.0, 20.0 ],
					"style" : "",
					"text" : "<out>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-4",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 87.0, 336.0, 191.0, 20.0 ],
					"style" : "",
					"text" : "prints messages to the console"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-34",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 336.0, 51.0, 20.0 ],
					"style" : "",
					"text" : "<print>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-24",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 553.0, 490.0, 216.0, 20.0 ],
					"style" : "",
					"text" : "resume other que's"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-33",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 471.0, 490.0, 65.0, 20.0 ],
					"style" : "",
					"text" : "<resume>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-14",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 553.0, 466.0, 191.0, 20.0 ],
					"style" : "",
					"text" : "pause other que's"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-22",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 471.0, 466.0, 57.0, 20.0 ],
					"style" : "",
					"text" : "<pause>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-13",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 503.0, 168.0, 579.0, 275.0 ],
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
									"id" : "obj-11",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 215.0, 112.0, 330.0, 33.0 ],
									"style" : "",
									"text" : "resume all the already executed que's that were paused\nresume the already executed specified que that was paused"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-12",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 31.0, 112.0, 178.0, 33.0 ],
									"style" : "",
									"text" : "<resume/>\n<resume name=\"quename\"/>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-9",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 215.0, 65.0, 276.0, 33.0 ],
									"style" : "",
									"text" : "pause all que's execept the on that is calling it\npause the specified que"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-10",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 31.0, 65.0, 178.0, 33.0 ],
									"style" : "",
									"text" : "<pause/>\n<pause name=\"quename\"/>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-8",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 215.0, 29.0, 150.0, 20.0 ],
									"style" : "",
									"text" : "play the specified que"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-6",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 215.0, 161.0, 276.0, 33.0 ],
									"style" : "",
									"text" : "shutdown all que's execept the on that is calling it\nshutdown the specified que"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-5",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 215.0, 202.0, 255.0, 33.0 ],
									"style" : "",
									"text" : "stops all que's execept the on that is calling it\nstops the specified que"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 31.0, 29.0, 169.0, 20.0 ],
									"style" : "",
									"text" : "<play name=\"quename\"/>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-1",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 31.0, 161.0, 178.0, 33.0 ],
									"style" : "",
									"text" : "<shutdown/>\n<shutdown name=\"quename\"/>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-3",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 31.0, 202.0, 156.0, 33.0 ],
									"style" : "",
									"text" : "<stop/>  \n<stop name=\"quename\"/>"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 777.0, 490.0, 111.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p play_pause_stop"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-9",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 553.0, 439.0, 191.0, 20.0 ],
					"style" : "",
					"text" : "play other que's"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-12",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 471.0, 439.0, 76.0, 20.0 ],
					"style" : "",
					"text" : "<play>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-6",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 553.0, 516.0, 191.0, 20.0 ],
					"style" : "",
					"text" : "shutdown other que's"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-7",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 471.0, 516.0, 76.0, 20.0 ],
					"style" : "",
					"text" : "<shutdown>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-1",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 553.0, 545.0, 191.0, 20.0 ],
					"style" : "",
					"text" : "stops other que's"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-3",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 471.0, 545.0, 47.0, 20.0 ],
					"style" : "",
					"text" : "<stop>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-32",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 594.0, 219.0, 598.0, 243.0 ],
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
									"id" : "obj-3",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 43.0, 208.0, 365.0, 20.0 ],
									"style" : "",
									"text" : "<osc>/path/to/somewhere aCustomComand {expr} {expr}</osc>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-1",
									"linecount" : 3,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 16.0, 155.0, 572.0, 47.0 ],
									"style" : "",
									"text" : "the <osc> message sends osc messages to the specified OSC Sender inside Properties. Where the first token (the first string until the first space) will be used as an osc address-pattern '/path/to/somewhere' (see below)"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-9",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 16.0, 19.0, 418.0, 20.0 ],
									"style" : "",
									"text" : " All messages allow the use of  the use of {expressions} (see more in {expr}) "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-8",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 43.0, 114.0, 514.0, 33.0 ],
									"style" : "",
									"text" : "IMPORTANT: the first string (/path/to/somewhere) will always be used as a MAXMsp receiving address where the rest of the string has to be sent. "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-4",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 43.0, 87.0, 365.0, 20.0 ],
									"style" : "",
									"text" : "<send>/path/to/somewhere aCustomComand {expr} {expr}</send>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 16.0, 52.0, 533.0, 33.0 ],
									"style" : "",
									"text" : "the <send> message sends messages out, where the first token (the first string until the first space) will be used as a send address for MaxMSP. i.e. '/path/to/somewhere' (see below)"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 334.0, 75.0, 22.0 ],
					"presentation" : 1,
					"presentation_rect" : [ 30.0, 30.0, 100.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p messages"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-31",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 83.0, 356.0, 639.0, 164.0 ],
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
									"id" : "obj-9",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 211.0, 102.0, 283.0, 20.0 ],
									"style" : "",
									"text" : "overrides the fadeout time of the <anim> command"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-7",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 211.0, 59.0, 411.0, 33.0 ],
									"style" : "",
									"text" : "the <anim> it tries to trigger to fadeout. if NO name is specified, all <ramp> commands will be affected!!"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-5",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 77.0, 102.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "fadeout=\"2s\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-4",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 77.0, 59.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "name=\"anim_name\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 21.0, 23.0, 158.0, 20.0 ],
									"style" : "",
									"text" : "<fade> takes two attributes:"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 436.0, 44.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p fade"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-29",
					"linecount" : 2,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 87.0, 437.0, 286.0, 33.0 ],
					"style" : "",
					"text" : "creates a fade message that causes animation commands to fadeout / stop"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-30",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 15.0, 437.0, 52.0, 20.0 ],
					"style" : "",
					"text" : "<fade>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-27",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 87.0, 294.0, 191.0, 20.0 ],
					"style" : "",
					"text" : "send messages to receive objects"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-28",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 294.0, 51.0, 20.0 ],
					"style" : "",
					"text" : "<send>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-26",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 85.0, 130.0, 640.0, 480.0 ],
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
									"id" : "obj-4",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 66.0, 357.0, 120.0, 33.0 ],
									"style" : "",
									"text" : "<if true=\"{expr}\">\n</if>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-3",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 66.0, 332.0, 191.0, 20.0 ],
									"style" : "",
									"text" : "<expr>{expr}</expr>, <debugger>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-1",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 218.0, 82.0, 258.0, 20.0 ],
									"style" : "",
									"text" : "only a-z, A_Z, 0-9, . and _ (underscore)"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-34",
									"linecount" : 3,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 31.5, 416.0, 492.0, 47.0 ],
									"style" : "",
									"text" : "if <anim> has no loop and has reached its target, it will send an anim-message with its name (\"anim_name\"), which can be picked up by a wait message. this way it is possible to let the quescript wait until an anim has finished until it continues."
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-30",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 78.0, 329.0, 640.0, 480.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-10",
													"linecount" : 19,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 67.0, 94.0, 569.0, 261.0 ],
													"style" : "",
													"text" : "if the <track> commands have three values (n) specified, \nthe <keys> command needs 1 (n-2) value, because the first and the last values are set:\n\nif the attribute timing=\"rel\", the values are set as normalized times (between 0. and 1.). if the <track> has three values, its key times are:\n\nvalue = 0. -> time = 0. (beginning of the ramp)\nvalue = 12 -> time = set by <keys>\nvalue = 10.5 -> time = 1. (end of the ramp)\n\nso the key command would look like : \n\n<keys timing=\"rel\">0.2</keys>\n\nif the attribute timing=\"abs\", the values are set as absolute times (i.e. 2s). \n\n<keys timing=\"abs\">2s</keys>\n\nif you put an absolute time where quescript expects a relative time, you will get a errormessages."
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-4",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 58.0, 54.0, 405.0, 20.0 ],
													"style" : "",
													"text" : "the attribute timing=\"rel|abs\" sets how the numbers have to be interpreted:"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-2",
													"linecount" : 2,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 39.0, 19.0, 530.0, 33.0 ],
													"style" : "",
													"text" : "the <keys> command allows to define the distribution of the key values inside the <track> commands"
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 261.5, 276.0, 45.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p keys"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-29",
									"maxclass" : "newobj",
									"numinlets" : 0,
									"numoutlets" : 0,
									"patcher" : 									{
										"fileversion" : 1,
										"appversion" : 										{
											"major" : 7,
											"minor" : 1,
											"revision" : 0,
											"architecture" : "x86",
											"modernui" : 1
										}
,
										"rect" : [ 261.0, 330.0, 743.0, 480.0 ],
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
										"boxes" : [ 											{
												"box" : 												{
													"id" : "obj-7",
													"linecount" : 2,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 469.0, 18.0, 244.0, 33.0 ],
													"style" : "",
													"text" : "see more about expression variables under {expr}"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-1",
													"linecount" : 4,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 111.0, 86.0, 413.0, 60.0 ],
													"style" : "",
													"text" : "the name sets the variable name for expressions. the name 't1' for example translates into two variables:\n$t1 for use inside the same <anim> node\n$anim_node_t1 for use inide the same <que>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-3",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 32.0, 86.0, 69.0, 20.0 ],
													"style" : "",
													"text" : "name=\"t1\""
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-16",
													"linecount" : 11,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 379.0, 286.0, 320.0, 154.0 ],
													"style" : "",
													"text" : "normalized time it is a factor of the <anim> duration\n\nif no <keys> object exists, <anim> will distribute the three key values evenly inside the duration. if the duration is 8s:\n\nvalue = 0. -> normalized time = 0.  -> at 0s\nvalue = {expr} -> normalized time = 0.5 -> at 4s\nvalue = 10.5 -> normalized time = 1.0 -> at 8s\n\nhowever, this distribution can be manually defined by the <keys> object"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-14",
													"linecount" : 9,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 111.0, 286.0, 256.0, 127.0 ],
													"style" : "",
													"text" : "this example defines three key values: \n\n0.\n{expr} evaluated in realtime\n10.5\n\nthe <anim> will create a linear interpolation between those values according to its normalized time"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-12",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 96.0, 253.0, 309.0, 20.0 ],
													"style" : "",
													"text" : "<track name=\"track1\" fadeto=\"0.\">0. {expr} 10.5</track>"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-10",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 32.0, 227.0, 338.0, 20.0 ],
													"style" : "",
													"text" : "a normal <track> command with attributes could look like this:"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-6",
													"linecount" : 4,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 111.0, 153.0, 470.0, 60.0 ],
													"style" : "",
													"text" : "if the <anim> command has defined a fadeout attribute, it will use this value to target the fadeout for this variable.\n\nthe value of the fadeto attribute can also be an {expr}"
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-4",
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 32.0, 153.0, 69.0, 20.0 ],
													"style" : "",
													"text" : "fadeto=\"0.\""
												}

											}
, 											{
												"box" : 												{
													"id" : "obj-2",
													"linecount" : 4,
													"maxclass" : "comment",
													"numinlets" : 1,
													"numoutlets" : 0,
													"patching_rect" : [ 22.0, 18.0, 433.0, 60.0 ],
													"style" : "",
													"text" : "<track name=\"t1\">  is a variable that changes its value over time. the variable can be used by all messages\n\nit has two attributes:"
												}

											}
 ],
										"lines" : [  ]
									}
,
									"patching_rect" : [ 261.5, 248.0, 47.0, 22.0 ],
									"saved_object_attributes" : 									{
										"description" : "",
										"digest" : "",
										"globalpatchername" : "",
										"style" : "",
										"tags" : ""
									}
,
									"style" : "",
									"text" : "p track"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-28",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 66.0, 306.0, 226.0, 20.0 ],
									"style" : "",
									"text" : "<send>, <print>, <trigger>, <out>, <osc>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-27",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 66.0, 276.0, 108.0, 20.0 ],
									"style" : "",
									"text" : "<keys>0.5</keys>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-24",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 66.0, 248.0, 179.0, 20.0 ],
									"style" : "",
									"text" : "<track name=\"t1\">0. 1.</track> "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-21",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 26.0, 217.0, 302.0, 20.0 ],
									"style" : "",
									"text" : "following nodes can be nested inside <anim>:"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-19",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 290.0, 15.0, 169.0, 20.0 ],
									"style" : "",
									"text" : "for the time format see <wait>"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-17",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 26.0, 55.0, 83.0, 20.0 ],
									"style" : "",
									"text" : "the attributes:"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-15",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 218.0, 166.0, 336.0, 20.0 ],
									"style" : "",
									"text" : "the time it takes to fadeout again"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-14",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 218.0, 138.0, 313.0, 20.0 ],
									"style" : "",
									"text" : "the time it takes from normalized keytime 0. to keytime 1."
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-12",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 218.0, 109.0, 258.0, 20.0 ],
									"style" : "",
									"text" : "the ramp can keep on looping in different ways"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-10",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 38.0, 166.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "fadeout=\"2s | {expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-8",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 38.0, 138.0, 123.0, 20.0 ],
									"style" : "",
									"text" : "duration=\"5s | {expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-6",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 38.0, 109.0, 164.0, 20.0 ],
									"style" : "",
									"text" : "loop=\"no|normal|palindrome\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-5",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 38.0, 82.0, 120.0, 20.0 ],
									"style" : "",
									"text" : "name=\"anim_name\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 20.0, 15.0, 217.0, 20.0 ],
									"style" : "",
									"text" : "<anim> is the most complex command"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 199.0, 47.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p anim"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-25",
					"linecount" : 2,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 84.0, 199.0, 296.0, 33.0 ],
					"style" : "",
					"text" : "starts an animation which can be set with attributes and more commands"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-23",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 199.0, 52.0, 20.0 ],
					"style" : "",
					"text" : "<anim>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-21",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 70.0, 358.0, 747.0, 482.0 ],
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
									"id" : "obj-17",
									"linecount" : 14,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 539.0, 261.0, 194.0, 194.0 ],
									"style" : "",
									"text" : "when using {expr}, the evaluated value is interpreted in milliseconds. it is also possible to create in {expr} a time format like:\n\n{(varSec * 1000) + 'ms'}\n\nassuming varSec = 5, it returns a string like '5000ms' which then is parsed.\n\nhowever, because each time a string parsing is needed, this method is relativly timeconsuming."
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-10",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 407.0, 217.0, 33.0 ],
									"style" : "",
									"text" : "waits while the {expr} is TRUE (1) and carries on if it turns FALSE (0)"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-12",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 407.0, 113.0, 20.0 ],
									"style" : "",
									"text" : "while=\"{expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-1",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 380.0, 213.0, 20.0 ],
									"style" : "",
									"text" : "waits until the {expr} is TRUE (1)"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-4",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 380.0, 113.0, 20.0 ],
									"style" : "",
									"text" : "until=\"{expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-8",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 172.0, 159.0, 20.0 ],
									"style" : "",
									"text" : "trigger=\"trig_name movie.1\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-3",
									"linecount" : 4,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 194.0, 372.0, 60.0 ],
									"style" : "",
									"text" : "here it triggers if a message \"trig_name\" or  \"trig_name movie.1\" arrives. \n\nbut NOT so if \"trig_name movie.2\" arrives."
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-29",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 442.0, 500.0, 33.0 ],
									"style" : "",
									"text" : "one more important thing about wait: <anim> objects will keep on ouputing their interpolation while the que is waiting for a wait to carry on."
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-27",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 341.0, 372.0, 33.0 ],
									"style" : "",
									"text" : "waits for a animation with the name  \"anim_name\" has reached its target. (if the animation is looping this will never happen!!!)"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-26",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 341.0, 117.0, 20.0 ],
									"style" : "",
									"text" : "anim=\"anim_name\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-24",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 315.0, 309.0, 20.0 ],
									"style" : "",
									"text" : "waits until 10h:15m o'clock is reached"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-23",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 315.0, 141.0, 20.0 ],
									"style" : "",
									"text" : "clock=\"10h:15m | {expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-22",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 289.0, 309.0, 20.0 ],
									"style" : "",
									"text" : "waits until the 10th minute of the current hour is reached"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-20",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 289.0, 143.0, 20.0 ],
									"style" : "",
									"text" : "hourglass=\"10m | {expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-18",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 261.0, 150.0, 20.0 ],
									"style" : "",
									"text" : "waits for 30 seconds"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-16",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 261.0, 145.0, 20.0 ],
									"style" : "",
									"text" : "countdown=\"30s | {expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-15",
									"linecount" : 3,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 118.0, 370.0, 47.0 ],
									"style" : "",
									"text" : "-> triggers with a trigger-message of value \"trig_name\". \n-> in this case it also triggers if the message has more than one value, like \"trig_name something else\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-13",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 118.0, 113.0, 20.0 ],
									"style" : "",
									"text" : "trigger=\"trig_name\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-11",
									"linecount" : 15,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 539.0, 20.0, 206.0, 208.0 ],
									"style" : "",
									"text" : "about time: the format for a time is follows:\n \n12h:34m:23s:345ms\n10m\n5s\n1m:30s\n500ms\n\nimportant is, the numbers are allways trailed by a charcter\n \nh, m, s or ms\n\nand the separation sign is a ':'."
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-9",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 88.0, 300.0, 20.0 ],
									"style" : "",
									"text" : "waits until the timer has 3 second passed"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-7",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 167.0, 45.0, 300.0, 33.0 ],
									"style" : "",
									"text" : "this is for nested wait commands and definies the boolean logic necessary to pass on"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-6",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 87.0, 107.0, 20.0 ],
									"style" : "",
									"text" : "timer=\"3s | {expr}\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-5",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 44.0, 128.0, 20.0 ],
									"style" : "",
									"text" : "complex =\"and|or|nor\""
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-2",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 13.0, 13.0, 177.0, 20.0 ],
									"style" : "",
									"text" : "wait requires only one attribute:"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 161.0, 42.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p wait"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-19",
					"linecount" : 2,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 84.0, 161.0, 271.0, 33.0 ],
					"style" : "",
					"text" : "allows to wait for different kind of events before the que progresses to execute commands"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-20",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 161.0, 53.0, 20.0 ],
					"style" : "",
					"text" : "<wait>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-17",
					"linecount" : 2,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 84.0, 121.0, 277.0, 33.0 ],
					"style" : "",
					"text" : "starts an internal timer onto which <wait> can reference"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-18",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 121.0, 53.0, 20.0 ],
					"style" : "",
					"text" : "<timer>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-16",
					"maxclass" : "newobj",
					"numinlets" : 0,
					"numoutlets" : 0,
					"patcher" : 					{
						"fileversion" : 1,
						"appversion" : 						{
							"major" : 7,
							"minor" : 1,
							"revision" : 0,
							"architecture" : "x86",
							"modernui" : 1
						}
,
						"rect" : [ 358.0, 344.0, 603.0, 159.0 ],
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
									"id" : "obj-11",
									"linecount" : 2,
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 187.0, 59.0, 354.0, 33.0 ],
									"style" : "",
									"text" : "name to start the que with a 'play <quename>' message. the name should only contain a-z, A-Z, 0-9 plus _ (underscote)"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-8",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 187.0, 98.0, 277.0, 20.0 ],
									"style" : "",
									"text" : "normal will keep looping the que once it is finished"
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-6",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 57.0, 98.0, 116.0, 20.0 ],
									"style" : "",
									"text" : "loop = \"no|normal\" "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-5",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 57.0, 59.0, 116.0, 20.0 ],
									"style" : "",
									"text" : "name = \"quename\" "
								}

							}
, 							{
								"box" : 								{
									"id" : "obj-3",
									"maxclass" : "comment",
									"numinlets" : 1,
									"numoutlets" : 0,
									"patching_rect" : [ 23.0, 31.0, 150.0, 20.0 ],
									"style" : "",
									"text" : "que needs two attributes:"
								}

							}
 ],
						"lines" : [  ]
					}
,
					"patching_rect" : [ 385.0, 61.0, 41.0, 22.0 ],
					"saved_object_attributes" : 					{
						"description" : "",
						"digest" : "",
						"globalpatchername" : "",
						"style" : "",
						"tags" : ""
					}
,
					"style" : "",
					"text" : "p que"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-15",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 84.0, 61.0, 277.0, 20.0 ],
					"style" : "",
					"text" : "que node. at least one should exist under <script>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-11",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 61.0, 53.0, 20.0 ],
					"style" : "",
					"text" : "<que>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-10",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 84.0, 35.0, 150.0, 20.0 ],
					"style" : "",
					"text" : "root node (required)"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-8",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 35.0, 53.0, 20.0 ],
					"style" : "",
					"text" : "<script>"
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-5",
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 12.0, 8.0, 356.0, 20.0 ],
					"style" : "",
					"text" : "QueScript is a simple xml based script with a few commands: "
				}

			}
, 			{
				"box" : 				{
					"id" : "obj-2",
					"linecount" : 29,
					"maxclass" : "comment",
					"numinlets" : 1,
					"numoutlets" : 0,
					"patching_rect" : [ 471.0, 19.0, 428.0, 395.0 ],
					"style" : "",
					"text" : "<?xml version=\"1.0\"?>\n<script>\n\t<que name=\"que1\" loop=\"normal\">\n\t\t<timer/>\n\t\t<wait timer=\"3s\"/>\n\t\t<send>/bs/node/movie.1 timerpassed</send>\n\t\t\t<wait complex=\"or\">>\n\t  \t\t\t<wait hourglass=\"1m\"/>\n\t\t  \t\t<wait timer=\"0m:10s\"/>\n  \t\t\t</wait>\n\t \t</wait>\n  \t\t<send>/bs/node/whatever this</send>\n    \t<anim name=\"cam2\" loop=\"palindrome\" duration=\"5s\" fadeout=\"5s\">\n    \t\t<keys timing=\"abs\">1s</keys>\n   \t\t\t<track name=\"t1\" fadeto=\"0.\">0.0 0.5 1.0</track>\n    \t\t<track name=\"t2\" fadeto=\"0.\">0.0 0.5 1.0</track>\n    \t\t<track name=\"t3\" fadeto=\"0.\">0.0 0.5 1.0</track>\n    \t\t<track name=\"t4\" fadeto=\"1.\">1.0 1.0 0.5</track>\n    \t\t<send>/bs/node/camera.2 erase_color {$t1} {$t2} {$t3} {$t4}</send>\n  \t\t</anim>\n    \t<wait countdown=\"5s\"/>\n\t\t<fade name=\"cam2\" fadeout=\"2s\"/>\n\t\t<wait countdown=\"2s\"/>\n\t\t<send>/bs/node/movie.1 stop</send>\n\t</que>\n\t<que name=\"anotherCue\" loop=\"no\">\n\t\t<send>/bs/node/movie.1 start</send>\n\t</que>\n</script>"
				}

			}
 ],
		"lines" : [  ],
		"dependency_cache" : [  ],
		"autosave" : 0
	}

}
