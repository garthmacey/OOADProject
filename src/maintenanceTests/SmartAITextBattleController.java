package maintenanceTests;

import java.util.List;

import BattleComponents.GameSettings;
import PlayableComponents.PlayerInformation;
import TextControllers.TextBattleController;
import TextControllers.TextFightController;

public class SmartAITextBattleController extends TextBattleController{

	public SmartAITextBattleController(GameSettings gameSettings) {
		super(gameSettings);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run()
	{
		for (int i = 0; i < battle.getNumberOfFights(); i++)
		{
			battle.getFightList().add(new TextFightController(battle, i));
			battle.getFightList().get(i).startFight(i);
		}
		
		List<PlayerInformation> winnerList = determineWinners();
		printPlayerWins();
		
		if(winnerList.size() == 1)
		{
			System.out.print("The winner is: ");
			System.out.print(battle.getPlayerList().get(winnerList.get(0).getPlayerIndex()).getPlayerName());
		}
		else
		{
			System.out.print("The winners are: ");
			for (int i = 0; i < winnerList.size(); i++)
			{
				System.out.print(battle.getPlayerList().get(winnerList.get(i).getPlayerIndex()).getPlayerName());
				if(i + 1 < winnerList.size())
					System.out.print(", ");
			}
		}
	}
	
	public int getSmartAIWins()	{
		for(int i = 0; i < battle.getPlayerInformationList().size(); i++)
		{
			if(battle.getPlayerInformationList().get(i).getPlayerIndex() == 0)
				return battle.getPlayerInformationList().get(i).getNumberOfWins();
		}
		
		return 0;
	}

}
