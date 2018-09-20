package de.core.neat.population;

/**
 * @author muellermak
 *
 *         Configuration for a neat population
 */
public class NeatPopulationConfig {

	/**
	 * Compatibility distance adjusting
	 */
	public double c1;
	public double c2;
	public double c3;

	/**
	 * Maximum distance within one species
	 */
	public double dt;

	/**
	 * 
	 */
	public NeatPopulationConfig() {
		this.c1 = 1.0;
		this.c2 = 1.0;
		this.c3 = 0.4;
		this.dt = 1.25;
	}
}
