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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _601_WatchingEyes extends Quest implements ScriptFile
{
	private static final int EYE_OF_ARGOS = 31683;
	private static final int PROOF_OF_AVENGER = 7188;
	private static final int DROP_CHANCE = 50;
	private static final int[] MOBS =
	{
		21306,
		21308,
		21309,
		21310,
		21311
	};
	private static final int[][] REWARDS =
	{
		{
			6699,
			90000,
			0,
			19
		},
		{
			6698,
			80000,
			20,
			39
		},
		{
			6700,
			40000,
			40,
			49
		},
		{
			0,
			230000,
			50,
			100
		}
	};
	
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
	
	public _601_WatchingEyes()
	{
		super(true);
		addStartNpc(EYE_OF_ARGOS);
		addKillId(MOBS);
		addQuestItem(PROOF_OF_AVENGER);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("eye_of_argos_q0601_0104.htm"))
		{
			if (st.getPlayer().getLevel() < 71)
			{
				htmltext = "eye_of_argos_q0601_0103.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				st.setState(STARTED);
				st.setCond(1);
				st.playSound(SOUND_ACCEPT);
			}
		}
		else if (event.equalsIgnoreCase("eye_of_argos_q0601_0201.htm"))
		{
			int random = Rnd.get(101);
			int i = 0;
			int item = 0;
			int adena = 0;
			
			while (i < REWARDS.length)
			{
				item = REWARDS[i][0];
				adena = REWARDS[i][1];
				
				if ((REWARDS[i][2] <= random) && (random <= REWARDS[i][3]))
				{
					break;
				}
				
				i++;
			}
			
			st.giveItems(ADENA_ID, adena, true);
			
			if (item != 0)
			{
				st.giveItems(item, 5, true);
				st.addExpAndSp(120000, 10000);
			}
			
			st.takeItems(PROOF_OF_AVENGER, -1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		
		if (cond == 0)
		{
			htmltext = "eye_of_argos_q0601_0101.htm";
		}
		else if (cond == 1)
		{
			htmltext = "eye_of_argos_q0601_0106.htm";
		}
		else if ((cond == 2) && (st.getQuestItemsCount(PROOF_OF_AVENGER) == 100))
		{
			htmltext = "eye_of_argos_q0601_0105.htm";
		}
		else
		{
			htmltext = "eye_of_argos_q0601_0202.htm";
			st.setCond(1);
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() == 1)
		{
			long count = st.getQuestItemsCount(PROOF_OF_AVENGER);
			
			if ((count < 100) && Rnd.chance(DROP_CHANCE))
			{
				st.giveItems(PROOF_OF_AVENGER, 1);
				
				if (count == 99)
				{
					st.setCond(2);
					st.playSound(SOUND_MIDDLE);
				}
				else
				{
					st.playSound(SOUND_ITEMGET);
				}
			}
		}
		
		return null;
	}
}
