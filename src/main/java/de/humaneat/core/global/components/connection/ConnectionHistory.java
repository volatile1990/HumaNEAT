package de.humaneat.core.global.components.connection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author muellermak
 *
 *         Stores information about all connections that have occured over a whole population. This keeps innovations unique.
 */
public class ConnectionHistory {

	public int fromNode;
	public int toNode;
	public int innovationNumber;
	public List<Integer> innovationNumbers;

	/**
	 *
	 */
	public ConnectionHistory(int fromNode, int toNode, int innovationNumber, List<Integer> innovationNumbers) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.innovationNumber = innovationNumber;

		this.innovationNumbers = new ArrayList<>();
		this.innovationNumbers.addAll(innovationNumbers);
	}

	/**
	 * @param genome
	 * @param fromInnovationNumber
	 * @param toInnovationNumber
	 * @return
	 */
	public boolean matches(List<Integer> innovationNumbers, int fromInnovationNumber, int toInnovationNumber) {

		if (innovationNumbers.size() != this.innovationNumbers.size()) {
			return false;
		}

		if (fromInnovationNumber != fromNode || toInnovationNumber != toNode) {
			return false;
		}

		for (Integer innovationNumber : innovationNumbers) {
			if (!this.innovationNumbers.contains(innovationNumber)) {
				return false;
			}
		}

		return true;
	}
}
