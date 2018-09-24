package de.humaneat.core.lstm.genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.humaneat.core.global.Random;
import de.humaneat.core.global.components.connection.ConnectionHistory;
import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.global.genome.DefaultGenomeMutator;
import de.humaneat.core.lstm.genes.connection.LstmConnectionGene;
import de.humaneat.core.lstm.genes.node.LstmNodeGene;
import de.humaneat.core.neat.Property;

/**
 * @author muellermak
 *
 */
public class LstmGenomeMutator implements DefaultGenomeMutator {

	private LstmGenome genome;

	/**
	 * @param genome
	 */
	public LstmGenomeMutator(LstmGenome genome) {
		this.genome = genome;
	}

	/**
	 * @param innovationHistory
	 */
	@Override
	public void addNodeMutation(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		// Get the highest innovations
		genome.getManager().updateNodeInnovation();
		genome.getManager().updateConnectionInnovation();

		// Get a random connection to create a node in between
		List<LstmConnectionGene> values = new ArrayList<>(genome.connections.values());
		LstmConnectionGene connection = Random.random(values);

		// Don't disconnect bias
		while (connection.from == genome.biasNode) {
			connection = Random.random(values);
		}

		LstmNodeGene in = connection.from;
		LstmNodeGene out = connection.to;

		// Disable old connection
		connection.enabled = false;

		// Create new node
		LstmNodeGene newNode = new LstmNodeGene(NodeGeneType.HIDDEN, genome.nodeInnovation.getNext());

		// Create in to new node connection
		int connectionInnovationNumber = genome.getManager().getConnectionInnovationNumber(innovationHistory, in.innovationNumber, newNode.innovationNumber);
		LstmConnectionGene inToNew = new LstmConnectionGene(in, newNode, true, connectionInnovationNumber);

		// Create new node to out connection
		connectionInnovationNumber = genome.getManager().getConnectionInnovationNumber(innovationHistory, newNode.innovationNumber, out.innovationNumber);
		LstmConnectionGene newToOut = new LstmConnectionGene(newNode, out, true, connectionInnovationNumber);

		// Create bias to out connection
		connectionInnovationNumber = genome.getManager().getConnectionInnovationNumber(innovationHistory, genome.biasNode.innovationNumber, out.innovationNumber);
		LstmConnectionGene biasToOut = new LstmConnectionGene(genome.biasNode, out, true, connectionInnovationNumber);

		// Add new node and connections
		genome.nodes.put(newNode.innovationNumber, newNode);
		genome.connections.put(inToNew.innvoationNumber, inToNew);
		genome.connections.put(newToOut.innvoationNumber, newToOut);
		genome.connections.put(biasToOut.innvoationNumber, biasToOut);

		// Link network
		genome.getLinker().generateNetwork();
	}

	/**
	 * @param innovationHistory
	 */
	@Override
	public void addConnectionMutation(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		// Get the highest connection innovation
		genome.getManager().updateConnectionInnovation();

		int tries = 0;
		boolean success = false;
		while (tries < Property.ADD_CONNECTION_MAX_ATTEMPTS.getValue() && !success) {

			++tries;
			LstmNodeGene node1 = genome.getRandomNode();
			LstmNodeGene node2 = genome.getRandomNode();

			// Swap for correct node order if neccessary
			LstmNodeGene first = null;
			LstmNodeGene second = null;
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

			int connectionInnovationNumber = genome.getManager().getConnectionInnovationNumber(innovationHistory, first.innovationNumber, second.innovationNumber);
			LstmConnectionGene connection = new LstmConnectionGene(first, second, true, connectionInnovationNumber);

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
	 * @param innovationHistory
	 */
	@Override
	public void mutate(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		if (Random.success(Property.WEIGHT_MUTATION_RATE.getValue())) {
			for (LstmNodeGene node : genome.nodes.values()) {

				if (Random.success(Property.PROBABILITY_PERTURBING.getValue())) {

					// Uniformly perturb weights
					double min = -1 * Property.UNIFORMLY_PERTURB_WEIGHT_RANGE.getValue();
					double max = Property.UNIFORMLY_PERTURB_WEIGHT_RANGE.getValue();

					node.weight.inputWeights.forgetGateWeight += Random.random(min, max);
					node.weight.inputWeights.inputGateWeight += Random.random(min, max);
					node.weight.inputWeights.outputGateWeight += Random.random(min, max);
					node.weight.inputWeights.selectGateWeight += Random.random(min, max);

					node.weight.recurrentWeights.forgetGateWeight += Random.random(min, max);
					node.weight.recurrentWeights.inputGateWeight += Random.random(min, max);
					node.weight.recurrentWeights.outputGateWeight += Random.random(min, max);
					node.weight.recurrentWeights.selectGateWeight += Random.random(min, max);

				} else {

					// Assign new weights
					double min = -1 * Property.WEIGHT_RANDOM_RANGE.getValue();
					double max = Property.WEIGHT_RANDOM_RANGE.getValue();

					node.weight.inputWeights.forgetGateWeight = Random.random(min, max);
					node.weight.inputWeights.inputGateWeight = Random.random(min, max);
					node.weight.inputWeights.outputGateWeight = Random.random(min, max);
					node.weight.inputWeights.selectGateWeight = Random.random(min, max);

					node.weight.recurrentWeights.forgetGateWeight = Random.random(min, max);
					node.weight.recurrentWeights.inputGateWeight = Random.random(min, max);
					node.weight.recurrentWeights.outputGateWeight = Random.random(min, max);
					node.weight.recurrentWeights.selectGateWeight = Random.random(min, max);

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
