package de.humaneat.core.global.components.node;

import de.humaneat.core.neat.genes.node.NodeGene;

/**
 * @author MannoR
 *
 */
public abstract class Node {

	public NodeGeneType type;
	public int innovationNumber;

	public double inputSum;
	public double outputValue;

	/**
	 * Checks whether this node is before the passed one concerning the type
	 *
	 * @param node
	 */
	public boolean before(Node node) {

		if (type == NodeGeneType.INPUT && node.type != NodeGeneType.INPUT) {
			return true;
		} else if (type == NodeGeneType.HIDDEN && node.type == NodeGeneType.OUTPUT) {
			return true;
		}

		return false;
	}

	/**
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {

		if (!(o instanceof NodeGene)) {
			return false;
		}

		Node in = (Node) o;

		return innovationNumber == in.innovationNumber;
	}

	/**
	 *
	 */
	public abstract Node copy();

}
