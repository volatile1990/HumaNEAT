package de.examples.tictactoe;

import java.util.List;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.genome.Genome;

/**
 * @author MannoR
 *
 */
public class TicTacToePlayer extends ArtificialIntelligence {

	public String name;
	private int anzInputs;

	private double[] inputs;
	private int[] nextMarkCoordinates;

	public TicTacToe ticTacToe;
	public boolean failed;

	/**
	 * @param anzInputs
	 * @param anzOutputs
	 */
	public TicTacToePlayer(String name, int anzInputs, int anzOutputs) {

		this.brain = new Genome(anzInputs, anzOutputs);

		this.anzInputs = anzInputs;
		this.inputs = new double[this.anzInputs];
		this.nextMarkCoordinates = new int[2];

		this.failed = false;
	}

	/**
	 * @param brain
	 */
	public TicTacToePlayer(Genome brain) {
		this.brain = brain;

		this.inputs = new double[brain.anzInputs];
		this.anzInputs = brain.anzInputs;
		this.nextMarkCoordinates = new int[2];

		this.failed = false;
	}

	/**
	 * @param playfield
	 */
	@Override
	public void setInputs(List<Double> playfield) {

		for (int i = 0; i < this.anzInputs; ++i) {
			this.inputs[i] = playfield.get(i);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.core.ArtificialIntelligence#think()
	 */
	@Override
	public void think() {

		// Give the brain input to get decision
		double[] decision = this.brain.feedForward(this.inputs);

//		double inputSum = 0;
//		for (int i = 0; i < this.inputs.length; ++i) {
//			inputSum += this.inputs[i];
//		}
//		System.out.println("INPUTSUM: " + inputSum + "DECISION: " + decision[0]);

		if (decision[0] >= 0 && decision[0] < 0.1) {
			this.nextMarkCoordinates[0] = 0;
			this.nextMarkCoordinates[1] = 0;
		} else if (decision[0] >= 0 && decision[0] < 0.2) {
			this.nextMarkCoordinates[0] = 0;
			this.nextMarkCoordinates[1] = 1;
		} else if (decision[0] >= 0.2 && decision[0] < 0.3) {
			this.nextMarkCoordinates[0] = 0;
			this.nextMarkCoordinates[1] = 2;
		} else if (decision[0] >= 0.3 && decision[0] < 0.4) {
			this.nextMarkCoordinates[0] = 1;
			this.nextMarkCoordinates[1] = 0;
		} else if (decision[0] >= 0.4 && decision[0] < 0.5) {
			this.nextMarkCoordinates[0] = 1;
			this.nextMarkCoordinates[1] = 1;
		} else if (decision[0] >= 0.5 && decision[0] < 0.6) {
			this.nextMarkCoordinates[0] = 1;
			this.nextMarkCoordinates[1] = 2;
		} else if (decision[0] >= 0.6 && decision[0] < 0.7) {
			this.nextMarkCoordinates[0] = 2;
			this.nextMarkCoordinates[1] = 0;
		} else if (decision[0] >= 0.7 && decision[0] < 0.8) {
			this.nextMarkCoordinates[0] = 2;
			this.nextMarkCoordinates[1] = 1;
		} else if (decision[0] >= 0.8 && decision[0] <= 1) {
			this.nextMarkCoordinates[0] = 2;
			this.nextMarkCoordinates[1] = 2;
		}
	}

	/**
	 * @param ticTacToe
	 */
	public void takeAction(TicTacToe ticTacToe) {

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
