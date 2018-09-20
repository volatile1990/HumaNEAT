package de.core.global;

/**
 * @author muellermak
 *
 *         Contains all available activation functions
 */
public class ActivationFunctions {

	/**
	 * Shrinked Sigmoid activation function; Tends to got for 0 or 1 easier
	 *
	 * @param x
	 * @return
	 */
	public static double shrinkedSigmoid(double x) {
		return 1D / (1D + Math.exp(-4.9 * x));
	}

	/**
	 * Sigmoid activation function
	 *
	 * @param x
	 * @return
	 */
	public static double sigmoid(double x) {
		return (1 / (1 + Math.pow(Math.E, -1 * x)));
	}

	/**
	 * Tanh activation function
	 *
	 * @param x
	 * @return
	 */
	public static double tanh(double x) {
		return Math.tanh(x);
	}

}
