package de.humaneat.core.lstm.genome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.humaneat.core.global.components.node.NodeEngagerVisitor;
import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.global.genome.DefaultGenomeFeeder;
import de.humaneat.core.lstm.genes.connection.LstmConnectionGene;
import de.humaneat.core.lstm.genes.node.LstmNodeGene;

/**
 * @author muellermak
 *
 */
public class LstmGenomeFeeder implements DefaultGenomeFeeder {

	private LstmGenome genome;

	/**
	 * @param genome
	 */
	public LstmGenomeFeeder(LstmGenome genome) {
		this.genome = genome;
	}

	/**
	 * @param inputValues
	 * @return
	 */
	@Override
	public double[] feedForward(double[] inputValues) {

		genome.getValidator().validateIntegrity();
		genome.getLinker().generateNetwork();

		List<LstmNodeGene> inputNodes = genome.getNodesByType(NodeGeneType.INPUT);
		List<LstmNodeGene> hiddenNodes = genome.getNodesByType(NodeGeneType.HIDDEN);
		List<LstmNodeGene> outputNodes = genome.getNodesByType(NodeGeneType.OUTPUT);

		// Sets up & engages inputs
		processInputs(inputValues, inputNodes);

		// Engage bias
		genome.biasNode.accept(new NodeEngagerVisitor());

		// Engage hidden nodes
		processHiddenNodes(hiddenNodes);

		// Engage output nodes
		processOutputNodes(outputNodes);

		// Reset connection activation
		for (LstmConnectionGene connection : genome.connections.values()) {
			connection.activated = false;
		}

		// Resets the input sum of all nodes
		for (LstmNodeGene node : genome.nodes.values()) {
			node.inputSum = 0;
		}

		return populateOutputs(outputNodes);
	}

	/**
	 * 1: Sets up the input nodes with the incoming values
	 * 2: Engages all inputs nodes
	 *
	 * @param inputValues
	 * @param inputNodes
	 */
	private void processInputs(double[] inputValues, List<LstmNodeGene> inputNodes) {

		// Populate inputSums of all input nodes with passed data
		for (LstmNodeGene input : inputNodes) {
			input.inputSum = inputValues[input.innovationNumber];
		}

		// Bias outputs 1
		genome.biasNode.outputValue = 1;

		// Engage inputs
		for (LstmNodeGene node : inputNodes) {
			node.accept(new NodeEngagerVisitor());
		}
	}

	/**
	 * Runs through all hidden nodes and engages them as soon as they have a payload on all incoming connections
	 * Repeat until all hidden nodes are engaged
	 *
	 * @param hiddenNodes
	 */
	private void processHiddenNodes(List<LstmNodeGene> hiddenNodes) {

		while (hiddenNodes.size() > 0) {

			Iterator<LstmNodeGene> iter = hiddenNodes.iterator();
			while (iter.hasNext()) {

				LstmNodeGene node = iter.next();

				// Collect all connections which have the current node as output
				List<LstmConnectionGene> nodeConnections = new ArrayList<>();
				for (LstmConnectionGene connection : genome.connections.values()) {

					if (connection.to == node && connection.enabled) {
						nodeConnections.add(connection);
					}
				}

				// Check whether all incoming connections have payload
				boolean dataIsComplete = true;
				double inputSum = 0;
				for (LstmConnectionGene connection : nodeConnections) {
					if (!connection.activated) {
						dataIsComplete = false;
					} else {
						inputSum += connection.payload;
					}
				}

				// Hidden note got input from all incoming connections
				// Set payload from incoming connections and engage the node
				if (dataIsComplete) {
					node.inputSum = inputSum;
					node.accept(new NodeEngagerVisitor());
					iter.remove();
				}
			}
		}
	}

	/**
	 * @param outputNodes
	 */
	private void processOutputNodes(List<LstmNodeGene> outputNodes) {

		// Engage output nodes
		for (LstmNodeGene node : outputNodes) {

			// Collect all connections which have the current node as output
			List<LstmConnectionGene> nodeConnections = new ArrayList<>();
			double inputSum = 0;
			for (LstmConnectionGene connection : genome.connections.values()) {
				if (connection.to == node) {
					nodeConnections.add(connection);
					inputSum += connection.payload;
				}
			}

			node.inputSum = inputSum;
			node.outputValue = inputSum;
			node.accept(new NodeEngagerVisitor());
		}
	}

	/**
	 * Populates the output vector
	 *
	 * @param outputNodes
	 * @return the populated outputs
	 */
	private double[] populateOutputs(List<LstmNodeGene> outputNodes) {

		double[] outputs = new double[genome.anzOutputs];
		int i = 0;
		for (LstmNodeGene output : outputNodes) {
			outputs[i++] = output.outputValue;
		}
		return outputs;
	}

}
