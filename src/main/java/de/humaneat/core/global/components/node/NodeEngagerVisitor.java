package de.humaneat.core.global.components.node;

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
	 * Activates all lstm node gates; For predictions the inputSum is zero and won't add any data to the equations
	 */
	public void activate(LstmNodeGene node) {

		Weights inputWeights = node.weight.inputWeights;
		Weights recurrentWeights = node.weight.recurrentWeights;

		double inputSum = node.inputSum;
		double lastOutputValue = node.outputValue;

		// Engage forget gate
		double concatForgetGateInput = recurrentWeights.forgetGateWeight * lastOutputValue + inputWeights.forgetGateWeight * inputSum;
		node.forgetGateOut = node.forgetGateActivation.activate(concatForgetGateInput);

		// Engage input gate
		double concatInputGateInput = recurrentWeights.inputGateWeight * lastOutputValue + inputWeights.inputGateWeight * inputSum;
		node.inputGateOut = node.inputGateActivation.activate(concatInputGateInput);

		// Engage select gate
		double concatSelectGateInput = recurrentWeights.selectGateWeight * lastOutputValue + inputWeights.selectGateWeight * inputSum;
		node.selectGateOut = node.selectGateActivation.activate(concatSelectGateInput);

		// Engage output gate
		double concatOutputGateInput = recurrentWeights.outputGateWeight * lastOutputValue + inputWeights.outputGateWeight * inputSum;
		node.outputGateOut = node.outputGateActivation.activate(concatOutputGateInput);
	}

	/**
	 * Updates the current cell state with the following operations:
	 * 1: Multiply with the forget gate output vector
	 * 2: Add to the muliplied input * select gate vector
	 *
	 */
	private void updateCellState(LstmNodeGene node) {

		// Multiply with select gate output vector
		node.cellState *= node.forgetGateOut;

		// Multiply input and select gate vectors
		double selectInputMultResult = node.inputGateOut * node.selectGateOut;

		// Add input * select to the cell state vector
		node.cellState += selectInputMultResult;
	}

	/**
	 * Generates the recurrentCellOutput with the following operations:
	 * 1: Use tanh on the cell state vector
	 * 2: Multiply the output gate result with the cell state vector
	 */
	public void fire(LstmNodeGene node) {

		// Output: activate cellstate and mulitply with the already activated outputGate outValue
		node.outputValue = node.cellStateActivation.activate(node.cellState) * node.outputGateOut;

		// Set the output as payload on all outgoing connections
		for (LstmConnectionGene connection : node.outputConnections) {

			if (!connection.enabled) {
				continue;
			}

			// Store outputValue on every outgoing connection
			connection.payload = node.outputValue;
			connection.activated = true;
		}
	}

}
