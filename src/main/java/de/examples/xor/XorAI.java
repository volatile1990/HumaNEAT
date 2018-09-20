package de.examples.xor;

import java.util.ArrayList;
import java.util.List;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.genome.Genome;

public class XorAI extends ArtificialIntelligence {

	public int anzInputs;
	public int anzOutputs;

	public double[] inputs;

	public List<Double> x;
	public List<Double> y;

	public List<Double> decisions;

	public XorAI(int anzInputs, int anzOutputs) {

		this.anzInputs = anzInputs;
		this.anzOutputs = anzOutputs;
		this.inputs = new double[this.anzInputs];

		this.brain = new Genome(anzInputs, anzOutputs);

		this.x = new ArrayList<>();
		this.y = new ArrayList<>();
		this.decisions = new ArrayList<>();
	}

	public XorAI(Genome brain) {

		this(brain.anzInputs, brain.anzOutputs);
		this.brain = brain;
	}

	@Override
	public void setInputs(List<Double> inputs) {
		for (int i = 0; i < this.anzInputs; ++i) {
			this.inputs[i] = inputs.get(i);
		}

		this.x.add(inputs.get(0));
		this.y.add(inputs.get(1));
	}

	@Override
	public void think() {
		double decision = this.brain.feedForward(this.inputs)[0];
		this.decisions.add(decision);

		double expectedOutput = Math.round(this.inputs[0]) ^ Math.round(this.inputs[1]);
//		System.out.println("X: " + this.inputs[0] + " Y: " + this.inputs[1] + " Result: " + decision + " Expected: " + expectedOutput);
	}

	@Override
	public double calculateFitness() {

		double unadjustedFitness = this.getFitness2();
		double secondUnadjustedFitness = this.getFitness1();

//		System.out.println("FITNESS 1: " + unadjustedFitness + " ## FITNESS 2: " + secondUnadjustedFitness);

		this.x.clear();
		this.y.clear();
		this.decisions.clear();

		return secondUnadjustedFitness;
	}

	private double getFitness1() {

		double off = 0;
		for (int i = 0; i < this.decisions.size(); ++i) {
			double expectedOutput = Math.round(this.x.get(i)) ^ Math.round(this.y.get(i));

			off += Math.abs(this.decisions.get(i) - expectedOutput);
		}

		double unadjustedFitness = 4 - off;
		if (unadjustedFitness < 0) {
			unadjustedFitness = 0;
		}

		return unadjustedFitness * unadjustedFitness;
	}

	private double getFitness2() {

		double unadjustedFitness = 0;
		for (int i = 0; i < this.decisions.size(); ++i) {
			double expectedOutput = Math.round(this.x.get(i)) ^ Math.round(this.y.get(i));

			unadjustedFitness += 1 - (expectedOutput - this.decisions.get(i)) * (expectedOutput - this.decisions.get(i));
		}

		return unadjustedFitness * unadjustedFitness;
	}

	@Override
	public ArtificialIntelligence getNewInstance(Genome genome) {
		return new XorAI(genome);
	}

}
