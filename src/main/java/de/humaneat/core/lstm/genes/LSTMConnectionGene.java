package de.humaneat.core.lstm.genes;

import de.humaneat.core.global.components.connection.Connection;
import de.humaneat.core.neat.genes.node.NodeGene;

/**
 * @author muellermak
 *
 */
public class LSTMConnectionGene extends Connection {

	/**
	 * @param in
	 * @param out
	 * @param enabled
	 * @param innovationNumber
	 */
	public LSTMConnectionGene(NodeGene in, NodeGene out, boolean enabled, int innovationNumber) {
		super(in, out, enabled, innovationNumber);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.humaneat.core.global.components.connection.Connection#copy()
	 */
	@Override
	public Connection copy() {
		return new LSTMConnectionGene(from.copy(), to.copy(), enabled, innvoationNumber);
	}
}
