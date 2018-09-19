package de.test;

import java.util.ArrayList;
import java.util.List;

import de.GenomePrinter;
import de.core.ArtificialIntelligence;
import de.core.genes.Counter;
import de.core.genome.Genome;
import de.core.population.Population;

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
			System.out.println("Highest fitness: " + eval.getFittestAI().brain.fitness);
			System.out.println("Number of connections: " + eval.getFittestAI().brain.getConnectionGenes().size());

			if (i % 10 == 0) {
				GenomePrinter.printGenome(eval.getFittestAI().brain, "D:/output/FewConnections/Test_ " + i + ".png");
			}
		}

	}
}
