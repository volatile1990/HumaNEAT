package de.core.neat.genes.connection;

import java.util.ArrayList;
import java.util.List;

import de.core.neat.genes.node.NodeGene;
import de.core.neat.genome.Genome;

/**
 * @author muellermak
 *
 *         Stores information about all connections that have occured over a whole population. This keeps innovations unique.
 */
public class ConnectionHistory {

	public int fromNode;
	public int toNode;
	public int innovationNumber;
	public List<Integer> innovationNumbers;

	/**
	 *
	 */
	public ConnectionHistory(int fromNode, int toNode, int innovationNumber, List<Integer> innovationNumbers) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.innovationNumber = innovationNumber;

		this.innovationNumbers = new ArrayList<>();
		this.innovationNumbers.addAll(innovationNumbers);
	}

	/**
	 * @param genome
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean matches(Genome genome, NodeGene from, NodeGene to) {

		if (genome.connections.size() != innovationNumbers.size()) {
			return false;
		}

		if (from.innovationNumber != fromNode || to.innovationNumber != toNode) {
			return false;
		}

		for (ConnectionGene connection : genome.connections.values()) {
			if (!innovationNumbers.contains(connection.innvoationNumber)) {
				return false;
			}
		}

		return true;
	}
}
