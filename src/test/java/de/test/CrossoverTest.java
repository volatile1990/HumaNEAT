package de.test;

import de.core.neat.genes.ConnectionGene;
import de.core.neat.genes.NodeGene;
import de.core.neat.genes.NodeGeneType;
import de.core.neat.genome.NeatGenome;
import de.core.neat.genome.NeatGenomeConfig;

public class CrossoverTest {
	public static void main(String[] args) {

		// Create first genome
		NeatGenome firstParent = new NeatGenome(new NeatGenomeConfig());

		// Three inputs
		for (int i = 0; i < 3; ++i) {
			NodeGene node = new NodeGene(NodeGeneType.INPUT, i + 1);
			firstParent.addNodeGene(node);
		}

		// One output, one hidden
		firstParent.addNodeGene(new NodeGene(NodeGeneType.OUTPUT, 4));
		firstParent.addNodeGene(new NodeGene(NodeGeneType.HIDDEN, 5));

		firstParent.addConnectionGene(new ConnectionGene(firstParent.getNodeGenes().get(1), firstParent.getNodeGenes().get(4), 1f, true, 1));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.getNodeGenes().get(2), firstParent.getNodeGenes().get(4), 1f, false, 2));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.getNodeGenes().get(3), firstParent.getNodeGenes().get(4), 1f, true, 3));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.getNodeGenes().get(2), firstParent.getNodeGenes().get(5), 1f, true, 4));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.getNodeGenes().get(5), firstParent.getNodeGenes().get(4), 1f, true, 5));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.getNodeGenes().get(1), firstParent.getNodeGenes().get(5), 1f, true, 8));

		// Create second genome
		NeatGenome secondParent = new NeatGenome(new NeatGenomeConfig());

		// Three inputs
		for (int i = 0; i < 3; ++i) {
			NodeGene node = new NodeGene(NodeGeneType.INPUT, i + 1);
			secondParent.addNodeGene(node);
		}

		// One output, two hidden
		secondParent.addNodeGene(new NodeGene(NodeGeneType.OUTPUT, 4));
		secondParent.addNodeGene(new NodeGene(NodeGeneType.HIDDEN, 5));
		secondParent.addNodeGene(new NodeGene(NodeGeneType.HIDDEN, 6));

		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(1), secondParent.getNodeGenes().get(4), 1f, true, 1));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(2), secondParent.getNodeGenes().get(4), 1f, false, 2));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(3), secondParent.getNodeGenes().get(4), 1f, true, 3));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(2), secondParent.getNodeGenes().get(5), 1f, true, 4));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(5), secondParent.getNodeGenes().get(4), 1f, false, 5));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(5), secondParent.getNodeGenes().get(6), 1f, true, 6));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(6), secondParent.getNodeGenes().get(4), 1f, true, 7));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(3), secondParent.getNodeGenes().get(5), 1f, true, 9));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.getNodeGenes().get(1), secondParent.getNodeGenes().get(6), 1f, true, 10));

		NeatGenome child = NeatGenome.crossover(secondParent, firstParent);

		for (ConnectionGene connection : child.getConnectionGenes().values()) {
			if (connection.isEnabled()) {
				System.out.println("IN-NODE-ID: " + connection.getFrom().getNumber() + " ## OUT-NODE-ID: " + connection.getTo().getNumber());
			}
		}
	}
}
