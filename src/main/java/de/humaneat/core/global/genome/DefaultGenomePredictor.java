package de.humaneat.core.global.genome;

/**
 * @author muellermak
 *
 */
public interface DefaultGenomePredictor {

	/**
	 * @return the next predicted value
	 */
	public double[] getNext();
}
