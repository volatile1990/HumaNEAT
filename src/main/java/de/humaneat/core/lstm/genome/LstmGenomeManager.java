package de.humaneat.core.lstm.genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.humaneat.core.global.components.connection.ConnectionHistory;
import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.global.genome.DefaultGenomeManager;
import de.humaneat.core.lstm.genes.connection.LstmConnectionGene;
import de.humaneat.core.lstm.genes.node.LstmNodeGene;

/**
 * @author muellermak
 *
 */
public class LstmGenomeManager implements DefaultGenomeManager {

	private LstmGenome genome;

	/**
	 * @param genome
	 */
	public LstmGenomeManager(LstmGenome genome) {
		this.genome = genome;
	}

	/**
	 * 
	 */
	@Override
	public void initialize() {

		// Connect all inputs to all outputs
		connectInputsAndOutputs(generateInputs(), generateOutputs());

		// Add bias node
		generateAndConnectBias();

		// Generate initial network for feed forward
		genome.getLinker().generateNetwork();
	}

	/**
	 * Generates all input nodes
	 *
	 * @param genome
	 * @return
	 */
	private List<LstmNodeGene> generateInputs() {

		List<LstmNodeGene> inputNodes = new ArrayList<>();
		for (int i = 0; i < genome.anzInputs; ++i) {

			// Get next node innovation number
			int nextInnovation = genome.nodeInnovation.getNext();

			// Create input node
			LstmNodeGene input = new LstmNodeGene(NodeGeneType.INPUT, nextInnovation);

			// Add input node
			genome.nodes.put(nextInnovation, input);
			inputNodes.add(input);
		}

		return inputNodes;
	}

	/**
	 * Generates all output nodes
	 *
	 * @param genome
	 * @return
	 */
	private List<LstmNodeGene> generateOutputs() {

		List<LstmNodeGene> outputNodes = new ArrayList<>();
		for (int i = 0; i < genome.anzOutputs; ++i) {

			// Get next node innovatin number
			int nextInnovation = genome.nodeInnovation.getNext();

			// Create output node
			LstmNodeGene output = new LstmNodeGene(NodeGeneType.OUTPUT, nextInnovation);

			// Add output node
			genome.nodes.put(nextInnovation, output);
			outputNodes.add(output);
		}

		return outputNodes;
	}

	/**
	 * Connects all generated input and output nodes
	 *
	 * @param genome
	 * @param inputNodes
	 * @param outputNodes
	 * @return
	 */
	private void connectInputsAndOutputs(List<LstmNodeGene> inputNodes, List<LstmNodeGene> outputNodes) {

		for (LstmNodeGene inputNode : inputNodes) {
			for (LstmNodeGene outputNode : outputNodes) {

				LstmConnectionGene connection = new LstmConnectionGene(inputNode, outputNode, true, genome.connectionInnovation.getNext());
				genome.addLstmConnectionGene(connection);
			}
		}
	}

	/**
	 * Generates the bias and connects it to all generated output nodes
	 *
	 * @param genome
	 * @param random
	 */
	private void generateAndConnectBias() {

		// Create and add bias node
		int biasInnovation = genome.nodeInnovation.getNext();
		genome.biasNode = new LstmNodeGene(NodeGeneType.BIAS, biasInnovation);
		genome.nodes.put(biasInnovation, genome.biasNode);

		// Connect the bias node to all outputs
		for (LstmNodeGene outputNode : genome.getNodesByType(NodeGeneType.OUTPUT)) {

			LstmConnectionGene connection = new LstmConnectionGene(genome.biasNode, outputNode, true, genome.connectionInnovation.getNext());
			genome.addLstmConnectionGene(connection);
		}
	}

	/**
	 * Sets this genomes connection innovation to the next connection innovation number
	 */
	@Override
	public void updateConnectionInnovation() {

		int highestInnovation = 0;
		for (LstmConnectionGene connection : genome.connections.values()) {
			if (connection.innvoationNumber > highestInnovation) {
				highestInnovation = connection.innvoationNumber;
			}
		}

		genome.connectionInnovation.setCurrent(++highestInnovation);
	}

	/**
	 * Sets this genomes node innovation to the next node innovation number
	 */
	@Override
	public void updateNodeInnovation() {

		int highestInnovation = 0;
		for (LstmNodeGene node : genome.nodes.values()) {
			if (node.innovationNumber > highestInnovation) {
				highestInnovation = node.innovationNumber;
			}
		}

		genome.nodeInnovation.setCurrent(++highestInnovation);
	}

	/**
	 * @param innovationHistory
	 * @param fromInnovationNumber
	 * @param toInnovationNumber
	 * @return the next innovationNumber for the connection between from and to
	 */
	@Override
	public int getConnectionInnovationNumber(Map<Integer, List<ConnectionHistory>> innovationHistory, int fromInnovationNumber, int toInnovationNumber) {

		boolean newConnection = true;
		int connectionInnovationNumber = genome.connectionInnovation.getCurrent() + 1;
		List<ConnectionHistory> possibleMatches = innovationHistory.get(genome.connections.size());
		if (possibleMatches != null) {
			for (int i = 0; i < possibleMatches.size(); ++i) {
				if (possibleMatches.get(i).matches(getInnovationNumbers(), fromInnovationNumber, toInnovationNumber)) {
					newConnection = false;
					return possibleMatches.get(i).innovationNumber;
				}
			}
		}

		if (newConnection) {

			ArrayList<Integer> innovationNumbers = new ArrayList<>();
			for (LstmConnectionGene connection : genome.connections.values()) {
				innovationNumbers.add(connection.innvoationNumber);
			}

			// Save new innovation history
			List<ConnectionHistory> existingHistories = innovationHistory.get(innovationNumbers.size());
			if (existingHistories == null) {
				existingHistories = new ArrayList<>();
			}
			existingHistories.add(new ConnectionHistory(fromInnovationNumber, toInnovationNumber, connectionInnovationNumber, innovationNumbers));
			innovationHistory.put(innovationNumbers.size(), existingHistories);

			genome.connectionInnovation.getNext();
		}

		return connectionInnovationNumber;
	}

	/**
	 * @return
	 */
	private List<Integer> getInnovationNumbers() {

		List<Integer> innovationNumbers = new ArrayList<>();
		for (LstmConnectionGene connection : genome.connections.values()) {
			innovationNumbers.add(connection.innvoationNumber);
		}

		return innovationNumbers;
	}

}
