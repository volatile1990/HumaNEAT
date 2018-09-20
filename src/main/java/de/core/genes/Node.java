package de.core.genes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author muellermak
 *
 */
public class NodeGene {

	private NodeGeneType type;
	private int number;

	public float inputSum;
	public float outputValue;

	public List<ConnectionGene> outputConnections;

	/**
	 * @param type
	 * @param id
	 */
	public NodeGene(NodeGeneType type, int number) {
		this.type = type;
		this.number = number;

		this.inputSum = 0;
		this.outputValue = 0;

		this.outputConnections = new ArrayList<>();
	}

	/**
	 * Checks whether this node is before the passed one concerning the type
	 *
	 * @param node
	 */
	public boolean before(NodeGene node) {

		if (this.type == NodeGeneType.INPUT && node.type != NodeGeneType.INPUT) {
			return true;
		} else if (this.type == NodeGeneType.HIDDEN && node.type == NodeGeneType.OUTPUT) {
			return true;
		}

		return false;
	}

	/**
	 * The node sends its output to the inputs of the nodes it is connected to
	 */
	public void engage() {

		this.activate();
		this.fire();
	}

	/**
	 * Activates the nodegene by using sigmoid
	 */
	public void activate() {
		// Don't apply sigmoid for inputs (including the bias node)
		if (this.type != NodeGeneType.BIAS && this.type != NodeGeneType.INPUT) {
			this.outputValue = this.sigmoid(this.inputSum);
		} else {
			this.outputValue = this.inputSum;
		}
	}

	/**
	 * Sigmoid activation function
	 *
	 * @param x
	 * @return
	 */
	public float sigmoid(float x) {
//		return (float) (1 / (1 + Math.pow(Math.E, -1 * x)));

		return (float) (1D / (1D + Math.exp(-4.9 * x)));
	}

	/**
	 * Sums up the input of the connected genome with its outputValue * weight
	 */
	public void fire() {
		for (ConnectionGene connection : this.outputConnections) {

			if (!connection.isEnabled()) {
				continue;
			}

			// Store weighted outputValue to the sum of the inputs of the connected nodes on every outgoing connection
			connection.payload = connection.getWeight() * this.outputValue;
			connection.activated = true;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {

		if (!(o instanceof NodeGene)) {
			return false;
		}

		NodeGene in = (NodeGene) o;

		return this.number == in.getNumber();
	}

	/**
	 *
	 */
	public NodeGene copy() {
		return new NodeGene(this.type, this.number);
	}

	/**
	 * @return the type
	 */
	public NodeGeneType getType() {
		return this.type;
	}

	/**
	 * @return the id
	 */
	public int getNumber() {
		return this.number;
	}
}
