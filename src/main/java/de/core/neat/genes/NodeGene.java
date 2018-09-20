package de.core.neat.genes;

import java.util.ArrayList;

import de.core.global.components.Node;
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
	public NodeGene(NodeGeneType type, int innovationNumber) {
		this.type = type;
		this.innovationNumber = innovationNumber;

		inputSum = 0;
		outputValue = 0;

		outputConnections = new ArrayList<>();
	}

	/**
	 * The node sends its output to the inputs of the nodes it is connected to
	 */
	@Override
	public void engage() {
		activate();
		fire();
	}

	/**
	 * Activates the nodegene by using sigmoid
	 */
	public void activate() {

		// Don't apply sigmoid for inputs or bias
		if (type != NodeGeneType.BIAS && type != NodeGeneType.INPUT) {
			outputValue = Property.ACTIVATION_FUNCTION.getActivationFunction().activate(inputSum);
		} else {
			outputValue = inputSum;
		}
	}

	/**
	 * Sums up the input of the connected genome with its outputValue * weight
	 */
	public void fire() {
		for (ConnectionGene connection : outputConnections) {

			if (!connection.enabled) {
				continue;
			}

			// Store weighted outputValue to the sum of the inputs of the connected nodes on every outgoing connection
			connection.payload = connection.weight * outputValue;
			connection.activated = true;
		}
	}

	/**
	 *
	 */
	public NodeGene copy() {
		return new NodeGene(type, innovationNumber);
	}
}
