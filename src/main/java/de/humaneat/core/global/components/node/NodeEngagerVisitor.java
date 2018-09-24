package de.humaneat.core.global.components.node;

import de.humaneat.core.global.activation.ActivationFunctions;
import de.humaneat.core.lstm.genes.connection.LstmConnectionGene;
import de.humaneat.core.lstm.genes.node.LstmNodeGene;
import de.humaneat.core.lstm.genes.node.Weights;
import de.humaneat.core.neat.Property;
import de.humaneat.core.neat.genes.connection.ConnectionGene;
import de.humaneat.core.neat.genes.node.NodeGene;

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
	 * Activates the nodegene by using the set activation function
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
	public void visit(LstmNodeGene node) {

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
	public void activate(LstmNodeGene node) {

		Weights inputWeights = node.weight.inputWeights;
		Weights recurrentWeights = node.weight.recurrentWeights;

		// Engage forget gate
		node.forgetGateOut[0] = node.forgetGateActivation.activate(recurrentWeights.forgetGateWeight * node.recurrentCellOutput);
		node.forgetGateOut[1] = node.forgetGateActivation.activate(inputWeights.forgetGateWeight * node.inputSum);

		// Engage input gate
		node.inputGateOut[0] = node.inputGateActivation.activate(recurrentWeights.inputGateWeight * node.recurrentCellOutput);
		node.inputGateOut[1] = node.inputGateActivation.activate(inputWeights.inputGateWeight * node.inputSum);

		// Engage select gate
		node.selectGateOut[0] = node.selectGateActivation.activate(recurrentWeights.selectGateWeight * node.recurrentCellOutput);
		node.selectGateOut[1] = node.selectGateActivation.activate(inputWeights.selectGateWeight * node.inputSum);

		// Engage output gate
		node.outputGateOut[0] = node.outputGateActivation.activate(recurrentWeights.outputGateWeight * node.recurrentCellOutput);
		node.outputGateOut[1] = node.outputGateActivation.activate(inputWeights.outputGateWeight * node.inputSum);
	}

	/**
	 * Updates the current cell state with the following operations:
	 * 1: Multiply with the forget gate output vector
	 * 2: Add to the muliplied input * select gate vector
	 *
	 */
	private void updateCellState(LstmNodeGene node) {

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
	public void fire(LstmNodeGene node) {

		node.recurrentCellOutput = node.cellStateActivation.activate(node.cellState[0]) * node.outputGateOut[0];
		node.outputValue = node.cellStateActivation.activate(node.cellState[1]) * node.outputGateOut[1];

		// Set the output as payload on all outgoing connections
		for (LstmConnectionGene connection : node.outputConnections) {

			if (!connection.enabled) {
				continue;
			}

			// Set reccurent cell output
			node.recurrentCellOutput = ActivationFunctions.tanh(node.cellState[0]) * node.outputGateOut[0];

			// Store weighted outputValue to the sum of the inputs of the connected nodes on every outgoing connection
			connection.payload = ActivationFunctions.tanh(node.cellState[1]) * node.outputGateOut[1];
			connection.activated = true;
		}
	}

}
