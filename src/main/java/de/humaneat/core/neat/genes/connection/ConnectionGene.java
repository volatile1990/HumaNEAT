package de.humaneat.core.neat.genes.connection;

import de.humaneat.core.global.components.connection.Connection;
import de.humaneat.core.neat.genes.node.NodeGene;

/**
 * @author muellermak
 *
 */
public class ConnectionGene extends Connection {

	public NodeGene from;
	public NodeGene to;

	public double weight;

	/**
	 * @param in
	 * @param out
	 * @param weight
	 * @param enabled
	 * @param innovationNumber
	 * @param circular2
	 */
	public ConnectionGene(NodeGene from, NodeGene to, double weight, boolean enabled, int innovationNumber) {
		super(enabled, innovationNumber);

		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	/**
	 * @param in
	 * @param out
	 * @param enabled
	 * @param innovationNumber
	 */
	public ConnectionGene(NodeGene in, NodeGene out, boolean enabled, int innovationNumber) {
		this(in, out, 0, enabled, innovationNumber);
	}

	/**
	 * @param to
	 * @param from
	 * @return a copy of this connection gene
	 */
	@Override
	public ConnectionGene copy() {
		return new ConnectionGene(from.copy(), to.copy(), weight, enabled, innvoationNumber);
	}

}
