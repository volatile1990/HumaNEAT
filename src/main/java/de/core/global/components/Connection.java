package de.core.global.components;

import de.core.neat.genes.node.NodeGene;

/**
 * @author MannoR
 *
 */
public abstract class Connection {

	public NodeGene from;
	public NodeGene to;
	public boolean enabled;
	public int innvoationNumber;

	public double payload;
	public boolean activated;

	/**
	 * @param from
	 * @param to
	 * @param weight
	 * @param enabled
	 * @param innovationNumber
	 * @param circular2
	 */
	public Connection(NodeGene from, NodeGene to, boolean enabled, int innovationNumber) {

		this.from = from;
		this.to = to;
		this.enabled = enabled;
		this.innvoationNumber = innovationNumber;
		this.payload = 0;
		this.activated = false;
	}

	public abstract Connection copy();
}
