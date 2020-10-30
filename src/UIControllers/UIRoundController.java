package UIControllers;

import java.util.List;
import java.util.Observable;

import BattleComponents.Fight;
import BattleComponents.Round;
import Controllers.RoundController;
import Damage.Damage;
import Damage.DamageCalculator;
import PetConponents.Skills;
import PlayableComponents.Playable;
import PlayableComponents.PlayerInformation;
import UI.*;
import UI.view.impl.PlayerViewImpl;

@SuppressWarnings("deprecation")
public class UIRoundController extends Observable implements RoundController 
{
	private Round round;
	
	public UIRoundController(Fight fight, int roundCount)
	{
		this.round = new Round(fight, roundCount);
	}
	
	public Round getRound()
	{
		return round;
	}
	
	public DamageCalculator getDamageCalculator()
	{
		return (DamageCalculator) round.getFight().getBattle().getCalculator();
	}
	
	public List<Playable> getPlayerList()
	{
		return this.round.getFight().getBattle().getPlayerList();
	}
	
	@Override
	public void startRound()
	{
		setChanged();
		notifyObservers(round.getStartEvent());
		clearChanged();
	}

	@Override
	public boolean setPlayerSkill(Skills skill, int playerIndex)
	{
		if(getPlayerList().get(playerIndex).getPet().canUseSkill(skill))
		{
			getPlayerList().get(playerIndex).setSkill(skill);
			getPlayerList().get(playerIndex).getPet().chargeSkills();
			round.getFight().getBattle().getPlayerList().get(round.getAttackList().get(playerIndex).getPlayerIndex()).getPet().useSkill(skill);
			return true;
		}
		PlayerViewImpl.updateText("Skill is on cooldown. Please Try Again" + '\n');
		return false;	
	}

	@Override
	public void attack()
	{
		Damage damage;
		getDamageCalculator().setRoundController(this);
		
		for (int attackerIndex = 0; attackerIndex < round.getAttackList().size(); attackerIndex++)
		{
			int j = (attackerIndex + 1) % round.getAttackList().size();
			int victimIndex = round.getAttackList().get(j).getPlayerIndex();
			damage = calculateDamage(round.getAttackList().get(attackerIndex), round.getAttackList().get(j));
			getPlayerList().get(victimIndex).getPet().takeDamage(damage.getTotalDamage());
			
			round.getAttackList().get(victimIndex).updateTotalRandomDiff(-damage.getRandom());
			round.getAttackList().get(attackerIndex).updateTotalRandomDiff(damage.getRandom());
			String attackerPlayerName = round.getFight().getBattle().getPlayerList().get(round.getAttackList().get(attackerIndex).getPlayerIndex()).getPlayerName();
			String attackerChosenSkill = round.getFight().getBattle().getPlayerList().get(round.getAttackList().get(attackerIndex).getPlayerIndex()).getSkill().toString();
			String victimName = round.getFight().getBattle().getPlayerList().get(victimIndex).getPlayerName();
			PlayerViewImpl.updateText(attackerPlayerName + " has chosen " + attackerChosenSkill + " and has applied " + damage.getTotalDamage() + " to "  + victimName + "\n");
			
		}
	}

	@Override
	public Damage calculateDamage(PlayerInformation attacker, PlayerInformation victim)
	{
		return getDamageCalculator().calculteDamage(attacker, victim);
		
	}

	@Override
	public void setPlayerPredictedSkill(Skills skill, int playerIndextAttackList) {
		getPlayerList().get(round.getAttackList().get(playerIndextAttackList).getPlayerIndex()).setSkill(skill);
		
	}

}
