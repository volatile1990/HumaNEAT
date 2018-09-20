package de.core.global.components;

/**
 * @author MannoR
 *
 */
public abstract class NodeEngager {

	public Node node;

	/**
	 * Activates the nodes output using the activation function
	 */
	public abstract void activate();

	/**
	 * Fires the output over all output connections
	 */
	public abstract void fire();

}
