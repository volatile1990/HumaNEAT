package de.test;

import java.util.ArrayList;
import java.util.List;

import de.GenomePrinter;
import de.core.ArtificialIntelligence;
import de.core.genes.ConnectionGene;
import de.core.genes.Counter;
import de.core.genes.NodeGene;
import de.core.genes.NodeGeneType;
import de.core.genome.Genome;
import de.core.population.Population;

/**
 * @author muellermak
 *
 */
public class TestEvaluatorManyConnections {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Counter nodeInnovation = new Counter();
		Counter connectionInnovation = new Counter();

		Genome genome = new Genome();

		NodeGene input1 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene input2 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene output = new NodeGene(NodeGeneType.OUTPUT, nodeInnovation.getNext());

		genome.addNodeGene(input1);
		genome.addNodeGene(input2);
		genome.addNodeGene(output);

		genome.addConnectionGene(new ConnectionGene(input1, output, 0.5f, true, connectionInnovation.getNext()));
		genome.addConnectionGene(new ConnectionGene(input2, output, 0.5f, true, connectionInnovation.getNext()));

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

			if (i % 10 == 0) {
				GenomePrinter.printGenome(eval.getFittestAI().brain, "D:/output/ManyConnections/Test_ " + i + ".png");
			}
		}
	}

}
