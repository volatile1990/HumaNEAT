package de.core.neat.genes.node;

import de.core.global.components.NodeEngager;
import de.core.neat.Property;
import de.core.neat.genes.connection.ConnectionGene;

/**
 * @author MannoR
 *
 */
public class NodeGeneEngager extends NodeEngager {

	/**
	 * Activates the nodegene by using sigmoid
	 */
	@Override
	public void activate() {

		// Don't apply sigmoid for inputs or bias
		NodeGeneType type = node.type;
		if (type != NodeGeneType.BIAS && type != NodeGeneType.INPUT) {
			node.outputValue = Property.ACTIVATION_FUNCTION.getActivationFunction().activate(node.inputSum);
		} else {
			node.outputValue = node.inputSum;
		}
	}

	/**
	 * Sums up the input of the connected genome with its outputValue * weight
	 */
	@Override
	public void fire() {
		for (ConnectionGene connection : node.outputConnections) {

			if (!connection.enabled) {
				continue;
			}

			// Store weighted outputValue to the sum of the inputs of the connected nodes on every outgoing connection
			connection.payload = connection.weight * node.outputValue;
			connection.activated = true;
		}
	}

}
