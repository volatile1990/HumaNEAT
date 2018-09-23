package de.humaneat.core.neat.genome;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.neat.genes.connection.ConnectionGene;
import de.humaneat.core.neat.genes.node.NodeGene;
import de.humaneat.core.neat.genes.node.NodeGeneType;

/**
 * @author MannoR
 *
 */
public class GenomeValidator {

	private Genome genome;

	/**
	 * @param genome
	 */
	public GenomeValidator(Genome genome) {
		this.genome = genome;
	}

	/**
	 * @param firstNode
	 * @return whether a connection between the given nodes is allowed
	 */
	public boolean connectionAllowed(NodeGene firstNode, NodeGene secondNode) {
		return !(genome.getValidator().connectionExists(firstNode, secondNode) || genome.getValidator().connectionImpossible(firstNode, secondNode)
				|| genome.getValidator().connectionCreatesCircular(firstNode, secondNode));
	}

	/**
	 * @param firstNode
	 * @param secondNode
	 * @return whether the connection already exists
	 */
	public boolean connectionExists(NodeGene firstNode, NodeGene secondNode) {

		// No connections exist
		if (genome.connections == null || genome.connections.size() <= 0) {
			return false;
		}

		for (ConnectionGene connection : genome.connections.values()) {

			if (connection.from.equals(firstNode) && connection.to.equals(secondNode)) {
				return true;
			} else if (connection.from.equals(secondNode) && connection.to.equals(firstNode)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param firstNode
	 * @param secondNode
	 * @return whether the connection between the given nodes is impossible
	 */
	public boolean connectionImpossible(NodeGene firstNode, NodeGene secondNode) {

		// Inputs and outputs can't be connected to each other, one node has to be a hidden node, can't connect bias to bias
		if (firstNode.type == NodeGeneType.INPUT && secondNode.type == NodeGeneType.INPUT) {
			return true;
		} else if (firstNode.type == NodeGeneType.OUTPUT && secondNode.type == NodeGeneType.OUTPUT) {
			return true;
		} else if (firstNode.type != NodeGeneType.HIDDEN && secondNode.type != NodeGeneType.HIDDEN) {
			return true;
		} else if (firstNode.type == NodeGeneType.BIAS && secondNode.type == NodeGeneType.BIAS) {
			return true;
		}

		if (firstNode == secondNode) {
			return true;
		}

		return false;
	}

	/**
	 * @param firstNode
	 * @param secondNode
	 * @return whether a connection between those two nodes create a circular connection
	 */
	public boolean connectionCreatesCircular(NodeGene firstNode, NodeGene secondNode) {

		List<ConnectionGene> outConnections = new ArrayList<>();
		for (ConnectionGene connection : genome.connections.values()) {
			if (connection.from == secondNode && connection.enabled) {
				outConnections.add(connection);
			}
		}

		for (ConnectionGene connection : outConnections) {

			// Can't create circular before input node
			NodeGene from = connection.from;
			if (from.type == NodeGeneType.INPUT || from.type == NodeGeneType.BIAS) {
				continue;
			}

			// Can't create circular after output node
			if (connection.to.type == NodeGeneType.OUTPUT) {
				continue;
			}

			if (connectionLeadsToNode(connection, firstNode)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param node
	 * @return whether the given connection at some point leads to the given node
	 */
	public boolean connectionLeadsToNode(ConnectionGene connection, NodeGene node) {

		NodeGene toNode = connection.to;
		if (toNode == node) {
			return true;
		}

		List<ConnectionGene> outConnections = new ArrayList<>();
		for (ConnectionGene outConnection : genome.connections.values()) {
			if (outConnection.from == toNode && outConnection.enabled) {
				outConnections.add(outConnection);
			}
		}

		for (ConnectionGene toNodeOutConnection : outConnections) {

			// Can't create circular before input node
			NodeGene from = toNodeOutConnection.from;
			if (from.type == NodeGeneType.INPUT || from.type == NodeGeneType.BIAS) {
				continue;
			}

			// Can't create circular after output node
			if (toNodeOutConnection.to.type == NodeGeneType.OUTPUT) {
				continue;
			}

			if (connectionLeadsToNode(toNodeOutConnection, node)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Validates whether all nodes used on the connection are present in the genome
	 */
	public void validateIntegrity() {

		for (ConnectionGene connection : genome.connections.values()) {

			NodeGene in = connection.from;
			NodeGene out = connection.to;

			boolean foundIn = false;
			boolean foundOut = false;
			for (NodeGene node : genome.nodes.values()) {
				if (node == in || in.type == NodeGeneType.BIAS) {
					foundIn = true;
				}
				if (node == out) {
					foundOut = true;
				}
			}

			if (!foundIn || !foundOut) {
				throw new RuntimeException("Genome integrity failure");
			}
		}
	}

}
