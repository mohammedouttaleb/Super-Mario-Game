package com.TETOSOFT.tilegame;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameEngineTest {

	@Test
	public void test() {
		System.out.println("test.................");
		GameEngine ge=new GameEngine();
		ge.setCreatureCoefficient(100);
		ge.setElapsedtime(10000);
		
		assertEquals(7,ge.UpdateScore(10));
		assertEquals(14,ge.UpdateScore(20));
	}

}
