package de.humaneat.core.global.normalization;

/**
 * @author muellermak
 *
 */
public interface Normalizer {

	/**
	 * Normalize a given string to a neural network input
	 * 
	 * @param content
	 * @return
	 */
	public double[] normalize(String content);

	/**
	 * @return the amount of inputs
	 */
	public int getInputCount();
}
