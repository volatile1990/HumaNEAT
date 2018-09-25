package de.humaneat.core.lstm.genome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.humaneat.core.global.Random;
import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.global.genome.DefaultGenome;
import de.humaneat.core.lstm.genes.connection.LstmConnectionGene;
import de.humaneat.core.lstm.genes.node.LstmNodeGene;
import de.humaneat.core.neat.genes.Counter;

/**
 * @author muellermak
 *
 */
public class LstmGenome extends DefaultGenome {

	/**
	 * Neural Network components
	 */
	public Map<Integer, LstmNodeGene> nodes;
	public Map<Integer, LstmConnectionGene> connections;
	public LstmNodeGene biasNode;

	/**
	 * The linked network for feeding forward
	 */
	public List<LstmNodeGene> network;

	/**
	 * Genome related operators
	 */
	private LstmGenomeManager manager;
	private LstmGenomeLinker linker;
	private LstmGenomeValidator validator;
	private LstmGenomeFeeder feeder;
	private LstmGenomeMutator mutator;
	private LstmGenomeHatchery hatchery;

	/**
	 *
	 */
	public LstmGenome() {

		nodes = new HashMap<>();
		connections = new HashMap<>();

		nodeInnovation = new Counter();
		connectionInnovation = new Counter();

		fitness = 0;
		unadjustedFitness = 0;

		manager = new LstmGenomeManager(this);
		linker = new LstmGenomeLinker(this);
		validator = new LstmGenomeValidator(this);
		feeder = new LstmGenomeFeeder(this);
		mutator = new LstmGenomeMutator(this);
		hatchery = new LstmGenomeHatchery(this);
	}

	/**
	 * @param inputs
	 * @param output
	 */
	public LstmGenome(int anzInputs, int anzOutputs) {

		this();

		this.anzInputs = anzInputs;
		this.anzOutputs = anzOutputs;

		manager.initialize();
	}

	/**
	 * @param gene
	 */
	public void addLstmNodeGene(LstmNodeGene node) {
		nodes.put(node.innovationNumber, node);
	}

	/**
	 * @param connection
	 */
	public void addLstmConnectionGene(LstmConnectionGene connection) {
		connections.put(connection.innvoationNumber, connection);
	}

	/**
	 * @param type
	 * @return
	 */
	public List<LstmNodeGene> getNodesByType(NodeGeneType type) {

		List<LstmNodeGene> nodes = new ArrayList<>();
		for (LstmNodeGene node : this.nodes.values()) {
			if (node.type == type && node != biasNode) {
				nodes.add(node);
			}
		}

		return nodes;
	}

	/**
	 * @return
	 */
	public LstmNodeGene getRandomNode() {

		List<LstmNodeGene> valuesList = new ArrayList<>(nodes.values());

		LstmNodeGene node = Random.random(valuesList);
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

		for (LstmConnectionGene connection : connections.values()) {
			if (connection.from.innovationNumber == fromId && connection.to.innovationNumber == toId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return a copy of the genome
	 */
	public LstmGenome copy() {

		// Create new genome
		LstmGenome genome = new LstmGenome();
		genome.anzInputs = anzInputs;
		genome.anzOutputs = anzOutputs;

		// Copy all nodes
		for (LstmNodeGene node : nodes.values()) {

			LstmNodeGene copy = node.copy();
			genome.nodes.put(copy.innovationNumber, copy);

			if (copy.type == NodeGeneType.BIAS) {
				genome.biasNode = copy;
			}
		}

		// Copy all connection
		for (LstmConnectionGene connection : connections.values()) {

			LstmConnectionGene copy = connection.copy();

			// Use the already copied nodes as from/to
			LstmNodeGene from = genome.nodes.get(connection.from.innovationNumber);
			LstmNodeGene to = genome.nodes.get(connection.to.innovationNumber);
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
	public LstmGenomeManager getManager() {
		return manager;
	}

	/**
	 * @return the linker
	 */
	public LstmGenomeLinker getLinker() {
		return linker;
	}

	/**
	 * @return the validator
	 */
	public LstmGenomeValidator getValidator() {
		return validator;
	}

	/**
	 * @return the feeder
	 */
	public LstmGenomeFeeder getFeeder() {
		return feeder;
	}

	/**
	 * @return
	 */
	public LstmGenomeMutator getMutator() {
		return mutator;
	}

	/**
	 * @return
	 */
	public LstmGenomeHatchery getHatchery() {
		return hatchery;
	}

}
