package de.humaneat.test;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.genes.Counter;
import de.humaneat.core.neat.genes.connection.ConnectionGene;
import de.humaneat.core.neat.genes.node.NodeGene;
import de.humaneat.core.neat.genes.node.NodeGeneType;
import de.humaneat.core.neat.genome.Genome;
import de.humaneat.core.neat.population.Population;
import de.humaneat.graphics.GenomePrinter;

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
			System.out.println("Highest fitness: " + eval.fittestAI.brain.fitness);
			System.out.println("Amount of species: " + eval.species.size());

			if (i % 10 == 0) {
				GenomePrinter.printGenome(eval.fittestAI.brain, "D:/output/ManyConnections/Test_ " + i + ".png");
			}
		}
	}

}
