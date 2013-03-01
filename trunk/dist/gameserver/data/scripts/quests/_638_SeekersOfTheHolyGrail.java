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

public class _638_SeekersOfTheHolyGrail extends Quest implements ScriptFile
{
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
	
	private static final int DROP_CHANCE = 10;
	private static final int INNOCENTIN = 31328;
	private static final int TOTEM = 8068;
	private static final int EAS = 960;
	private static final int EWS = 959;
	
	public _638_SeekersOfTheHolyGrail()
	{
		super(true);
		addStartNpc(INNOCENTIN);
		addQuestItem(TOTEM);
		for (int i = 22137; i <= 22176; i++)
		{
			addKillId(i);
		}
		addKillId(22194);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("highpriest_innocentin_q0638_03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("highpriest_innocentin_q0638_09.htm"))
		{
			st.playSound(SOUND_GIVEUP);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int id = st.getState();
		if (id == CREATED)
		{
			if (st.getPlayer().getLevel() >= 73)
			{
				htmltext = "highpriest_innocentin_q0638_01.htm";
			}
			else
			{
				htmltext = "highpriest_innocentin_q0638_02.htm";
			}
		}
		else
		{
			htmltext = tryRevard(st);
		}
		return htmltext;
	}
	
	private String tryRevard(QuestState st)
	{
		boolean ok = false;
		while (st.getQuestItemsCount(TOTEM) >= 2000)
		{
			st.takeItems(TOTEM, 2000);
			int rnd = Rnd.get(100);
			if (rnd < 50)
			{
				st.giveItems(ADENA_ID, 3576000, false);
			}
			else if (rnd < 85)
			{
				st.giveItems(EAS, 1, false);
			}
			else
			{
				st.giveItems(EWS, 1, false);
			}
			ok = true;
		}
		if (ok)
		{
			st.playSound(SOUND_MIDDLE);
			return "highpriest_innocentin_q0638_10.htm";
		}
		return "highpriest_innocentin_q0638_05.htm";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		st.rollAndGive(TOTEM, 1, DROP_CHANCE * npc.getTemplate().rateHp);
		if (((npc.getNpcId() == 22146) || (npc.getNpcId() == 22151)) && Rnd.chance(10))
		{
			npc.dropItem(st.getPlayer(), 8275, 1);
		}
		if (((npc.getNpcId() == 22140) || (npc.getNpcId() == 22149)) && Rnd.chance(10))
		{
			npc.dropItem(st.getPlayer(), 8273, 1);
		}
		if (((npc.getNpcId() == 22142) || (npc.getNpcId() == 22143)) && Rnd.chance(10))
		{
			npc.dropItem(st.getPlayer(), 8274, 1);
		}
		return null;
	}
}