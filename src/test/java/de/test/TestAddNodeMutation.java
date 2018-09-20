package de.test;

import java.util.Random;

import de.core.neat.genes.ConnectionGene;
import de.core.neat.genes.Counter;
import de.core.neat.genes.NodeGene;
import de.core.neat.genes.NodeGeneType;
import de.core.neat.genome.NeatGenome;
import de.core.neat.genome.NeatGenomeConfig;

/**
 * @author muellermak
 *
 */
public class TestAddNodeMutation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Counter innovation = new Counter();
		Random random = new Random();

		NeatGenome genome = new NeatGenome(new NeatGenomeConfig());

		NodeGene input1 = new NodeGene(NodeGeneType.INPUT, -3);
		NodeGene input2 = new NodeGene(NodeGeneType.INPUT, -2);
		NodeGene output = new NodeGene(NodeGeneType.OUTPUT, -1);

		genome.addNodeGene(input1);
		genome.addNodeGene(input2);
		genome.addNodeGene(output);

		ConnectionGene con1 = new ConnectionGene(input1, output, 0.5f, true, -2);
		ConnectionGene con2 = new ConnectionGene(input2, output, 1f, true, -1);

		genome.addConnectionGene(con1);
		genome.addConnectionGene(con2);

//		genome.addNodeMutation(new Random());

		for (ConnectionGene connection : genome.getConnectionGenes().values()) {
			if (connection.isEnabled()) {
				System.out.println(
						"IN-NODE-ID: " + connection.getFrom().getNumber() + " ## OUT-NODE-ID: " + connection.getTo().getNumber() + " ## WEIGHT: " + connection.getWeight());
			}
		}
	}
}
