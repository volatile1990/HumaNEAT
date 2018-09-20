package de.core.global.activation;

/**
 * @author muellermak
 *
 */
@FunctionalInterface
public interface Activation {

	/**
	 * Node activation function
	 * 
	 * @param x
	 * @return
	 */
	public double activate(double x);
}
