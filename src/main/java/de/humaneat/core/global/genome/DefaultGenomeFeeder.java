package de.humaneat.core.global.genome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.humaneat.core.global.components.node.NodeEngagerVisitor;
import de.humaneat.core.neat.genes.connection.ConnectionGene;
import de.humaneat.core.neat.genes.node.NodeGene;
import de.humaneat.core.neat.genes.node.NodeGeneType;
import de.humaneat.core.neat.genome.Genome;

/**
 * @author MannoR
 *
 *         Contains all available methods to feed data through a genome
 */
public abstract class DefaultGenomeFeeder {

	private Genome genome;

	/**
	 * @param genome
	 */
	public DefaultGenomeFeeder(Genome genome) {
		this.genome = genome;
	}

	/**
	 * @param inputValues
	 * @return
	 */
	public double[] feedForward(double[] inputValues) {

		genome.getValidator().validateIntegrity();
		genome.getLinker().generateNetwork();

		List<NodeGene> inputNodes = genome.getNodesByType(NodeGeneType.INPUT);
		List<NodeGene> hiddenNodes = genome.getNodesByType(NodeGeneType.HIDDEN);
		List<NodeGene> outputNodes = genome.getNodesByType(NodeGeneType.OUTPUT);

		// Sets up & engages inputs
		processInputs(inputValues, inputNodes);

		// Engage bias
		genome.biasNode.accept(new NodeEngagerVisitor());

		// Engage hidden nodes
		processHiddenNodes(hiddenNodes);

		// Engage output nodes
		processOutputNodes(outputNodes);

		// Reset connection activation
		for (ConnectionGene connection : genome.connections.values()) {
			connection.activated = false;
		}

		// Resets the input sum of all nodes
		for (NodeGene node : genome.nodes.values()) {
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
	private void processInputs(double[] inputValues, List<NodeGene> inputNodes) {

		// Populate inputSums of all input nodes with passed data
		for (NodeGene input : inputNodes) {
			input.inputSum = inputValues[input.innovationNumber];
		}

		// Bias outputs 1
		genome.biasNode.outputValue = 1;

		// Engage inputs
		for (NodeGene node : inputNodes) {
			node.accept(new NodeEngagerVisitor());
		}
	}

	/**
	 * Runs through all hidden nodes and engages them as soon as they have a payload on all incoming connections
	 * Repeat until all hidden nodes are engaged
	 *
	 * @param hiddenNodes
	 */
	private void processHiddenNodes(List<NodeGene> hiddenNodes) {

		while (hiddenNodes.size() > 0) {

			Iterator<NodeGene> iter = hiddenNodes.iterator();
			while (iter.hasNext()) {

				NodeGene node = iter.next();

				// Collect all connections which have the current node as output
				List<ConnectionGene> nodeConnections = new ArrayList<>();
				for (ConnectionGene connection : genome.connections.values()) {

					if (connection.to == node && connection.enabled) {
						nodeConnections.add(connection);
					}
				}

				// Check whether all incoming connections have payload
				boolean dataIsComplete = true;
				double inputSum = 0;
				for (ConnectionGene connection : nodeConnections) {
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
	private void processOutputNodes(List<NodeGene> outputNodes) {

		// Engage output nodes
		for (NodeGene node : outputNodes) {

			// Collect all connections which have the current node as output
			List<ConnectionGene> nodeConnections = new ArrayList<>();
			double inputSum = 0;
			for (ConnectionGene connection : genome.connections.values()) {
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
	private double[] populateOutputs(List<NodeGene> outputNodes) {

		double[] outputs = new double[genome.anzOutputs];
		int i = 0;
		for (NodeGene output : outputNodes) {
			outputs[i++] = output.outputValue;
		}
		return outputs;
	}
}
