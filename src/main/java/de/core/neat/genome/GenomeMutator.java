package de.core.neat.genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.core.global.Random;
import de.core.neat.Property;
import de.core.neat.genes.connection.ConnectionGene;
import de.core.neat.genes.connection.ConnectionHistory;
import de.core.neat.genes.node.NodeGene;
import de.core.neat.genes.node.NodeGeneType;

/**
 * @author MannoR
 *
 *         Executes all available genome mutations
 */
public class GenomeMutator {

	private Genome genome;

	/**
	 * @param genome
	 */
	public GenomeMutator(Genome genome) {
		this.genome = genome;
	}

	/**
	 * Adds a new node at a random position:
	 * Old Structure: Node -> Connection -> Node
	 * New Structure: Node -> Connection -> NewNode -> Connection -> Node
	 *
	 * @param random
	 * @param innovationHistory
	 */
	public void addNodeMutation(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		// Get the highest innovations
		genome.getManager().updateNodeInnovation();
		genome.getManager().updateConnectionInnovation();

		// Get a random connection to create a node in between
		List<ConnectionGene> values = new ArrayList<>(genome.connections.values());
		ConnectionGene connection = Random.random(values);

		// Don't disconnect bias
		while (connection.from == genome.biasNode) {
			connection = Random.random(values);
		}

		NodeGene in = connection.from;
		NodeGene out = connection.to;

		// Disable old connection
		connection.enabled = false;

		// Create new node
		NodeGene newNode = new NodeGene(NodeGeneType.HIDDEN, genome.nodeInnovation.getNext());

		// Create in to new node connection
		int connectionInnovationNumber = genome.getManager().getConnectionInnovationNumber(innovationHistory, in, newNode);
		ConnectionGene inToNew = new ConnectionGene(in, newNode, 1f, true, connectionInnovationNumber);

		// Create new node to out connection
		connectionInnovationNumber = genome.getManager().getConnectionInnovationNumber(innovationHistory, newNode, out);
		ConnectionGene newToOut = new ConnectionGene(newNode, out, connection.weight, true, connectionInnovationNumber);

		// Create bias to out connection
		connectionInnovationNumber = genome.getManager().getConnectionInnovationNumber(innovationHistory, genome.biasNode, out);
		ConnectionGene biasToOut = new ConnectionGene(genome.biasNode, out, 0, true, connectionInnovationNumber);

		// Add new node and connections
		genome.nodes.put(newNode.innovationNumber, newNode);
		genome.connections.put(inToNew.innvoationNumber, inToNew);
		genome.connections.put(newToOut.innvoationNumber, newToOut);
		genome.connections.put(biasToOut.innvoationNumber, biasToOut);

		// Link network
		genome.getLinker().generateNetwork();
	}

	/**
	 * Randomly connects two unconnected nodes
	 *
	 * @param random
	 * @param innovationHistory
	 * @param connectionInnovation
	 */
	public void addConnectionMutation(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		// Get the highest connection innovation
		genome.getManager().updateConnectionInnovation();

		int tries = 0;
		boolean success = false;
		while (tries < Property.ADD_CONNECTION_MAX_ATTEMPTS.getValue() && !success) {

			++tries;
			NodeGene node1 = genome.getRandomNode();
			NodeGene node2 = genome.getRandomNode();

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

			if (!genome.getValidator().connectionAllowed(first, second)) {
				continue;
			}

			double max = Property.WEIGHT_RANDOM_RANGE.getValue();
			double min = -1 * Property.WEIGHT_RANDOM_RANGE.getValue();
			double weight = Random.random(min, max);

			int connectionInnovationNumber = genome.getManager().getConnectionInnovationNumber(innovationHistory, first, second);
			ConnectionGene connection = new ConnectionGene(first, second, weight, true, connectionInnovationNumber);

			genome.connections.put(connection.innvoationNumber, connection);

			success = true;
		}

		if (!success) {
//			System.out.println("Couldn't add more connections");
		}

		// Link network
		genome.getLinker().generateNetwork();
	}

	/**
	 * Mutates the genomes connection weights
	 *
	 * @param innovationHistory
	 */
	public void mutate(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		if (Random.success(Property.WEIGHT_MUTATION_RATE.getValue())) {
			for (ConnectionGene connection : genome.connections.values()) {
				if (Random.success(Property.PROBABILITY_PERTURBING.getValue())) {

					// Uniformly perturb weight
					double min = -1 * Property.UNIFORMLY_PERTURB_WEIGHT_RANGE.getValue();
					double max = Property.UNIFORMLY_PERTURB_WEIGHT_RANGE.getValue();
					double disturbance = Random.random(min, max);
					connection.weight += disturbance;

				} else {

					// Assign new weight
					double max = Property.WEIGHT_RANDOM_RANGE.getValue();
					double min = -1 * Property.WEIGHT_RANDOM_RANGE.getValue();
					double weight = Random.random(min, max);
					connection.weight = weight;
				}
			}
		}

		// Add mutations for the child genome
		if (Random.success(Property.ADD_CONNECTION_RATE.getValue())) {
			addConnectionMutation(innovationHistory);
		}
		if (Random.success(Property.ADD_NODE_RATE.getValue())) {
			addNodeMutation(innovationHistory);
		}
	}

}
