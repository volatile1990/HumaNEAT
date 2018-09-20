package de.core.neat.genes;

import de.core.global.Connection;

/**
 * @author muellermak
 *
 */
public class ConnectionGene extends Connection {

	public double weight;

	/**
	 * @param in
	 * @param out
	 * @param weight
	 * @param enabled
	 * @param innovationNumber
	 * @param circular2
	 */
	public ConnectionGene(NodeGene in, NodeGene out, double weight, boolean enabled, int innovationNumber) {
		super(in, out, enabled, innovationNumber);
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
		return new ConnectionGene(this.from.copy(), this.to.copy(), this.weight, this.enabled, this.innvoationNumber);
	}

}
