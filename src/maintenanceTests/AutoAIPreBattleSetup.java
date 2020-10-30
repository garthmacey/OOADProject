package maintenanceTests;

import java.util.ArrayList;
import java.util.List;

import BattleComponents.GameSettings;
import BattleComponents.PreBattleSetup;
import InputOutput.IOManager;
import PetConponents.Pet;
import PetConponents.PetTypes;
import PlayableComponents.AIPlayer;
import PlayableComponents.Playable;
import PlayableComponents.PlayerTypes;

public class AutoAIPreBattleSetup extends PreBattleSetup
{
	private final double STARTING_HP = 100;
	public AutoAIPreBattleSetup(IOManager ioManager) 
	{
		super(ioManager);
	}
	
	public GameSettings runAutoAI(PetTypes type)
	{
		GameSettings tempgameSettings = new GameSettings();
		
		List<Playable> playerList = new ArrayList<>();
		long randomNumber = 1;
		int numFight = 1000;
		playerList.add(new AIPlayer("Random1 " + type.toString(), new Pet("Random2Pet ", type, STARTING_HP)));
		playerList.add(new AIPlayer("Random2 " + type.toString(), new Pet("Random2Pet ", type, STARTING_HP)));
		tempgameSettings.setSettings(playerList, numFight, randomNumber);
		return tempgameSettings;
	}
}