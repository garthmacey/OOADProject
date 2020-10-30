package Controllers;

import java.util.List;

import Damage.Damage;
import PetConponents.Skills;
import PlayableComponents.Playable;
import PlayableComponents.PlayerInformation;

public interface RoundController
{
	public void attack();
	public Damage calculateDamage(PlayerInformation attacker, PlayerInformation victim);
	public List<Playable> getPlayerList();
	public boolean setPlayerSkill(Skills skill, int playerIndex);
	public void setPlayerPredictedSkill(Skills skill, int playerIndextAttackList);
	public void startRound();
	//hello
}
