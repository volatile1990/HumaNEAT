package de.examples.tictactoe;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.genome.Genome;

/**
 * @author MannoR
 *
 */
public class TicTacToePlayer extends ArtificialIntelligence {

	public String name;
	private int[] nextMarkCoordinates;

	public TicTacToe ticTacToe;
	public boolean failed;

	/**
	 * @param anzInputs
	 * @param anzOutputs
	 */
	public TicTacToePlayer(String name, int anzInputs, int anzOutputs) {

		super(anzInputs, anzOutputs);

		this.brain = new Genome(anzInputs, anzOutputs);
		this.nextMarkCoordinates = new int[2];
		this.failed = false;
	}

	/**
	 * @param brain
	 */
	public TicTacToePlayer(Genome brain) {

		super(brain);

		this.nextMarkCoordinates = new int[2];
		this.failed = false;
	}

	/**
	 * @param ticTacToe
	 */
	public void takeAction(TicTacToe ticTacToe) {

		double[] lastOutput = this.outputs.get(this.outputs.size() - 1);

		if (lastOutput[0] >= 0 && lastOutput[0] < 0.1) {
			this.nextMarkCoordinates[0] = 0;
			this.nextMarkCoordinates[1] = 0;
		} else if (lastOutput[0] >= 0 && lastOutput[0] < 0.2) {
			this.nextMarkCoordinates[0] = 0;
			this.nextMarkCoordinates[1] = 1;
		} else if (lastOutput[0] >= 0.2 && lastOutput[0] < 0.3) {
			this.nextMarkCoordinates[0] = 0;
			this.nextMarkCoordinates[1] = 2;
		} else if (lastOutput[0] >= 0.3 && lastOutput[0] < 0.4) {
			this.nextMarkCoordinates[0] = 1;
			this.nextMarkCoordinates[1] = 0;
		} else if (lastOutput[0] >= 0.4 && lastOutput[0] < 0.5) {
			this.nextMarkCoordinates[0] = 1;
			this.nextMarkCoordinates[1] = 1;
		} else if (lastOutput[0] >= 0.5 && lastOutput[0] < 0.6) {
			this.nextMarkCoordinates[0] = 1;
			this.nextMarkCoordinates[1] = 2;
		} else if (lastOutput[0] >= 0.6 && lastOutput[0] < 0.7) {
			this.nextMarkCoordinates[0] = 2;
			this.nextMarkCoordinates[1] = 0;
		} else if (lastOutput[0] >= 0.7 && lastOutput[0] < 0.8) {
			this.nextMarkCoordinates[0] = 2;
			this.nextMarkCoordinates[1] = 1;
		} else if (lastOutput[0] >= 0.8 && lastOutput[0] <= 1) {
			this.nextMarkCoordinates[0] = 2;
			this.nextMarkCoordinates[1] = 2;
		}

		// Make move
		this.failed = !ticTacToe.setMark(this.nextMarkCoordinates[0], this.nextMarkCoordinates[1]);

		// Get updated game
		this.ticTacToe = ticTacToe;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.core.ArtificialIntelligence#calculateFitness()
	 */
	@Override
	public double calculateFitness() {

		int unadjustedFitness = 0;
		int[][] playfield = this.ticTacToe.playfield;
		for (int[] element : playfield) {
			for (int j = 0; j < playfield[0].length; ++j) {
				if (element[j] != 0) {
					unadjustedFitness += 1000;
				}
			}
		}

		if (this.failed) {
			unadjustedFitness -= 750;
		}

		if (unadjustedFitness < 0) {
			unadjustedFitness = 0;
		}

//		if (this.ticTacToe.playfield[2][2] != 0) {
//			unadjustedFitness += 10;
//		}

		return unadjustedFitness;
	}

	@Override
	public ArtificialIntelligence getNewInstance(Genome genome) {
		return new TicTacToePlayer(genome);
	}

}
