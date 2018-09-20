package de.core.neat.genes;

import java.util.ArrayList;
import java.util.List;

import de.core.neat.genome.NeatGenome;

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
	public boolean matches(NeatGenome genome, NodeGene from, NodeGene to) {

		if (genome.getConnectionGenes().size() != this.innovationNumbers.size()) {
			return false;
		}

		if (from.getNumber() != this.fromNode || to.getNumber() != this.toNode) {
			return false;
		}

		for (ConnectionGene connection : genome.getConnectionGenes().values()) {
			if (!this.innovationNumbers.contains(connection.getInnvoationNumber())) {
				return false;
			}
		}

		return true;
	}
}
