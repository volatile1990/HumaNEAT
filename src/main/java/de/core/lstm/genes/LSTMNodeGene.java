package de.core.lstm.genes;

import de.core.global.activation.Activation;
import de.core.global.activation.ActivationFunctions;
import de.core.global.components.Node;
import de.core.neat.genes.node.NodeGeneType;

/**
 * @author muellermak
 *
 */
public class LSTMNodeGene extends Node {

	// Cell state
	public double[] cellState;
	public static final double INITIAL_CELL_STATE = 0;

	// Cell output used for every loop
	public double recurrentCellOutput;

	// Used activation functions
	public Activation forgetGateActivation;
	public Activation inputGateActivation;
	public Activation selectGateActivation;
	public Activation outputGateActivation;
	public Activation cellStateActivation;

	/**
	 * There are four lstm weights
	 * 0: forget gate weight
	 * 1: input gate weight
	 * 2: select gate weight
	 * 3: output gate weight
	 */
	public double[] weights;

	/**
	 * Output vectors of the gates
	 */
	double[] forgetGateOut = new double[2];
	double[] inputGateOut = new double[2];
	double[] selectGateOut = new double[2];
	double[] outputGateOut = new double[2];

	/**
	 * @param type
	 * @param number
	 */
	public LSTMNodeGene(NodeGeneType type, int number) {

		super(new LSTMNodeEngager());

		cellState = new double[2];
		cellState[0] = LSTMNodeGene.INITIAL_CELL_STATE;
		cellState[1] = LSTMNodeGene.INITIAL_CELL_STATE;

		recurrentCellOutput = 0;

		weights = new double[4];

		// Default activation functions
		forgetGateActivation = ActivationFunctions::sigmoid;
		inputGateActivation = ActivationFunctions::sigmoid;
		selectGateActivation = ActivationFunctions::tanh;
		outputGateActivation = ActivationFunctions::sigmoid;
		cellStateActivation = ActivationFunctions::tanh;
	}

	/**
	 * Engages the LSTM node with all its gates
	 */
	public void engage() {

		// Activate all gates
		activate();

		// Update the cell state with current gate outputs
		updateCellState();

		// Output the result on the outgoing connection
		fire();
	}

	/**
	 * Updates the current cell state with the following operations:
	 * 1: Multiply with the forget gate output vector
	 * 2: Add to the muliplied input * select gate vector
	 *
	 */
	private void updateCellState() {

		// Multiply with select gate output vector
		cellState[0] *= forgetGateOut[0];
		cellState[1] *= forgetGateOut[1];

		// Multiply input and select gate vectors
		double[] selectInputMultResult = new double[2];
		selectInputMultResult[0] = inputGateOut[0] * selectGateOut[0];
		selectInputMultResult[1] = inputGateOut[1] * selectGateOut[1];

		// Add input * select to the cell state vector
		cellState[0] += selectInputMultResult[0];
		cellState[1] += selectInputMultResult[1];
	}

}
