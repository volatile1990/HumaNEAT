package de.humaneat.core.global.genome;

/**
 * @author muellermak
 *
 */
public interface DefaultGenomeLinker {

	/**
	 * Sets up the neural network as a list of nodes in order to be engange (INPUT -> HIDDEN -> OUTPUT)
	 *
	 */
	public void generateNetwork();

	/**
	 * Sets the all outgoing connections of a node gene into its output connections
	 */
	public void connectNodes();

}
