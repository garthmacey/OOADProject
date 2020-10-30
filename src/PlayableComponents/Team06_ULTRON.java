
package PlayableComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import Events.*;
import PetConponents.Pet;
import PetConponents.PetTypes;
import PetConponents.Skills;
//import edu.dselent.player.PetInstance;
//import edu.dselent.settings.PlayerInfo;
//import edu.dselent.skill.Skills;

@SuppressWarnings("deprecation")
public class Team06_ULTRON extends Player
{
	private int roundID;
	private Playable opponent;
	private double randomDiff;
	private double currentHealth;
	private int playerIndex;
	private int opponentIndex;
	private PlayerEventInformation opponentInfo;
	private List<PlayerEventInformation> playerEventList;
	private List<Playable> playerList;

	public Team06_ULTRON(String name, Pet pet)
	{
		super(name, new Pet("Ultron", PetTypes.INTELLIGENCE, pet.getMaxHealth()), PlayerTypes.ULTRON);
		this.currentHealth = pet.getMaxHealth();
		this.playerType = PlayerTypes.ULTRON;
	}
	
	public void findUs(List<PlayerEventInformation> playerInfo)
	{
		int index = 0;
		for(PlayerEventInformation pInfo: playerInfo)
		{
			if(getPetName() == pInfo.getPetName())
				playerIndex = index;
			playerList.add(new AIPlayer(pInfo.getPetName(), new Pet(pInfo.getPetName(), pInfo.getPetType(), pInfo.getStartingHp())));
			index++;
		}
		
	}
	public int FindOpponent()
	{
		int opponentIndex = (playerIndex + 1) % playerList.size();
		while(playerIndex != opponentIndex)
		{
			if(playerList.get(opponentIndex).getCurrentHp() > 0)
			{
				opponent = playerList.get(opponentIndex);
				return opponentIndex;
			}
			opponentIndex = (opponentIndex + 1) % playerList.size();
		}
		return 0;
	}
	
	@Override
	public void update(Observable o, Object arg)
	{
		if (arg instanceof AttackEvent) 
		{
			AttackEvent aE = ((AttackEvent) arg);
			playerList.get(aE.getVictimPlayableIndex()).updateHp(aE.getRandomDamage() + aE.getConditionalDamage());
			if(aE.getVictimPlayableIndex() == playerIndex)
				randomDiff += -(aE.getRandomDamage());
			if(aE.getAttackingPlayableIndex() == playerIndex)
				randomDiff += aE.getRandomDamage();
			playerList.get(aE.getAttackingPlayableIndex()).getPet().useSkill(aE.getAttackingSkillChoice());
			playerList.get(aE.getAttackingPlayableIndex()).setPredictedSkill(aE.getPredictedSkillEnum());
			
			
		}
		if(arg instanceof RoundStartEvent)  
		{
			roundID = ((RoundStartEvent) arg).getRoundNumber();
			opponentIndex = FindOpponent();
		}
		if (arg instanceof FightStartEvent) 
		{	
			playerList = new ArrayList<>();
			playerEventList = ((FightStartEvent) arg).getPlayerEventInformation();
			findUs(playerEventList);
			opponentIndex = FindOpponent();
		}
	}
	
	
	@Override
	public Skills chooseSkill()
	{
		Skills chosenSkill;
		if(roundID == 0)
		{
			chosenSkill = getBestFirstMove();
		}
		else if(opponent.getPetType() == PetTypes.INTELLIGENCE)
			chosenSkill = getBestSkillAgainstIntelligence();
		else if(opponent.getPetType() == PetTypes.POWER)
			chosenSkill = getBestSkillAgainstPower();
		else if(opponent.getPetType() == PetTypes.SPEED)
			chosenSkill = getBestSkillAgainstSpeed();
		else
			chosenSkill = getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT, Skills.SCISSORS_POKE);
		if(chosenSkill == Skills.SHOOT_THE_MOON)
			setSkillPredeiction();
		setSkill(chosenSkill);
		while(!getPet().canUseSkill(chosenSkill))
			chosenSkill = getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT, Skills.SCISSORS_POKE);
		return chosenSkill;
		
	}
	

	@Override
	public Skills setSkillPredeiction() 
	{
		Skills predicted = getBestPredictedSkill();
		setPredictedSkill(predicted);
		return this.predictedSkill = predicted;
		
	}
	
	private Skills getBestPredictedSkill() {
		PetTypes opponentType = opponent.getPetType();
		switch(opponentType)
		{
		case POWER:
			return Skills.SHOOT_THE_MOON;
		case INTELLIGENCE:
			return getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT, Skills.SCISSORS_POKE);
		case SPEED:
			return Skills.ROCK_THROW;
		}
		return getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT, Skills.SCISSORS_POKE);
	}

	public Skills getPredictedSkill()
	{
		return this.predictedSkill;
	}

	@Override
	public void setSkill(Skills skill) {
		this.choosenSkill = skill;
		
	}

	@Override
	public Skills getSkill() {
		return this.choosenSkill;
	}
	
	private Skills chooseCoreDamageSkillPower()
	{
		Skills bestSkill = null;
		while(bestSkill == null)
		{
			if (opponent.getPet().getSkill(Skills.ROCK_THROW).isCharging())
				bestSkill = getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT);
			else if (opponent.getPet().getSkill(Skills.PAPER_CUT).isCharging())
				bestSkill = getRandomSkill(Skills.PAPER_CUT, Skills.SCISSORS_POKE);
			else if (opponent.getPet().getSkill(Skills.SCISSORS_POKE).isCharging())
				bestSkill = getRandomSkill(Skills.SCISSORS_POKE, Skills.ROCK_THROW);
			else
				bestSkill = getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT, Skills.SCISSORS_POKE);
		}
		return bestSkill;
	}
	
	/**
	 * 
	 * @return
	 */
	private Skills chooseCoreDamageSkillIntelligence()
	{
		Skills bestSkill = null;
		if (opponent.getPet().getSkill(Skills.ROCK_THROW).isCharging() && getPet().canUseSkill(Skills.PAPER_CUT))
			bestSkill = Skills.PAPER_CUT;
		else if(opponent.getPet().getSkill(Skills.PAPER_CUT).isCharging() && getPet().canUseSkill(Skills.SCISSORS_POKE))
			bestSkill = Skills.SCISSORS_POKE;
		else if(opponent.getPet().getSkill(Skills.SCISSORS_POKE).isCharging() && getPet().canUseSkill(Skills.ROCK_THROW))
			bestSkill = Skills.ROCK_THROW;
		else
			bestSkill = getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT, Skills.SCISSORS_POKE);
		return bestSkill;
	}
	
	/**
	 * 
	 * @return
	 */
	private Skills chooseCoreDamageSkillIntelligence_2()
	{
		Skills bestSkill = null;
		if (opponent.getPet().getSkill(Skills.ROCK_THROW).isCharging())
		{
			if(getPet().canUseSkill(Skills.PAPER_CUT))
				bestSkill = Skills.PAPER_CUT;
			else
				bestSkill = Skills.ROCK_THROW;
		}
		else if(opponent.getPet().getSkill(Skills.PAPER_CUT).isCharging())
		{
			if(getPet().canUseSkill(Skills.SCISSORS_POKE))
				bestSkill = Skills.SCISSORS_POKE;
			else
				bestSkill = Skills.PAPER_CUT;
		}
		else if(opponent.getPet().getSkill(Skills.SCISSORS_POKE).isCharging())
		{
			if(getPet().canUseSkill(Skills.ROCK_THROW))
				bestSkill = Skills.ROCK_THROW;
			else
				bestSkill = Skills.SCISSORS_POKE;
		}
		else
			bestSkill = getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT, Skills.SCISSORS_POKE);
		return bestSkill;
	}
	
	public Skills getRandomSkill(Skills skill1, Skills skill2) {
		Random random = new Random();
		int min = 1, max = 2;
		int skillValue =  (random.nextInt((max - min) + 1) + min);
		return skillValue == 1 ? skill1 : skill2;
	}
	
	public Skills getRandomSkill(Skills skill1, Skills skill2, Skills skill3) {
		Random random = new Random();
		int min = 1, max = 3;
		int skillValue = (random.nextInt((max - min) + 1) + min);
		switch (skillValue) {
		case 1:
			return skill1;
		case 2:
			return skill2;
		case 3:
			return skill3;
		}
		return skill1;
	}
	
	public Skills getBestSkillAgainstPower()
	{
		if(randomDiff > opponent.getCurrentHp() * 0.25)
			return Skills.REVERSAL_OF_FORTUNE;
		else if(opponent.getPet().getSkill(Skills.SHOOT_THE_MOON).getCurrentRechargeTime() == 6 && getPet().getSkill(Skills.SHOOT_THE_MOON).getCurrentRechargeTime() == 0)
		{
			return Skills.SHOOT_THE_MOON;
		}
		else
		{
			return chooseCoreDamageSkillPower();
		}
	}
	
	public Skills getBestSkillAgainstIntelligence()
	{
		if(randomDiff> opponent.getCurrentHp() * 0.25)
			return Skills.REVERSAL_OF_FORTUNE;
		else 
			return chooseCoreDamageSkillIntelligence_2();
		
	}
	
	public Skills getBestFirstMove()
	{
		PetTypes opponentType = opponent.getPetType();
		switch(opponentType)
		{
		case POWER:
			if(getPet().canUseSkill(Skills.SHOOT_THE_MOON))
				return Skills.SHOOT_THE_MOON;
			break;
		case INTELLIGENCE:
			if(getPet().canUseSkill(Skills.REVERSAL_OF_FORTUNE))
				return Skills.REVERSAL_OF_FORTUNE;
		case SPEED:
			if(getPet().canUseSkill(Skills.SHOOT_THE_MOON))
				return Skills.SHOOT_THE_MOON;
		}
		return getRandomSkill(Skills.ROCK_THROW, Skills.PAPER_CUT, Skills.SCISSORS_POKE);
	}

	
	public Skills getBestSkillAgainstSpeed()
	{
//		double percentage = getPet().getHealth()/getPet().getMaxHealth();
//		if(percentage >= .75 && getPet().canUseSkill(Skills.ROCK_THROW))
//		{
//			return Skills.ROCK_THROW;
//		}
//		else if((percentage >= .25 && percentage < .75) && getPet().canUseSkill(Skills.SCISSORS_POKE))
//		{
//			return Skills.SCISSORS_POKE;
//		}
//		else if(percentage < .25 && getPet().canUseSkill(Skills.PAPER_CUT))
//		{
//			return Skills.PAPER_CUT;
//		}
//		else
//			return chooseCoreDamageSkillIntelligence();

		
		Skills returnableSkill = null;
		double ultronsHP = getCurrentHp();
		if(randomDiff > opponent.getCurrentHp() * 0.25)
		{
			if(getPet().canUseSkill(Skills.REVERSAL_OF_FORTUNE))
			{
				returnableSkill = Skills.REVERSAL_OF_FORTUNE;
			}

		}
		else
		{
			if(opponent.getPet().getSkill(Skills.ROCK_THROW).getCurrentRechargeTime() > 0)
			{
				if(ultronsHP>=75)
				{
					if(getPet().canUseSkill(Skills.PAPER_CUT)) 
					{
						returnableSkill =  Skills.PAPER_CUT;
					}
					else
						returnableSkill = Skills.ROCK_THROW;
				}
				else if (ultronsHP < 25)
				{
					if(getPet().canUseSkill(Skills.PAPER_CUT))
					{
						returnableSkill = Skills.PAPER_CUT;
					}
					else
						returnableSkill = Skills.SCISSORS_POKE;
				}
				else
				{
					if(getPet().canUseSkill(Skills.SCISSORS_POKE))
					{
						returnableSkill = Skills.SCISSORS_POKE;
					}
					else
						returnableSkill = Skills.PAPER_CUT;
				}
			}
			else if(opponent.getPet().getSkill(Skills.SCISSORS_POKE).getCurrentRechargeTime() > 0)
			{
				if(ultronsHP < 75 && ultronsHP >= 25)
				{
					if(getPet().canUseSkill(Skills.ROCK_THROW))
					{
						returnableSkill = Skills.ROCK_THROW;
					}
					else
						returnableSkill = Skills.SCISSORS_POKE;
				}
				else if (ultronsHP >= 75)
				{
					if(getPet().canUseSkill(Skills.ROCK_THROW))
					{
						returnableSkill = Skills.ROCK_THROW;
					}
					else 
						returnableSkill = Skills.PAPER_CUT;
				}
				else
				{
					if(getPet().canUseSkill(Skills.PAPER_CUT))
					{
						returnableSkill = Skills.PAPER_CUT;
					}
					else 
						returnableSkill = Skills.ROCK_THROW;
				}
			}
			else if(opponent.getPet().getSkill(Skills.PAPER_CUT).getCurrentRechargeTime() > 0)
			{
				if(ultronsHP < 25)
				{
					if(getPet().canUseSkill(Skills.SCISSORS_POKE))
					{
						returnableSkill = Skills.SCISSORS_POKE;
					}
					else
					{
						returnableSkill = Skills.PAPER_CUT;
					}
				}
				else if (ultronsHP < 75 && ultronsHP >= 25)
				{
					if(getPet().canUseSkill(Skills.SCISSORS_POKE))
					{
						returnableSkill = Skills.SCISSORS_POKE;
					}
					else
						returnableSkill = Skills.ROCK_THROW;
				}
				else
				{
					if(getPet().canUseSkill(Skills.ROCK_THROW))
					{
						returnableSkill = Skills.ROCK_THROW;
					}
					else
						returnableSkill = Skills.PAPER_CUT;
				}
			}
		}
		if(returnableSkill == null)
		{
			if(getPet().canUseSkill(Skills.SHOOT_THE_MOON))
			{
				returnableSkill = Skills.SHOOT_THE_MOON;
			}
			else
			{
				if(getPet().canUseSkill(Skills.SCISSORS_POKE))
				{
					returnableSkill = Skills.SCISSORS_POKE;
				}
				else
					returnableSkill = Skills.ROCK_THROW;
				
			}
		}
		return returnableSkill;
	}
	
}