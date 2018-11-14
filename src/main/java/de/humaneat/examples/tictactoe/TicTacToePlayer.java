package de.humaneat.examples.tictactoe;

import java.util.List;

import de.humaneat.core.neat.ArtificialIntelligence;

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
	 * @param ticTacToe
	 */
	public void takeAction(TicTacToe ticTacToe) {

		double[] lastOutput = outputs.get(outputs.size() - 1);

		if (lastOutput[0] >= 0 && lastOutput[0] < 0.1) {
			nextMarkCoordinates[0] = 0;
			nextMarkCoordinates[1] = 0;
		} else if (lastOutput[0] >= 0 && lastOutput[0] < 0.2) {
			nextMarkCoordinates[0] = 0;
			nextMarkCoordinates[1] = 1;
		} else if (lastOutput[0] >= 0.2 && lastOutput[0] < 0.3) {
			nextMarkCoordinates[0] = 0;
			nextMarkCoordinates[1] = 2;
		} else if (lastOutput[0] >= 0.3 && lastOutput[0] < 0.4) {
			nextMarkCoordinates[0] = 1;
			nextMarkCoordinates[1] = 0;
		} else if (lastOutput[0] >= 0.4 && lastOutput[0] < 0.5) {
			nextMarkCoordinates[0] = 1;
			nextMarkCoordinates[1] = 1;
		} else if (lastOutput[0] >= 0.5 && lastOutput[0] < 0.6) {
			nextMarkCoordinates[0] = 1;
			nextMarkCoordinates[1] = 2;
		} else if (lastOutput[0] >= 0.6 && lastOutput[0] < 0.7) {
			nextMarkCoordinates[0] = 2;
			nextMarkCoordinates[1] = 0;
		} else if (lastOutput[0] >= 0.7 && lastOutput[0] < 0.8) {
			nextMarkCoordinates[0] = 2;
			nextMarkCoordinates[1] = 1;
		} else if (lastOutput[0] >= 0.8 && lastOutput[0] <= 1) {
			nextMarkCoordinates[0] = 2;
			nextMarkCoordinates[1] = 2;
		}

		// Make move
		failed = !ticTacToe.setMark(nextMarkCoordinates[0], nextMarkCoordinates[1]);

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
		int[][] playfield = ticTacToe.playfield;
		for (int[] element : playfield) {
			for (int j = 0; j < playfield[0].length; ++j) {
				if (element[j] != 0) {
					unadjustedFitness += 1000;
				}
			}
		}

		if (failed) {
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
	public void doAiLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void takeAction(List<Double> output) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Double> getInputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initializeNormalizer() {
		// No normalizer used here
	}

}
