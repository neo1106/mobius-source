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
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

public class Q00022_TragedyInVonHellmannForest extends Quest implements ScriptFile
{
	private final int Well = 31527;
	private final int Tifaren = 31334;
	private final int Innocentin = 31328;
	private final int SoulOfWell = 27217;
	private final int GhostOfPriest = 31528;
	private final int GhostOfAdventurer = 31529;
	private final int ReportBox = 7147;
	private final int LostSkullOfElf = 7142;
	private final int CrossOfEinhasad = 7141;
	private final int SealedReportBox = 7146;
	private final int LetterOfInnocentin = 7143;
	private final int JewelOfAdventurerRed = 7145;
	private final int JewelOfAdventurerGreen = 7144;
	private static final List<Integer> Mobs = new ArrayList<>();
	static
	{
		for (int i : new int[]
		{
			21547,
			21548,
			21549,
			21550,
			21551,
			21552,
			21553,
			21554,
			21555,
			21556,
			21557,
			21558,
			21559,
			21560,
			21561,
			21562,
			21563,
			21564,
			21565,
			21566,
			21567,
			21568,
			21569,
			21570,
			21571,
			21572,
			21573,
			21574,
			21575,
			21576,
			21577,
			21578
		})
		{
			Mobs.add(i);
		}
	}
	private static NpcInstance GhostOfPriestInstance = null;
	private static NpcInstance SoulOfWellInstance = null;
	
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
	
	public Q00022_TragedyInVonHellmannForest()
	{
		super(false);
		addStartNpc(Tifaren);
		addTalkId(Tifaren);
		addTalkId(GhostOfPriest);
		addTalkId(Innocentin);
		addTalkId(GhostOfAdventurer);
		addTalkId(Well);
		addKillId(SoulOfWell);
		
		for (int npcId = 21547; npcId <= 21578; npcId++)
		{
			addKillId(npcId);
		}
		
		addQuestItem(LostSkullOfElf);
	}
	
	private void spawnGhostOfPriest(QuestState st)
	{
		GhostOfPriestInstance = Functions.spawn(Location.findPointToStay(st.getPlayer(), 50, 100), GhostOfPriest);
	}
	
	private void spawnSoulOfWell(QuestState st)
	{
		SoulOfWellInstance = Functions.spawn(Location.findPointToStay(st.getPlayer(), 50, 100), SoulOfWell);
	}
	
	private void despawnGhostOfPriest()
	{
		if (GhostOfPriestInstance != null)
		{
			GhostOfPriestInstance.deleteMe();
		}
	}
	
	private void despawnSoulOfWell()
	{
		if (SoulOfWellInstance != null)
		{
			SoulOfWellInstance.deleteMe();
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("31334-03.htm"))
		{
			st.setState(STARTED);
			st.setCond(3);
			st.takeItems(CrossOfEinhasad, -1);
		}
		else if (event.equalsIgnoreCase("31334-06.htm"))
		{
			st.setCond(4);
		}
		else if (event.equalsIgnoreCase("31528-07.htm"))
		{
			despawnGhostOfPriest();
			st.setCond(8);
		}
		else if (event.equalsIgnoreCase("31328-06.htm"))
		{
			st.setCond(9);
			st.giveItems(LetterOfInnocentin, 1);
		}
		else if (event.equalsIgnoreCase("31529-09.htm"))
		{
			st.setCond(10);
			despawnSoulOfWell();
			spawnSoulOfWell(st);
			st.startQuestTimer("attack_timer", 60000);
			st.takeItems(JewelOfAdventurerGreen, -1);
			st.takeItems(JewelOfAdventurerRed, -1);
			st.giveItems(JewelOfAdventurerGreen, 1);
			st.takeItems(LetterOfInnocentin, 1);
		}
		else if (event.equalsIgnoreCase("explore"))
		{
			despawnSoulOfWell();
			spawnSoulOfWell(st);
			st.setCond(11);
			st.giveItems(JewelOfAdventurerGreen, 1);
			htmltext = "<html><body>Attack Soul of Well but do not kill while stone will not change colour...</body></html>";
		}
		else if (event.equalsIgnoreCase("attack_timer"))
		{
			despawnSoulOfWell();
			st.giveItems(JewelOfAdventurerRed, 1);
			st.takeItems(JewelOfAdventurerGreen, -1);
			st.setCond(12);
			htmltext = "";
		}
		else if (event.equalsIgnoreCase("31328-08.htm"))
		{
			st.startQuestTimer("wait_timer", 600000);
			st.setCond(15);
			st.takeItems(ReportBox, 1);
		}
		else if (event.equalsIgnoreCase("wait_timer"))
		{
			st.setCond(16);
			htmltext = "<html><body>Innocentin wants with you to speak...</body></html>";
		}
		else if (event.equalsIgnoreCase("31328-16.htm"))
		{
			htmltext = "31328-17.htm";
			st.addExpAndSp(8249480, 7930280);
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("waite"))
		{
			st.setCond(6);
			htmltext = "31334-08.htm";
		}
		else if (event.equalsIgnoreCase("waite1"))
		{
			st.setCond(7);
			htmltext = "31334-09.htm";
			st.takeItems(LostSkullOfElf, 1);
			despawnGhostOfPriest();
			spawnGhostOfPriest(st);
		}
		else if (event.equalsIgnoreCase("next_wait_timer"))
		{
			st.setCond(18);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getId();
		int cond = st.getCond();
		String htmltext = "noquest";
		
		if (npcId == Tifaren)
		{
			if (cond == 0)
			{
				QuestState hiddenTruth = st.getPlayer().getQuestState(Q00021_HiddenTruth.class);
				
				if (hiddenTruth != null)
				{
					if (hiddenTruth.isCompleted())
					{
						htmltext = "31334-01.htm";
					}
					else
					{
						htmltext = "<html><head><body>You not complite quest Hidden Truth...</body></html>";
					}
				}
				else
				{
					htmltext = "<html><head><body>You not complite quest Hidden Truth...</body></html>";
				}
			}
			else if (cond == 3)
			{
				return "31334-04.htm";
			}
			else if (cond == 4)
			{
				htmltext = "31334-06.htm";
			}
			else if (cond == 5)
			{
				if (st.getQuestItemsCount(LostSkullOfElf) != 0)
				{
					htmltext = "31334-07.htm";
				}
				else
				{
					st.setCond(4);
					htmltext = "31334-06.htm";
				}
			}
			else if (cond == 6)
			{
				htmltext = "31334-07.htm";
			}
			else if (cond == 7)
			{
				despawnGhostOfPriest();
				spawnGhostOfPriest(st);
				htmltext = "31334-09.htm";
			}
		}
		else if (npcId == GhostOfPriest)
		{
			if (cond == 7)
			{
				htmltext = "31528-00.htm";
			}
			else if (cond == 8)
			{
				htmltext = "31528-07.htm";
			}
		}
		else if (npcId == Innocentin)
		{
			if (cond == 0)
			{
				htmltext = "31328-17.htm";
			}
			
			if (cond == 8)
			{
				htmltext = "31328-00.htm";
			}
			else if (cond == 9)
			{
				htmltext = "31328-06.htm";
			}
			else if (cond == 14)
			{
				if (st.getQuestItemsCount(ReportBox) != 0)
				{
					htmltext = "31328-07.htm";
				}
				else
				{
					st.setCond(13);
					htmltext = "Go away!";
				}
			}
			else if (cond == 15)
			{
				if (!st.isRunningQuestTimer("wait_timer"))
				{
					st.setCond(16);
				}
				
				htmltext = "31328-09.htm";
			}
		}
		else if (npcId == GhostOfAdventurer)
		{
			if (cond == 9)
			{
				if (st.getQuestItemsCount(LetterOfInnocentin) != 0)
				{
					htmltext = "31529-00.htm";
				}
				else
				{
					htmltext = "You have no Letter of Innocentin! Are they Please returned to High Priest Innocentin...";
				}
			}
			else if (cond == 10)
			{
				despawnSoulOfWell();
				spawnSoulOfWell(st);
				st.startQuestTimer("attack_timer", 60000);
				st.takeItems(JewelOfAdventurerGreen, -1);
				st.takeItems(JewelOfAdventurerRed, -1);
				st.giveItems(JewelOfAdventurerGreen, 1);
				htmltext = "<html><body>Attack Soul of Well but do not kill while stone will not change colour...</body></html>";
			}
			else if (cond == 11)
			{
				if (st.getQuestItemsCount(JewelOfAdventurerRed) != 0)
				{
					htmltext = "31529-10.htm";
					st.takeItems(JewelOfAdventurerRed, 1);
					st.setCond(12);
				}
			}
			else if (cond == 12)
			{
				htmltext = "31529-09.htm";
			}
			else if (cond == 13)
			{
				if (st.getQuestItemsCount(SealedReportBox) != 0)
				{
					htmltext = "31529-11.htm";
					st.setCond(14);
					st.takeItems(SealedReportBox, 1);
					st.giveItems(ReportBox, 1);
				}
				else
				{
					st.setCond(12);
					htmltext = "31529-10.htm";
				}
			}
		}
		else if ((npcId == Well) && (cond == 12))
		{
			htmltext = "31527-01.htm";
			st.setCond(13);
			st.giveItems(SealedReportBox, 1);
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getId();
		int cond = st.getCond();
		
		if (Mobs.contains(npcId))
		{
			if ((cond == 4) && Rnd.chance(99))
			{
				st.giveItems(LostSkullOfElf, 1);
				st.playSound(SOUND_MIDDLE);
				st.setCond(5);
			}
		}
		
		if (npcId == SoulOfWell)
		{
			if (cond == 10)
			{
				st.setCond(9);
				st.takeItems(JewelOfAdventurerGreen, -1);
				st.takeItems(JewelOfAdventurerRed, -1);
				st.cancelQuestTimer("attack_timer");
			}
		}
		
		return null;
	}
}