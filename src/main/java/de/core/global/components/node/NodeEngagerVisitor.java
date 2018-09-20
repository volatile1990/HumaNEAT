package de.core.global.components.node;

import de.core.lstm.genes.LSTMNodeGene;
import de.core.neat.Property;
import de.core.neat.genes.connection.ConnectionGene;
import de.core.neat.genes.node.NodeGene;
import de.core.neat.genes.node.NodeGeneType;

/**
 * @author MannoR
 *
 */
public class NodeEngagerVisitor implements NodeVisitor {

	/**
	 * Engages the node gene
	 */
	@Override
	public void visit(NodeGene node) {

		// Activate the output using the activation function
		activate(node);

		// Fire the output to all outgoing connections
		fire(node);
	}

	/**
	 * Activates the nodegene by using sigmoid
	 */
	public void activate(NodeGene node) {

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
	public void fire(NodeGene node) {
		for (ConnectionGene connection : node.outputConnections) {

			if (!connection.enabled) {
				continue;
			}

			// Store weighted outputValue to the sum of the inputs of the connected nodes on every outgoing connection
			connection.payload = connection.weight * node.outputValue;
			connection.activated = true;
		}
	}

	/**
	 * Engages the LSTM node with all its gates
	 */
	@Override
	public void visit(LSTMNodeGene node) {

		// Activate all gates
		activate(node);

		// Update the cell state with current gate outputs
		updateCellState(node);

		// Output the result on the outgoing connection
		fire(node);
	}

	/**
	 * Activates all lstm node gates
	 */
	public void activate(LSTMNodeGene node) {

		// Engage forget gate
		node.forgetGateOut[0] = node.forgetGateActivation.activate(node.weights[0] * node.recurrentCellOutput);
		node.forgetGateOut[0] = node.forgetGateActivation.activate(node.weights[0] * node.inputSum);

		// Engage input gate
		node.inputGateOut[0] = node.inputGateActivation.activate(node.weights[1] * node.recurrentCellOutput);
		node.inputGateOut[1] = node.inputGateActivation.activate(node.weights[1] * node.inputSum);

		// Engage select gate
		node.selectGateOut[0] = node.selectGateActivation.activate(node.weights[2] * node.recurrentCellOutput);
		node.selectGateOut[1] = node.selectGateActivation.activate(node.weights[2] * node.inputSum);

		// Engage output gate
		node.outputGateOut[0] = node.outputGateActivation.activate(node.weights[3] * node.recurrentCellOutput);
		node.outputGateOut[1] = node.outputGateActivation.activate(node.weights[3] * node.inputSum);
	}

	/**
	 * Updates the current cell state with the following operations:
	 * 1: Multiply with the forget gate output vector
	 * 2: Add to the muliplied input * select gate vector
	 *
	 */
	private void updateCellState(LSTMNodeGene node) {

		// Multiply with select gate output vector
		node.cellState[0] *= node.forgetGateOut[0];
		node.cellState[1] *= node.forgetGateOut[1];

		// Multiply input and select gate vectors
		double[] selectInputMultResult = new double[2];
		selectInputMultResult[0] = node.inputGateOut[0] * node.selectGateOut[0];
		selectInputMultResult[1] = node.inputGateOut[1] * node.selectGateOut[1];

		// Add input * select to the cell state vector
		node.cellState[0] += selectInputMultResult[0];
		node.cellState[1] += selectInputMultResult[1];
	}

	/**
	 * Generates the recurrentCellOutput with the following operations:
	 * 1: Use tanh on the cell state vector
	 * 2: Multiply the output gate result with the cell state vector
	 */
	public void fire(LSTMNodeGene node) {

		node.recurrentCellOutput = node.cellStateActivation.activate(node.cellState[0]) * node.outputGateOut[0];
		node.outputValue = node.cellStateActivation.activate(node.cellState[1]) * node.outputGateOut[1];

		// Set the output as payload on all outgoing connections
		for (ConnectionGene connection : node.outputConnections) {

			if (!connection.enabled) {
				continue;
			}

			// Store weighted outputValue to the sum of the inputs of the connected nodes on every outgoing connection
			connection.payload = node.outputValue;
			connection.activated = true;
		}
	}

}
