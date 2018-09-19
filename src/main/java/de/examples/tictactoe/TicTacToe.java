package de.examples.tictactoe;

import java.util.ArrayList;
import java.util.List;

import de.GenomePrinter;
import de.core.ArtificialIntelligence;
import de.core.population.Population;

/**
 * @author muellermak
 *
 */
public class TicTacToe {

	// 0: Not set, 1: X, 2: O
	public int[][] playfield;

	private static final int WIDTH = 3;
	private static final int HEIGHT = 3;

	private TicTacToePlayer artificialIntelligence;

	// 0: game running, 1: player one won, 2: player two won, 3: no one won
	private int gameState;

	public static void main(String[] args) {

		int populationSize = 1000;
		int inputs = 9;
		int outputs = 1;

		List<ArtificialIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < populationSize; ++i) {
			ais.add(new TicTacToePlayer("Player_" + i, inputs, outputs));
		}

		// Setup Population for first player
		Population population = new Population(ais);

		TicTacToePlayer winner = null;

		int generations = 100;
		for (int i = 0; i < generations; ++i) {

			// Let every player play the game until it is finished
			for (ArtificialIntelligence artificialPlayer : population.artificialIntelligences) {

				// Runs until the game is finished
				TicTacToe ticTacToe = new TicTacToe((TicTacToePlayer) artificialPlayer);
				ticTacToe.run(false);

				if (ticTacToe.gameState == 5) {

					// AI did it
					winner = (TicTacToePlayer) artificialPlayer;
				}
			}

			if (winner != null) {
				break;
			}

			// Cultivate a new generation in the population
			population.evolve();

			System.out.println("Generation: " + (i + 1) + " ## Fitness: " + population.getFittestAI().brain.fitness + " ## Species: " + population.getSpecies().size());
		}

		if (winner != null) {
			TicTacToe ttt = new TicTacToe(winner);
			ttt.run(true);
			GenomePrinter.printGenome(winner.brain, "D:/output/TicTacToe/Winner.png");
		}
	}

	/**
	 * @param human
	 */
	public TicTacToe(TicTacToePlayer artificialPlayer) {
		this.artificialIntelligence = artificialPlayer;
		this.resetGame();
		this.artificialIntelligence.ticTacToe = this;
	}

	/**
	 *
	 */
	private void run(boolean showGame) {

		while (this.gameState == 0) {

			this.artificialIntelligence.setInputs(this.normalizePlayfield());
			this.artificialIntelligence.think();
			this.artificialIntelligence.takeAction(this);

			if (showGame) {
				this.printPlayfield();
			}

			this.interpretGameState();
		}
	}

	/**
	 * Converts the playfield into a ordered list of data
	 * This is the normalized format for the neural net
	 *
	 * @return
	 */
	private List<Float> normalizePlayfield() {

		List<Float> normalized = new ArrayList<>();

		normalized.add(Float.valueOf(this.playfield[0][0]));
		normalized.add(Float.valueOf(this.playfield[0][1]));
		normalized.add(Float.valueOf(this.playfield[0][2]));
		normalized.add(Float.valueOf(this.playfield[1][0]));
		normalized.add(Float.valueOf(this.playfield[1][1]));
		normalized.add(Float.valueOf(this.playfield[1][2]));
		normalized.add(Float.valueOf(this.playfield[2][0]));
		normalized.add(Float.valueOf(this.playfield[2][1]));
		normalized.add(Float.valueOf(this.playfield[2][2]));

		return normalized;
	}

	/**
	 *
	 */
	private void resetGame() {

		// Initialize playfield
		this.playfield = new int[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				this.playfield[i][j] = 0;
			}
		}

		this.gameState = 0;
	}

	/**
	 *
	 */
	private void interpretGameState() {

		List<List<Integer>> allLines = this.getAllLines();

//		if (this.lineWon(allLines)) {
//			this.printPlayfield();
//			this.gameState = 3;
//		}

		if (this.fieldIsFull()) {
			this.gameState = 5;
			System.out.println("DRAW");
		}
	}

	/**
	 * @return
	 */
	private List<List<Integer>> getAllLines() {
		List<List<Integer>> allLines = new ArrayList<>();

		// Horizontal check
		List<Integer> line = null;
		for (int j = 0; j < HEIGHT; ++j) {

			line = new ArrayList<>();
			for (int i = 0; i < WIDTH; ++i) {
				line.add(this.playfield[i][j]);
			}

			allLines.add(line);
		}

		// Vertical check
		for (int j = 0; j < HEIGHT; ++j) {

			line = new ArrayList<>();
			for (int i = 0; i < WIDTH; ++i) {
				line.add(this.playfield[j][i]);
			}

			allLines.add(line);
		}

		// Diagonal check
		line = new ArrayList<>();
		line.add(this.playfield[0][0]);
		line.add(this.playfield[1][1]);
		line.add(this.playfield[2][2]);
		allLines.add(line);

		line = new ArrayList<>();
		line.add(this.playfield[0][2]);
		line.add(this.playfield[1][1]);
		line.add(this.playfield[2][0]);
		allLines.add(line);
		return allLines;
	}

	/**
	 * @return
	 */
	private boolean lineWon(List<List<Integer>> lines) {

		for (List<Integer> line : lines) {

			int lastMark = 0;
			int anzInRow = 1;
			for (Integer currentMark : line) {

				if (lastMark == currentMark && currentMark != 0) {
					++anzInRow;
				} else {
					anzInRow = 1;
				}

				if (anzInRow >= 3) {
					return true;
				}

				lastMark = currentMark;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	private boolean fieldIsFull() {

		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				if (this.playfield[i][j] == 0) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean setMark(int x, int y) {

		if (this.playfield[x][y] == 0) {
			this.playfield[x][y] = 1;
			return true;
		}

		this.gameState = 3;
		return false;

//			System.out.println(winningPlayer.name + " HAS WON");
	}

	/**
	 * @param playerOne
	 * @return
	 */
//	private int getTotalScore(TTTPlayer player) {
//
//		int totalScore = 0;
//		int mark = player.mark;
//		List<List<Integer>> allLines = this.getAllLines();
//
//		for (List<Integer> line : allLines) {
//			for (Integer field : line) {
//				if (field.equals(mark)) {
//					totalScore += 5;
//				}
//			}
//		}
//
//		return totalScore;
//	}

	/**
	 * @param playerOne
	 * @return
	 */
//	private int getMarksInRow(TicTacToePlayer player) {
//
//		int score = 0;
//		int mark = player.mark;
//		List<List<Integer>> allLines = this.getAllLines();
//
//		for (List<Integer> line : allLines) {
//
//			int inRow = 0;
//			for (Integer field : line) {
//
//				if (field.equals(mark)) {
//					++inRow;
//				}
//			}
//
//			score += inRow * 10;
//		}
//
//		return score;
//	}

	/**
	 *
	 */
	public void printPlayfield() {

		System.out.println("------------------------------");

		for (int i = 0; i < HEIGHT; ++i) {
			for (int j = 0; j < WIDTH; ++j) {

				if (this.playfield[i][j] == 1) {
					System.out.print("X");
				} else if (this.playfield[i][j] == 2) {
					System.out.print("O");
				} else {
					System.out.print(" ");
				}

				if (j + 1 < WIDTH) {
					System.out.print(" | ");
				}
			}
			System.out.println();
			System.out.print("__ __ __");
			System.out.println();
		}
		System.out.println("------------------------------");
	}
}
