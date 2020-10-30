package maintenanceTests;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import BattleComponents.GameSettings;
import BattleComponents.PreBattleSetup;
import InputOutput.DefaultInputGetter;
import InputOutput.DefaultOutputSender;
import InputOutput.IOManager;
import InputOutput.Inputtable;
import InputOutput.Outputtable;
import PetConponents.PetTypes;
import TextControllers.TextBattleController;
import javafx.stage.Stage;

public class AITests 
{
	private Stage primaryStage;
	private final double ACCEPTABLE_WIN_PERCENTAGE = .9;
	private final double TOTAL_FIGHTS = 1000;

	@Test
	public void AIAutoGenerated()
	{
		double numWins = 0;
      
      	Inputtable inputGetter = new DefaultInputGetter();
      	Outputtable outputSender = new DefaultOutputSender();
      	GameSettings gameSettings = new GameSettings();
      	IOManager ioManager = new IOManager(inputGetter, outputSender);
      	AutoAITextBattleController battleController;
      	AutoAIPreBattleSetup pbs = new AutoAIPreBattleSetup(ioManager);
      	
      	gameSettings = pbs.runAutoAI(PetTypes.INTELLIGENCE);
      	battleController = new AutoAITextBattleController(gameSettings);
      	battleController.startBattle();
      	numWins += battleController.getAutoAI1Wins();
      	
      	gameSettings = pbs.runAutoAI(PetTypes.POWER);
      	battleController = new AutoAITextBattleController(gameSettings);
      	battleController.startBattle();
      	numWins += battleController.getAutoAI1Wins();
      
     	gameSettings = pbs.runAutoAI(PetTypes.SPEED);
      	battleController = new AutoAITextBattleController(gameSettings);
      	battleController.startBattle();
      	numWins += battleController.getAutoAI1Wins();
   
      	assertTrue(numWins > 0);
	}
  	
	/**
	 * If an error is throw within the program, then the test will fail.
	 * If there are no errors within the program, then the test will pass.
	 */
	@Test
	public void SmartAIBattle()
	{
		try
		{
			double numWins = 0;
			
			Inputtable inputGetter = new DefaultInputGetter();
			Outputtable outputSender = new DefaultOutputSender();
			GameSettings gameSettings = new GameSettings();
			IOManager ioManager = new IOManager(inputGetter, outputSender);
			SmartAITextBattleController battleController;
			SmartAIPreBattleSetup pbs = new SmartAIPreBattleSetup(ioManager);
			
			gameSettings = pbs.runSmartAI(PetTypes.POWER);
			battleController = new SmartAITextBattleController(gameSettings);
			battleController.startBattle();
			numWins += battleController.getSmartAIWins();
			assertTrue(true);
		}
		catch(Exception e)
		{
			assertTrue(false);
		}
	}
}

