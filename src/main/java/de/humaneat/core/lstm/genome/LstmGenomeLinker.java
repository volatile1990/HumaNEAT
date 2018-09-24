package de.humaneat.core.lstm.genome;

import java.util.ArrayList;

import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.global.genome.DefaultGenomeLinker;
import de.humaneat.core.lstm.genes.connection.LstmConnectionGene;
import de.humaneat.core.lstm.genes.node.LstmNodeGene;

/**
 * @author muellermak
 *
 */
public class LstmGenomeLinker implements DefaultGenomeLinker {

	private LstmGenome genome;

	/**
	 * @param genome
	 */
	public LstmGenomeLinker(LstmGenome genome) {
		this.genome = genome;
	}

	/**
	 * Sets up the neural network as a list of nodes in order to be engange (INPUT -> HIDDEN -> OUTPUT)
	 *
	 */
	@Override
	public void generateNetwork() {

		connectNodes();
		genome.network = new ArrayList<>();

		for (NodeGeneType layerType : NodeGeneType.values()) {
			for (LstmNodeGene node : genome.nodes.values()) {

				if (node.type != layerType) {
					continue;
				}
				genome.network.add(node);
			}
		}
	}

	/**
	 * Sets the all outgoing connections of a node gene into its output connections
	 */
	@Override
	public void connectNodes() {

		// Clear all output connections
		for (LstmNodeGene node : genome.nodes.values()) {
			node.outputConnections.clear();
		}

		// Adds the connection as an outgoing connection to its from node
		for (LstmConnectionGene connection : genome.connections.values()) {
			connection.from.outputConnections.add(connection);
		}

	}

}
