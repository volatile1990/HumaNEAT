package de.test;

import java.util.List;

import de.core.neat.genes.ConnectionGene;
import de.core.neat.genes.Counter;
import de.core.neat.genes.NodeGene;
import de.core.neat.genes.NodeGeneType;
import de.core.neat.genome.Genome;

/**
 * @author MannoR
 *
 */
public class GenomeFeedForwardTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		firstTest();
		secondTest();
		thirdTest();
	}

	/**
	 *
	 */
	private static void firstTest() {

		Genome genome = new Genome(2, 1);

		NodeGene hidden1 = new NodeGene(NodeGeneType.HIDDEN, 3);

		List<NodeGene> inputNodes = genome.getNodesByType(NodeGeneType.INPUT);
		List<NodeGene> outputNodes = genome.getNodesByType(NodeGeneType.OUTPUT);

		ConnectionGene in1ToHidden = inputNodes.get(0).outputConnections.get(0);
		ConnectionGene in2ToHidden = inputNodes.get(1).outputConnections.get(0);
		ConnectionGene hiddenToOut = new ConnectionGene(hidden1, outputNodes.get(0), 2.5f, true, 3);

		in1ToHidden.to = hidden1;
		in2ToHidden.to = hidden1;

		genome.addConnectionGene(in1ToHidden);
		genome.addConnectionGene(in2ToHidden);
		genome.addConnectionGene(hiddenToOut);
		genome.addNodeGene(hidden1);

		in1ToHidden.weight = 0.75f;
		in2ToHidden.weight = 0.25f;

		genome.getLinker().generateNetwork();

//		GenomePrinter.printGenome(genome, "D:/output/test/out.png");

		double[] inputData = new double[2];
		inputData[0] = 0.5f;
		inputData[1] = 1.5f;

		System.out.println(genome.getFeeder().feedForward(inputData)[0] == 0.8452664f);
	}

	/**
	 *
	 */
	private static void secondTest() {

		Genome genome = new Genome();

		Counter nodeInnovation = new Counter();
		Counter connectionInnovation = new Counter();

		NodeGene input1 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene input2 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene hidden1 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene output = new NodeGene(NodeGeneType.OUTPUT, nodeInnovation.getNext());

		ConnectionGene in1ToHidden1 = new ConnectionGene(input1, hidden1, 0.75f, true, connectionInnovation.getNext());
		ConnectionGene int2ToHidden1 = new ConnectionGene(input2, hidden1, 0.25f, true, connectionInnovation.getNext());
		ConnectionGene hiddenToOut = new ConnectionGene(hidden1, output, 2.5f, true, connectionInnovation.getNext());

		genome.addNodeGene(input1);
		genome.addNodeGene(input2);
		genome.addNodeGene(hidden1);
		genome.addNodeGene(output);

		genome.addConnectionGene(in1ToHidden1);
		genome.addConnectionGene(int2ToHidden1);
		genome.addConnectionGene(hiddenToOut);

		genome.getLinker().generateNetwork();

		double[] inputData = new double[2];
		inputData[0] = 0.5f;
		inputData[1] = 1.5f;

//		GenomePrinter.printGenome(genome, "D:/output/test/out.png");

		System.out.println(genome.getFeeder().feedForward(inputData)[0] == 0.8452664f);
	}

	/**
	 *
	 */
	private static void thirdTest() {

		Genome genome = new Genome();

		Counter nodeInnovation = new Counter();
		Counter connectionInnovation = new Counter();

		NodeGene input1 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene input2 = new NodeGene(NodeGeneType.INPUT, nodeInnovation.getNext());
		NodeGene hidden1 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene hidden2 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
		NodeGene hidden3 = new NodeGene(NodeGeneType.HIDDEN, nodeInnovation.getNext());
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
		genome.addNodeGene(output);

		genome.addConnectionGene(in1ToHidden1);
		genome.addConnectionGene(in2ToHidden1);
		genome.addConnectionGene(in2ToHidden3);
		genome.addConnectionGene(hidden1ToHidden2);
		genome.addConnectionGene(hidden2ToHidden3);
		genome.addConnectionGene(hidden1ToOut);
		genome.addConnectionGene(hidden3ToOut);

		genome.getLinker().generateNetwork();

		double[] inputData = new double[2];
		inputData[0] = 0.5f;
		inputData[1] = 1.5f;

//		GenomePrinter.printGenome(genome, "D:/output/test/out.png");

		System.out.println(genome.getFeeder().feedForward(inputData)[0] == 0.93950474f);
	}
}
