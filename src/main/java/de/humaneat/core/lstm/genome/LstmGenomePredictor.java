package de.humaneat.core.lstm.genome;

import de.humaneat.core.global.genome.DefaultGenomePredictor;

/**
 * @author muellermak
 *
 */
public class LstmGenomePredictor implements DefaultGenomePredictor {

	private LstmGenome genome;

	/**
	 * @param genome
	 */
	public LstmGenomePredictor(LstmGenome genome) {
		this.genome = genome;
	}

	/**
	 * @return
	 */
	@Override
	public double[] getNext() {
		return genome.getFeeder().feedForward(null);
	}
}
