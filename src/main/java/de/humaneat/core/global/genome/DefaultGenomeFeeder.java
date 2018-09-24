package de.humaneat.core.global.genome;

/**
 * @author muellermak
 *
 */
public interface DefaultGenomeFeeder {

	/**
	 * @param inputValues
	 * @return the neural network output
	 * 
	 *         Processes a feed forward through the whole genome
	 */
	public double[] feedForward(double[] inputValues);

}
