package de.test;

import de.core.neat.genes.ConnectionGene;
import de.core.neat.genes.Counter;
import de.core.neat.genes.NodeGene;
import de.core.neat.genes.NodeGeneType;
import de.core.neat.genome.Genome;
import de.core.neat.genome.GenomeSpeciation;

/**
 * @author muellermak
 *
 */
public class CountGenomesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Genome first = generateFirstGenome();
		Genome second = generateSecondGenome();

		GenomeSpeciation genomeInfo = new GenomeSpeciation(first, second);

		System.out.println("MATCHING GENES: " + genomeInfo.matchingGenes + " CORRECT ANSWER: 5");
		System.out.println("DISJOINT GENES: " + genomeInfo.disjointGenes + " CORRECT ANSWER: 4");
		System.out.println("EXCESS GENES  : " + genomeInfo.excessGenes + " CORRECT ANSWER: 3");

//		System.out.println("COMPATIBILITY DISTANCE: " + genomeInfo.compatibilityDistance(first, second, c1, c2, c3));
	}

	private static Genome generateFirstGenome() {
		Genome genome = new Genome();

		NodeGene input = new NodeGene(NodeGeneType.INPUT, 0);
		NodeGene hidden1 = new NodeGene(NodeGeneType.HIDDEN, 2);
		NodeGene hidden2 = new NodeGene(NodeGeneType.HIDDEN, 3);
		NodeGene output = new NodeGene(NodeGeneType.OUTPUT, 4);
		NodeGene output2 = new NodeGene(NodeGeneType.OUTPUT, 5);

		genome.addNodeGene(input);
		genome.addNodeGene(hidden1);
		genome.addNodeGene(hidden2);
		genome.addNodeGene(output);
		genome.addNodeGene(output2);

		Counter innovation = new Counter();
		genome.addConnectionGene(new ConnectionGene(input, hidden1, 1, true, innovation.getNext()));
		genome.addConnectionGene(new ConnectionGene(input, hidden2, 1, true, innovation.getNext()));
		genome.addConnectionGene(new ConnectionGene(hidden1, output, 1, true, innovation.getNext()));

		return genome;
	}

	private static Genome generateSecondGenome() {
		Genome genome = new Genome();

		NodeGene input = new NodeGene(NodeGeneType.INPUT, 0);
		NodeGene hidden1 = new NodeGene(NodeGeneType.HIDDEN, 1);
		NodeGene hidden2 = new NodeGene(NodeGeneType.HIDDEN, 2);
		NodeGene output = new NodeGene(NodeGeneType.OUTPUT, 6);
		NodeGene output2 = new NodeGene(NodeGeneType.OUTPUT, 7);

		genome.addNodeGene(input);
		genome.addNodeGene(hidden1);
		genome.addNodeGene(hidden2);
		genome.addNodeGene(output);
		genome.addNodeGene(output2);

		Counter innovation = new Counter();
		genome.addConnectionGene(new ConnectionGene(input, hidden1, 1, true, innovation.getNext()));
		genome.addConnectionGene(new ConnectionGene(input, hidden2, 1, true, innovation.getNext()));
		genome.addConnectionGene(new ConnectionGene(hidden1, output, 1, true, innovation.getNext()));
		genome.addConnectionGene(new ConnectionGene(hidden2, output, 1, true, 5));

		return genome;
	}
}
