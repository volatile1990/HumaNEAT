package de.core.global.components;

import java.util.List;

import de.core.neat.genes.connection.ConnectionGene;
import de.core.neat.genes.node.NodeGene;
import de.core.neat.genes.node.NodeGeneType;

/**
 * @author MannoR
 *
 */
public abstract class Node {

	public NodeGeneType type;
	public int innovationNumber;

	public double inputSum;
	public double outputValue;

	public List<ConnectionGene> outputConnections;

	public NodeEngager nodeEngager;

	/**
	 * @param engager
	 */
	public Node(NodeEngager engager) {
		nodeEngager = engager;
	}

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
	 * Engages the node to calc and pass its data in the neural network
	 */
	public void engage() {
		nodeEngager.engage(this);
	}

	/**
	 *
	 */
	public void activate() {
		nodeEngager.activate(this);
	}

	/**
	 *
	 */
	public void fire() {
		nodeEngager.fire(this);
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

}
