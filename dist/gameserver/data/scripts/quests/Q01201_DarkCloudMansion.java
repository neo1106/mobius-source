/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CharacterAI;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncMul;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import gnu.trove.map.hash.TIntObjectHashMap;

public class Q01201_DarkCloudMansion extends Quest implements ScriptFile
{
	private static final int INCSTANCED_ZONE_ID = 9;
	private static final int CC = 9690;
	private static final int YIYEN = 32282;
	private static final int SOFaith = 32288;
	private static final int SOAdversity = 32289;
	private static final int SOAdventure = 32290;
	private static final int SOTruth = 32291;
	private static final int BSM = 32324;
	private static final int SC = 22402;
	private static final int[] CCG = new int[]
	{
		18369,
		18370
	};
	private static final int[] BM = new int[]
	{
		22272,
		22273,
		22274
	};
	private static final int[] HG = new int[]
	{
		22264,
		22265
	};
	private static final int[] BS = new int[]
	{
		18371,
		18372,
		18373,
		18374,
		18375,
		18376,
		18377
	};
	private static final int D1 = 24230001;
	private static final int D2 = 24230002;
	private static final int D3 = 24230005;
	private static final int D4 = 24230003;
	private static final int D5 = 24230004;
	private static final int D6 = 24230006;
	private static final int W1 = 24230007;
	private static final int[][] order = new int[][]
	{
		{
			1,
			2,
			3,
			4,
			5,
			6
		},
		{
			6,
			5,
			4,
			3,
			2,
			1
		},
		{
			4,
			5,
			6,
			3,
			2,
			1
		},
		{
			2,
			6,
			3,
			5,
			1,
			4
		},
		{
			4,
			1,
			5,
			6,
			2,
			3
		},
		{
			3,
			5,
			1,
			6,
			2,
			4
		},
		{
			6,
			1,
			3,
			4,
			5,
			2
		},
		{
			5,
			6,
			1,
			2,
			4,
			3
		},
		{
			5,
			2,
			6,
			3,
			4,
			1
		},
		{
			1,
			5,
			2,
			6,
			3,
			4
		},
		{
			1,
			2,
			3,
			6,
			5,
			4
		},
		{
			6,
			4,
			3,
			1,
			5,
			2
		},
		{
			3,
			5,
			2,
			4,
			1,
			6
		},
		{
			3,
			2,
			4,
			5,
			1,
			6
		},
		{
			5,
			4,
			3,
			1,
			6,
			2
		}
	};
	private static final int[][] golems = new int[][]
	{
		{
			CCG[0],
			148060,
			181389
		},
		{
			CCG[1],
			147910,
			181173
		},
		{
			CCG[0],
			147810,
			181334
		},
		{
			CCG[1],
			147713,
			181179
		},
		{
			CCG[0],
			147569,
			181410
		},
		{
			CCG[1],
			147810,
			181517
		},
		{
			CCG[0],
			147805,
			181281
		}
	};
	private static final int[][] rows = new int[][]
	{
		{
			1,
			1,
			0,
			1,
			0
		},
		{
			0,
			1,
			1,
			0,
			1
		},
		{
			1,
			0,
			1,
			1,
			0
		},
		{
			0,
			1,
			0,
			1,
			1
		},
		{
			1,
			0,
			1,
			0,
			1
		}
	};
	private static final int[][] beleths = new int[][]
	{
		{
			1,
			0,
			1,
			0,
			1,
			0,
			0
		},
		{
			0,
			0,
			1,
			0,
			1,
			1,
			0
		},
		{
			0,
			0,
			0,
			1,
			0,
			1,
			1
		},
		{
			1,
			0,
			1,
			1,
			0,
			0,
			0
		},
		{
			1,
			1,
			0,
			0,
			0,
			1,
			0
		},
		{
			0,
			1,
			0,
			1,
			0,
			1,
			0
		},
		{
			0,
			0,
			0,
			1,
			1,
			1,
			0
		},
		{
			1,
			0,
			1,
			0,
			0,
			1,
			0
		},
		{
			0,
			1,
			1,
			0,
			0,
			0,
			1
		}
	};
	
	private class World
	{
		public World()
		{
		}
		
		int instanceId;
		int status;
		List<Long> rewarded;
		Room StartRoom;
		Room Hall;
		Room FirstRoom;
		Room SecondRoom;
		Room ThirdRoom;
		Room ForthRoom;
		Room FifthRoom;
	}
	
	private class Room
	{
		public Room()
		{
		}
		
		Map<NpcInstance, Boolean> npclist;
		List<long[]> npclist2;
		List<long[]> monolith;
		int[] monolithOrder;
		List<int[]> belethOrder;
		int counter;
	}
	
	private static final TIntObjectHashMap<World> worlds = new TIntObjectHashMap<>();
	
	public Q01201_DarkCloudMansion()
	{
		super(true);
		addStartNpc(YIYEN);
		addTalkId(SOTruth);
		addFirstTalkId(BSM);
		addAttackId(SC);
		addAttackId(BS);
		addKillId(BS);
		addKillId(BM);
		addKillId(CCG);
		addKillId(SC);
		addKillId(HG);
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final World world = worlds.get(player.getReflectionId());
		
		if (world.status == 4)
		{
			for (long[] npcObj : world.SecondRoom.monolith)
			{
				if (npcObj[0] == npc.getStoredId())
				{
					checkStone(npc, world.SecondRoom.monolithOrder, npcObj, world);
				}
			}
			
			if (allStonesDone(world))
			{
				removeMonoliths(world);
				runHall3(world);
			}
		}
		
		return null;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final Player player = qs.getPlayer();
		
		if (npcId == YIYEN)
		{
			qs.setState(STARTED);
			enterInstance(player);
			return null;
		}
		
		if (npc.getReflection() == ReflectionManager.DEFAULT)
		{
			return null;
		}
		
		final World world = worlds.get(npc.getReflectionId());
		
		if (world != null)
		{
			if (npcId == SOTruth)
			{
				player.setReflection(0);
				player.teleToLocation(new Location(139968, 150367, -3111));
				
				if (!world.rewarded.contains(player.getStoredId()))
				{
					qs.giveItems(CC, 1);
					world.rewarded.add(player.getStoredId());
				}
			}
		}
		
		return null;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final World world = worlds.get(npc.getReflectionId());
		
		if (world == null)
		{
			return null;
		}
		
		switch (world.status)
		{
			case 0:
				if (checkKillProgress(npc, world.StartRoom))
				{
					runHall(world);
				}
				break;
			
			case 1:
				if (checkKillProgress(npc, world.Hall))
				{
					runFirstRoom(world);
				}
				break;
			
			case 2:
				if (checkKillProgress(npc, world.FirstRoom))
				{
					runHall2(world);
				}
				break;
			
			case 3:
				if (checkKillProgress(npc, world.Hall))
				{
					runSecondRoom(world);
				}
				break;
			
			case 5:
				if (checkKillProgress(npc, world.Hall))
				{
					runThirdRoom(world);
				}
				break;
			
			case 6:
				if (checkKillProgress(npc, world.ThirdRoom))
				{
					runForthRoom(world);
				}
				break;
			
			case 7:
				chkShadowColumn(world, npc);
				break;
			
			case 8:
				BelethSampleKilled(world, npc, player);
				break;
		}
		
		return null;
	}
	
	@Override
	public String onAttack(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final World world = worlds.get(player.getReflectionId());
		
		if ((world != null) && (world.status == 7))
		{
			for (long[] mob : world.ForthRoom.npclist2)
			{
				if (mob[0] == npc.getStoredId())
				{
					if (Rnd.chance(12) && npc.isBusy())
					{
						addSpawnToInstance(BM[Rnd.get(BM.length)], player.getLoc(), 100, world.instanceId);
					}
				}
			}
		}
		
		if ((world != null) && (world.status == 8))
		{
			BelethSampleAttacked(world, npc, player);
		}
		
		return null;
	}
	
	private void endInstance(World world)
	{
		world.status = 9;
		addSpawnToInstance(SOTruth, new Location(148911, 181940, -6117, 16383), 0, world.instanceId);
		world.StartRoom = null;
		world.Hall = null;
		world.SecondRoom = null;
		world.ThirdRoom = null;
		world.ForthRoom = null;
		world.FifthRoom = null;
	}
	
	private void enterInstance(Player player)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(INCSTANCED_ZONE_ID))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(INCSTANCED_ZONE_ID))
		{
			Reflection newInstance = ReflectionUtils.enterReflection(player, INCSTANCED_ZONE_ID);
			World world = new World();
			world.rewarded = new ArrayList<>();
			world.instanceId = newInstance.getId();
			worlds.put(newInstance.getId(), world);
			runStartRoom(world);
			
			for (Player member : player.getParty().getPartyMembers())
			{
				if (member != player)
				{
					newQuestState(member, STARTED);
				}
			}
		}
	}
	
	private void runStartRoom(World world)
	{
		world.status = 0;
		world.StartRoom = new Room();
		world.StartRoom.npclist = new HashMap<>();
		NpcInstance newNpc;
		newNpc = addSpawnToInstance(BM[0], new Location(146817, 180335, -6117), 0, world.instanceId);
		world.StartRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[0], new Location(146741, 180589, -6117), 0, world.instanceId);
		world.StartRoom.npclist.put(newNpc, false);
	}
	
	private void spawnHall(World world)
	{
		world.Hall = new Room();
		world.Hall.npclist = new HashMap<>();
		NpcInstance newNpc = addSpawnToInstance(BM[1], new Location(147217, 180112, -6117), 0, world.instanceId);
		world.Hall.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[2], new Location(147217, 180209, -6117), 0, world.instanceId);
		world.Hall.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[1], new Location(148521, 180112, -6117), 0, world.instanceId);
		world.Hall.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[0], new Location(148521, 180209, -6117), 0, world.instanceId);
		world.Hall.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[1], new Location(148525, 180910, -6117), 0, world.instanceId);
		world.Hall.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[2], new Location(148435, 180910, -6117), 0, world.instanceId);
		world.Hall.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[1], new Location(147242, 180910, -6117), 0, world.instanceId);
		world.Hall.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[2], new Location(147242, 180819, -6117), 0, world.instanceId);
		world.Hall.npclist.put(newNpc, false);
	}
	
	private void runHall(World world)
	{
		world.status = 1;
		ReflectionManager.getInstance().get(world.instanceId).openDoor(D1);
		spawnHall(world);
	}
	
	private void runFirstRoom(World world)
	{
		world.status = 2;
		ReflectionManager.getInstance().get(world.instanceId).openDoor(D2);
		world.FirstRoom = new Room();
		world.FirstRoom.npclist = new HashMap<>();
		NpcInstance newNpc = addSpawnToInstance(HG[1], new Location(147842, 179837, -6117), 0, world.instanceId);
		world.FirstRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(HG[0], new Location(147711, 179708, -6117), 0, world.instanceId);
		world.FirstRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(HG[1], new Location(147842, 179552, -6117), 0, world.instanceId);
		world.FirstRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(HG[0], new Location(147964, 179708, -6117), 0, world.instanceId);
		world.FirstRoom.npclist.put(newNpc, false);
	}
	
	private void runHall2(World world)
	{
		world.status = 3;
		spawnHall(world);
	}
	
	private void runSecondRoom(World world)
	{
		NpcInstance newNpc = addSpawnToInstance(SOFaith, new Location(147818, 179643, -6117), 0, world.instanceId);
		world.status = 4;
		ReflectionManager.getInstance().get(world.instanceId).openDoor(D3);
		world.SecondRoom = new Room();
		world.SecondRoom.monolith = new ArrayList<>();
		int i = Rnd.get(order.length);
		world.SecondRoom.monolithOrder = new int[]
		{
			1,
			0,
			0,
			0,
			0,
			0,
			0
		};
		newNpc = addSpawnToInstance(BSM, new Location(147800, 181150, -6117), 0, world.instanceId);
		world.SecondRoom.monolith.add(new long[]
		{
			newNpc.getStoredId(),
			order[i][0],
			0
		});
		newNpc = addSpawnToInstance(BSM, new Location(147900, 181215, -6117), 0, world.instanceId);
		world.SecondRoom.monolith.add(new long[]
		{
			newNpc.getStoredId(),
			order[i][1],
			0
		});
		newNpc = addSpawnToInstance(BSM, new Location(147900, 181345, -6117), 0, world.instanceId);
		world.SecondRoom.monolith.add(new long[]
		{
			newNpc.getStoredId(),
			order[i][2],
			0
		});
		newNpc = addSpawnToInstance(BSM, new Location(147800, 181410, -6117), 0, world.instanceId);
		world.SecondRoom.monolith.add(new long[]
		{
			newNpc.getStoredId(),
			order[i][3],
			0
		});
		newNpc = addSpawnToInstance(BSM, new Location(147700, 181345, -6117), 0, world.instanceId);
		world.SecondRoom.monolith.add(new long[]
		{
			newNpc.getStoredId(),
			order[i][4],
			0
		});
		newNpc = addSpawnToInstance(BSM, new Location(147700, 181215, -6117), 0, world.instanceId);
		world.SecondRoom.monolith.add(new long[]
		{
			newNpc.getStoredId(),
			order[i][5],
			0
		});
	}
	
	private void runHall3(World world)
	{
		addSpawnToInstance(SOAdversity, new Location(147808, 181281, -6117, 16383), 0, world.instanceId);
		world.status = 5;
		spawnHall(world);
	}
	
	private void runThirdRoom(World world)
	{
		world.status = 6;
		ReflectionManager.getInstance().get(world.instanceId).openDoor(D4);
		world.ThirdRoom = new Room();
		world.ThirdRoom.npclist = new HashMap<>();
		NpcInstance newNpc = addSpawnToInstance(BM[1], new Location(148765, 180450, -6117), 0, world.instanceId);
		world.ThirdRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[2], new Location(148865, 180190, -6117), 0, world.instanceId);
		world.ThirdRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[1], new Location(148995, 180190, -6117), 0, world.instanceId);
		world.ThirdRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[0], new Location(149090, 180450, -6117), 0, world.instanceId);
		world.ThirdRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[1], new Location(148995, 180705, -6117), 0, world.instanceId);
		world.ThirdRoom.npclist.put(newNpc, false);
		newNpc = addSpawnToInstance(BM[2], new Location(148865, 180705, -6117), 0, world.instanceId);
		world.ThirdRoom.npclist.put(newNpc, false);
	}
	
	private void runForthRoom(World world)
	{
		world.status = 7;
		ReflectionManager.getInstance().get(world.instanceId).openDoor(D5);
		world.ForthRoom = new Room();
		world.ForthRoom.npclist2 = new ArrayList<>();
		world.ForthRoom.counter = 0;
		int[] temp = new int[7];
		int[][] templist = new int[7][];
		
		for (int i = 0; i < temp.length; i++)
		{
			temp[i] = Rnd.get(rows.length);
		}
		
		for (int i = 0; i < temp.length; i++)
		{
			templist[i] = rows[temp[i]];
		}
		
		int xx = 0;
		int yy = 0;
		
		for (int x = 148660; x <= 149160; x += 125)
		{
			yy = 0;
			
			for (int y = 179280; y >= 178530; y -= 125)
			{
				NpcInstance newNpc = addSpawnToInstance(SC, new Location(x, y, -6115, 16215), 0, world.instanceId);
				newNpc.setAI(new CharacterAI(newNpc));
				
				if (templist[yy][xx] == 0)
				{
					newNpc.setBusy(true);
					newNpc.addStatFunc(new FuncMul(Stats.MAGIC_DEFENCE, 0x30, this, 1000));
					newNpc.addStatFunc(new FuncMul(Stats.POWER_DEFENCE, 0x30, this, 1000));
				}
				
				world.ForthRoom.npclist2.add(new long[]
				{
					newNpc.getStoredId(),
					templist[yy][xx],
					yy
				});
				yy += 1;
			}
			
			xx += 1;
		}
	}
	
	private void runFifthRoom(World world)
	{
		world.status = 8;
		ReflectionManager.getInstance().get(world.instanceId).openDoor(D6);
		world.FifthRoom = new Room();
		addSpawnToInstance(SOAdventure, new Location(148910, 178397, -6117, 16383), 0, world.instanceId);
		spawnBelethSample(world);
	}
	
	private void spawnBelethSample(World world)
	{
		world.FifthRoom.npclist2 = new ArrayList<>();
		int[] beleth = beleths[Rnd.get(beleths.length)];
		world.FifthRoom.belethOrder = new ArrayList<>();
		world.FifthRoom.belethOrder.add(beleth);
		int idx = 0;
		
		for (int x = 148720; x <= 149110; x += 65)
		{
			NpcInstance newNpc = addSpawnToInstance(BS[idx], new Location(x, 182145, -6117, 48810), 0, world.instanceId);
			world.FifthRoom.npclist2.add(new long[]
			{
				newNpc.getStoredId(),
				idx,
				beleth[idx]
			});
			idx += 1;
		}
	}
	
	private boolean checkKillProgress(NpcInstance npc, Room room)
	{
		if (room.npclist.containsKey(npc))
		{
			room.npclist.put(npc, true);
		}
		
		for (boolean value : room.npclist.values())
		{
			if (!value)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private void spawnRndGolem(World world)
	{
		int i = Rnd.get(golems.length);
		int id = golems[i][0];
		int x = golems[i][1];
		int y = golems[i][2];
		addSpawnToInstance(id, new Location(x, y, -6117), 0, world.instanceId);
	}
	
	private void checkStone(NpcInstance npc, int[] order, long[] npcObj, World world)
	{
		for (int i = 1; i <= 6; i++)
		{
			if ((order[i] == 0) && (order[i - 1] != 0))
			{
				if ((npcObj[1] == i) && (npcObj[2] == 0))
				{
					order[i] = 1;
					npcObj[2] = 1;
					npc.broadcastPacket(new MagicSkillUse(npc, npc, 5441, 1, 1, 0));
					return;
				}
			}
		}
		
		spawnRndGolem(world);
	}
	
	private void BelethSampleAttacked(World world, NpcInstance npc, Player player)
	{
		for (long[] list : world.FifthRoom.npclist2)
		{
			if (list[0] == npc.getStoredId())
			{
				if (list[2] == 1)
				{
					Functions.npcSay(npc, "You have done well!");
					npc.decayMe();
					world.FifthRoom.counter += 1;
					
					if (world.FifthRoom.counter >= 3)
					{
						unspawnBelethSample(world);
						endInstance(world);
						return;
					}
				}
				else
				{
					world.FifthRoom.counter = 0;
				}
				
				return;
			}
		}
	}
	
	private void BelethSampleKilled(World world, NpcInstance npc, Player player)
	{
		for (long[] list : world.FifthRoom.npclist2)
		{
			if (list[0] == npc.getStoredId())
			{
				world.FifthRoom.counter = 0;
				unspawnBelethSample(world);
				spawnBelethSample(world);
				return;
			}
		}
	}
	
	private void unspawnBelethSample(World world)
	{
		for (long[] list : world.FifthRoom.npclist2)
		{
			final NpcInstance npc = GameObjectsStorage.getAsNpc(list[0]);
			
			if (npc != null)
			{
				npc.decayMe();
			}
		}
	}
	
	private void removeMonoliths(World world)
	{
		for (long[] list : world.SecondRoom.monolith)
		{
			final NpcInstance npc = GameObjectsStorage.getAsNpc(list[0]);
			
			if (npc != null)
			{
				npc.decayMe();
			}
		}
	}
	
	private boolean allStonesDone(World world)
	{
		for (long[] list : world.SecondRoom.monolith)
		{
			if (list[2] != 1)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private void chkShadowColumn(World world, NpcInstance npc)
	{
		final Reflection ref = ReflectionManager.getInstance().get(world.instanceId);
		
		for (long[] mob : world.ForthRoom.npclist2)
		{
			if (mob[0] == npc.getStoredId())
			{
				for (int i = 0; i <= 7; i++)
				{
					if ((mob[2] == i) && (world.ForthRoom.counter == i))
					{
						ref.openDoor(W1 + i);
						world.ForthRoom.counter += 1;
						
						if (world.ForthRoom.counter == 7)
						{
							runFifthRoom(world);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}
