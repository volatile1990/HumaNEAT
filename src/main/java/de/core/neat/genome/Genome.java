package de.core.neat.genome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.core.global.Random;
import de.core.neat.genes.Counter;
import de.core.neat.genes.connection.ConnectionGene;
import de.core.neat.genes.node.NodeGene;
import de.core.neat.genes.node.NodeGeneType;

/**
 * @author muellermak
 *
 *         A Genome is representating a whole neural network
 */
public class Genome {

	/**
	 * Neural Network components
	 */
	public Map<Integer, NodeGene> nodes;
	public Map<Integer, ConnectionGene> connections;
	public NodeGene biasNode;

	/**
	 * The linked network for feeding forward
	 */
	public List<NodeGene> network;

	/**
	 * Innovation counters
	 */
	public Counter nodeInnovation;
	public Counter connectionInnovation;

	/**
	 * Amount of inputs and outputs this genome takes/produces
	 */
	public int anzInputs;
	public int anzOutputs;

	/**
	 * Fitness: unadjustedFitness / nodes.size()
	 * unadjustedFitness: The result calculated by 'calculateFitness()'
	 */
	public double fitness;
	public double unadjustedFitness;

	/**
	 * Genome related operators
	 */
	private GenomeManager manager;
	private GenomeLinker linker;
	private GenomeValidator validator;
	private GenomeFeeder feeder;
	private GenomeMutator mutator;

	/**
	 *
	 */
	public Genome() {

		nodes = new HashMap<>();
		connections = new HashMap<>();

		nodeInnovation = new Counter();
		connectionInnovation = new Counter();

		fitness = 0;
		unadjustedFitness = 0;

		manager = new GenomeManager(this);
		linker = new GenomeLinker(this);
		validator = new GenomeValidator(this);
		feeder = new GenomeFeeder(this);
		mutator = new GenomeMutator(this);
	}

	/**
	 * @param inputs
	 * @param output
	 */
	public Genome(int anzInputs, int anzOutputs) {

		this();

		this.anzInputs = anzInputs;
		this.anzOutputs = anzOutputs;

		manager.initialize();
	}

	/**
	 * @param gene
	 */
	public void addNodeGene(NodeGene node) {
		nodes.put(node.innovationNumber, node);
	}

	/**
	 * @param connection
	 */
	public void addConnectionGene(ConnectionGene connection) {
		connections.put(connection.innvoationNumber, connection);
	}

	/**
	 * @return
	 */
	public NodeGene getRandomNode() {

		List<NodeGene> valuesList = new ArrayList<>(nodes.values());

		NodeGene node = Random.random(valuesList);
		while (node == biasNode) {
			node = Random.random(valuesList);
		}

		return node;
	}

	/**
	 * @param fromId
	 * @param toId
	 * @return
	 */
	public boolean containsConnection(int fromId, int toId) {

		for (ConnectionGene connection : connections.values()) {
			if (connection.from.innovationNumber == fromId && connection.to.innovationNumber == toId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param type
	 * @return
	 */
	public List<NodeGene> getNodesByType(NodeGeneType type) {

		List<NodeGene> nodes = new ArrayList<>();
		for (NodeGene node : this.nodes.values()) {
			if (node.type == type && node != biasNode) {
				nodes.add(node);
			}
		}

		return nodes;
	}

	/**
	 * @return a copy of the genome
	 */
	public Genome copy() {

		// Create new genome
		Genome genome = new Genome();
		genome.anzInputs = anzInputs;
		genome.anzOutputs = anzOutputs;

		// Copy all nodes
		for (NodeGene node : nodes.values()) {

			NodeGene copy = node.copy();
			genome.nodes.put(copy.innovationNumber, copy);

			if (copy.type == NodeGeneType.BIAS) {
				genome.biasNode = copy;
			}
		}

		// Copy all connection
		for (ConnectionGene connection : connections.values()) {

			ConnectionGene copy = connection.copy();

			// Use the already copied nodes as from/to
			NodeGene from = genome.nodes.get(connection.from.innovationNumber);
			NodeGene to = genome.nodes.get(connection.to.innovationNumber);
			copy.from = from;
			copy.to = to;

			genome.connections.put(connection.innvoationNumber, copy);
		}

		genome.nodeInnovation = nodeInnovation;
		genome.connectionInnovation = connectionInnovation;
		genome.linker.generateNetwork();

		return genome;
	}

	/**
	 * @return the manager
	 */
	public GenomeManager getManager() {
		return manager;
	}

	/**
	 * @return the linker
	 */
	public GenomeLinker getLinker() {
		return linker;
	}

	/**
	 * @return the validator
	 */
	public GenomeValidator getValidator() {
		return validator;
	}

	/**
	 * @return the feeder
	 */
	public GenomeFeeder getFeeder() {
		return feeder;
	}

	/**
	 * @return
	 */
	public GenomeMutator getMutator() {
		return mutator;
	}

}
