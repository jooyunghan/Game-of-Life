package com.jooyunghan.gameoflife;

public enum Cell {
	DEAD {
		public Cell next(int neighbors) {
			return (neighbors == 3) ? ALIVE : DEAD;
		}
	},
	ALIVE {
		public Cell next(int neighbors) {
			return (neighbors >= 2 && neighbors <= 3) ? ALIVE : DEAD;
		}
	};

	abstract public Cell next(int neighbors);
}
