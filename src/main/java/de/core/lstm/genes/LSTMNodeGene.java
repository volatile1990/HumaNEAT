package de.core.lstm.genes;

import de.core.global.Activation;
import de.core.global.ActivationFunctions;
import de.core.neat.genes.ConnectionGene;
import de.core.neat.genes.NodeGene;
import de.core.neat.genes.NodeGeneType;

/**
 * @author muellermak
 *
 */
public class LSTMNodeGene extends NodeGene {

	// Cell state
	private double[] cellState;
	private static final double INITIAL_CELL_STATE = 0;

	// Cell output used for every loop
	private double recurrentCellOutput;

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
	private double[] weights;

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
		super(type, number);

		this.cellState = new double[2];
		this.cellState[0] = LSTMNodeGene.INITIAL_CELL_STATE;
		this.cellState[1] = LSTMNodeGene.INITIAL_CELL_STATE;

		this.recurrentCellOutput = 0;

		this.weights = new double[4];

		// Default activation functions
		this.forgetGateActivation = ActivationFunctions::sigmoid;
		this.inputGateActivation = ActivationFunctions::sigmoid;
		this.selectGateActivation = ActivationFunctions::tanh;
		this.outputGateActivation = ActivationFunctions::sigmoid;
		this.cellStateActivation = ActivationFunctions::tanh;
	}

	/**
	 * Engages the LSTM node with all its gates
	 */
	@Override
	public void engage() {

		// Engage forget gate
		this.forgetGateOut[0] = this.forgetGateActivation.activate(this.weights[0] * this.recurrentCellOutput);
		this.forgetGateOut[0] = this.forgetGateActivation.activate(this.weights[0] * this.inputSum);

		// Engage input gate
		this.inputGateOut[0] = this.inputGateActivation.activate(this.weights[1] * this.recurrentCellOutput);
		this.inputGateOut[1] = this.inputGateActivation.activate(this.weights[1] * this.inputSum);

		// Engage select gate
		this.selectGateOut[0] = this.selectGateActivation.activate(this.weights[2] * this.recurrentCellOutput);
		this.selectGateOut[1] = this.selectGateActivation.activate(this.weights[2] * this.inputSum);

		// Engage output gate
		this.outputGateOut[0] = this.outputGateActivation.activate(this.weights[3] * this.recurrentCellOutput);
		this.outputGateOut[1] = this.outputGateActivation.activate(this.weights[3] * this.inputSum);

		// Update the cell state with current gate outputs
		this.updateCellState();

		// Output the result on the outgoing connection
		this.fire();
	}

	/**
	 * Updates the current cell state with the following operations:
	 * 1: Multiply with the forget gate output vector
	 * 2: Add to the muliplied input * select gate vector
	 *
	 */
	private void updateCellState() {

		// Multiply with select gate output vector
		this.cellState[0] *= this.forgetGateOut[0];
		this.cellState[1] *= this.forgetGateOut[1];

		// Multiply input and select gate vectors
		double[] selectInputMultResult = new double[2];
		selectInputMultResult[0] = this.inputGateOut[0] * this.selectGateOut[0];
		selectInputMultResult[1] = this.inputGateOut[1] * this.selectGateOut[1];

		// Add input * select to the cell state vector
		this.cellState[0] += selectInputMultResult[0];
		this.cellState[1] += selectInputMultResult[1];
	}

	/**
	 * Generates the recurrentCellOutput with the following operations:
	 * 1: Use tanh on the cell state vector
	 * 2: Multiply the output gate result with the cell state vector
	 */
	@Override
	public void fire() {

		this.recurrentCellOutput = this.cellStateActivation.activate(this.cellState[0]) * this.outputGateOut[0];
		this.outputValue = this.cellStateActivation.activate(this.cellState[1]) * this.outputGateOut[1];

		// Set the output as payload on all outgoing connections
		for (ConnectionGene connection : this.outputConnections) {

			if (!connection.enabled) {
				continue;
			}

			// Store weighted outputValue to the sum of the inputs of the connected nodes on every outgoing connection
			connection.payload = this.outputValue;
			connection.activated = true;
		}
	}
}
