package de.examples.pathfinding;

import java.util.ArrayList;
import java.util.List;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.genome.Genome;

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
		this.init();
	}

	/**
	 * @param brain
	 */
	public PathfindingAI(Genome brain) {
		super(brain);
		this.init();
	}

	/**
	 *
	 */
	private void init() {

		// Initial position
		this.currentX = 3;
		this.currentY = 7;

		// Target position
		this.targetX = 45;
		this.targetY = 49;

		// Playfield
		this.playfield = new int[50][50];
	}

	/**
	 * @return
	 */
	@Override
	public double calculateFitness() {

		double unadjustedFitness = 0;
		unadjustedFitness += Math.abs(this.targetX - this.currentX);
		unadjustedFitness += Math.abs(this.targetY - this.currentY);

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

		int height = this.playfield.length;
		int width = this.playfield[0].length;
		double[] lastOutput = this.outputs.get(this.outputs.size() - 1);

		boolean didVerticalMove = false;
		boolean didHorizontalMove = false;
		if (lastOutput[0] > 0.8) {

			// Move left
			didHorizontalMove = true;
			if (this.currentY > 0) {
				--this.currentY;
			}

		}
		if (lastOutput[1] > 0.8) {

			// Move down
			didVerticalMove = true;
			if (this.currentX < height) {
				++this.currentX;
			}

		}
		if (lastOutput[2] > 0.8) {

			// Move right
			if (didVerticalMove) {
				return;
			}
			if (this.currentY < width) {
				++this.currentY;
			}

		}
		if (lastOutput[3] > 0.8) {

			// Move up
			if (didHorizontalMove) {
				return;
			}
			if (this.currentX > 0) {
				--this.currentX;
			}
		}
	}

	/**
	 * @return
	 */
	public List<Double> getCurrentPosition() {

		List<Double> currentPosition = new ArrayList<>();
		currentPosition.add((double) this.currentX);
		currentPosition.add((double) this.currentY);

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
