package de.test;

import java.util.ArrayList;
import java.util.List;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.genes.ConnectionGene;
import de.core.neat.genome.Genome;
import de.core.neat.population.Population;

/**
 * @author MannoR
 *
 */
public class TestEvaluatorHundredSum {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<ArtificialIntelligence> ais = new ArrayList<>();
		for (int i = 0; i < 100; ++i) {
			ais.add(new TestAI(2, 1));
		}

		Population eval = new Population(ais);

		for (int i = 0; i <= 100; ++i) {
			eval.evolve();
			System.out.println("Generation: " + i);
			System.out.println("Highest fitness: " + eval.getFittestAI().brain.fitness);
			System.out.println("Amount of species: " + eval.getSpecies().size());

			Genome fittestGenome = eval.getFittestAI().brain;
			float sum = 0f;
			for (ConnectionGene connection : fittestGenome.getConnectionGenes().values()) {
				if (connection.isEnabled()) {
					sum += Math.abs(connection.getWeight());
				}
			}

			System.out.println("Connection weights sum: " + Math.abs(sum));

			if (i % 10 == 0) {
//				GenomePrinter.printGenome(fittestGenome, "D:/output/HundredWeightSum/Test_ " + i + ".png");
			}
		}

	}
}
