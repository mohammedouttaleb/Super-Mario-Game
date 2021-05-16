package com.TETOSOFT.UnitTests;

import com.TETOSOFT.test.GameCore;
import com.TETOSOFT.tilegame.GameEngine;

import junit.framework.TestCase;

public class GameEngineTests extends TestCase {

	
	public void testUpdateScore() {
		
		GameEngine ge=new GameEngine();
		ge.setCreatureCoefficient(100);
		ge.setElapsedtime(10000);
		
		assertEquals(7,ge.UpdateScore(10));
		assertEquals(14,ge.UpdateScore(20));
	}

	
	public void testUpdateHighScoreList() {
		    
		    GameEngine ge=new GameEngine();
			boolean isAhighScore=ge.UpdateHighScoreList(2);
			boolean isAhighScore2=ge.UpdateHighScoreList(20);
			assertEquals(false,isAhighScore);
			assertEquals(true,isAhighScore2);
			

	}
	
	public void testscorexists() {
		
		GameEngine ge=new GameEngine();
		 
		 ge.UpdateHighScoreList(0);
		
		 boolean exists=ge.scorexists(21);
		 boolean exists2=ge.scorexists(4);
		 boolean exists3=ge.scorexists(6);
		 
		 assertEquals(false, exists);
		 assertEquals(true, exists2);
		 assertEquals(true, exists3);
		
	}

}
