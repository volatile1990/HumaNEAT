package de.humaneat.core.global.genome;

import java.util.List;
import java.util.Map;

import de.humaneat.core.global.components.connection.ConnectionHistory;

/**
 * @author muellermak
 *
 */
public interface DefaultGenomeManager {

	/**
	 * Generates all inputs and outputs
	 * Links all inputs to all outputs
	 * Creates the bias nodes and links it to all generated outputs
	 *
	 * @param genome
	 */
	public void initialize();

	/**
	 * Sets this genomes connection innovation to the next connection innovation number
	 */
	public void updateConnectionInnovation();

	/**
	 * Sets this genomes node innovation to the next node innovation number
	 */
	public void updateNodeInnovation();

	/**
	 * @param innovationHistory
	 * @param fromInnovationNumber
	 * @param toInnovationNumber
	 * @return the next innovationNumber for the connection between from and to
	 */
	public int getConnectionInnovationNumber(Map<Integer, List<ConnectionHistory>> innovationHistory, int fromInnovationNumber, int toInnovationNumber);
}
