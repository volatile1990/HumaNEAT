package de.test;

import de.core.genes.ConnectionGene;
import de.core.genes.NodeGene;
import de.core.genes.NodeGeneType;
import de.core.genome.Genome;

/**
 * @author muellermak
 *
 */
public class AddConnectionMutationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Genome genome = new Genome();

		NodeGene input1 = new NodeGene(NodeGeneType.INPUT, 1);
		NodeGene input2 = new NodeGene(NodeGeneType.INPUT, 2);
		NodeGene output = new NodeGene(NodeGeneType.OUTPUT, 3);

		ConnectionGene con1 = new ConnectionGene(input1, input2, 1f, true, 1);
		ConnectionGene con2 = new ConnectionGene(input2, output, 1f, true, 1);

		genome.addNodeGene(input1);
		genome.addNodeGene(input2);
		genome.addNodeGene(output);

		genome.addConnectionGene(con1);
		genome.addConnectionGene(con2);

//		genome.addConnectionMutation(new Random(), 150);
		System.out.println();
	}
}
