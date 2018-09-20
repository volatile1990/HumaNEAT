package de.core.lstm.genes;

import de.core.neat.genes.connection.ConnectionGene;
import de.core.neat.genes.node.NodeGene;

/**
 * @author muellermak
 *
 */
public class LSTMConnectionGene extends ConnectionGene {

	/**
	 * @param in
	 * @param out
	 * @param enabled
	 * @param innovationNumber
	 */
	public LSTMConnectionGene(NodeGene in, NodeGene out, boolean enabled, int innovationNumber) {
		super(in, out, enabled, innovationNumber);
	}
}
