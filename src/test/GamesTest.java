package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.TETOSOFT.tilegame.GameEngine;

public class GamesTest {

	
	
	@Test
public void testUpdateScore() {
		System.out.println("Test1...............");
		GameEngine ge=new GameEngine();
		ge.setCreatureCoefficient(100);
		ge.setElapsedtime(10000);
		
		assertEquals(7,ge.UpdateScore(10));
		assertEquals(14,ge.UpdateScore(20));
	}

	@Test
	public void testUpdateHighScoreList() {
		System.out.println("Test2...............");
		    GameEngine ge=new GameEngine();
			boolean isAhighScore=ge.UpdateHighScoreList(2);
			boolean isAhighScore2=ge.UpdateHighScoreList(20);
			assertEquals(false,isAhighScore);
			assertEquals(false,isAhighScore2);
			

	}
/*	@Test
	public void testscorexists() {
		
		System.out.println("Test3...............");
		GameEngine ge=new GameEngine();
		 
		 ge.UpdateHighScoreList(0);
		
		 boolean exists=ge.scorexists(21);
		 boolean exists2=ge.scorexists(4);
		 boolean exists3=ge.scorexists(6);
		 
		 assertEquals(false, exists);
		 assertEquals(false, exists2);
		 assertEquals(false, exists3);
		
	}*/
}
