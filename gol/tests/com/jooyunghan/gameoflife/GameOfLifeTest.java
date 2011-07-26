package com.jooyunghan.gameoflife;

import junit.framework.TestCase;

public class GameOfLifeTest extends TestCase {
	public void testWorld() throws Exception {
		new World(10, 10);
		assertEquals(10, new World().width());
		assertEquals(10, new World().height());
	}

	public void testSetAlive() throws Exception {
		World w = new World();

		w.set(0, 0, Cell.ALIVE);
		assertEquals(Cell.ALIVE, w.get(0, 0));
	}

	public void testNewBornIfThreeNeighbors() {
		assertEquals(Cell.ALIVE, Cell.DEAD.next(3));
		assertEquals(Cell.DEAD, Cell.DEAD.next(2));
		assertEquals(Cell.DEAD, Cell.DEAD.next(4));
	}

	public void testLiveOnIfModerateNeighbors() {
		assertEquals(Cell.DEAD, Cell.ALIVE.next(1));
		assertEquals(Cell.ALIVE, Cell.ALIVE.next(2));
		assertEquals(Cell.ALIVE, Cell.ALIVE.next(3));
		assertEquals(Cell.DEAD, Cell.ALIVE.next(4));
	}

	public void testCountNeighbors() throws Exception {
		World w = new World();
		w.set(0, 0, Cell.ALIVE);
		w.set(1, 0, Cell.ALIVE);
		w.set(0, 1, Cell.ALIVE);

		assertEquals(3, w.countNeighbors(1, 1));
	}

	public void testNextWorld() {
		World w = new World();
		w.set(0, 0, Cell.ALIVE);
		w.set(1, 0, Cell.ALIVE);
		w.set(0, 1, Cell.ALIVE);

		World next = new World();
		next.set(0, 0, Cell.ALIVE);
		next.set(1, 0, Cell.ALIVE);
		next.set(0, 1, Cell.ALIVE);
		next.set(1, 1, Cell.ALIVE);

		assertEquals(next, w.next().toString());
	}
	
	public void testRestore() throws Exception {
		World w = new World();
		w.shake();
		
		World w2 = new World();
		w2.setCells(w.toString());
		
		assertEquals(w.toString(), w2.toString());
	}

	public void testShakeRandomizeTheWorld_Make30PercentOfAliveCells() {
		World w = new World(10, 10);
		int count = 0;
		for (int i=0; i<100; i++) {
			w.shake();
			count += countAlive(w);
		}	
		assertTrue( count > 2500 && count < 3500 );
	}

	private int countAlive(World w) {
		int count = 0;
		for (int i=0; i<w.width(); i++)
			for (int j=0; j<w.height(); j++)
				if (w.get(i, j) == Cell.ALIVE)
					count++;
		return count;
	}
}
