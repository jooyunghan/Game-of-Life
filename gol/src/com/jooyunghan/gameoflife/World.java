package com.jooyunghan.gameoflife;

import java.util.Random;

public class World {

	private int width;
	private int height;
	private Cell cells[][];

	public World() {
		this(10, 10);
	}

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.cells = new Cell[width][height];
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				cells[x][y] = Cell.DEAD;
	}

	public int width() {
		return this.width;
	}

	public int height() {
		return this.height;
	}

	public void set(int x, int y, Cell cell) {
		if (x<0 || y<0 || x>=width || y>=height)
			return;
		cells[x][y] = cell;
	}

	public Cell get(int x, int y) {
		return cells[x][y];
	}

	public World next() {
		World next = new World(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int neighbors = countNeighbors(x, y);
				next.set(x, y, get(x, y).next(neighbors));
			}
		}

		return next;
	}

	public int countNeighbors(int my_x, int my_y) {
		int count = 0;
		int xbegin = Math.max(my_x - 1, 0);
		int ybegin = Math.max(my_y - 1, 0);
		int xend = Math.min(my_x + 2, width);
		int yend = Math.min(my_y + 2, height);
		for (int x = xbegin; x < xend; x++) {
			for (int y = ybegin; y < yend; y++) {
				if (x == my_x && y == my_y)
					continue;
				if (get(x, y) == Cell.ALIVE)
					count++;
			}
		}
		return count;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sb.append(get(x, y) == Cell.ALIVE ? "*" : " ");
			}
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public void shake() {
		 Random r = new Random();
		 int number = (int) (width*height*0.3);
		 for (int i=0; i<number; i++) {
			 int x = r.nextInt(width);
			 int y = r.nextInt(height);
			 set(x, y, Cell.ALIVE);
		 }
	}

	public void setCells(String cells) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				set(x, y, (cells.charAt(y*width+x) == '*') ? Cell.ALIVE : Cell.DEAD);
			}
		}
	}
}
