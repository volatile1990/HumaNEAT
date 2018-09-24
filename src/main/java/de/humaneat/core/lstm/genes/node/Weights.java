package de.humaneat.core.lstm.genes.node;

/**
 * @author MannoR
 *
 */
public class Weights {

	public double forgetGateWeight;
	public double inputGateWeight;
	public double selectGateWeight;
	public double outputGateWeight;

	/**
	 * @param forgetGateWeight
	 * @param inputGateWeight
	 * @param selectGateWeight
	 * @param outputGateWeight
	 */
	public Weights(double forgetGateWeight, double inputGateWeight, double selectGateWeight, double outputGateWeight) {
		this.forgetGateWeight = forgetGateWeight;
		this.inputGateWeight = inputGateWeight;
		this.selectGateWeight = selectGateWeight;
		this.outputGateWeight = outputGateWeight;
	}
}
