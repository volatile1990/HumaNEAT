package de.humaneat.core.neat.genome;

import java.util.ArrayList;

import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.global.genome.DefaultGenomeLinker;
import de.humaneat.core.neat.genes.connection.ConnectionGene;
import de.humaneat.core.neat.genes.node.NodeGene;

/**
 * @author MannoR
 *
 */
public class GenomeLinker implements DefaultGenomeLinker {

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
	@Override
	public void generateNetwork() {

		connectNodes();
		genome.network = new ArrayList<>();

		for (NodeGeneType layerType : NodeGeneType.values()) {
			for (NodeGene node : genome.nodes.values()) {

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
		for (NodeGene node : genome.nodes.values()) {
			node.outputConnections.clear();
		}

		// Adds the connection as an outgoing connection to its from node
		for (ConnectionGene connection : genome.connections.values()) {
			connection.from.outputConnections.add(connection);
		}

	}

}
