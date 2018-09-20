package de.core.neat.genome;

import java.util.ArrayList;

import de.core.neat.genes.connection.ConnectionGene;
import de.core.neat.genes.node.NodeGene;
import de.core.neat.genes.node.NodeGeneType;

/**
 * @author MannoR
 *
 */
public class GenomeLinker {

	private Genome genome;

	/**
	 * @param genome
	 */
	public GenomeLinker(Genome genome) {
		this.genome = genome;
	}

	/**
	 * Sets up the neural network as a list of nodes in order to be engange (INPUT -> HIDDEN -> OUTPUT)
	 *
	 */
	public void generateNetwork() {
		connectNodes();
		genome.network = new ArrayList<>();

		for (NodeGeneType layerType : NodeGeneType.values()) {
			for (NodeGene node : genome.nodes.values()) {
				if (node.type == layerType) {
					genome.network.add(node);
				}
			}
		}
	}

	/**
	 * Sets the all outgoing connections of a node gene into its output connections
	 */
	public void connectNodes() {

		// Clear all output connections
		for (NodeGene node : genome.nodes.values()) {
			node.outputConnections.clear();
		}

		// Adds the connection as an outgoing connection to its from node
		for (ConnectionGene connection : genome.connections.values()) {
			connection.from.outputConnections.add(connection);
		}

	}

}
