package de.test;

import java.util.ArrayList;
import java.util.List;

import de.GenomePrinter;
import de.core.neat.ArtificialIntelligence;
import de.core.neat.genes.Counter;
import de.core.neat.genome.NeatGenome;
import de.core.neat.genome.NeatGenomeConfig;
import de.core.neat.population.NeatPopulation;
import de.core.neat.population.NeatPopulationConfig;

/**
 * @author MannoR
 *
 */
public class TestEvaluatorFewConnections {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Counter nodeInnovation = new Counter();
		Counter connectionInnovation = new Counter();

		NeatGenome genome = new NeatGenome(2, 1, new NeatGenomeConfig());

		List<ArtificialIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < 100; ++i) {
			ais.add(new TestAI(2, 1));
		}

		NeatPopulation eval = new NeatPopulation(ais, new NeatPopulationConfig());

		for (int i = 0; i <= 100; ++i) {
			eval.evolve();
			System.out.println("Generation: " + i);
			System.out.println("Highest fitness: " + eval.getFittestAI().brain.fitness);
			System.out.println("Number of connections: " + eval.getFittestAI().brain.getConnectionGenes().size());

			if (i % 10 == 0) {
				GenomePrinter.printGenome(eval.getFittestAI().brain, "D:/output/FewConnections/Test_ " + i + ".png");
			}
		}

	}
}
