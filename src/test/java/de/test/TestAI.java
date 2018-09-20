package de.test;

import java.util.List;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.genes.ConnectionGene;
import de.core.neat.genome.Genome;

public class TestAI extends ArtificialIntelligence {

	/**
	 * @param anzInputs
	 * @param anzOutputs
	 */
	public TestAI(int anzInputs, int anzOutputs) {
		this.brain = new Genome(anzInputs, anzOutputs);
	}

	public TestAI(Genome brain) {

		this(brain.anzInputs, brain.anzOutputs);
		this.brain = brain;
	}

	@Override
	public double calculateFitness() {
		double weightSum = 0f;
		for (ConnectionGene connection : this.brain.getConnectionGenes().values()) {
			if (connection.isEnabled()) {
				weightSum += Math.abs(connection.getWeight());
			}
		}

		double difference = Math.abs(weightSum - 100f);
		return 1000f / difference;
	}

	@Override
	public void think() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInputs(List<Double> inputs) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArtificialIntelligence getNewInstance(Genome genome) {
		return new TestAI(genome);
	}

}
