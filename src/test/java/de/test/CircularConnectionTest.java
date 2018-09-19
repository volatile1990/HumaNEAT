package de.test;

import de.GenomePrinter;
import de.core.genes.ConnectionGene;
import de.core.genes.Counter;
import de.core.genes.NodeGene;
import de.core.genes.NodeGeneType;
import de.core.genome.Genome;

/**
 * @author MannoR
 *
 */
public class CircularConnectionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Genome genome = new Genome();

		Counter nodeInnovation = new Counter();
		Counter connectionInnovation = new Counter();

		NodeGene input1 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene input2 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene hidden1 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene hidden2 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene hidden3 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene hidden4 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene output = new NodeGene(NodeGeneType.OUTPUT, nodeInnovation.getNext());

		ConnectionGene in1ToHidden1 = new ConnectionGene(input1, hidden1, 0.75f, true, connectionInnovation.getNext());
		ConnectionGene in2ToHidden1 = new ConnectionGene(input2, hidden1, 0.25f, true, connectionInnovation.getNext());
		ConnectionGene in2ToHidden3 = new ConnectionGene(input2, hidden3, 3.1f, true, connectionInnovation.getNext());

		ConnectionGene hidden1ToHidden2 = new ConnectionGene(hidden1, hidden2, 2.2f, true, connectionInnovation.getNext());
		ConnectionGene hidden2ToHidden3 = new ConnectionGene(hidden2, hidden3, 4.45f, true, connectionInnovation.getNext());

		ConnectionGene hidden1ToOut = new ConnectionGene(hidden1, output, 0.8f, true, connectionInnovation.getNext());
		ConnectionGene hidden3ToOut = new ConnectionGene(hidden3, output, 2.2f, true, connectionInnovation.getNext());

		genome.addNodeGene(input1);
		genome.addNodeGene(input2);
		genome.addNodeGene(hidden1);
		genome.addNodeGene(hidden2);
		genome.addNodeGene(hidden3);
		genome.addNodeGene(hidden4);
		genome.addNodeGene(output);

		genome.addConnectionGene(in1ToHidden1);
		genome.addConnectionGene(in2ToHidden1);
		genome.addConnectionGene(in2ToHidden3);
		genome.addConnectionGene(hidden1ToHidden2);
		genome.addConnectionGene(hidden2ToHidden3);
		genome.addConnectionGene(hidden1ToOut);
		genome.addConnectionGene(hidden3ToOut);

		ConnectionGene hidden3ToHidden4 = new ConnectionGene(hidden3, hidden4, 1f, true, connectionInnovation.getNext());
		genome.addConnectionGene(hidden3ToHidden4);

		ConnectionGene hidden4ToHidden1 = new ConnectionGene(hidden4, hidden1, 4.45f, true, connectionInnovation.getNext());
		genome.addConnectionGene(hidden4ToHidden1);

		GenomePrinter.printGenome(genome, "D:/output/test.png");
		System.out.println(genome.connectionCreatesCircular(hidden4, output));

//		genome.generateNetwork();
//
//		float[] inputData = new float[2];
//		inputData[0] = 0.5f;
//		inputData[1] = 1.5f;
//
//		GenomePrinter.printGenome(genome, "D:/output/test/out.png");
//
//		System.out.println(genome.feedForward(inputData)[0] == 0.93950474f);
	}
}
