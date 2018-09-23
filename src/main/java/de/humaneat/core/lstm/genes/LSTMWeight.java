package de.humaneat.core.lstm.genes;

/**
 * @author MannoR
 *         This class contains all weights for a LSTM cell
 */
public class LSTMWeight {

	/**
	 * There are four lstm weights
	 * 0: forget gate weight
	 * 1: input gate weight
	 * 2: select gate weight
	 * 3: output gate weight
	 */
	public Weights inputWeights;
	public Weights recurrentWeights;

	/**
	 * @param forgetGateWeight
	 * @param inputGateWeight
	 * @param selectGateWeight
	 * @param outputGateWeight
	 */
	public LSTMWeight(Weights inputWeights, Weights recurrentWeights) {
		this.inputWeights = inputWeights;
		this.recurrentWeights = recurrentWeights;
	}

}
