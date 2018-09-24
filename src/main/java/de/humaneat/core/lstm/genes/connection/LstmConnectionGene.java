package de.humaneat.core.lstm.genes.connection;

import de.humaneat.core.global.components.connection.Connection;
import de.humaneat.core.lstm.genes.node.LstmNodeGene;

/**
 * @author muellermak
 *
 */
public class LstmConnectionGene extends Connection {

	public LstmNodeGene from;
	public LstmNodeGene to;

	/**
	 * @param in
	 * @param out
	 * @param enabled
	 * @param innovationNumber
	 */
	public LstmConnectionGene(LstmNodeGene from, LstmNodeGene to, boolean enabled, int innovationNumber) {
		super(enabled, innovationNumber);

		this.from = from;
		this.to = to;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.humaneat.core.global.components.connection.Connection#copy()
	 */
	@Override
	public LstmConnectionGene copy() {
		return new LstmConnectionGene(from.copy(), to.copy(), enabled, innvoationNumber);
	}
}
