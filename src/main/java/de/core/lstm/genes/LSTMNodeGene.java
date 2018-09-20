package de.core.lstm.genes;

import de.core.global.Visitor;
import de.core.global.activation.Activation;
import de.core.global.activation.ActivationFunctions;
import de.core.global.components.node.Node;
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
	public double[] forgetGateOut = new double[2];
	public double[] inputGateOut = new double[2];
	public double[] selectGateOut = new double[2];
	public double[] outputGateOut = new double[2];

	/**
	 * @param type
	 * @param number
	 */
	public LSTMNodeGene(NodeGeneType type, int number) {

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
	 * @param visitor
	 */
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
