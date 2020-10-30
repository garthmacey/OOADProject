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
import PlayableComponents.Team06_ULTRON;

public class SmartAIPreBattleSetup extends PreBattleSetup{

	private final double STARTING_HP = 100;
	
	public SmartAIPreBattleSetup(IOManager ioManager) {
		super(ioManager);
	}
	
	public GameSettings runSmartAI(PetTypes type)
	{
		GameSettings tempgameSettings = new GameSettings();

		List<Playable> playerList = new ArrayList<>();
		long randomNumber = 1;
		int numFight = 1000;
		
		playerList.add(new Team06_ULTRON("Ultron", new Pet("Ultron", PetTypes.INTELLIGENCE, STARTING_HP)));
		playerList.add(new AIPlayer("AIPlayer " + type.toString(), new Pet("AIPlayer", type, STARTING_HP)));

		tempgameSettings.setSettings(playerList, numFight, randomNumber);
		return tempgameSettings;
	}

}
