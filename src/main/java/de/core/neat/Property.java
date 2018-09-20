package de.core.neat;

import de.core.global.Activation;
import de.core.global.ActivationFunctions;

public enum Property {

	/**
	 * Compatibility distance adjusting
	 */
	C1(1.0), C2(1.0), C3(0.4),

	/**
	 * Maximum distance within one species
	 */
	DT(1.25),

	/**
	 * Chance that the weights of a genome mutate
	 */
	WEIGHT_MUTATION_RATE(1),

	/**
	 * Maximum range of a weight on random generation
	 */
	WEIGHT_RANDOM_RANGE(2),

	/**
	 * Chance that a weight is uniformly pertrubed instead of assigning a new one
	 */
	PROBABILITY_PERTURBING(0.9),

	/**
	 * Maxium weight change of uniformly perturbing it
	 */
	UNIFORMLY_PERTURB_WEIGHT_RANGE(0.2),

	/**
	 * Chance that a connection is added during mutation
	 */
	ADD_CONNECTION_RATE(0.3),

	/**
	 * Chance that a node is added during mutation
	 */
	ADD_NODE_RATE(0.001),

	/**
	 * How many percent of members of species are removed in each generation
	 */
	REMOVE_BAD_SPECIES_MEMBERS_PERCENT(0.85),

	/**
	 * Chance that a genome is added to the next generation without crossover
	 */
	ADD_GENOME_WITHOUT_CROSSOVER_RATE(0.25),

	/**
	 * Chance that a connection of a child genome gets disabled if either of the parents connection is too
	 */
	DISABLE_CONNECTION_ON_CHILD_RATE(0.75),

	/**
	 * The used activation function
	 */
	ACTIVATION_FUNCTION(ActivationFunctions::shrinkedSigmoid);

	private double value;
	private Activation activationFunction;

	/**
	 * @param value
	 */
	private Property(double value) {
		this.value = value;
	}

	/**
	 * @param activationFunction
	 */
	private Property(Activation activationFunction) {
		this.activationFunction = activationFunction;
	}

	/**
	 * @return the property value
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * @return the activation function
	 */
	public Activation getActivationFunction() {
		return this.activationFunction;
	}

}
