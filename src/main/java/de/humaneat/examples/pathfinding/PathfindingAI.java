package de.humaneat.examples.pathfinding;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.neat.ArtificialIntelligence;

/**
 * @author muellermak
 *
 */
public class PathfindingAI extends ArtificialIntelligence {

	public int currentX;
	public int currentY;

	public int targetX;
	public int targetY;

	public int[][] playfield;

	/**
	 *
	 */
	public PathfindingAI() {

		// Initial position
		currentX = 3;
		currentY = 7;

		// Target position
		targetX = 45;
		targetY = 49;

		// Playfield
		playfield = new int[50][50];

		// Draw player on java ui
//		PlayFrame.getInstance().canvas.drawPlayer(this);
	}

	/**
	 * Move 100 times using current brain
	 */
	@Override
	public void doAiLogic() {

		for (int i = 0; i < 100; ++i) {
			think();

			// Update players position on UI
//			PlayFrame.getInstance().canvas.update();
		}

	}

	/**
	 * @return
	 */
	@Override
	public List<Double> getInputs() {

		List<Double> input = new ArrayList<>();
		input.add((double) currentX);
		input.add((double) currentY);

		return input;
	}

	/**
	 * @param output
	 */
	@Override
	public void takeAction(List<Double> output) {
		move();
	}

	/**
	 * @return
	 */
	@Override
	public double calculateFitness() {

		double unadjustedFitness = 0;
		unadjustedFitness += Math.abs(targetX - currentX);
		unadjustedFitness += Math.abs(targetY - currentY);

		if (unadjustedFitness == 0) {
			unadjustedFitness = 0.001f;
		}

		return 1000f / (unadjustedFitness * unadjustedFitness);
	}

	/**
	 * 4 Outputs:
	 * 1: Move left
	 * 2: Move down
	 * 3: Move right
	 * 4: Move up
	 */
	public void move() {

		int height = playfield.length;
		int width = playfield[0].length;
		double[] lastOutput = outputs.get(outputs.size() - 1);

		boolean didVerticalMove = false;
		boolean didHorizontalMove = false;
		if (lastOutput[0] > 0.8) {

			// Move left
			didHorizontalMove = true;
			if (currentY > 0) {
				--currentY;
			}

		}
		if (lastOutput[1] > 0.8) {

			// Move down
			didVerticalMove = true;
			if (currentX < height) {
				++currentX;
			}

		}
		if (lastOutput[2] > 0.8) {

			// Move right
			if (didVerticalMove) {
				return;
			}
			if (currentY < width) {
				++currentY;
			}

		}
		if (lastOutput[3] > 0.8) {

			// Move up
			if (didHorizontalMove) {
				return;
			}
			if (currentX > 0) {
				--currentX;
			}
		}
	}

	/**
	 * @return
	 */
	public List<Double> getCurrentPosition() {

		List<Double> currentPosition = new ArrayList<>();
		currentPosition.add((double) currentX);
		currentPosition.add((double) currentY);

		return currentPosition;
	}

}
