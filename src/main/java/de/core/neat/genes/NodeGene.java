package de.core.neat.genes;

import java.util.ArrayList;

import de.core.global.Node;
import de.core.neat.Property;

/**
 * @author muellermak
 *
 */
public class NodeGene extends Node {

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
	 * The node sends its output to the inputs of the nodes it is connected to
	 */
	@Override
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
			this.outputValue = Property.ACTIVATION_FUNCTION.getActivationFunction().activate(this.inputSum);
		} else {
			this.outputValue = this.inputSum;
		}
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
