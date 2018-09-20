package de.test;

import de.core.neat.genes.ConnectionGene;
import de.core.neat.genes.NodeGene;
import de.core.neat.genes.NodeGeneType;
import de.core.neat.genome.Genome;
import de.core.neat.genome.GenomeHatchery;

public class CrossoverTest {
	public static void main(String[] args) {

		// Create first genome
		Genome firstParent = new Genome();

		// Three inputs
		for (int i = 0; i < 3; ++i) {
			NodeGene node = new NodeGene(NodeGeneType.INPUT, i + 1);
			firstParent.addNodeGene(node);
		}

		// One output, one hidden
		firstParent.addNodeGene(new NodeGene(NodeGeneType.OUTPUT, 4));
		firstParent.addNodeGene(new NodeGene(NodeGeneType.HIDDEN, 5));

		firstParent.addConnectionGene(new ConnectionGene(firstParent.nodes.get(1), firstParent.nodes.get(4), 1f, true, 1));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.nodes.get(2), firstParent.nodes.get(4), 1f, false, 2));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.nodes.get(3), firstParent.nodes.get(4), 1f, true, 3));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.nodes.get(2), firstParent.nodes.get(5), 1f, true, 4));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.nodes.get(5), firstParent.nodes.get(4), 1f, true, 5));
		firstParent.addConnectionGene(new ConnectionGene(firstParent.nodes.get(1), firstParent.nodes.get(5), 1f, true, 8));

		// Create second genome
		Genome secondParent = new Genome();

		// Three inputs
		for (int i = 0; i < 3; ++i) {
			NodeGene node = new NodeGene(NodeGeneType.INPUT, i + 1);
			secondParent.addNodeGene(node);
		}

		// One output, two hidden
		secondParent.addNodeGene(new NodeGene(NodeGeneType.OUTPUT, 4));
		secondParent.addNodeGene(new NodeGene(NodeGeneType.HIDDEN, 5));
		secondParent.addNodeGene(new NodeGene(NodeGeneType.HIDDEN, 6));

		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(1), secondParent.nodes.get(4), 1f, true, 1));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(2), secondParent.nodes.get(4), 1f, false, 2));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(3), secondParent.nodes.get(4), 1f, true, 3));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(2), secondParent.nodes.get(5), 1f, true, 4));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(5), secondParent.nodes.get(4), 1f, false, 5));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(5), secondParent.nodes.get(6), 1f, true, 6));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(6), secondParent.nodes.get(4), 1f, true, 7));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(3), secondParent.nodes.get(5), 1f, true, 9));
		secondParent.addConnectionGene(new ConnectionGene(secondParent.nodes.get(1), secondParent.nodes.get(6), 1f, true, 10));

		Genome child = GenomeHatchery.crossover(secondParent, firstParent);

		for (ConnectionGene connection : child.connections.values()) {
			if (connection.enabled) {
				System.out.println("IN-NODE-ID: " + connection.from.innovationNumber + " ## OUT-NODE-ID: " + connection.to.innovationNumber);
			}
		}
	}
}
