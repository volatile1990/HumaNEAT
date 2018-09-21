package de.humaneat.test;

import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.genes.connection.ConnectionGene;
import de.humaneat.core.neat.genome.Genome;

public class TestAI extends ArtificialIntelligence {

	/**
	 * @param anzInputs
	 * @param anzOutputs
	 */
	public TestAI(int anzInputs, int anzOutputs) {
		super(anzInputs, anzOutputs);
	}

	public TestAI(Genome brain) {
		super(brain);
	}

	@Override
	public double calculateFitness() {
		double weightSum = 0f;
		for (ConnectionGene connection : brain.connections.values()) {
			if (connection.enabled) {
				weightSum += Math.abs(connection.weight);
			}
		}

		double difference = Math.abs(weightSum - 100f);
		return 1000f / difference;
	}

	@Override
	public ArtificialIntelligence getNewInstance(Genome genome) {
		return new TestAI(genome);
	}

}
