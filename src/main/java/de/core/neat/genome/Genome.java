package de.core.neat.genome;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.core.neat.Property;
import de.core.neat.genes.ConnectionGene;
import de.core.neat.genes.ConnectionHistory;
import de.core.neat.genes.Counter;
import de.core.neat.genes.NodeGene;
import de.core.neat.genes.NodeGeneType;

/**
 * @author muellermak
 *
 */
public class Genome {

	private Map<Integer, NodeGene> nodes;
	private Map<Integer, ConnectionGene> connections;

	private NodeGene biasNode;

	private List<NodeGene> network;

	public Counter nodeInnovation;
	public Counter connectionInnovation;

	public int anzInputs;
	public int anzOutputs;

	public double fitness;
	public double unadjustedFitness;

	/**
	 *
	 */
	public Genome() {

		this.nodes = new HashMap<>();
		this.connections = new HashMap<>();

		this.nodeInnovation = new Counter();
		this.connectionInnovation = new Counter();

		this.fitness = 0;
		this.unadjustedFitness = 0;
	}

	/**
	 * @param inputs
	 * @param output
	 */
	public Genome(int inputs, int outputs) {

		this();

		this.nodeInnovation = new Counter();
		this.connectionInnovation = new Counter();

		this.anzInputs = inputs;
		this.anzOutputs = outputs;

		this.fitness = 0;
		this.unadjustedFitness = 0;

		// Generate Inputs
		List<NodeGene> inputNodes = new ArrayList<>();
		for (int i = 0; i < inputs; ++i) {
			int nextInnovation = this.nodeInnovation.getNext();
			NodeGene input = new NodeGene(NodeGeneType.INPUT, nextInnovation);
			this.nodes.put(nextInnovation, input);
			inputNodes.add(input);
		}

		// Generate outputs
		List<NodeGene> outputNodes = new ArrayList<>();
		for (int i = 0; i < outputs; ++i) {
			int nextInnovation = this.nodeInnovation.getNext();
			NodeGene output = new NodeGene(NodeGeneType.OUTPUT, nextInnovation);
			this.nodes.put(nextInnovation, output);
			outputNodes.add(output);
		}

		// Connect all inputs to all outputs
		Random random = new SecureRandom();
		for (NodeGene inputNode : inputNodes) {
			for (NodeGene outputNode : outputNodes) {

				double max = Property.WEIGHT_RANDOM_RANGE.getValue();
				double min = -1 * Property.WEIGHT_RANDOM_RANGE.getValue();
				double weight = min + (max - min) * random.nextFloat();

				ConnectionGene connection = new ConnectionGene(inputNode, outputNode, weight, true, this.connectionInnovation.getNext());
				this.addConnectionGene(connection);
			}
		}

		this.generateNetwork();

		// Add bias node
		int biasInnovation = this.nodeInnovation.getNext();
		this.biasNode = new NodeGene(NodeGeneType.BIAS, biasInnovation);
		this.nodes.put(biasInnovation, this.biasNode);

		// Connect the bias node to all outputs
		for (NodeGene outputNode : this.getNodesByType(NodeGeneType.OUTPUT)) {

			ConnectionGene connection = new ConnectionGene(this.biasNode, outputNode, random.nextFloat() * 4f - 2f, true, this.connectionInnovation.getNext());
			this.addConnectionGene(connection);
		}
	}

	/**
	 * Sets the all outgoing connections of a node gene into its output connections
	 */
	public void connectNodes() {

		// Clear all output connections
		for (NodeGene node : this.nodes.values()) {
			node.outputConnections.clear();
		}

		// Adds the connection as an outgoing connection to its from node
		for (ConnectionGene connection : this.connections.values()) {
			connection.getFrom().outputConnections.add(connection);
		}

	}

	/**
	 * @param inputValues
	 * @return
	 */
	public double[] feedForward(double[] inputValues) {

		this.validateIntegrity();
		this.generateNetwork();

		List<NodeGene> inputNodes = this.getNodesByType(NodeGeneType.INPUT);
		List<NodeGene> hiddenNodes = this.getNodesByType(NodeGeneType.HIDDEN);
		List<NodeGene> outputNodes = this.getNodesByType(NodeGeneType.OUTPUT);

		// Populate inputSums of all input nodes with passed data
		for (NodeGene input : inputNodes) {
			input.inputSum = inputValues[input.getNumber()];
		}

		// Bias outputs 1
		this.biasNode.outputValue = 1;

		// Engage inputs
		for (NodeGene node : inputNodes) {
			node.engage();
		}

		// Engage bias
		this.biasNode.engage();

		// Engage hidden nodes
		while (hiddenNodes.size() > 0) {

			Iterator<NodeGene> iter = hiddenNodes.iterator();
			while (iter.hasNext()) {

				NodeGene node = iter.next();

				// Collect all connections which have the current node as output
				List<ConnectionGene> nodeConnections = new ArrayList<>();
				for (ConnectionGene connection : this.connections.values()) {

					if (connection.getTo() == node && connection.isEnabled()) {
						nodeConnections.add(connection);
					}
				}

				// Check whether there is one connection with a payload
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
					node.engage();
					iter.remove();
				}
			}
		}

		// Engage output nodes
		for (NodeGene node : outputNodes) {

			// Collect all connections which have the current node as output
			List<ConnectionGene> nodeConnections = new ArrayList<>();
			double inputSum = 0;
			for (ConnectionGene connection : this.connections.values()) {
				if (connection.getTo() == node) {
					nodeConnections.add(connection);
					inputSum += connection.payload;
				}
			}

			node.inputSum = inputSum;
			node.outputValue = inputSum;
			node.activate();
		}

		// Reset connection activation
		for (ConnectionGene connection : this.connections.values()) {
			connection.activated = false;
		}

		double[] outputs = new double[this.anzOutputs];
		int i = 0;
		for (NodeGene output : outputNodes) {
			outputs[i++] = output.outputValue;
		}

		for (NodeGene node : this.nodes.values()) {
			node.inputSum = 0;
		}

		return outputs;
	}

	/**
	 * Sets up the neural network as a list of nodes in order to be engange (INPUT
	 * -> HIDDEN -> OUTPUT
	 *
	 */
	public void generateNetwork() {
		this.connectNodes();
		this.network = new ArrayList<>();

		for (NodeGeneType layerType : NodeGeneType.values()) {
			for (NodeGene node : this.nodes.values()) {
				if (node.getType() == layerType) {
					this.network.add(node);
				}
			}
		}
	}

	/**
	 * @param gene
	 */
	public void addNodeGene(NodeGene node) {
		this.nodes.put(node.getNumber(), node);
	}

	/**
	 * @param connection
	 */
	public void addConnectionGene(ConnectionGene connection) {
		this.connections.put(connection.getInnvoationNumber(), connection);
	}

	/**
	 * @param random
	 * @param innovationHistory
	 * @param connectionInnovation
	 */
	public void addConnectionMutation(Random random, int maxAttempts, Map<Integer, List<ConnectionHistory>> innovationHistory) {

		// Get the highest connection innovation
		this.updateConnectionInnovation();

		int tries = 0;
		boolean success = false;
		while (tries < maxAttempts && !success) {

			++tries;
			NodeGene node1 = this.getRandomNode();
			NodeGene node2 = this.getRandomNode();

			// Swap for correct node order if neccessary
			NodeGene first = null;
			NodeGene second = null;
			boolean reversed = node2.before(node1);
			if (reversed) {
				first = node2;
				second = node1;
			} else {
				first = node1;
				second = node2;
			}

			if (this.connectionExists(first, second) || this.connectionImpossible(first, second) || this.connectionCreatesCircular(first, second)) {
				continue;
			}

			double weight = -2 + random.nextFloat() * 4f;
			int connectionInnovationNumber = this.getConnectionInnovationNumber(innovationHistory, first, second);
			ConnectionGene connection = new ConnectionGene(first, second, weight, true, connectionInnovationNumber);

			this.connections.put(connection.getInnvoationNumber(), connection);

			success = true;
		}

		if (!success) {
//			System.out.println("Couldn't add more connections");
		}

		this.generateNetwork();
	}

	/**
	 *
	 */
	private void updateConnectionInnovation() {

		int highestInnovation = 0;
		for (ConnectionGene connection : this.connections.values()) {
			if (connection.getInnvoationNumber() > highestInnovation) {
				highestInnovation = connection.getInnvoationNumber();
			}
		}

		this.connectionInnovation.setCurrent(++highestInnovation);
	}

	/**
	 * @return
	 */
	private NodeGene getRandomNode() {

		Random random = new SecureRandom();
		List<NodeGene> valuesList = new ArrayList<>(this.nodes.values());

		NodeGene node = valuesList.get(random.nextInt(valuesList.size()));
		while (node == this.biasNode) {
			node = valuesList.get(random.nextInt(valuesList.size()));
		}

		return node;
	}

	/**
	 * @param node1
	 * @param node2
	 * @return whether the connection already exists
	 */
	private boolean connectionExists(NodeGene node1, NodeGene node2) {

		// No connections exist
		if (this.connections == null || this.connections.size() <= 0) {
			return false;
		}

		for (ConnectionGene connection : this.connections.values()) {

			if (connection.getFrom().equals(node1) && connection.getTo().equals(node2)) {
				return true;
			} else if (connection.getFrom().equals(node2) && connection.getTo().equals(node1)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param node1
	 * @param node2
	 * @return whether the connection is impossible
	 */
	private boolean connectionImpossible(NodeGene node1, NodeGene node2) {

		// Inputs and outputs can't be connected to each other
		if (node1.getType() == NodeGeneType.INPUT && node2.getType() == NodeGeneType.INPUT) {
			return true;
		} else if (node1.getType() == NodeGeneType.OUTPUT && node2.getType() == NodeGeneType.OUTPUT) {
			return true;
		} else if (node1.getType() != NodeGeneType.HIDDEN && node2.getType() != NodeGeneType.HIDDEN) {
			return true;
		} else if (node1.getType() == NodeGeneType.BIAS && node2.getType() == NodeGeneType.BIAS) {
			return true;
		}

		if (node1 == node2) {
			return true;
		}

		return false;
	}

	/**
	 * @param node1
	 * @param node2
	 * @return whether a connection between those two nodes create a circular connection
	 */
	public boolean connectionCreatesCircular(NodeGene node1, NodeGene node2) {

		List<ConnectionGene> outConnections = new ArrayList<>();
		for (ConnectionGene connection : this.connections.values()) {
			if (connection.getFrom() == node2 && connection.isEnabled()) {
				outConnections.add(connection);
			}
		}

		for (ConnectionGene connection : outConnections) {

			// Can't create circular before input node
			NodeGene from = connection.getFrom();
			if (from.getType() == NodeGeneType.INPUT || from.getType() == NodeGeneType.BIAS) {
				continue;
			}

			// Can't create circular after output node
			if (connection.getTo().getType() == NodeGeneType.OUTPUT) {
				continue;
			}

			if (this.connectionLeadsToNode(connection, node1)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param node1
	 * @return whether the given connection at some point leads to the given node
	 */
	private boolean connectionLeadsToNode(ConnectionGene connection, NodeGene node) {

		NodeGene toNode = connection.getTo();
		if (toNode == node) {
			return true;
		}

		List<ConnectionGene> outConnections = new ArrayList<>();
		for (ConnectionGene outConnection : this.connections.values()) {
			if (outConnection.getFrom() == toNode && outConnection.isEnabled()) {
				outConnections.add(outConnection);
			}
		}

		for (ConnectionGene toNodeOutConnection : outConnections) {

			// Can't create circular before input node
			NodeGene from = toNodeOutConnection.getFrom();
			if (from.getType() == NodeGeneType.INPUT || from.getType() == NodeGeneType.BIAS) {
				continue;
			}

			// Can't create circular after output node
			if (toNodeOutConnection.getTo().getType() == NodeGeneType.OUTPUT) {
				continue;
			}

			if (this.connectionLeadsToNode(toNodeOutConnection, node)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param random
	 * @param innovationHistory
	 */
	public void addNodeMutation(Random random, Map<Integer, List<ConnectionHistory>> innovationHistory) {

		// Get the highest innovations
		this.updateNodeInnovation();
		this.updateConnectionInnovation();

		// Get a random connection to create a node in between
		List<ConnectionGene> values = new ArrayList<>(this.connections.values());
		ConnectionGene connection = values.get(random.nextInt(values.size()));

		// Don't disconnect bias
		while (connection.getFrom() == this.biasNode) {
			connection = values.get(random.nextInt(values.size()));
		}

		NodeGene in = connection.getFrom();
		NodeGene out = connection.getTo();

		connection.setEnabled(false);

		NodeGene newNode = new NodeGene(NodeGeneType.HIDDEN, this.nodeInnovation.getNext());

		int connectionInnovationNumber = this.getConnectionInnovationNumber(innovationHistory, in, newNode);
		ConnectionGene inToNew = new ConnectionGene(in, newNode, 1f, true, connectionInnovationNumber);

		connectionInnovationNumber = this.getConnectionInnovationNumber(innovationHistory, newNode, out);
		ConnectionGene newToOut = new ConnectionGene(newNode, out, connection.getWeight(), true, connectionInnovationNumber);

		connectionInnovationNumber = this.getConnectionInnovationNumber(innovationHistory, this.biasNode, out);
		ConnectionGene biasToOut = new ConnectionGene(this.biasNode, out, 0, true, connectionInnovationNumber);

		this.nodes.put(newNode.getNumber(), newNode);
		this.connections.put(inToNew.getInnvoationNumber(), inToNew);
		this.connections.put(newToOut.getInnvoationNumber(), newToOut);
		this.connections.put(biasToOut.getInnvoationNumber(), biasToOut);

		this.generateNetwork();
	}

	/**
	 *
	 */
	private void updateNodeInnovation() {

		int highestInnovation = 0;
		for (NodeGene node : this.nodes.values()) {
			if (node.getNumber() > highestInnovation) {
				highestInnovation = node.getNumber();
			}
		}

		this.nodeInnovation.setCurrent(++highestInnovation);
	}

	/**
	 * This method assumes that parent1 is always the most fit genome. It is
	 * producing a crossover from two matching genomes to generate a new one.
	 *
	 * @param fitParent   more fit parent
	 * @param unfitParent less fit parent
	 * @return
	 */
	public static Genome crossover(Genome fitParent, Genome unfitParent) {

		Genome childGenome = new Genome(fitParent.anzInputs, fitParent.anzOutputs);
		Random random = new SecureRandom();

		childGenome.nodes.clear();
		childGenome.connections.clear();

		// Copy nodes from the most fit parent
		for (NodeGene node : fitParent.getNodeGenes().values()) {
			NodeGene copy = node.copy();
			childGenome.addNodeGene(copy);
			if (node.getType() == NodeGeneType.BIAS) {
				childGenome.biasNode = copy;
			}
		}

		// Copy matching connection genes
		Map<Integer, Boolean> childConnectionEnabled = new HashMap<>();
		for (ConnectionGene fitParentConnection : fitParent.getConnectionGenes().values()) {

			// Check if the both parents have the connection with same from & to node innovationNumbers
			boolean matchingGene = false;
			ConnectionGene unfitConnection = unfitParent.getConnectionGenes().get(fitParentConnection.getInnvoationNumber());
			if (unfitConnection != null && fitParent.containsConnection(unfitConnection.getFrom().getNumber(), unfitConnection.getTo().getNumber())) {
				matchingGene = true;
			}

			if (matchingGene) {

				// Randomly take connection from first or second parent
				ConnectionGene sourceConnection = null;
				ConnectionGene unfitParentConnection = unfitConnection;
				if (random.nextBoolean()) {
					sourceConnection = fitParentConnection;
				} else {
					sourceConnection = unfitParentConnection;
				}

				// If either of the parents connection is disabled there is a chance to disable it for the child
				boolean enabled = true;
				if (!fitParentConnection.isEnabled() || !unfitParentConnection.isEnabled()) {
					if (random.nextFloat() <= Property.DISABLE_CONNECTION_ON_CHILD_RATE.getValue()) {
						enabled = false;
					}
				}
				childConnectionEnabled.put(sourceConnection.getInnvoationNumber(), enabled);

				childGenome.addConnectionGene(sourceConnection);
			} else {

				// Disjoint/Excess ConnectionGene (Not present in unfit parent) will always be
				// taken from the more fit parent
				childConnectionEnabled.put(fitParentConnection.getInnvoationNumber(), fitParentConnection.isEnabled());
				childGenome.addConnectionGene(fitParentConnection);
			}
		}

		// Clone all inherited connections to connect the childs new nodes
		List<ConnectionGene> newConnetions = new ArrayList<>();
		for (ConnectionGene connection : childGenome.connections.values()) {

			NodeGene from = childGenome.nodes.get(connection.getFrom().getNumber());
			NodeGene to = childGenome.nodes.get(connection.getTo().getNumber());
			Boolean enabled = childConnectionEnabled.get(connection.getInnvoationNumber());

			ConnectionGene newConnection = new ConnectionGene(from, to, connection.getWeight(), enabled, connection.getInnvoationNumber());
			newConnetions.add(newConnection);
		}

		// Remove parents template connections after its own were created
		childGenome.connections.clear();

		// Add the new connections
		for (ConnectionGene newConnection : newConnetions) {

			NodeGene from = newConnection.getFrom();
			NodeGene to = newConnection.getTo();

			if (childGenome.connectionExists(from, to)) {
				continue;
			}

			childGenome.addConnectionGene(newConnection);
		}

		childGenome.connectNodes();
		childGenome.validateIntegrity();

		return childGenome;
	}

	/**
	 * @param fromId
	 * @param toId
	 * @return
	 */
	private boolean containsConnection(int fromId, int toId) {

		for (ConnectionGene connection : this.connections.values()) {
			if (connection.getFrom().getNumber() == fromId && connection.getTo().getNumber() == toId) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 */
	public void validateIntegrity() {

		for (ConnectionGene connection : this.getConnectionGenes().values()) {

			NodeGene in = connection.getFrom();
			NodeGene out = connection.getTo();

			boolean foundIn = false;
			boolean foundOut = false;
			for (NodeGene node : this.getNodeGenes().values()) {
				if (node == in || in.getType() == NodeGeneType.BIAS) {
					foundIn = true;
				}
				if (node == out) {
					foundOut = true;
				}
			}

			if (!foundIn || !foundOut) {
				System.out.println("INTEGRITY FAILURE");
				break;
			}
		}
	}

	/**
	 * @param innovationHistory
	 */
	public void mutate(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		Random random = new SecureRandom();
		if (random.nextDouble() <= Property.WEIGHT_MUTATION_RATE.getValue()) {
			for (ConnectionGene connection : this.connections.values()) {
				if (random.nextFloat() < Property.PROBABILITY_PERTURBING.getValue()) {

					// Uniformly perturbed weight
					double value = Property.UNIFORMLY_PERTURB_WEIGHT_RANGE.getValue();
					double min = -1 * (value / 2);
					double disturbance = min + random.nextFloat() * value;
					connection.setWeight(connection.getWeight() + disturbance);
				} else {

					// Assign new weight
					double max = Property.WEIGHT_RANDOM_RANGE.getValue();
					double min = -1 * Property.WEIGHT_RANDOM_RANGE.getValue();
					double weight = min + (max - min) * random.nextFloat();
					connection.setWeight(weight);
				}
			}
		}

		// Add mutations for the child genome
		if (random.nextFloat() < Property.ADD_CONNECTION_RATE.getValue()) {
			this.addConnectionMutation(random, 10, innovationHistory);
		}
		if (random.nextFloat() < Property.ADD_NODE_RATE.getValue()) {
			this.addNodeMutation(random, innovationHistory);
		}
	}

	/**
	 * @param innovationHistory
	 * @param from
	 * @param to
	 * @return the innovationNumber for the connection between from and to
	 */
	private int getConnectionInnovationNumber(Map<Integer, List<ConnectionHistory>> innovationHistory, NodeGene from, NodeGene to) {

		boolean newConnection = true;
		int connectionInnovationNumber = this.connectionInnovation.getCurrent() + 1;
		List<ConnectionHistory> possibleMatches = innovationHistory.get(this.getConnectionGenes().size());
		if (possibleMatches != null) {
			for (int i = 0; i < possibleMatches.size(); ++i) {
				if (possibleMatches.get(i).matches(this, from, to)) {
					newConnection = false;
					return possibleMatches.get(i).innovationNumber;
				}
			}
		}

		if (newConnection) {

			ArrayList<Integer> innovationNumbers = new ArrayList<>();
			for (ConnectionGene connection : this.connections.values()) {
				innovationNumbers.add(connection.getInnvoationNumber());
			}

			// Save new innovation history
			List<ConnectionHistory> existingHistories = innovationHistory.get(innovationNumbers.size());
			if (existingHistories == null) {
				existingHistories = new ArrayList<>();
			}
			existingHistories.add(new ConnectionHistory(from.getNumber(), to.getNumber(), connectionInnovationNumber, innovationNumbers));
			innovationHistory.put(innovationNumbers.size(), existingHistories);

			this.connectionInnovation.getNext();
		}

		return connectionInnovationNumber;
	}

	/**
	 * @param type
	 * @return
	 */
	public List<NodeGene> getNodesByType(NodeGeneType type) {

		List<NodeGene> nodes = new ArrayList<>();
		for (NodeGene node : this.nodes.values()) {
			if (node.getType() == type && node != this.biasNode) {
				nodes.add(node);
			}
		}

		return nodes;
	}

	/**
	 * @return
	 */
	public Genome copy() {

		Genome genome = new Genome();
		genome.anzInputs = this.anzInputs;
		genome.anzOutputs = this.anzOutputs;

		for (NodeGene node : this.nodes.values()) {

			NodeGene copy = node.copy();
			genome.nodes.put(copy.getNumber(), copy);

			if (copy.getType() == NodeGeneType.BIAS) {
				genome.biasNode = copy;
			}
		}

		for (ConnectionGene connection : this.connections.values()) {

			NodeGene from = genome.nodes.get(connection.getFrom().getNumber());
			NodeGene to = genome.nodes.get(connection.getTo().getNumber());

			genome.connections.put(connection.getInnvoationNumber(), connection.copy(from, to));
		}

		genome.nodeInnovation = this.nodeInnovation;
		genome.connectionInnovation = this.connectionInnovation;
		genome.generateNetwork();

		return genome;
	}

	/**
	 * @return the nodes
	 */
	public Map<Integer, NodeGene> getNodeGenes() {
		return this.nodes;
	}

	/**
	 * @return the connections
	 */
	public Map<Integer, ConnectionGene> getConnectionGenes() {
		return this.connections;
	}

}
