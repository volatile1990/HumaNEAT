package de.test;

import java.util.ArrayList;
import java.util.List;

import de.GenomePrinter;
import de.core.neat.ArtificialIntelligence;
import de.core.neat.genes.Counter;
import de.core.neat.genome.Genome;
import de.core.neat.population.Population;

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

		Genome genome = new Genome(2, 1);

		List<ArtificialIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < 100; ++i) {
			ais.add(new TestAI(2, 1));
		}

		Population eval = new Population(ais);

		for (int i = 0; i <= 100; ++i) {
			eval.evolve();
			System.out.println("Generation: " + i);
			System.out.println("Highest fitness: " + eval.fittestAI.brain.fitness);
			System.out.println("Number of connections: " + eval.fittestAI.brain.connections.size());

			if (i % 10 == 0) {
				GenomePrinter.printGenome(eval.fittestAI.brain, "D:/output/FewConnections/Test_ " + i + ".png");
			}
		}

	}
}
