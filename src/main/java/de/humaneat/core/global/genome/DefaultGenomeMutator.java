package de.humaneat.core.global.genome;

import java.util.List;
import java.util.Map;

import de.humaneat.core.global.components.connection.ConnectionHistory;

/**
 * @author muellermak
 *
 */
public interface DefaultGenomeMutator {

	/**
	 * Adds a new node at a random position:
	 * Old Structure: Node -> Connection -> Node
	 * New Structure: Node -> Connection -> NewNode -> Connection -> Node
	 *
	 * @param random
	 * @param innovationHistory
	 */
	public void addNodeMutation(Map<Integer, List<ConnectionHistory>> innovationHistory);

	/**
	 * Randomly connects two unconnected nodes
	 *
	 * @param random
	 * @param innovationHistory
	 * @param connectionInnovation
	 */
	public void addConnectionMutation(Map<Integer, List<ConnectionHistory>> innovationHistory);

	/**
	 * Mutates the genomes connection weights
	 *
	 * @param innovationHistory
	 */
	public void mutate(Map<Integer, List<ConnectionHistory>> innovationHistory);
}
