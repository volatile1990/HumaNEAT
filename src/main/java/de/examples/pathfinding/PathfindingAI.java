package de.examples.pathfinding;

import java.util.ArrayList;
import java.util.List;

import de.core.ArtificialIntelligence;
import de.core.genome.Genome;

/**
 * @author muellermak
 *
 */
public class PathfindingAI extends ArtificialIntelligence {

	public int anzInputs;
	public int anzOutputs;

	public float[] inputs;
	public float[] decision;

	public int currentX;
	public int currentY;

	public int targetX;
	public int targetY;

	public int[][] playfield;

	// Results for fitness calculation
	public List<Integer> reachedX;
	public List<Integer> reachedY;

	public List<Integer> wasTargetX;
	public List<Integer> wasTargetY;

	/**
	 * 
	 */
	public PathfindingAI(int anzInputs, int anzOutputs) {

		this.anzInputs = anzInputs;
		this.anzOutputs = anzOutputs;
		this.inputs = new float[anzInputs];

		this.brain = new Genome(anzInputs, anzOutputs);

		this.init();
	}

	/**
	 * @param brain
	 */
	public PathfindingAI(Genome brain) {

		this(brain.anzInputs, brain.anzOutputs);
		this.brain = brain;

		this.init();
	}

	private void init() {

		this.reachedX = new ArrayList<>();
		this.reachedY = new ArrayList<>();
		this.wasTargetX = new ArrayList<>();
		this.wasTargetY = new ArrayList<>();

//		// Initial position
//		this.currentX = 3;
//		this.currentY = 7;
//
//		// Target position
//		this.targetX = 45;
//		this.targetY = 49;

		// Playfield
		this.playfield = new int[50][50];
	}

	@Override
	public void setInputs(List<Float> inputs) {

		for (int i = 0; i < inputs.size(); ++i) {
			this.inputs[i] = inputs.get(i);
		}
	}

	@Override
	public void think() {
		this.decision = this.brain.feedForward(this.inputs);
	}

	@Override
	public float calculateFitness() {

		float unadjustedFitness = 0;
		for (int i = 0; i < this.reachedX.size(); ++i) {

			float runFitness = 0;

			runFitness += Math.abs(this.wasTargetX.get(i) - this.reachedX.get(i));
			runFitness += Math.abs(this.wasTargetY.get(i) - this.reachedY.get(i));

			if (runFitness == 0) {
				runFitness = 0.001f;
			}

			runFitness = 100f / runFitness;

			unadjustedFitness += runFitness;
		}
		return unadjustedFitness / 1000;

		// Calculation for single target run
//		float unadjustedFitness = 0;
//		unadjustedFitness += Math.abs(this.targetX - this.currentX);
//		unadjustedFitness += Math.abs(this.targetY - this.currentY);
//
//		if (unadjustedFitness == 0) {
//			unadjustedFitness = 0.001f;
//		}
//
//		return 1000f / (unadjustedFitness * unadjustedFitness);
	}

	/**
	 * 
	 */
	public void saveDatasets() {
		this.reachedX.add(this.currentX);
		this.reachedY.add(this.currentY);
		this.wasTargetX.add(this.targetX);
		this.wasTargetY.add(this.targetY);
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

		boolean didVerticalMove = false;
		boolean didHorizontalMove = false;
		if (this.decision[0] > 0.8) {

			// Move left
			didHorizontalMove = true;
			if (this.currentY > 0) {
				--this.currentY;
			}

		}
		if (this.decision[1] > 0.8) {

			// Move down
			didVerticalMove = true;
			if (this.currentX < height) {
				++this.currentX;
			}

		}
		if (this.decision[2] > 0.8) {

			// Move right
			if (didVerticalMove) {
				return;
			}
			if (this.currentY < width) {
				++this.currentY;
			}

		}
		if (this.decision[3] > 0.8) {

			// Move up
			if (didHorizontalMove) {
				return;
			}
			if (this.currentX > 0) {
				--this.currentX;
			}
		}
	}

	public List<Float> getCurrentPosition() {

		List<Float> currentPosition = new ArrayList<>();
		currentPosition.add((float) this.currentX);
		currentPosition.add((float) this.currentY);

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
