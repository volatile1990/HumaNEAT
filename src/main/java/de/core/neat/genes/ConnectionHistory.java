package de.core.neat.genes;

import java.util.ArrayList;
import java.util.List;

import de.core.neat.genome.Genome;

/**
 * @author muellermak
 *
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

		if (genome.connections.size() != this.innovationNumbers.size()) {
			return false;
		}

		if (from.innovationNumber != this.fromNode || to.innovationNumber != this.toNode) {
			return false;
		}

		for (ConnectionGene connection : genome.connections.values()) {
			if (!this.innovationNumbers.contains(connection.innvoationNumber)) {
				return false;
			}
		}

		return true;
	}
}
