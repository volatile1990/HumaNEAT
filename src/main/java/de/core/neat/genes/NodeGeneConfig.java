package de.core.neat.genes;

import de.core.global.Activation;
import de.core.global.ActivationFunctions;

/**
 * @author muellermak
 *
 *         The used config for a node gene
 */
public class NodeGeneConfig {

	/**
	 * The used activation function
	 */
	public Activation activationFunction;

	/**
	 * 
	 */
	public NodeGeneConfig() {
		this.activationFunction = ActivationFunctions::sigmoid;
	}

}
