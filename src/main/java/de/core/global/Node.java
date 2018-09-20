package de.core.global;

import java.util.List;

import de.core.neat.genes.ConnectionGene;
import de.core.neat.genes.NodeGene;
import de.core.neat.genes.NodeGeneType;

public abstract class Node {

	public NodeGeneType type;
	public int number;

	public double inputSum;
	public double outputValue;

	public List<ConnectionGene> outputConnections;

	/**
	 * Checks whether this node is before the passed one concerning the type
	 *
	 * @param node
	 */
	public boolean before(Node node) {

		if (this.type == NodeGeneType.INPUT && node.type != NodeGeneType.INPUT) {
			return true;
		} else if (this.type == NodeGeneType.HIDDEN && node.type == NodeGeneType.OUTPUT) {
			return true;
		}

		return false;
	}

	/**
	 * Engages the node to calc and pass its data in the neural network
	 */
	public abstract void engage();

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

		return this.number == in.number;
	}

}
