package de.humaneat.examples.pathfinding;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.genome.Genome;

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
	public PathfindingAI(int anzInputs, int anzOutputs) {
		super(anzInputs, anzOutputs);
		init();
	}

	/**
	 * @param brain
	 */
	public PathfindingAI(Genome brain) {
		super(brain);
		init();
	}

	/**
	 *
	 */
	private void init() {

		// Initial position
		currentX = 3;
		currentY = 7;

		// Target position
		targetX = 45;
		targetY = 49;

		// Playfield
		playfield = new int[50][50];
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

	/*
	 * (non-Javadoc)
	 *
	 * @see de.core.ArtificialIntelligence#getNewInstance(de.core.genome.Genome)
	 */
	@Override
	public ArtificialIntelligence getNewInstance(Genome genome) {
		return new PathfindingAI(genome);
	}
}
