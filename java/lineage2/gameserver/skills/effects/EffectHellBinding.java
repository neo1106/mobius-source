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
package lineage2.gameserver.skills.effects;

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.ExAlterSkillRequest;
import lineage2.gameserver.stats.Env;
import gnu.trove.map.hash.TIntIntHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EffectHellBinding extends Effect
{
	private static final TIntIntHashMap _ChainedAirSkills = new TIntIntHashMap(8);
	private static final TIntIntHashMap _ChainedTemporalReplace = new TIntIntHashMap(8);
	
	public EffectHellBinding(Env env, EffectTemplate template)
	{
		super(env, template);
		_ChainedAirSkills.clear();
		_ChainedTemporalReplace.clear();
		_ChainedAirSkills.put(139, 10249);
		_ChainedAirSkills.put(140, 10499);
		_ChainedAirSkills.put(141, 10749);
		_ChainedAirSkills.put(142, 10999);
		_ChainedAirSkills.put(143, 11247);
		_ChainedAirSkills.put(143, 11249);
		_ChainedAirSkills.put(144, 11749);
		_ChainedAirSkills.put(145, 11499);
		_ChainedAirSkills.put(146, 11999);
		_ChainedAirSkills.put(148, 10249);
		_ChainedAirSkills.put(149, 10249);
		_ChainedAirSkills.put(150, 10249);
		_ChainedAirSkills.put(151, 10249);
		_ChainedAirSkills.put(152, 10499);
		_ChainedAirSkills.put(153, 10499);
		_ChainedAirSkills.put(154, 10499);
		_ChainedAirSkills.put(155, 10499);
		_ChainedAirSkills.put(156, 10499);
		_ChainedAirSkills.put(157, 10499);
		_ChainedAirSkills.put(158, 10749);
		_ChainedAirSkills.put(159, 10749);
		_ChainedAirSkills.put(160, 10749);
		_ChainedAirSkills.put(161, 10749);
		_ChainedAirSkills.put(162, 10999);
		_ChainedAirSkills.put(163, 10999);
		_ChainedAirSkills.put(164, 10999);
		_ChainedAirSkills.put(165, 10999);
		_ChainedAirSkills.put(166, 11247);
		_ChainedAirSkills.put(167, 11247);
		_ChainedAirSkills.put(168, 11247);
		_ChainedAirSkills.put(169, 11247);
		_ChainedAirSkills.put(170, 11247);
		_ChainedAirSkills.put(171, 11749);
		_ChainedAirSkills.put(172, 11749);
		_ChainedAirSkills.put(173, 11749);
		_ChainedAirSkills.put(174, 11749);
		_ChainedAirSkills.put(175, 11749);
		_ChainedAirSkills.put(176, 11499);
		_ChainedAirSkills.put(177, 11499);
		_ChainedAirSkills.put(178, 11499);
		_ChainedAirSkills.put(179, 11999);
		_ChainedAirSkills.put(180, 11999);
		_ChainedAirSkills.put(181, 11999);
		_ChainedTemporalReplace.put(10249, 10008); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
		_ChainedTemporalReplace.put(10499, 10258); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
		_ChainedTemporalReplace.put(10749, 10508); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
		_ChainedTemporalReplace.put(10999, 10760); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
		_ChainedTemporalReplace.put(11247, 11017); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
		_ChainedTemporalReplace.put(11249, 11017); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
		_ChainedTemporalReplace.put(11749, 11509); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
		_ChainedTemporalReplace.put(11499, 11263); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
		_ChainedTemporalReplace.put(11999, 11814); // FIXME: Sometime causes: java.lang.ArrayIndexOutOfBoundsException
	}
	
	/**
	 * Method checkCondition.
	 * @return boolean
	 */
	@Override
	public boolean checkCondition()
	{
		if (_effected.isParalyzeImmune() || _effected.isAirBinded() || _effected.isKnockedDown())
		{
			return false;
		}
		
		return super.checkCondition();
	}
	
	/**
	 * Method onStart.
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		
		for (Player playerNearEffected : World.getAroundPlayers(_effected, 1200, 400))
		{
			if ((playerNearEffected.getTarget() == _effected) && playerNearEffected.isAwaking())
			{
				int chainSkill = _ChainedAirSkills.get(playerNearEffected.getClassId().getId());
				int temporalReplaceSkill = _ChainedTemporalReplace.get(chainSkill);
				playerNearEffected.sendPacket(new ExAlterSkillRequest(chainSkill, temporalReplaceSkill, 5));
			}
		}
		
		if (!_effected.isAirBinded())
		{
			_effected.startAirbinding();
		}
		
		_effected.abortAttack(true, true);
		_effected.abortCast(true, true);
		_effected.stopMove();
	}
	
	/**
	 * Method onExit.
	 */
	@Override
	public void onExit()
	{
		super.onExit();
		
		if (_effected.isAirBinded())
		{
			_effected.stopAirbinding();
		}
	}
	
	/**
	 * Method onActionTime.
	 * @return boolean
	 */
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
