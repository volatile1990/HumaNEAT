package de.humaneat.core.lstm.genes.node;

import java.util.List;

import de.humaneat.core.global.Random;
import de.humaneat.core.global.activation.Activation;
import de.humaneat.core.global.activation.ActivationFunctions;
import de.humaneat.core.global.components.node.Node;
import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.global.components.node.NodeVisitor;
import de.humaneat.core.lstm.genes.connection.LstmConnectionGene;
import de.humaneat.core.neat.Property;

/**
 * @author muellermak
 *
 */
public class LstmNodeGene extends Node {

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
	 * Output vectors of the gates
	 */
	public double[] forgetGateOut = new double[2];
	public double[] inputGateOut = new double[2];
	public double[] selectGateOut = new double[2];
	public double[] outputGateOut = new double[2];

	/**
	 * Output connections
	 */
	public List<LstmConnectionGene> outputConnections;

	/**
	 * All weights associated with that lstm cell
	 */
	public LstmWeight weight;

	/**
	 * @param type
	 * @param number
	 */
	public LstmNodeGene(NodeGeneType type, int number) {

		cellState = new double[2];
		cellState[0] = LstmNodeGene.INITIAL_CELL_STATE;
		cellState[1] = LstmNodeGene.INITIAL_CELL_STATE;

		recurrentCellOutput = 0;

		// Default activation functions
		forgetGateActivation = ActivationFunctions::sigmoid;
		inputGateActivation = ActivationFunctions::sigmoid;
		selectGateActivation = ActivationFunctions::tanh;
		outputGateActivation = ActivationFunctions::sigmoid;
		cellStateActivation = ActivationFunctions::tanh;

		// Initialize with random weights
		weight = new LstmWeight(getRandomWeights(), getRandomWeights());
	}

	/**
	 * @return
	 */
	private Weights getRandomWeights() {

		double min = -1 * Property.WEIGHT_RANDOM_RANGE.getValue();
		double max = Property.WEIGHT_RANDOM_RANGE.getValue();

		double forgetGateWeight = Random.random(min, max);
		double inputGateWeight = Random.random(min, max);
		double selectGateWeight = Random.random(min, max);
		double outputGateWeight = Random.random(min, max);
		Weights weights = new Weights(forgetGateWeight, inputGateWeight, selectGateWeight, outputGateWeight);

		return weights;

	}

	/**
	 * @param visitor
	 */
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * @return
	 */
	@Override
	public LstmNodeGene copy() {
		return new LstmNodeGene(type, innovationNumber);
	}
}
