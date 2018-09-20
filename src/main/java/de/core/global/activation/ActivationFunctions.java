package de.core.global.activation;

/**
 * @author muellermak
 *
 *         Contains all available activation functions
 */
public class ActivationFunctions {

	/**
	 * Binary step activation function
	 *
	 * @param x
	 * @return
	 */
	public static double binaryStep(double x) {
		return 1D / (1D + Math.exp(-1000 * x));
	}

	/**
	 * Sigmoid activation function
	 *
	 * @param x
	 * @return
	 */
	public static double sigmoid(double x) {
		return 1 / (1 + Math.pow(Math.E, -1 * x));
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
